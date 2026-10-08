package com.example.evcharging.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ChargingStationResponse(
        String id,
        String name,
        double latitude,
        double longitude,
        List<ConnectorResponse> connectors
) {}
