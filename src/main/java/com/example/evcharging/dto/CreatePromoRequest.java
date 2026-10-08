package com.example.evcharging.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record CreatePromoRequest(
        @NotBlank String code,
        @DecimalMin("0.0") @DecimalMax("100.0") double percentageDiscount
) {}
