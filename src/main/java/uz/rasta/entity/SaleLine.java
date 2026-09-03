package uz.rasta.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "sale_lines", indexes = {
        @Index(name = "idx_sale_lines_sale_id", columnList = "sale_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleLine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @Column(name = "product_id")
    private UUID productId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 100)
    private String label;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(precision = 12, scale = 2)
    private BigDecimal cost;

    @Column(nullable = false)
    @Builder.Default
    private Integer returned = 0;
}
