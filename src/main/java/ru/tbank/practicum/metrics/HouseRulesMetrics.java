package ru.tbank.practicum.metrics;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class HouseRulesMetrics {

    private final Timer processTimer;
    private final Counter successCounter;
    private final Counter errorCounter;
    private final AtomicInteger runningNow;

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

        this.runningNow = meterRegistry.gauge("house_rules_process_running", new AtomicInteger(0));
    }

    public Timer timer() {
        return processTimer;
    }

    public Counter successCounter() {
        return successCounter;
    }

    public Counter errorCounter() {
        return errorCounter;
    }

    public AtomicInteger runningNow() {
        return runningNow;
    }
}