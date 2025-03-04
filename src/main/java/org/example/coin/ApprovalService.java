package org.example.coin;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    @Value("${api.koreainvestment.app.key}")
    private String koreainvestmentAppKey;

    @Value("${api.koreainvestment.secret.key}")
    private String koreainvestmentSecretKey;

    private final WebClient koreaInvestmentWebClient;


    public Mono<String> getApprovalKey() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "client_credentials");
        requestBody.put("appkey", koreainvestmentAppKey);
        requestBody.put("secretkey", koreainvestmentSecretKey);

        return koreaInvestmentWebClient.post()
                .uri("/oauth2/Approval")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }
}
