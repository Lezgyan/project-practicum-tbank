package ru.tbank.practicum.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.enums.HouseRulesProcessResult;

@Component
@RequiredArgsConstructor
public class HouseRulesMetrics {

    private final MeterRegistry meterRegistry;

    public void record(HouseRulesProcessResult result, long durationNanos) {
        Timer.builder("house_rules_process")
                .description("Duration of house rules processing")
                .tag("result", result.name())
                .publishPercentileHistogram()
                .register(meterRegistry)
                .record(durationNanos, TimeUnit.NANOSECONDS);
    }
}
