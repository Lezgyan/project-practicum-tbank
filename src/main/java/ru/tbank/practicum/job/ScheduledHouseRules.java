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
        metrics.runningNow().set(1);
        try {
            metrics.timer().record(automationService::process);
            metrics.successCounter().increment();

        } catch (Exception e) {
            metrics.errorCounter().increment();
            e.printStackTrace();
        } finally {
            metrics.runningNow().set(0);
        }
    }
}
