package com.example.evcharging.repository;

import com.example.evcharging.model.PromoCode;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PromoCodeRepository {
    private final ConcurrentHashMap<String, PromoCode> storage = new ConcurrentHashMap<>();

    public PromoCode save(PromoCode promoCode) {
        storage.put(promoCode.getCode().toUpperCase(), promoCode);
        return promoCode;
    }

    public Optional<PromoCode> findByCode(String code) {
        if (code == null) return Optional.empty();
        return Optional.ofNullable(storage.get(code.toUpperCase()));
    }

    public void delete(String code) {
        storage.remove(code.toUpperCase());
    }
}
