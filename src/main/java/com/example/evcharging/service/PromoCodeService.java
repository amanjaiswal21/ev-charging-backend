package com.example.evcharging.service;

import com.example.evcharging.dto.CreatePromoRequest;
import com.example.evcharging.model.PromoCode;
import com.example.evcharging.model.PromoType;
import com.example.evcharging.repository.PromoCodeRepository;
import org.springframework.stereotype.Service;

@Service
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public PromoCode create(CreatePromoRequest request) {
        PromoCode promo = new PromoCode(
                request.code().toUpperCase(),
                PromoType.PERCENTAGE,
                request.percentageDiscount(),
                true
        );
        return promoCodeRepository.save(promo);
    }

    public void delete(String code) {
        promoCodeRepository.delete(code);
    }
}
