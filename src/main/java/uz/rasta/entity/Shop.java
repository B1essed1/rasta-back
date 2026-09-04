package uz.rasta.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "shops", indexes = {
        @Index(name = "idx_shops_handle", columnList = "handle", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 60)
    private String handle;

    @Column(nullable = false, length = 120)
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> tagline;

    @Column(length = 200)
    private String location;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String type = "other";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private ShopStatus status = ShopStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(precision = 2)
    @Builder.Default
    private Double rating = 0.0;

    @Column(length = 4)
    private String initials;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private ShopPlan plan = ShopPlan.STARTER;

    @Column(name = "cover_color", length = 20)
    private String coverColor;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @Column(length = 100)
    private String instagram;

    @Column(length = 100)
    private String telegram;

    @Column(length = 20)
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public enum ShopStatus {
        LIVE, PAUSED, DRAFT
    }

    public enum ShopPlan {
        STARTER, PRO
    }
}
