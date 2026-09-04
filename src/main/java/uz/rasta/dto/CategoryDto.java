package uz.rasta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import uz.rasta.entity.Category;

import java.time.Instant;
import java.util.UUID;

public final class CategoryDto {

    private CategoryDto() {
    }

    public record CreateRequest(
            @NotNull(message = "Kind is required")
            Category.Kind kind,

            @NotBlank(message = "Slug is required")
            @Size(max = 50)
            @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must be lowercase alphanumeric with hyphens")
            String slug,

            @NotBlank(message = "English name is required")
            @Size(max = 100)
            String nameEn,

            @Size(max = 100)
            String nameUz,

            @Size(max = 100)
            String nameRu,

            @Size(max = 10)
            String icon,

            Integer sortOrder
    ) {
    }

    public record UpdateRequest(
            @Size(max = 100)
            String nameEn,

            @Size(max = 100)
            String nameUz,

            @Size(max = 100)
            String nameRu,

            @Size(max = 10)
            String icon,

            Integer sortOrder,
            Boolean active
    ) {
    }

    public record Response(
            UUID id,
            Category.Kind kind,
            String slug,
            String nameEn,
            String nameUz,
            String nameRu,
            String icon,
            Integer sortOrder,
            Boolean active,
            Instant createdAt
    ) {
        public static Response from(Category c) {
            return new Response(
                    c.getId(),
                    c.getKind(),
                    c.getSlug(),
                    c.getNameEn(),
                    c.getNameUz(),
                    c.getNameRu(),
                    c.getIcon(),
                    c.getSortOrder(),
                    c.getActive(),
                    c.getCreatedAt()
            );
        }
    }
}
