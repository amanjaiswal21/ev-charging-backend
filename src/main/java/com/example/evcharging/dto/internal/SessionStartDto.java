package com.example.evcharging.dto.internal;

import com.example.evcharging.model.ConnectorType;
import lombok.Builder;

@Builder
public record SessionStartDto(
        String driverId,
        double latitude,
        double longitude,
        double radiusKm,
        ConnectorType requestedConnectorType,
        String promoCode
) {}
