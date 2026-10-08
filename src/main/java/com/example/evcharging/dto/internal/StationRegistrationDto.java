package com.example.evcharging.dto.internal;

import lombok.Builder;

import java.util.List;

@Builder
public record StationRegistrationDto(
        String name,
        double latitude,
        double longitude,
        List<ConnectorDto> connectors
) {}
