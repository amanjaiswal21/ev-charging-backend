package com.example.evcharging.controller;

import lombok.RequiredArgsConstructor;

import com.example.evcharging.dto.CreatePromoRequest;
import com.example.evcharging.dto.PromoCodeResponse;
import com.example.evcharging.mapper.ApiDtoMapper;
import com.example.evcharging.service.PromoCodeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promos")
@RequiredArgsConstructor
public class PromoCodeController {
    private final PromoCodeService promoCodeService;
    private final ApiDtoMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PromoCodeResponse create(@Valid @RequestBody CreatePromoRequest request) {
        return mapper.toResponse(promoCodeService.create(mapper.toCommand(request)));
    }

    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String code) {
        promoCodeService.delete(code);
    }
}
