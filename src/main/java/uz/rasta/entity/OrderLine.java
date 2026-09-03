package uz.rasta.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_lines", indexes = {
        @Index(name = "idx_order_lines_order_id", columnList = "order_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 100)
    private String label;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    @Builder.Default
    private Boolean dropped = false;
}
