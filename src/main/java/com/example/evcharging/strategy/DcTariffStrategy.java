package com.example.evcharging.strategy;

import com.example.evcharging.model.ConnectorType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DcTariffStrategy extends TieredTariffStrategy {
    public DcTariffStrategy() {
        super(150.0, List.of(
                new TariffSlab(10.0, 20.0),
                new TariffSlab(25.0, 14.0),
                new TariffSlab(Double.POSITIVE_INFINITY, 9.0)
        ));
    }

    @Override
    public ConnectorType supports() {
        return ConnectorType.DC;
    }
}
