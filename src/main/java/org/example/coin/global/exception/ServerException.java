package org.example.coin.global.exception;

public class ServerException extends BaseException {
    public ServerException(String message) {
        super(message, 500, "INTERNAL_SERVER_ERROR");
    }
}