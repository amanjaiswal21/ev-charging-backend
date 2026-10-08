package com.example.evcharging.dto;

import com.example.evcharging.model.PromoType;

public record PromoCodeResponse(String code, PromoType type, double value, boolean active) {}
