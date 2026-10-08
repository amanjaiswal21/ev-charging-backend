package com.example.evcharging.strategy;

import com.example.evcharging.model.ChargingStation;
import com.example.evcharging.model.Connector;
import com.example.evcharging.model.ConnectorType;

public record ConnectorAllocation(
        ChargingStation station,
        Connector connector,
        ConnectorType billingConnectorType,
        double distanceKm
) {}
