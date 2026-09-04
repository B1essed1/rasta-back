package uz.rasta.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.rasta.config.ApiException;
import uz.rasta.dto.OrderDto;
import uz.rasta.entity.*;
import uz.rasta.repository.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final SaleRepository saleRepository;
    private final SaleLineRepository saleLineRepository;
    private final StockMovementRepository stockMovementRepository;

    public List<OrderDto.Response> listByShop(UUID shopId) {
        List<Order> orders = orderRepository.findByShopIdOrderByCreatedAtDesc(shopId);
        return orders.stream()
                .map(order -> {
                    List<OrderLine> lines = orderLineRepository.findByOrderId(order.getId());
                    return OrderDto.Response.from(order, lines);
                })
                .toList();
    }

    @Transactional
    public OrderDto.Response create(UUID shopId, OrderDto.CreateRequest request) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> ApiException.notFound("shop.not.found"));

        int nextOrderNo = orderRepository.findMaxOrderNoByShopId(shopId) + 1;

        BigDecimal goodsTotal = BigDecimal.ZERO;
        for (OrderDto.LineRequest item : request.items()) {
            goodsTotal = goodsTotal.add(item.unitPrice().multiply(BigDecimal.valueOf(item.qty())));
        }

        BigDecimal deliveryFee = request.deliveryFee() != null ? request.deliveryFee() : BigDecimal.ZERO;
        BigDecimal total = goodsTotal.add(deliveryFee);

        Order order = Order.builder()
                .shop(shop)
                .orderNo(nextOrderNo)
                .customerName(request.customerName())
                .customerPhone(request.customerPhone())
                .customerAddress(request.customerAddress())
                .deliveryMethod(request.deliveryMethod())
                .deliveryFee(deliveryFee)
                .payMethod(request.payMethod())
                .note(request.note())
                .goodsTotal(goodsTotal)
                .total(total)
                .build();

        order = orderRepository.save(order);

        List<OrderLine> lines = new ArrayList<>();
        for (OrderDto.LineRequest item : request.items()) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> ApiException.notFound("product.not.found"));

            ProductVariant variant = null;
            if (item.variantId() != null) {
                variant = variantRepository.findById(item.variantId()).orElse(null);
            }

            OrderLine line = OrderLine.builder()
                    .order(order)
                    .product(product)
                    .variant(variant)
                    .name(item.name())
                    .label(item.label())
                    .qty(item.qty())
                    .unitPrice(item.unitPrice())
                    .build();

            lines.add(orderLineRepository.save(line));
        }

        log.info("Order #{} created for shop {}", nextOrderNo, shopId);
        return OrderDto.Response.from(order, lines);
    }

    @Transactional
    public OrderDto.Response confirmOrder(UUID shopId, UUID orderId, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> ApiException.notFound("shop.not.found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("shop.not.owner");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> ApiException.notFound("order.not.found"));

        if (!order.getShop().getId().equals(shopId)) {
            throw ApiException.badRequest("order.not.in.shop");
        }

        if (order.getStatus() != Order.OrderStatus.NEW) {
            throw ApiException.conflict("order.only.new.confirm");
        }

        order.setStatus(Order.OrderStatus.CONFIRMED);
        orderRepository.save(order);

        // Create sale from order
        List<OrderLine> lines = orderLineRepository.findByOrderId(orderId);
        int nextSaleNo = saleRepository.findMaxSaleNoByShopId(shopId) + 1;

        Sale sale = Sale.builder()
                .shop(shop)
                .saleNo(nextSaleNo)
                .payMethod(order.getPayMethod())
                .status("COMPLETED")
                .orderId(orderId)
                .total(order.getTotal())
                .build();
        sale = saleRepository.save(sale);

        // Create sale lines and deduct stock for each order line
        for (OrderLine line : lines) {
            ProductVariant variant = line.getVariant();

            SaleLine saleLine = SaleLine.builder()
                    .sale(sale)
                    .variant(variant)
                    .productId(line.getProduct() != null ? line.getProduct().getId() : null)
                    .name(line.getName())
                    .label(line.getLabel())
                    .qty(line.getQty())
                    .unitPrice(line.getUnitPrice())
                    .cost(variant != null ? variant.getAvgCost() : null)
                    .build();
            saleLineRepository.save(saleLine);

            if (variant != null) {
                variant.setQty(variant.getQty() - line.getQty());
                variantRepository.save(variant);

                StockMovement movement = StockMovement.builder()
                        .shop(shop)
                        .variantId(variant.getId())
                        .productId(line.getProduct().getId())
                        .delta(-line.getQty())
                        .reason(StockMovement.MovementReason.SALE)
                        .unitCost(variant.getAvgCost())
                        .saleId(sale.getId())
                        .orderId(orderId)
                        .build();
                stockMovementRepository.save(movement);
            }
        }

        log.info("Order #{} confirmed, Sale #{} created for shop {}", order.getOrderNo(), nextSaleNo, shopId);
        return OrderDto.Response.from(order, lines);
    }

    @Transactional
    public OrderDto.Response updateStatus(UUID shopId, UUID orderId, Order.OrderStatus newStatus, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> ApiException.notFound("shop.not.found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("shop.not.owner");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> ApiException.notFound("order.not.found"));

        if (!order.getShop().getId().equals(shopId)) {
            throw ApiException.badRequest("order.not.in.shop");
        }

        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw ApiException.conflict("order.cancelled");
        }

        if (order.getStatus() == Order.OrderStatus.COMPLETED) {
            throw ApiException.conflict("order.completed");
        }

        order.setStatus(newStatus);
        orderRepository.save(order);

        List<OrderLine> lines = orderLineRepository.findByOrderId(orderId);
        return OrderDto.Response.from(order, lines);
    }

    @Transactional
    public OrderDto.Response cancelOrder(UUID shopId, UUID orderId, OrderDto.CancelRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> ApiException.notFound("order.not.found"));

        if (!order.getShop().getId().equals(shopId)) {
            throw ApiException.badRequest("order.not.in.shop");
        }

        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw ApiException.conflict("order.cancelled");
        }

        if (order.getStatus() == Order.OrderStatus.COMPLETED) {
            throw ApiException.conflict("order.completed");
        }

        boolean wasConfirmed = order.getStatus() != Order.OrderStatus.NEW;

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setCancelReason(request.reason());
        order.setCancelledBy(request.cancelledBy());
        orderRepository.save(order);

        // Return stock if the order was confirmed (stock was already deducted)
        if (wasConfirmed) {
            Shop shop = order.getShop();
            List<OrderLine> lines = orderLineRepository.findByOrderId(orderId);

            for (OrderLine line : lines) {
                if (line.getVariant() != null) {
                    ProductVariant variant = variantRepository.findById(line.getVariant().getId())
                            .orElse(null);
                    if (variant != null) {
                        variant.setQty(variant.getQty() + line.getQty());
                        variantRepository.save(variant);

                        StockMovement movement = StockMovement.builder()
                                .shop(shop)
                                .variantId(variant.getId())
                                .productId(line.getProduct().getId())
                                .delta(line.getQty())
                                .reason(StockMovement.MovementReason.RETURN)
                                .orderId(orderId)
                                .note("Order #" + order.getOrderNo() + " cancelled: " + request.reason())
                                .build();
                        stockMovementRepository.save(movement);
                    }
                }
            }

            log.info("Order #{} cancelled, stock returned for shop {}", order.getOrderNo(), shopId);
        } else {
            log.info("Order #{} cancelled (no stock to return) for shop {}", order.getOrderNo(), shopId);
        }

        List<OrderLine> lines = orderLineRepository.findByOrderId(orderId);
        return OrderDto.Response.from(order, lines);
    }
}
