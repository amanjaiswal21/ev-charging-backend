package com.example.evcharging.dto;

import com.example.evcharging.model.ConnectorType;
import com.example.evcharging.model.SessionStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChargingSessionResponse(
        String id,
        String driverId,
        String stationId,
        String connectorId,
        ConnectorType requestedConnectorType,
        ConnectorType actualConnectorType,
        ConnectorType billingConnectorType,
        SessionStatus status,
        LocalDateTime startTime,
        LocalDateTime endTime,
        double energyDeliveredKwh,
        double finalCost,
        String promoCode,
        double promoDiscountPercentage
) {}
