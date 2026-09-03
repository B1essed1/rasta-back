package uz.rasta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.rasta.dto.ReviewDto;
import uz.rasta.service.ReviewService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shops/{shopId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDto.Response>> list(
            @PathVariable UUID shopId,
            @RequestParam(required = false) UUID productId) {
        if (productId != null) {
            return ResponseEntity.ok(reviewService.listByShopAndProduct(shopId, productId));
        }
        return ResponseEntity.ok(reviewService.listByShop(shopId));
    }

    @PostMapping
    public ResponseEntity<ReviewDto.Response> create(
            @PathVariable UUID shopId,
            @Valid @RequestBody ReviewDto.CreateRequest request) {
        ReviewDto.Response response = reviewService.create(shopId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
