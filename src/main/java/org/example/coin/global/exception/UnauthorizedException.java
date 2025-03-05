package org.example.coin.global.exception;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        super("Unauthorized access.", 401, "UNAUTHORIZED");
    }
}