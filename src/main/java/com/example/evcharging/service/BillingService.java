package com.example.evcharging.service;

import com.example.evcharging.exception.BadRequestException;
import com.example.evcharging.model.ConnectorType;
import com.example.evcharging.strategy.TariffStrategy;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class BillingService {
    private final Map<ConnectorType, TariffStrategy> tariffStrategies = new EnumMap<>(ConnectorType.class);

    public BillingService(List<TariffStrategy> strategies) {
        strategies.forEach(strategy -> tariffStrategies.put(strategy.supports(), strategy));
    }

    public double calculate(ConnectorType billingType, double energyKwh, double promoDiscountPercentage) {
        if (promoDiscountPercentage < 0.0 || promoDiscountPercentage > 100.0) {
            throw new BadRequestException("Promo discount must be between 0 and 100");
        }

        TariffStrategy strategy = tariffStrategies.get(billingType);
        if (strategy == null) {
            throw new BadRequestException("No tariff configured for: " + billingType);
        }

        double amount = strategy.calculate(energyKwh);
        amount = amount * (1.0 - promoDiscountPercentage / 100.0);

        return Math.round(amount * 100.0) / 100.0;
    }
}
