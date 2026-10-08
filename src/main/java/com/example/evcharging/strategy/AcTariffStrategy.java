package com.example.evcharging.strategy;

import com.example.evcharging.model.ConnectorType;
import org.springframework.stereotype.Component;

@Component
public class AcTariffStrategy implements TariffStrategy {
    @Override
    public ConnectorType supports() {
        return ConnectorType.AC;
    }

    @Override
    public double calculate(double energyKwh) {
        // TODO: Fill AC slab rates after stating the assumption in README.
        throw new UnsupportedOperationException("AC tariff not implemented yet");
    }
}
