package org.example.coin.global.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final int status;
    private final String errorCode;

    public BaseException(String message, int status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}