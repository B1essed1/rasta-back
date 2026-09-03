package uz.rasta.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sales", indexes = {
        @Index(name = "idx_sales_shop_id", columnList = "shop_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "sale_no", nullable = false)
    private Integer saleNo;

    @Column(name = "pay_method", length = 30)
    private String payMethod;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "COMPLETED";

    @Column(name = "order_id")
    private UUID orderId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
