package ru.tbank.practicum.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.enums.HouseRulesProcessResult;

import java.util.concurrent.TimeUnit;

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
