package org.example.coin.global.exception;

public class BadRequestException extends BaseException{
    public BadRequestException(String message) {
        super(message, 400, "BAD_REQUEST");
    }
}
