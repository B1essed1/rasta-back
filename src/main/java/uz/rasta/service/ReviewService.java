package uz.rasta.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.rasta.dto.ReviewDto;
import uz.rasta.entity.Review;
import uz.rasta.entity.Shop;
import uz.rasta.repository.ReviewRepository;
import uz.rasta.repository.ShopRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ShopRepository shopRepository;

    public List<ReviewDto.Response> listByShop(UUID shopId) {
        return reviewRepository.findByShopIdOrderByCreatedAtDesc(shopId)
                .stream()
                .map(ReviewDto.Response::from)
                .toList();
    }

    public List<ReviewDto.Response> listByShopAndProduct(UUID shopId, UUID productId) {
        return reviewRepository.findByShopIdAndProductIdOrderByCreatedAtDesc(shopId, productId)
                .stream()
                .map(ReviewDto.Response::from)
                .toList();
    }

    @Transactional
    public ReviewDto.Response create(UUID shopId, ReviewDto.CreateRequest request) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        Review review = Review.builder()
                .shop(shop)
                .productId(request.productId())
                .rating(request.rating())
                .name(request.name())
                .text(request.text())
                .build();

        review = reviewRepository.save(review);

        // Update shop average rating
        Double avgRating = reviewRepository.findAverageRatingByShopId(shopId);
        if (avgRating != null) {
            shop.setRating(Math.round(avgRating * 10.0) / 10.0);
            shopRepository.save(shop);
        }

        return ReviewDto.Response.from(review);
    }
}
