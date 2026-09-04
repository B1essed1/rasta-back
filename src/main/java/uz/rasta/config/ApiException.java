package uz.rasta.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final String code;
    private final HttpStatus status;

    public ApiException(String code, HttpStatus status) {
        super(code);
        this.code = code;
        this.status = status;
    }

    public static ApiException notFound(String code) {
        return new ApiException(code, HttpStatus.NOT_FOUND);
    }

    public static ApiException badRequest(String code) {
        return new ApiException(code, HttpStatus.BAD_REQUEST);
    }

    public static ApiException forbidden(String code) {
        return new ApiException(code, HttpStatus.FORBIDDEN);
    }

    public static ApiException conflict(String code) {
        return new ApiException(code, HttpStatus.CONFLICT);
    }
}
