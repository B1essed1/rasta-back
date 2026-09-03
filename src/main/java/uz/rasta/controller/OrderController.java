package uz.rasta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.rasta.dto.OrderDto;
import uz.rasta.entity.User;
import uz.rasta.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shops/{shopId}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto.Response>> list(
            @PathVariable UUID shopId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(orderService.listByShop(shopId));
    }

    @PostMapping
    public ResponseEntity<OrderDto.Response> create(
            @PathVariable UUID shopId,
            @Valid @RequestBody OrderDto.CreateRequest request) {
        OrderDto.Response response = orderService.create(shopId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<OrderDto.Response> confirm(
            @PathVariable UUID shopId,
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(orderService.confirmOrder(shopId, id, currentUser));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDto.Response> updateStatus(
            @PathVariable UUID shopId,
            @PathVariable UUID id,
            @Valid @RequestBody OrderDto.StatusRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(orderService.updateStatus(shopId, id, request.status(), currentUser));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderDto.Response> cancel(
            @PathVariable UUID shopId,
            @PathVariable UUID id,
            @Valid @RequestBody OrderDto.CancelRequest request) {
        return ResponseEntity.ok(orderService.cancelOrder(shopId, id, request));
    }
}
