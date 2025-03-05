package org.example.coin.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.coin.global.res.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BaseException ex) {
        logException(ex);
        return ErrorResponse.of(ex.getErrorCode(), ex.getMessage(), HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logException(ex);
        return ErrorResponse.of("UNKNOWN_ERROR", "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void logException(Exception ex) {

        // 현재 시간 포맷
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        log.error("========= Unhandled Exception =========");
        log.error("Timestamp: {}", timestamp);
        log.error("Message: {}", ex.getMessage());
        log.error("Stack Trace: ", ex);

    }
}
