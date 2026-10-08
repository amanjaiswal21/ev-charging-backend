package com.example.evcharging.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RegisterStationRequest(
        @NotBlank String name,
        double latitude,
        double longitude,
        @NotEmpty List<@Valid ConnectorRequest> connectors
) {}
