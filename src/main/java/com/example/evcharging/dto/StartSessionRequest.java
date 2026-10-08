package com.example.evcharging.dto;

import com.example.evcharging.model.ConnectorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StartSessionRequest(
        @NotBlank String driverId,
        double latitude,
        double longitude,
        @Positive double radiusKm,
        @NotNull ConnectorType requestedConnectorType,
        String promoCode
) {}
