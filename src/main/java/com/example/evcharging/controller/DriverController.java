package com.example.evcharging.controller;

import lombok.RequiredArgsConstructor;

import com.example.evcharging.dto.ChargingSessionResponse;
import com.example.evcharging.dto.DriverResponse;
import com.example.evcharging.dto.RegisterDriverRequest;
import com.example.evcharging.mapper.ApiDtoMapper;
import com.example.evcharging.service.DriverService;
import com.example.evcharging.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;
    private final SessionService sessionService;
    private final ApiDtoMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverResponse register(@Valid @RequestBody RegisterDriverRequest request) {
        return mapper.toResponse(driverService.register(mapper.toCommand(request)));
    }

    @GetMapping("/{driverId}/sessions")
    public List<ChargingSessionResponse> history(@PathVariable String driverId) {
        return sessionService.getDriverHistory(driverId).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
