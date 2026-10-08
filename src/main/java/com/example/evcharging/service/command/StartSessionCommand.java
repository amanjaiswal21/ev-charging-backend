package com.example.evcharging.service.command;

import com.example.evcharging.model.ConnectorType;
import lombok.Builder;

@Builder
public record StartSessionCommand(
        String driverId,
        double latitude,
        double longitude,
        double radiusKm,
        ConnectorType requestedConnectorType,
        String promoCode
) {}
