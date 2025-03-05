package org.example.coin.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${api.koreainvestment.base-url}")
    private String koreainvestmentBaseUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    // 한국투자증권 WebClient 빈 등록
    @Bean
    public WebClient koreaInvestmentWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(koreainvestmentBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
