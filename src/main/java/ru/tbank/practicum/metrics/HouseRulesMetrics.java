package ru.tbank.practicum.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class HouseRulesMetrics {

    private final Timer processTimer;
    private final Counter successCounter;
    private final Counter errorCounter;

    public HouseRulesMetrics(MeterRegistry meterRegistry) {
        this.processTimer = Timer.builder("house_rules_process_duration")
                .description("Duration of house rules processing")
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.successCounter = Counter.builder("house_rules_process_success_total")
                .description("Total successful house rules runs")
                .register(meterRegistry);

        this.errorCounter = Counter.builder("house_rules_process_error_total")
                .description("Total failed house rules runs")
                .register(meterRegistry);
    }

    public void incrementSuccess() {
        successCounter.increment();
    }

    public void incrementError() {
        errorCounter.increment();
    }

    public void timer(Runnable f) {
        processTimer.record(f);
    }
}
