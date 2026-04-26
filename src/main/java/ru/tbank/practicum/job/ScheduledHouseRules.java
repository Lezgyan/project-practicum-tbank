package ru.tbank.practicum.job;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.metrics.HouseRulesMetrics;
import ru.tbank.practicum.service.AutomationService;

@RequiredArgsConstructor
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
            e.printStackTrace();
        }
    }
}
