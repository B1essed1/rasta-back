package uz.rasta.dto;

import jakarta.validation.constraints.*;
import uz.rasta.entity.Review;

import java.time.Instant;
import java.util.UUID;

public final class ReviewDto {

    private ReviewDto() {
    }

    public record CreateRequest(
            UUID productId,

            @NotNull(message = "Rating is required")
            @Min(value = 1, message = "Rating must be at least 1")
            @Max(value = 5, message = "Rating must be at most 5")
            Integer rating,

            @NotBlank(message = "Name is required")
            @Size(max = 100, message = "Name must not exceed 100 characters")
            String name,

            String text
    ) {
    }

    public record Response(
            UUID id,
            UUID shopId,
            UUID productId,
            Integer rating,
            String name,
            String text,
            Instant createdAt
    ) {
        public static Response from(Review review) {
            return new Response(
                    review.getId(),
                    review.getShop().getId(),
                    review.getProductId(),
                    review.getRating(),
                    review.getName(),
                    review.getText(),
                    review.getCreatedAt()
            );
        }
    }
}
