package com.example.evcharging.controller;

import lombok.RequiredArgsConstructor;

import com.example.evcharging.dto.ChargingSessionResponse;
import com.example.evcharging.dto.ChargingStationResponse;
import com.example.evcharging.dto.ConnectorResponse;
import com.example.evcharging.dto.RegisterStationRequest;
import com.example.evcharging.model.ConnectorStatus;
import com.example.evcharging.mapper.ApiDtoMapper;
import com.example.evcharging.service.SessionService;
import com.example.evcharging.service.StationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;
    private final SessionService sessionService;
    private final ApiDtoMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChargingStationResponse register(@Valid @RequestBody RegisterStationRequest request) {
        return mapper.toResponse(stationService.register(mapper.toInternal(request)));
    }

    @PatchMapping("/{stationId}/connectors/{connectorId}/status")
    public ConnectorResponse updateConnectorStatus(@PathVariable String stationId,
                                                   @PathVariable String connectorId,
                                                   @RequestParam ConnectorStatus status) {
        return mapper.toResponse(stationService.updateConnectorStatus(stationId, connectorId, status));
    }

    @GetMapping("/{stationId}/sessions")
    public List<ChargingSessionResponse> history(@PathVariable String stationId) {
        return sessionService.getStationHistory(stationId).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
