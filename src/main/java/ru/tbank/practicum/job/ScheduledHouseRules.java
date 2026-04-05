package ru.tbank.practicum.job;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.service.AutomationService;

@RequiredArgsConstructor
@Component
public class ScheduledHouseRules {

    private final AutomationService automationService;

    @Scheduled(cron = "${app.house-rules.rate}")
    private void checkHouseRules() {
        try {
            automationService.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
