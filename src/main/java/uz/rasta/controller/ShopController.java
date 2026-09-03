package uz.rasta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<List<ShopDto.Response>> listAll() {
        return ResponseEntity.ok(shopService.listAll());
    }

    @GetMapping("/{handle:[a-z0-9-]+}")
    public ResponseEntity<ShopDto.Response> getByHandle(@PathVariable String handle) {
        return ResponseEntity.ok(shopService.getByHandle(handle));
    }

    @PostMapping
    public ResponseEntity<ShopDto.Response> create(
            @Valid @RequestBody ShopDto.CreateRequest request,
            @AuthenticationPrincipal User currentUser) {
        ShopDto.Response response = shopService.create(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShopDto.Response> update(
            @PathVariable UUID id,
            @Valid @RequestBody ShopDto.UpdateRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(shopService.update(id, request, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        shopService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{shopId}/config")
    public ResponseEntity<ShopDto.ConfigResponse> getConfig(@PathVariable UUID shopId) {
        return ResponseEntity.ok(shopService.getConfig(shopId));
    }

    @PutMapping("/{shopId}/config")
    public ResponseEntity<ShopDto.ConfigResponse> updateConfig(
            @PathVariable UUID shopId,
            @Valid @RequestBody ShopDto.ConfigRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(shopService.updateConfig(shopId, request, currentUser));
    }
}
