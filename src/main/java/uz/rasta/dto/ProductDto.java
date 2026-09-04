package uz.rasta.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uz.rasta.entity.Product;
import uz.rasta.entity.ProductImage;
import uz.rasta.entity.ProductVariant;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class ProductDto {

    private ProductDto() {
    }

    public record CreateRequest(
            String catId,
            @NotBlank(message = "Product name (en) is required")
            String nameEn,
            String nameRu,
            String nameUz,
            String descEn,
            String descRu,
            String descUz,
            @NotNull(message = "Price is required")
            @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
            BigDecimal price,
            Boolean visible,
            Integer sortOrder,
            String tone,
            List<VariantRequest> variants
    ) {
    }

    public record UpdateRequest(
            String catId,
            String nameEn,
            String nameRu,
            String nameUz,
            String descEn,
            String descRu,
            String descUz,
            @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
            BigDecimal price,
            Boolean visible,
            Integer sortOrder,
            String tone,
            List<VariantRequest> variants
    ) {
    }

    public record VariantRequest(
            UUID id,
            String optionsJson,
            String barcode,
            Integer qty,
            BigDecimal avgCost,
            Integer threshold
    ) {
    }

    public record ImageResponse(
            UUID id,
            UUID variantId,
            String url,
            Integer sortOrder
    ) {
        public static ImageResponse from(ProductImage img) {
            return new ImageResponse(
                    img.getId(),
                    img.getVariant() != null ? img.getVariant().getId() : null,
                    img.getUrl(),
                    img.getSortOrder()
            );
        }
    }

    public record Response(
            UUID id,
            UUID shopId,
            String catId,
            String nameEn,
            String nameRu,
            String nameUz,
            String descEn,
            String descRu,
            String descUz,
            BigDecimal price,
            Boolean visible,
            Integer sortOrder,
            String tone,
            List<ImageResponse> images,
            List<VariantResponse> variants,
            Instant createdAt
    ) {
        public static Response from(Product product, List<ProductVariant> variants, List<ProductImage> images) {
            return new Response(
                    product.getId(),
                    product.getShop().getId(),
                    product.getCatId(),
                    product.getNameEn(),
                    product.getNameRu(),
                    product.getNameUz(),
                    product.getDescEn(),
                    product.getDescRu(),
                    product.getDescUz(),
                    product.getPrice(),
                    product.getVisible(),
                    product.getSortOrder(),
                    product.getTone(),
                    images.stream().map(ImageResponse::from).toList(),
                    variants.stream().map(VariantResponse::from).toList(),
                    product.getCreatedAt()
            );
        }
    }

    public record VariantResponse(
            UUID id,
            String optionsJson,
            String barcode,
            Integer qty,
            BigDecimal avgCost,
            Integer threshold
    ) {
        public static VariantResponse from(ProductVariant v) {
            return new VariantResponse(
                    v.getId(),
                    v.getOptionsJson(),
                    v.getBarcode(),
                    v.getQty(),
                    v.getAvgCost(),
                    v.getThreshold()
            );
        }
    }

    public record RestockRequest(
            @NotNull UUID variantId,
            @NotNull UUID productId,
            @NotNull Integer qty,
            BigDecimal unitCost,
            String note
    ) {
    }

    public record AdjustRequest(
            @NotNull UUID variantId,
            @NotNull UUID productId,
            @NotNull Integer delta,
            @NotNull String reason,
            String note
    ) {
    }
}
