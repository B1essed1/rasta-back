package uz.rasta.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "stock_movements", indexes = {
        @Index(name = "idx_stock_movements_shop_id", columnList = "shop_id"),
        @Index(name = "idx_stock_movements_variant_id", columnList = "variant_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "variant_id", nullable = false)
    private UUID variantId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Integer delta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MovementReason reason;

    @Column(name = "unit_cost", precision = 12, scale = 2)
    private BigDecimal unitCost;

    @Column(columnDefinition = "text")
    private String note;

    @Column(name = "sale_id")
    private UUID saleId;

    @Column(name = "order_id")
    private UUID orderId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public enum MovementReason {
        RESTOCK, SALE, RETURN, CORRECTION, LOSS
    }
}
