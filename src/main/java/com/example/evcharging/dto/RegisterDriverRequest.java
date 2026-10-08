package com.example.evcharging.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterDriverRequest(
        @NotBlank String name,
        @NotBlank String vehicleRegistrationNumber
) {}
