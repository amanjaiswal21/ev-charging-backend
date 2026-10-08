package com.example.evcharging.strategy;

import com.example.evcharging.model.ConnectorType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AcTariffStrategy extends TieredTariffStrategy {
    public AcTariffStrategy() {
        super(50.0, List.of(
                new TariffSlab(20.0, 10.0),
                new TariffSlab(Double.POSITIVE_INFINITY, 8.0)
        ));
    }

    @Override
    public ConnectorType supports() {
        return ConnectorType.AC;
    }
}
