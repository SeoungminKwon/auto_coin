package org.example.coin.global.logging.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.coin.global.logging.LogType;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "api_logs")
public class ApiLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LogType logType; // 로그 타입 (INFO, DEBUG, WARN, ERROR)

    private String apiMethod; // API 메서드 (GET, POST 등)
    private String url; // 요청 URL
    private String params; // 요청 파라미터
    private String ip; // 사용자 IP
    private String userAgent; // 브라우저 정보
    private String errorMessage; // 실패 시 오류 메시지
    private LocalDateTime timestamp; // 요청 시간

    public ApiLog(LogType logType, String apiMethod, String url, String params, String ip, String userAgent, String errorMessage) {
        this.logType = logType;
        this.apiMethod = apiMethod;
        this.url = url;
        this.params = params;
        this.ip = ip;
        this.userAgent = userAgent;
        this.errorMessage = errorMessage;
        this.timestamp = LocalDateTime.now();
    }

    // ✅ INFO 로그
    public static ApiLog info(String apiMethod, String url, String params, String ip, String userAgent) {
        return new ApiLog(LogType.INFO, apiMethod, url, params, ip, userAgent, null);
    }

    // ✅ DEBUG 로그
    public static ApiLog debug(String apiMethod, String url, String params, String ip, String userAgent) {
        return new ApiLog(LogType.DEBUG, apiMethod, url, params, ip, userAgent, null);
    }

    // ✅ WARN 로그
    public static ApiLog warn(String apiMethod, String url, String params, String ip, String userAgent, String warningMessage) {
        return new ApiLog(LogType.WARN, apiMethod, url, params, ip, userAgent, warningMessage);
    }

    // ✅ ERROR 로그 (에러 메시지 포함)
    public static ApiLog error(String apiMethod, String url, String params, String ip, String userAgent, String errorMessage) {
        return new ApiLog(LogType.ERROR, apiMethod, url, params, ip, userAgent, errorMessage);
    }
}
