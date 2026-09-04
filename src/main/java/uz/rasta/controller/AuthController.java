package uz.rasta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.rasta.config.ApiException;
import uz.rasta.config.ApiResponse;
import uz.rasta.dto.AuthRequest;
import uz.rasta.dto.AuthResponse;
import uz.rasta.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse<AuthResponse>> sendCode(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.sendCode(request.phone());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<AuthResponse>> verify(@Valid @RequestBody AuthRequest request) {
        if (request.code() == null || request.code().isBlank()) {
            throw ApiException.badRequest("auth.code.invalid");
        }
        AuthResponse response = authService.verify(request.phone(), request.code());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
