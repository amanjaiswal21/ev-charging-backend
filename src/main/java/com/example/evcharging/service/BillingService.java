package com.example.evcharging.service;

import com.example.evcharging.exception.BadRequestException;
import com.example.evcharging.model.ConnectorType;
import com.example.evcharging.model.PromoCode;
import com.example.evcharging.repository.PromoCodeRepository;
import com.example.evcharging.strategy.TariffStrategy;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class BillingService {
    private final Map<ConnectorType, TariffStrategy> tariffStrategies = new EnumMap<>(ConnectorType.class);
    private final PromoCodeRepository promoCodeRepository;

    public BillingService(List<TariffStrategy> strategies, PromoCodeRepository promoCodeRepository) {
        strategies.forEach(strategy -> tariffStrategies.put(strategy.supports(), strategy));
        this.promoCodeRepository = promoCodeRepository;
    }

    public double calculate(ConnectorType billingType, double energyKwh, String promoCode) {
        TariffStrategy strategy = tariffStrategies.get(billingType);
        if (strategy == null) {
            throw new BadRequestException("No tariff configured for: " + billingType);
        }

        double amount = strategy.calculate(energyKwh);

        // Assumption for starter: tariff strategy applies minimum charge first, then promo is applied.
        if (promoCode != null && !promoCode.isBlank()) {
            PromoCode promo = promoCodeRepository.findByCode(promoCode)
                    .filter(PromoCode::isActive)
                    .orElseThrow(() -> new BadRequestException("Invalid promo code: " + promoCode));
            amount = amount * (1.0 - promo.getValue() / 100.0);
        }

        return Math.round(amount * 100.0) / 100.0;
    }
}
