package org.example.coin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KoreaInvestmentWebSocketClient implements WebSocketHandler {

    public static final String tr_key = "DNASAAPL";

    private static final String WS_URL = "ws://ops.koreainvestment.com:21000"; // 한국투자증권 WebSocket 서버 주소
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱을 위한 ObjectMapper
    private WebSocketSession session; // 현재 WebSocket 세션

    private final ApprovalService approvalService; // Approval Key 발급 서비스

    /**
     * Spring Bean이 생성된 후 자동으로 실행됨.
     * WebSocket 서버에 연결을 시도한다.
     */
//    @PostConstruct
//    public void connect() {
//        WebSocketClient client = new StandardWebSocketClient(); // WebSocket 클라이언트 생성
//        WebSocketHttpHeaders headers = new WebSocketHttpHeaders(); // WebSocket 요청 헤더 (필요하면 수정 가능)
//        URI uri = URI.create(WS_URL); // WebSocket 서버의 URI 생성
//
//        // 비동기 방식으로 WebSocket 연결을 시도함
//        CompletableFuture<WebSocketSession> futureSession = client.execute(this, headers, uri);
//
//        futureSession.thenAccept(session -> {
//            this.session = session;
//            log.info("✅ WebSocket 연결 완료!");
//            // Approval Key 발급 후, 실시간 시세 구독 요청
//            approvalService.getWebSocketApprovalKey().subscribe(this::sendSubscription);
//        }).exceptionally(e -> {
//            log.error("WebSocket 연결 실패", e);
//            return null;
//        });
//    }

    /**
     * WebSocket 연결이 성공적으로 맺어진 경우 호출됨.
     *
     * @param session WebSocket 세션 객체
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.session = session;
        log.info("✅ WebSocket 연결 완료!");
    }

    /**
     * WebSocket 서버에서 메시지를 수신할 때 호출됨.
     * JSON 형식의 데이터를 파싱하여 로그로 출력.
     *
     * @param session WebSocket 세션 객체
     * @param message 수신된 메시지
     * @throws IOException JSON 파싱 오류 발생 시 예외 처리
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        String payload = message.getPayload().toString();
        log.info("📩 수신 데이터: {}", payload);

        // 1️⃣ JSON 데이터인지 확인 (JSON은 `{`로 시작함)
        if (payload.trim().startsWith("{")) {
            try {
                JsonNode jsonNode = objectMapper.readTree(payload);

                // ✅ PING 메시지 체크
                if (jsonNode.has("header") && jsonNode.get("header").has("tr_id") &&
                        "PINGPONG".equals(jsonNode.get("header").get("tr_id").asText())) {
                    log.info("🟢 PING 메시지 수신 - 응답 없음 (무시)");
                    return;
                }

                // ✅ 정상적인 응답 데이터 체크
                if (!jsonNode.has("body") || !jsonNode.get("body").has("rt_cd")) {
                    log.warn("⚠️ JSON 응답 데이터 형식 오류: {}", payload);
                    return;
                }

                log.info("✅ JSON 응답 데이터 처리 완료");
                return;
            } catch (Exception e) {
                log.error("🚨 JSON 데이터 파싱 오류: {}", payload, e);
                return;
            }
        }

        // 2️⃣ "|" 로 구분된 실시간 데이터 처리
        try {
            String[] parts = payload.split("\\|");
            if (parts.length < 4) {
                log.warn("⚠️ 잘못된 실시간 데이터 형식: {}", payload);
                return;
            }

            String trId = parts[1];
            String recordCount = parts[2];
            String rawData = parts[3];

            log.info("📊 실시간 데이터 수신 - TR_ID: {}, 건수: {}", trId, recordCount);

            // 3️⃣ "^"로 구분된 데이터 파싱
            String[] dataFields = rawData.split("\\^");
            if (dataFields.length < 5) {
                log.warn("⚠️ 데이터 필드 개수가 부족함: {}", rawData);
                return;
            }

            // 데이터 출력 (예제: 첫 번째 값은 종목 코드)
            String stockCode = dataFields[0];
            String stockName = dataFields[1];
            String price = dataFields[8];
            log.info("📈 종목 [{} - {}] 현재가: {}", stockCode, stockName, price);
        } catch (Exception e) {
            log.error("🚨 실시간 데이터 파싱 오류: {}", payload, e);
        }
    }

    /**
     * WebSocket을 통해 특정 종목의 실시간 데이터를 구독 요청을 보냄.
     *
     * @param approvalKeyJson 한국투자증권 API에서 발급받은 승인 키
     */
    private void sendSubscription(String approvalKeyJson) {
        if (session == null || !session.isOpen()) {
            log.warn("❌ WebSocket 세션이 닫혀있습니다. 연결 확인 필요!");
            return;
        }

        try {
            // ✅ `approval_key` JSON에서 String 값만 추출
            JsonNode jsonNode = objectMapper.readTree(approvalKeyJson);
            String approvalKey = jsonNode.get("approval_key").asText();

            Map<String, Object> requestMap = new HashMap<>();

            // Header Map 생성
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("approval_key", approvalKey);
            headerMap.put("tr_type", "1"); // 1: 등록, 2: 해제
            headerMap.put("custtype", "P"); // B: 법인, P: 개인
            headerMap.put("content-type", "utf-8");

            // Body Map 생성
            Map<String, Map> bodyMap = new HashMap<>();
            HashMap<String, String> input = new HashMap<>();
            input.put("tr_id", "HDFSCNT0");
            input.put("tr_key", tr_key);
            bodyMap.put("input", input);
            // Header와 Body를 전체 Map에 추가
            requestMap.put("header", headerMap);
            requestMap.put("body", bodyMap);

            String jsonRequest = objectMapper.writeValueAsString(requestMap);
            session.sendMessage(new TextMessage(jsonRequest));

            log.info("📡 WebSocket 구독 요청 전송: {}", jsonRequest);
        } catch (IOException e) {
            log.error("WebSocket 구독 요청 실패", e);
        }
    }

    /**
     * WebSocket 연결 중 오류가 발생하면 호출됨.
     *
     * @param session WebSocket 세션 객체
     * @param exception 발생한 예외
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("⚠️ WebSocket 오류 발생", exception);
    }

    /**
     * WebSocket 연결이 닫힐 때 호출됨.
     *
     * @param session WebSocket 세션 객체
     * @param status 연결 종료 상태
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.warn("🔴 WebSocket 연결 종료: {}", status);
    }

    /**
     * WebSocket에서 부분 메시지를 지원할지 여부를 반환함.
     * 기본적으로 false를 반환하여 부분 메시지를 사용하지 않음.
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}