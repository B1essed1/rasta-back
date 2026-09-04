package uz.rasta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import uz.rasta.entity.Shop;
import uz.rasta.entity.ShopConfig;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public final class ShopDto {

    private ShopDto() {
    }

    public record CreateRequest(
            @NotBlank(message = "Handle is required")
            @Size(min = 3, max = 60, message = "Handle must be between 3 and 60 characters")
            @Pattern(regexp = "^[a-z0-9-]+$", message = "Handle must contain only lowercase letters, numbers, and hyphens")
            String handle,

            @NotBlank(message = "Shop name is required")
            @Size(max = 120, message = "Name must not exceed 120 characters")
            String name,

            Map<String, String> tagline,
            String location,
            String type,
            String coverColor,
            String logoUrl,
            String coverUrl,
            String instagram,
            String telegram,
            String phone
    ) {
    }

    public record UpdateRequest(
            @Size(max = 120, message = "Name must not exceed 120 characters")
            String name,
            Map<String, String> tagline,
            String location,
            String type,
            Shop.ShopStatus status,
            String coverColor,
            String logoUrl,
            String coverUrl,
            String instagram,
            String telegram,
            String phone,
            Shop.ShopPlan plan
    ) {
    }

    public record Response(
            UUID id,
            String handle,
            String name,
            Map<String, String> tagline,
            String location,
            String type,
            Shop.ShopStatus status,
            UUID ownerId,
            Double rating,
            String initials,
            Shop.ShopPlan plan,
            String coverColor,
            String logoUrl,
            String coverUrl,
            String instagram,
            String telegram,
            String phone,
            Instant createdAt
    ) {
        public static Response from(Shop shop) {
            return new Response(
                    shop.getId(),
                    shop.getHandle(),
                    shop.getName(),
                    shop.getTagline(),
                    shop.getLocation(),
                    shop.getType(),
                    shop.getStatus(),
                    shop.getOwner().getId(),
                    shop.getRating(),
                    shop.getInitials(),
                    shop.getPlan(),
                    shop.getCoverColor(),
                    shop.getLogoUrl(),
                    shop.getCoverUrl(),
                    shop.getInstagram(),
                    shop.getTelegram(),
                    shop.getPhone(),
                    shop.getCreatedAt()
            );
        }
    }

    public record ConfigRequest(
            ShopConfig.Theme theme,
            String palette,
            ShopConfig.Layout layout,
            String font,
            String customPaletteJson
    ) {
    }

    public record ConfigResponse(
            UUID id,
            UUID shopId,
            ShopConfig.Theme theme,
            String palette,
            ShopConfig.Layout layout,
            String font,
            String customPaletteJson
    ) {
        public static ConfigResponse from(ShopConfig config) {
            return new ConfigResponse(
                    config.getId(),
                    config.getShop().getId(),
                    config.getTheme(),
                    config.getPalette(),
                    config.getLayout(),
                    config.getFont(),
                    config.getCustomPaletteJson()
            );
        }
    }
}
