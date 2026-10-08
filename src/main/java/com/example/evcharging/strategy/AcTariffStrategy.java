package com.example.evcharging.strategy;

import com.example.evcharging.config.TariffProperties;
import com.example.evcharging.model.ConnectorType;
import org.springframework.stereotype.Component;

@Component
public class AcTariffStrategy extends TieredTariffStrategy {
    public AcTariffStrategy(TariffProperties properties) {
        super(properties.ac().minimumCharge(), properties.ac().toTariffSlabs());
    }

    @Override
    public ConnectorType supports() {
        return ConnectorType.AC;
    }
}
