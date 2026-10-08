package com.example.evcharging.strategy;

import com.example.evcharging.model.ConnectorType;
import org.springframework.stereotype.Component;

@Component
public class DcTariffStrategy implements TariffStrategy {
    @Override
    public ConnectorType supports() {
        return ConnectorType.DC;
    }

    @Override
    public double calculate(double energyKwh) {
        // TODO: Implement the tiered DC tariff from the problem statement.
        throw new UnsupportedOperationException("DC tariff not implemented yet");
    }
}
