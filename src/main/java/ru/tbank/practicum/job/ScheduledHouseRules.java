package ru.tbank.practicum.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.enums.HouseRulesProcessResult;
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
        long start = System.nanoTime();

        try {
            automationService.process();
            metrics.record(HouseRulesProcessResult.SUCCESS, System.nanoTime() - start);
        } catch (Exception e) {
            metrics.record(HouseRulesProcessResult.FAILED, System.nanoTime() - start);
            log.error("Не удалаось применить правила", e);
        }
    }
}
