package org.example.coin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalService {

    @Value("${api.koreainvestment.app.key}")
    private String koreainvestmentAppKey;

    @Value("${api.koreainvestment.secret.key}")
    private String koreainvestmentSecretKey;

    private final WebClient koreaInvestmentWebClient;
    private String cachedToken;
    private Instant tokenExpiryTime;


    public Mono<String> getApprovalKey(String uri) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "client_credentials");
        requestBody.put("appkey", koreainvestmentAppKey);
        requestBody.put("secretkey", koreainvestmentSecretKey);

        return koreaInvestmentWebClient.post()
                .uri(uri)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getWebSocketApprovalKey() {
        return getApprovalKey("/oauth2/Approval");
    }

    public Mono<String> getTokenApprovalKey() {
        // 🔹 1. 기존 토큰이 유효하면 그대로 반환
        if (cachedToken != null && tokenExpiryTime != null && Instant.now().isBefore(tokenExpiryTime)) {
            log.info("✅ 기존 Token Approval Key 사용 (만료 시간: {})", tokenExpiryTime);
            return Mono.just(cachedToken);
        }

        // 🔹 2. 새로운 토큰 요청
        log.info("🔄 새로운 Token Approval Key 발급 요청");
        Mono<String> result = null;
        try {
            result = getApprovalKey("/oauth2/tokenP")
                    .map(token -> {
                        cachedToken = token;
                        tokenExpiryTime = Instant.now().plus(Duration.ofHours(24)); // 24시간 후 만료
                        log.info("✅ 새 Token Approval Key 발급 완료! (만료 시간: {})", tokenExpiryTime);
                        return token;
                    });

        } catch (Exception e) {
            log.warn("새로운 token 발급 실패 !!!");
        }
        return result;
    }
}
