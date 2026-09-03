package uz.rasta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import uz.rasta.entity.Order;
import uz.rasta.entity.OrderLine;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class OrderDto {

    private OrderDto() {
    }

    public record CreateRequest(
            @NotBlank(message = "Customer name is required")
            String customerName,
            @NotBlank(message = "Customer phone is required")
            String customerPhone,
            String customerAddress,
            String deliveryMethod,
            BigDecimal deliveryFee,
            String payMethod,
            String note,
            @NotNull(message = "Items are required")
            @Size(min = 1, message = "At least one item is required")
            List<LineRequest> items
    ) {
    }

    public record LineRequest(
            @NotNull UUID productId,
            UUID variantId,
            @NotBlank String name,
            String label,
            @NotNull Integer qty,
            @NotNull BigDecimal unitPrice
    ) {
    }

    public record StatusRequest(
            @NotNull Order.OrderStatus status
    ) {
    }

    public record CancelRequest(
            @NotBlank(message = "Cancel reason is required")
            String reason,
            String cancelledBy
    ) {
    }

    public record Response(
            UUID id,
            UUID shopId,
            Integer orderNo,
            Order.OrderStatus status,
            String customerName,
            String customerPhone,
            String customerAddress,
            String deliveryMethod,
            BigDecimal deliveryFee,
            String payMethod,
            String note,
            BigDecimal goodsTotal,
            BigDecimal total,
            String cancelReason,
            String cancelledBy,
            List<LineResponse> items,
            Instant createdAt
    ) {
        public static Response from(Order order, List<OrderLine> lines) {
            return new Response(
                    order.getId(),
                    order.getShop().getId(),
                    order.getOrderNo(),
                    order.getStatus(),
                    order.getCustomerName(),
                    order.getCustomerPhone(),
                    order.getCustomerAddress(),
                    order.getDeliveryMethod(),
                    order.getDeliveryFee(),
                    order.getPayMethod(),
                    order.getNote(),
                    order.getGoodsTotal(),
                    order.getTotal(),
                    order.getCancelReason(),
                    order.getCancelledBy(),
                    lines.stream().map(LineResponse::from).toList(),
                    order.getCreatedAt()
            );
        }
    }

    public record LineResponse(
            UUID id,
            UUID productId,
            UUID variantId,
            String name,
            String label,
            Integer qty,
            BigDecimal unitPrice,
            Boolean dropped
    ) {
        public static LineResponse from(OrderLine line) {
            return new LineResponse(
                    line.getId(),
                    line.getProduct() != null ? line.getProduct().getId() : null,
                    line.getVariant() != null ? line.getVariant().getId() : null,
                    line.getName(),
                    line.getLabel(),
                    line.getQty(),
                    line.getUnitPrice(),
                    line.getDropped()
            );
        }
    }

    public record SaleCreateRequest(
            String payMethod,
            @NotNull @Size(min = 1) List<SaleLineRequest> items
    ) {
    }

    public record SaleLineRequest(
            UUID variantId,
            @NotNull UUID productId,
            @NotBlank String name,
            String label,
            @NotNull Integer qty,
            @NotNull BigDecimal unitPrice,
            BigDecimal cost
    ) {
    }

    public record SaleResponse(
            UUID id,
            UUID shopId,
            Integer saleNo,
            String payMethod,
            String status,
            UUID orderId,
            BigDecimal total,
            Instant createdAt
    ) {
    }
}
