package com.example.evcharging.strategy;

import com.example.evcharging.config.TariffProperties;
import com.example.evcharging.model.ConnectorType;
import org.springframework.stereotype.Component;

@Component
public class DcTariffStrategy extends TieredTariffStrategy {
    public DcTariffStrategy(TariffProperties properties) {
        super(properties.dc().minimumCharge(), properties.dc().toTariffSlabs());
    }

    @Override
    public ConnectorType supports() {
        return ConnectorType.DC;
    }
}
