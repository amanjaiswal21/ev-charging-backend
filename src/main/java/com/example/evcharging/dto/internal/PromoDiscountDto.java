package com.example.evcharging.dto.internal;

public record PromoDiscountDto(String code, double percentage) {
    public static PromoDiscountDto none() {
        return new PromoDiscountDto(null, 0.0);
    }
}
