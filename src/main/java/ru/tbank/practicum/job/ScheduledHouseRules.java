package ru.tbank.practicum.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.metrics.HouseRulesMetrics;
import ru.tbank.practicum.service.AutomationService;

@RequiredArgsConstructor
@Slf4j
@Component
public class ScheduledHouseRules {

    private final AutomationService automationService;

    private final HouseRulesMetrics metrics;

    @Scheduled(cron = "${app.house-rules.rate}")
    private void checkHouseRules() {
        try {
            metrics.timer(automationService::process);
            metrics.incrementSuccess();
        } catch (Exception e) {
            metrics.incrementError();
            log.error("Не удалаось применить правила", e);
        }
    }
}
