package org.example.coin.global.res;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SuccessResponse<T> extends ApiResponse{
    private T data;

    public SuccessResponse(T data) {
        super(true);
        this.data = data;
    }

    public static <T> ResponseEntity<SuccessResponse<T>> of(T data, HttpStatus status) {
        return ResponseEntity.status(status).body(new SuccessResponse<>(data));
    }


    public static <T> ResponseEntity<SuccessResponse<T>> of(T data) {
        return of(data, HttpStatus.OK);
    }
}
