package uz.rasta.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_orders_shop_id", columnList = "shop_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "order_no", nullable = false)
    private Integer orderNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.NEW;

    @Column(name = "customer_name", length = 120)
    private String customerName;

    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    @Column(name = "customer_address", columnDefinition = "text")
    private String customerAddress;

    @Column(name = "delivery_method", length = 30)
    private String deliveryMethod;

    @Column(name = "delivery_fee", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal deliveryFee = BigDecimal.ZERO;

    @Column(name = "pay_method", length = 30)
    private String payMethod;

    @Column(columnDefinition = "text")
    private String note;

    @Column(name = "goods_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal goodsTotal;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "cancel_reason", columnDefinition = "text")
    private String cancelReason;

    @Column(name = "cancelled_by", length = 30)
    private String cancelledBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public enum OrderStatus {
        NEW, CONFIRMED, READY, OUT, COMPLETED, CANCELLED
    }
}
