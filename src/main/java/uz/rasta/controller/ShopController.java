package uz.rasta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.rasta.config.ApiResponse;
import uz.rasta.dto.ShopDto;
import uz.rasta.entity.User;
import uz.rasta.service.ShopService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShopDto.Response>>> listAll() {
        return ResponseEntity.ok(ApiResponse.ok(shopService.listAll()));
    }

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<ShopDto.Response>> mine(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.ok(shopService.getByOwner(currentUser)));
    }

    @GetMapping("/{handle:[a-z0-9-]+}")
    public ResponseEntity<ApiResponse<ShopDto.Response>> getByHandle(@PathVariable String handle) {
        return ResponseEntity.ok(ApiResponse.ok(shopService.getByHandle(handle)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ShopDto.Response>> create(
            @Valid @RequestBody ShopDto.CreateRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(shopService.create(request, currentUser)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ShopDto.Response>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ShopDto.UpdateRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.ok(shopService.update(id, request, currentUser)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        shopService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{shopId}/config")
    public ResponseEntity<ApiResponse<ShopDto.ConfigResponse>> getConfig(@PathVariable UUID shopId) {
        return ResponseEntity.ok(ApiResponse.ok(shopService.getConfig(shopId)));
    }

    @PutMapping("/{shopId}/config")
    public ResponseEntity<ApiResponse<ShopDto.ConfigResponse>> updateConfig(
            @PathVariable UUID shopId,
            @Valid @RequestBody ShopDto.ConfigRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.ok(shopService.updateConfig(shopId, request, currentUser)));
    }
}
