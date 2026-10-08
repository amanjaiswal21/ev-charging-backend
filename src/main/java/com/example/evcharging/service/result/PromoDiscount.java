package com.example.evcharging.service.result;

public record PromoDiscount(String code, double percentage) {
    public static PromoDiscount none() {
        return new PromoDiscount(null, 0.0);
    }
}
