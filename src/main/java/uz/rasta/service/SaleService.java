package uz.rasta.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.rasta.dto.OrderDto;
import uz.rasta.entity.*;
import uz.rasta.repository.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleLineRepository saleLineRepository;
    private final ShopRepository shopRepository;
    private final ProductVariantRepository variantRepository;
    private final StockMovementRepository stockMovementRepository;

    public List<OrderDto.SaleResponse> listByShop(UUID shopId) {
        return saleRepository.findByShopIdOrderByCreatedAtDesc(shopId)
                .stream()
                .map(sale -> new OrderDto.SaleResponse(
                        sale.getId(),
                        sale.getShop().getId(),
                        sale.getSaleNo(),
                        sale.getPayMethod(),
                        sale.getStatus(),
                        sale.getOrderId(),
                        sale.getTotal(),
                        sale.getCreatedAt()
                ))
                .toList();
    }

    @Transactional
    public OrderDto.SaleResponse createPosSale(UUID shopId, OrderDto.SaleCreateRequest request, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not the owner of this shop");
        }

        int nextSaleNo = saleRepository.findMaxSaleNoByShopId(shopId) + 1;

        BigDecimal total = BigDecimal.ZERO;
        for (OrderDto.SaleLineRequest item : request.items()) {
            total = total.add(item.unitPrice().multiply(BigDecimal.valueOf(item.qty())));
        }

        Sale sale = Sale.builder()
                .shop(shop)
                .saleNo(nextSaleNo)
                .payMethod(request.payMethod())
                .status("COMPLETED")
                .total(total)
                .build();
        sale = saleRepository.save(sale);

        // Create sale lines and deduct stock
        for (OrderDto.SaleLineRequest item : request.items()) {
            ProductVariant variant = null;
            if (item.variantId() != null) {
                variant = variantRepository.findById(item.variantId())
                        .orElseThrow(() -> new IllegalArgumentException("Variant not found: " + item.variantId()));
            }

            SaleLine saleLine = SaleLine.builder()
                    .sale(sale)
                    .variant(variant)
                    .productId(item.productId())
                    .name(item.name())
                    .label(item.label())
                    .qty(item.qty())
                    .unitPrice(item.unitPrice())
                    .cost(item.cost() != null ? item.cost() : (variant != null ? variant.getAvgCost() : null))
                    .build();
            saleLineRepository.save(saleLine);

            if (variant != null) {
                variant.setQty(variant.getQty() - item.qty());
                variantRepository.save(variant);

                StockMovement movement = StockMovement.builder()
                        .shop(shop)
                        .variantId(variant.getId())
                        .productId(item.productId())
                        .delta(-item.qty())
                        .reason(StockMovement.MovementReason.SALE)
                        .unitCost(variant.getAvgCost())
                        .saleId(sale.getId())
                        .build();
                stockMovementRepository.save(movement);
            }
        }

        log.info("POS Sale #{} created for shop {}", nextSaleNo, shopId);

        return new OrderDto.SaleResponse(
                sale.getId(),
                sale.getShop().getId(),
                sale.getSaleNo(),
                sale.getPayMethod(),
                sale.getStatus(),
                sale.getOrderId(),
                sale.getTotal(),
                sale.getCreatedAt()
        );
    }
}
