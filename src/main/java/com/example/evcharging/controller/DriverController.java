package com.example.evcharging.controller;

import com.example.evcharging.dto.RegisterDriverRequest;
import com.example.evcharging.model.ChargingSession;
import com.example.evcharging.model.Driver;
import com.example.evcharging.service.DriverService;
import com.example.evcharging.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {
    private final DriverService driverService;
    private final SessionService sessionService;

    public DriverController(DriverService driverService, SessionService sessionService) {
        this.driverService = driverService;
        this.sessionService = sessionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Driver register(@Valid @RequestBody RegisterDriverRequest request) {
        return driverService.register(request);
    }

    @GetMapping("/{driverId}/sessions")
    public List<ChargingSession> history(@PathVariable String driverId) {
        return sessionService.getDriverHistory(driverId);
    }
}
