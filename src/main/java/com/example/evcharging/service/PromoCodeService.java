package com.example.evcharging.service;

import lombok.RequiredArgsConstructor;

import com.example.evcharging.exception.BadRequestException;
import com.example.evcharging.model.PromoCode;
import com.example.evcharging.model.PromoType;
import com.example.evcharging.repository.PromoCodeRepository;
import com.example.evcharging.dto.internal.PromoCodeCreationDto;
import com.example.evcharging.dto.internal.PromoDiscountDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    public PromoCode create(PromoCodeCreationDto input) {
        if (input.percentageDiscount() < 0.0 || input.percentageDiscount() > 100.0) {
            throw new BadRequestException("Promo discount must be between 0 and 100");
        }

        PromoCode promo = PromoCode.builder()
                .code(normalizeRequired(input.code()))
                .type(PromoType.PERCENTAGE)
                .value(input.percentageDiscount())
                .active(true)
                .build();
        return promoCodeRepository.save(promo);
    }

    public void delete(String code) {
        promoCodeRepository.delete(normalizeRequired(code));
    }

    public PromoDiscountDto resolveDiscount(String code) {
        if (code == null || code.isBlank()) {
            return PromoDiscountDto.none();
        }

        String normalizedCode = normalizeRequired(code);
        PromoCode promo = promoCodeRepository.findByCode(normalizedCode)
                .filter(PromoCode::isActive)
                .orElseThrow(() -> new BadRequestException("Invalid promo code: " + normalizedCode));

        return new PromoDiscountDto(promo.getCode(), promo.getValue());
    }

    private String normalizeRequired(String code) {
        if (code == null || code.isBlank()) {
            throw new BadRequestException("Promo code is required");
        }
        return code.trim().toUpperCase();
    }
}
