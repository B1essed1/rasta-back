package uz.rasta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.rasta.dto.AuthRequest;
import uz.rasta.dto.AuthResponse;
import uz.rasta.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-code")
    public ResponseEntity<AuthResponse> sendCode(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.sendCode(request.phone());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verify(@Valid @RequestBody AuthRequest request) {
        if (request.code() == null || request.code().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(AuthResponse.message("Verification code is required"));
        }
        AuthResponse response = authService.verify(request.phone(), request.code());
        return ResponseEntity.ok(response);
    }
}
