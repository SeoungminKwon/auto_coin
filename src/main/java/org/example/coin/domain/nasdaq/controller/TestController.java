package org.example.coin.domain.nasdaq.controller;

import lombok.RequiredArgsConstructor;
import org.example.coin.global.exception.BadRequestException;
import org.example.coin.domain.nasdaq.service.NasdaqService;
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

        throw new BadRequestException("잘못된 요청입니다.");
//        return nasdaqService.getNasdaqIndex(startDate, endDate);
    }
}
