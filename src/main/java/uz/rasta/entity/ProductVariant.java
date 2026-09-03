package uz.rasta.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product_variants", indexes = {
        @Index(name = "idx_product_variants_product_id", columnList = "product_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "options_json", columnDefinition = "jsonb")
    private String optionsJson;

    @Column(length = 50)
    private String barcode;

    @Column(nullable = false)
    @Builder.Default
    private Integer qty = 0;

    @Column(name = "avg_cost", precision = 12, scale = 2)
    private BigDecimal avgCost;

    @Column(nullable = false)
    @Builder.Default
    private Integer threshold = 5;
}
