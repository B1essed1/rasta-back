package uz.rasta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthRequest(
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+998\\d{9}$", message = "Phone must be in +998XXXXXXXXX format")
        String phone,

        String code
) {
}
