package org.example.coin.global.res;

import lombok.Getter;

@Getter
public abstract class ApiResponse {
    private final boolean success;

    protected ApiResponse(boolean success) {
        this.success = success;
    }
}
