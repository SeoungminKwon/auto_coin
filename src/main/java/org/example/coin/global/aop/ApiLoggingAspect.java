package org.example.coin.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.example.coin.global.logging.Service.LoggingService;
import org.example.coin.global.logging.entity.ApiLog;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect {

    private final LoggingService loggingService;

    // ✅ 성공한 API 요청 로그 저장 (INFO)
    @AfterReturning(pointcut = "execution(* org.example.coin.domain..controller..*(..))", returning = "result")
    public void logSuccess(JoinPoint joinPoint, Object result) {
        logApiRequest(joinPoint, "INFO", null);
    }

    // ❌ 예외 발생 시 ERROR 로그 저장
    @AfterThrowing(pointcut = "execution(* org.example.coin.domain..controller..*(..))", throwing = "ex")
    public void logError(JoinPoint joinPoint, Exception ex) {
        logApiRequest(joinPoint, "ERROR", ex.getMessage());
    }

    private void logApiRequest(JoinPoint joinPoint, String logLevel, String errorMessage) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return;

        HttpServletRequest request = attributes.getRequest();
        String apiMethod = joinPoint.getSignature().toShortString();
        String url = request.getRequestURI();
        String params = request.getQueryString();
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        ApiLog log;
        if ("ERROR".equals(logLevel)) {
            log = ApiLog.error(apiMethod, url, params, ip, userAgent, errorMessage);
        } else {
            log = ApiLog.info(apiMethod, url, params, ip, userAgent);
        }

        loggingService.saveLog(log);
    }
}
