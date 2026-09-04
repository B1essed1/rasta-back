package uz.rasta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.rasta.config.ApiResponse;
import uz.rasta.dto.ProductDto;
import uz.rasta.entity.User;
import uz.rasta.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/shops/{shopId}/products")
    public ResponseEntity<ApiResponse<List<ProductDto.Response>>> list(
            @PathVariable UUID shopId,
            @AuthenticationPrincipal User currentUser) {
        boolean publicView = (currentUser == null);
        return ResponseEntity.ok(ApiResponse.ok(productService.listByShop(shopId, publicView)));
    }

    @GetMapping("/api/shops/{shopId}/products/{id}")
    public ResponseEntity<ApiResponse<ProductDto.Response>> getById(
            @PathVariable UUID shopId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(productService.getById(id)));
    }

    @PostMapping("/api/shops/{shopId}/products")
    public ResponseEntity<ApiResponse<ProductDto.Response>> create(
            @PathVariable UUID shopId,
            @Valid @RequestBody ProductDto.CreateRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(productService.create(shopId, request, currentUser)));
    }

    @PutMapping("/api/shops/{shopId}/products/{id}")
    public ResponseEntity<ApiResponse<ProductDto.Response>> update(
            @PathVariable UUID shopId,
            @PathVariable UUID id,
            @Valid @RequestBody ProductDto.UpdateRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.ok(productService.update(shopId, id, request, currentUser)));
    }

    @PostMapping("/api/shops/{shopId}/products/{productId}/images")
    public ResponseEntity<ApiResponse<ProductDto.ImageResponse>> addImage(
            @PathVariable UUID shopId,
            @PathVariable UUID productId,
            @RequestParam String url,
            @RequestParam(required = false) UUID variantId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(productService.addImage(shopId, productId, url, variantId, currentUser)));
    }

    @DeleteMapping("/api/shops/{shopId}/products/{productId}/images/{imageId}")
    public ResponseEntity<Void> removeImage(
            @PathVariable UUID shopId,
            @PathVariable UUID productId,
            @PathVariable UUID imageId,
            @AuthenticationPrincipal User currentUser) {
        productService.removeImage(shopId, productId, imageId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/shops/{shopId}/products/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID shopId,
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        productService.delete(shopId, id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/shops/{shopId}/inventory/restock")
    public ResponseEntity<ApiResponse<Void>> restock(
            @PathVariable UUID shopId,
            @Valid @RequestBody ProductDto.RestockRequest request,
            @AuthenticationPrincipal User currentUser) {
        productService.restock(shopId, request, currentUser);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PostMapping("/api/shops/{shopId}/inventory/adjust")
    public ResponseEntity<ApiResponse<Void>> adjust(
            @PathVariable UUID shopId,
            @Valid @RequestBody ProductDto.AdjustRequest request,
            @AuthenticationPrincipal User currentUser) {
        productService.adjust(shopId, request, currentUser);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
