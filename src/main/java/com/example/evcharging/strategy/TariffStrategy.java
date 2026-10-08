package com.example.evcharging.strategy;

import com.example.evcharging.model.ConnectorType;

/**
 * Strategy interface keeps tariff logic separate from session orchestration.
 * Adding a new connector tariff should require a new strategy, not changes all over the service.
 */
public interface TariffStrategy {
    ConnectorType supports();
    double calculate(double energyKwh);
}
