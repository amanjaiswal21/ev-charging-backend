package com.example.evcharging.strategy;

public record TariffSlab(double upToKwh, double ratePerKwh) {
    public TariffSlab {
        if (upToKwh <= 0.0) {
            throw new IllegalArgumentException("Tariff slab limit must be positive");
        }
        if (ratePerKwh < 0.0) {
            throw new IllegalArgumentException("Tariff slab rate cannot be negative");
        }
    }
}
