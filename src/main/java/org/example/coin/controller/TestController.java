package org.example.coin.controller;

import lombok.RequiredArgsConstructor;
import org.example.coin.service.NasdaqService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final NasdaqService nasdaqService;

    @GetMapping("/test")
    public Object test() {

        String startDate = "20000301";
        String endDate = "20250305";

        return nasdaqService.getNasdaqIndex(startDate, endDate);
    }
}
