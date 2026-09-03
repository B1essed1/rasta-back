package uz.rasta.dto;

import java.util.UUID;

public record AuthResponse(
        String token,
        UserInfo user,
        String message
) {
    public record UserInfo(
            UUID id,
            String phone,
            String name
    ) {
    }

    public static AuthResponse message(String message) {
        return new AuthResponse(null, null, message);
    }

    public static AuthResponse authenticated(String token, UUID id, String phone, String name) {
        return new AuthResponse(token, new UserInfo(id, phone, name), "Authentication successful");
    }
}
