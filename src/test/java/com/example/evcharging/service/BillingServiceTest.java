package com.example.evcharging.service;

import com.example.evcharging.exception.BadRequestException;
import com.example.evcharging.model.ConnectorType;
import com.example.evcharging.strategy.AcTariffStrategy;
import com.example.evcharging.strategy.DcTariffStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BillingServiceTest {
    private final BillingService billingService = new BillingService(List.of(
            new AcTariffStrategy(),
            new DcTariffStrategy()
    ));

    @Test
    void appliesDcMinimumChargeAndTieredSlabs() {
        assertEquals(150.0, billingService.calculate(ConnectorType.DC, 5.0, 0.0));
        assertEquals(200.0, billingService.calculate(ConnectorType.DC, 10.0, 0.0));
        assertEquals(340.0, billingService.calculate(ConnectorType.DC, 20.0, 0.0));
        assertEquals(455.0, billingService.calculate(ConnectorType.DC, 30.0, 0.0));
    }

    @Test
    void appliesAcTariff() {
        assertEquals(50.0, billingService.calculate(ConnectorType.AC, 4.0, 0.0));
        assertEquals(240.0, billingService.calculate(ConnectorType.AC, 25.0, 0.0));
    }

    @Test
    void appliesPromoCodeDiscountAfterTariffAndMinimumCharge() {
        assertEquals(306.0, billingService.calculate(ConnectorType.DC, 20.0, 10.0));
        assertEquals(135.0, billingService.calculate(ConnectorType.DC, 5.0, 10.0));
    }

    @Test
    void acRequestedButServedOnDcUsesAcBillingType() {
        assertEquals(240.0, billingService.calculate(ConnectorType.AC, 25.0, 0.0));
    }

    @Test
    void rejectsInvalidPromoDiscounts() {
        assertThrows(BadRequestException.class, () -> billingService.calculate(ConnectorType.DC, 10.0, 101.0));
    }
}
