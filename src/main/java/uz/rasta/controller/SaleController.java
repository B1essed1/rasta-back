package uz.rasta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.rasta.dto.OrderDto;
import uz.rasta.entity.User;
import uz.rasta.service.SaleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shops/{shopId}/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @GetMapping
    public ResponseEntity<List<OrderDto.SaleResponse>> list(
            @PathVariable UUID shopId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(saleService.listByShop(shopId));
    }

    @PostMapping
    public ResponseEntity<OrderDto.SaleResponse> create(
            @PathVariable UUID shopId,
            @Valid @RequestBody OrderDto.SaleCreateRequest request,
            @AuthenticationPrincipal User currentUser) {
        OrderDto.SaleResponse response = saleService.createPosSale(shopId, request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
