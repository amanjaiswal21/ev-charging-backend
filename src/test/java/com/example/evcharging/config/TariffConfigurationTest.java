package com.example.evcharging.config;

import com.example.evcharging.model.ConnectorType;
import com.example.evcharging.service.BillingService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class TariffConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withInitializer(new ConfigDataApplicationContextInitializer())
            .withUserConfiguration(TariffTestConfiguration.class);

    @Test
    void overridesMinimumChargesLimitsAndRatesForBothConnectorTypes() {
        contextRunner.withPropertyValues(
                "charging.tariffs.ac.minimum-charge=75",
                "charging.tariffs.ac.slabs[0].up-to-kwh=5",
                "charging.tariffs.ac.slabs[0].rate-per-kwh=12",
                "charging.tariffs.ac.slabs[1].rate-per-kwh=6",
                "charging.tariffs.dc.minimum-charge=180",
                "charging.tariffs.dc.slabs[0].up-to-kwh=5",
                "charging.tariffs.dc.slabs[0].rate-per-kwh=30",
                "charging.tariffs.dc.slabs[1].up-to-kwh=15",
                "charging.tariffs.dc.slabs[1].rate-per-kwh=10",
                "charging.tariffs.dc.slabs[2].rate-per-kwh=5"
        ).run(context -> {
            assertThat(context).hasNotFailed();
            BillingService billing = context.getBean(BillingService.class);
            assertThat(billing.calculate(ConnectorType.AC, 1, 0)).isEqualTo(75.0);
            assertThat(billing.calculate(ConnectorType.AC, 10, 0)).isEqualTo(90.0);
            assertThat(billing.calculate(ConnectorType.DC, 1, 0)).isEqualTo(180.0);
            assertThat(billing.calculate(ConnectorType.DC, 20, 0)).isEqualTo(275.0);
        });
    }

    @Test
    void rejectsUnorderedSlabLimitsAtStartup() {
        contextRunner.withPropertyValues("charging.tariffs.dc.slabs[1].up-to-kwh=5")
                .run(context -> assertThat(context).hasFailed());
    }

    @Test
    void rejectsNegativeRatesAtStartup() {
        contextRunner.withPropertyValues("charging.tariffs.ac.slabs[0].rate-per-kwh=-1")
                .run(context -> assertThat(context).hasFailed());
    }

    @Test
    void rejectsBoundedFinalSlabAtStartup() {
        contextRunner.withPropertyValues("charging.tariffs.ac.slabs[1].up-to-kwh=40")
                .run(context -> assertThat(context).hasFailed());
    }

}
