package com.example.evcharging.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreatePromoRequest(
        @NotBlank String code,
        @Min(0) @Max(100) double percentageDiscount
) {}
