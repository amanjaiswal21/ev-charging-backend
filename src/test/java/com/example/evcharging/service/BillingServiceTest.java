package com.example.evcharging.service;

import com.example.evcharging.config.TariffTestConfiguration;
import com.example.evcharging.exception.BadRequestException;
import com.example.evcharging.model.ConnectorType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(TariffTestConfiguration.class)
@TestPropertySource("classpath:application.properties")
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
class BillingServiceTest {
    @Autowired
    private BillingService billingService;

    @Test
    void appliesDcMinimumChargeAndTieredSlabs() {
        assertEquals(150.0, billingService.calculate(ConnectorType.DC, 5.0, 0.0), 0.000001);
        assertEquals(200.0, billingService.calculate(ConnectorType.DC, 10.0, 0.0), 0.000001);
        assertEquals(340.0, billingService.calculate(ConnectorType.DC, 20.0, 0.0), 0.000001);
        assertEquals(455.0, billingService.calculate(ConnectorType.DC, 30.0, 0.0), 0.000001);
    }

    @Test
    void appliesAcTariff() {
        assertEquals(50.0, billingService.calculate(ConnectorType.AC, 4.0, 0.0), 0.000001);
        assertEquals(240.0, billingService.calculate(ConnectorType.AC, 25.0, 0.0), 0.000001);
    }

    @Test
    void appliesPromoCodeDiscountAfterTariffAndMinimumCharge() {
        assertEquals(306.0, billingService.calculate(ConnectorType.DC, 20.0, 10.0), 0.000001);
        assertEquals(135.0, billingService.calculate(ConnectorType.DC, 5.0, 10.0), 0.000001);
    }

    @Test
    void acRequestedButServedOnDcUsesAcBillingType() {
        assertEquals(240.0, billingService.calculate(ConnectorType.AC, 25.0, 0.0), 0.000001);
    }

    @Test
    void rejectsInvalidPromoDiscounts() {
        assertThrows(BadRequestException.class, () -> billingService.calculate(ConnectorType.DC, 10.0, 101.0));
    }
}
