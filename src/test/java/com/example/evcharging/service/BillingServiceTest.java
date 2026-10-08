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

import java.math.BigDecimal;

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
        assertEquals(new BigDecimal("150.00"), billingService.calculate(ConnectorType.DC, 5.0, 0.0));
        assertEquals(new BigDecimal("200.00"), billingService.calculate(ConnectorType.DC, 10.0, 0.0));
        assertEquals(new BigDecimal("340.00"), billingService.calculate(ConnectorType.DC, 20.0, 0.0));
        assertEquals(new BigDecimal("455.00"), billingService.calculate(ConnectorType.DC, 30.0, 0.0));
    }

    @Test
    void appliesAcTariff() {
        assertEquals(new BigDecimal("50.00"), billingService.calculate(ConnectorType.AC, 4.0, 0.0));
        assertEquals(new BigDecimal("240.00"), billingService.calculate(ConnectorType.AC, 25.0, 0.0));
    }

    @Test
    void appliesPromoCodeDiscountAfterTariffAndMinimumCharge() {
        assertEquals(new BigDecimal("306.00"), billingService.calculate(ConnectorType.DC, 20.0, 10.0));
        assertEquals(new BigDecimal("135.00"), billingService.calculate(ConnectorType.DC, 5.0, 10.0));
    }

    @Test
    void acRequestedButServedOnDcUsesAcBillingType() {
        assertEquals(new BigDecimal("240.00"), billingService.calculate(ConnectorType.AC, 25.0, 0.0));
    }

    @Test
    void rejectsInvalidPromoDiscounts() {
        assertThrows(BadRequestException.class, () -> billingService.calculate(ConnectorType.DC, 10.0, 101.0));
    }
}
