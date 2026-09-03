package uz.rasta.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_products_shop_id", columnList = "shop_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "cat_id", length = 50)
    private String catId;

    @Column(name = "name_en", length = 200)
    private String nameEn;

    @Column(name = "name_ru", length = 200)
    private String nameRu;

    @Column(name = "name_uz", length = 200)
    private String nameUz;

    @Column(name = "desc_en", columnDefinition = "text")
    private String descEn;

    @Column(name = "desc_ru", columnDefinition = "text")
    private String descRu;

    @Column(name = "desc_uz", columnDefinition = "text")
    private String descUz;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    @Builder.Default
    private Boolean visible = true;

    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;

    @Column(length = 30)
    private String tone;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
