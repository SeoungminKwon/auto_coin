package org.example.coin.global.res;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ErrorResponse extends ApiResponse{
    private final String error;
    private final String message;
    private final int status;

    public ErrorResponse(String error, String message, HttpStatus status) {
        super(false);
        this.error = error;
        this.message = message;
        this.status = status.value();
    }

    // ✅ 실패 응답 (예외 핸들링용)
    public static ResponseEntity<ErrorResponse> of(String error, String message, HttpStatus status) {
        return ResponseEntity.status(status).body(new ErrorResponse(error, message, status));
    }
}
