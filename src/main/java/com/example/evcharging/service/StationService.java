package com.example.evcharging.service;

import com.example.evcharging.dto.RegisterStationRequest;
import com.example.evcharging.exception.NotFoundException;
import com.example.evcharging.model.ChargingStation;
import com.example.evcharging.model.Connector;
import com.example.evcharging.model.ConnectorStatus;
import com.example.evcharging.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public ChargingStation register(RegisterStationRequest request) {
        var connectors = request.connectors().stream()
                .map(c -> new Connector(UUID.randomUUID().toString(), c.type()))
                .toList();

        ChargingStation station = new ChargingStation(
                UUID.randomUUID().toString(),
                request.name(),
                request.latitude(),
                request.longitude(),
                connectors
        );

        return stationRepository.save(station);
    }

    public ChargingStation getById(String id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Station not found: " + id));
    }

    public Connector updateConnectorStatus(String stationId, String connectorId, ConnectorStatus status) {
        ChargingStation station = getById(stationId);
        Connector connector = station.getConnectors().stream()
                .filter(c -> c.getId().equals(connectorId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Connector not found: " + connectorId));

        // TODO: add validation, e.g. do not mark an active connector OUT_OF_SERVICE accidentally.
        connector.setStatus(status);
        stationRepository.save(station);
        return connector;
    }
}
