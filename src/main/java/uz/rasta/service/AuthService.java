package uz.rasta.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.rasta.config.ApiException;
import uz.rasta.config.JwtUtil;
import uz.rasta.dto.AuthResponse;
import uz.rasta.entity.User;
import uz.rasta.repository.UserRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final SmsService smsService;
    private final JwtUtil jwtUtil;

    public AuthResponse sendCode(String phone) {
        smsService.sendCode(phone);
        return AuthResponse.message("Verification code sent to " + phone);
    }

    @Transactional
    public AuthResponse verify(String phone, String code) {
        if (!smsService.verifyCode(phone, code)) {
            throw ApiException.badRequest("auth.code.invalid");
        }

        User user = userRepository.findByPhone(phone)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .phone(phone)
                            .name(phone)
                            .build();
                    return userRepository.save(newUser);
                });

        user.setLastLoginAt(Instant.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getPhone());
        log.info("User {} authenticated successfully", phone);

        return AuthResponse.authenticated(token, user.getId(), user.getPhone(), user.getName());
    }
}
