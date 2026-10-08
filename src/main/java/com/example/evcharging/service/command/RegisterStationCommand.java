package com.example.evcharging.service.command;

import lombok.Builder;

import java.util.List;

@Builder
public record RegisterStationCommand(
        String name,
        double latitude,
        double longitude,
        List<ConnectorCommand> connectors
) {}
