package com.example.evcharging.controller;

import com.example.evcharging.dto.CreatePromoRequest;
import com.example.evcharging.model.PromoCode;
import com.example.evcharging.service.PromoCodeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promos")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PromoCode create(@Valid @RequestBody CreatePromoRequest request) {
        return promoCodeService.create(request);
    }

    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String code) {
        promoCodeService.delete(code);
    }
}
