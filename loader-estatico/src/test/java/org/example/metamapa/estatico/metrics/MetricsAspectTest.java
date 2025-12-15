package org.example.metamapa.estatico.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.search.Search;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {BaseMetricsAspect.class, MetricsAspectTest.TestConfig.class, MetricsAspectTest.DummyService.class})
class MetricsAspectTest {

    @Autowired
    DummyService dummyService;

    @Autowired
    MeterRegistry registry;

    @Test
    void aspectRecordsTimerAndCounters() {
        // success call
        dummyService.perform(false);

        // failing call
        try {
            dummyService.perform(true);
        } catch (RuntimeException ignored) {
        }

        // verify timer recorded for service.calls
        assertThat(registry.find("service.calls").timer()).isNotNull();
        assertThat(registry.find("service.calls").timer().count()).isGreaterThanOrEqualTo(1);

        // verify error counter exists
        assertThat(registry.find("service.calls.errors").counters()).isNotEmpty();
    }

    @Configuration
    static class TestConfig {
        @Bean
        public MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }

    @Service
    static class DummyService {
        public String perform(boolean fail) {
            if (fail) throw new RuntimeException("boom");
            return "ok";
        }
    }
}
