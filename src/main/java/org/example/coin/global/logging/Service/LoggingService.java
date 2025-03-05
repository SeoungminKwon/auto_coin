package org.example.coin.global.logging.Service;

import lombok.RequiredArgsConstructor;
import org.example.coin.global.logging.entity.ApiLog;
import org.example.coin.global.logging.repository.ApiLogRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoggingService {
    private final ApiLogRepository apiLogRepository;

    public void saveLog(ApiLog log) {
        apiLogRepository.save(log);
    }
}
