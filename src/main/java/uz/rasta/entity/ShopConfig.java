package uz.rasta.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "shop_configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false, unique = true)
    private Shop shop;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Theme theme = Theme.MINIMAL;

    @Column(length = 30)
    private String palette;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Layout layout = Layout.GRID;

    @Column(length = 50)
    private String font;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "custom_palette_json", columnDefinition = "jsonb")
    private String customPaletteJson;

    public enum Theme {
        MINIMAL, BOUTIQUE, BOLD, PLAYFUL
    }

    public enum Layout {
        GRID, GALLERY, LIST, MAGAZINE
    }
}
