package uz.rasta.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final long timestamp;
    private final int status;
    private final String code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .timestamp(System.currentTimeMillis())
                .status(200)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .timestamp(System.currentTimeMillis())
                .status(201)
                .data(data)
                .build();
    }

    public static ApiResponse<Void> error(int status, String code, String message) {
        return ApiResponse.<Void>builder()
                .timestamp(System.currentTimeMillis())
                .status(status)
                .code(code)
                .message(message)
                .build();
    }
}
