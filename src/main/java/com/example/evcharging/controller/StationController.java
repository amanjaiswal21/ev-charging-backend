package com.example.evcharging.controller;

import com.example.evcharging.dto.RegisterStationRequest;
import com.example.evcharging.model.ChargingSession;
import com.example.evcharging.model.ChargingStation;
import com.example.evcharging.model.Connector;
import com.example.evcharging.model.ConnectorStatus;
import com.example.evcharging.service.SessionService;
import com.example.evcharging.service.StationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
public class StationController {
    private final StationService stationService;
    private final SessionService sessionService;

    public StationController(StationService stationService, SessionService sessionService) {
        this.stationService = stationService;
        this.sessionService = sessionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChargingStation register(@Valid @RequestBody RegisterStationRequest request) {
        return stationService.register(request);
    }

    @PatchMapping("/{stationId}/connectors/{connectorId}/status")
    public Connector updateConnectorStatus(@PathVariable String stationId,
                                           @PathVariable String connectorId,
                                           @RequestParam ConnectorStatus status) {
        return stationService.updateConnectorStatus(stationId, connectorId, status);
    }

    @GetMapping("/{stationId}/sessions")
    public List<ChargingSession> history(@PathVariable String stationId) {
        return sessionService.getStationHistory(stationId);
    }
}
