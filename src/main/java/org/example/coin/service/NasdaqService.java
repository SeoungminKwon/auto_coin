package org.example.coin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class NasdaqService {

    @Value("${api.koreainvestment.app.key}")
    private String koreaInvestmentAppKey;

    @Value("${api.koreainvestment.secret.key}")
    private String koreaInvestmentSecretKey;

    private final WebClient webClient;
    private final ApprovalService approvalService;
    private final ObjectMapper objectMapper;

    public String getNasdaqIndex(String startDate, String endDate) {
        String url = "https://openapi.koreainvestment.com:9443/uapi/overseas-price/v1/quotations/inquire-daily-chartprice"
                + "?FID_COND_MRKT_DIV_CODE=N&FID_INPUT_ISCD=COMP"
                + "&FID_INPUT_DATE_1=" + startDate + "&FID_INPUT_DATE_2=" + endDate
                + "&FID_PERIOD_DIV_CODE=D";

        log.info("getNasdaqIndex url={}", url);
        String tokenRes = approvalService.getTokenApprovalKey().block();
        String token = "";
        try {
            JsonNode jsonNode = objectMapper.readTree(tokenRes);
            token = jsonNode.get("access_token").asText();
        } catch (Exception e) {
            log.warn("ObjectMapper 파싱 실패");
        }

        log.info("getNasdaqIndex token={}", token);
        log.info("authorization: Bearer {}",token);

        Mono<String> result = webClient.get()
                .uri(url)
                .header("content-type", "application/json")
                .header("authorization", "Bearer " + token)
                .header("appkey", koreaInvestmentAppKey)
                .header("appsecret", koreaInvestmentSecretKey)
                .header("tr_id", "FHKST03030100")
                .retrieve()
                .bodyToMono(String.class);

        return result.block();


    }
}
