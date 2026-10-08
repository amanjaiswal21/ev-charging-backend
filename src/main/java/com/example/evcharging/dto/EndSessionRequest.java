package com.example.evcharging.dto;

import jakarta.validation.constraints.PositiveOrZero;

public record EndSessionRequest(
        @PositiveOrZero double energyDeliveredKwh
) {}
