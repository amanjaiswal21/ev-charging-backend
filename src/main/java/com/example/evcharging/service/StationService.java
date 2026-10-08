package com.example.evcharging.service;

import lombok.RequiredArgsConstructor;

import com.example.evcharging.exception.BadRequestException;
import com.example.evcharging.exception.NotFoundException;
import com.example.evcharging.model.ChargingStation;
import com.example.evcharging.model.Connector;
import com.example.evcharging.model.ConnectorStatus;
import com.example.evcharging.repository.StationRepository;
import com.example.evcharging.service.command.RegisterStationCommand;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StationService {
    private final StationRepository stationRepository;

    public ChargingStation register(RegisterStationCommand command) {
        var connectors = command.connectors().stream()
                .map(c -> Connector.builder()
                        .id(UUID.randomUUID().toString())
                        .type(c.type())
                        .build())
                .toList();

        ChargingStation station = ChargingStation.builder()
                .id(UUID.randomUUID().toString())
                .name(command.name())
                .latitude(command.latitude())
                .longitude(command.longitude())
                .connectors(connectors)
                .build();

        return stationRepository.save(station);
    }

    public ChargingStation getById(String id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Station not found: " + id));
    }

    public Collection<ChargingStation> getAll() {
        return stationRepository.findAll();
    }

    public synchronized Connector updateConnectorStatus(String stationId, String connectorId, ConnectorStatus status) {
        ChargingStation station = getById(stationId);
        Connector connector = findConnector(station, connectorId);

        if (status == null) {
            throw new BadRequestException("Connector status is required");
        }
        if (status == ConnectorStatus.IN_USE) {
            throw new BadRequestException("Connector status IN_USE is managed by charging sessions");
        }
        if (connector.getStatus() == ConnectorStatus.IN_USE) {
            throw new BadRequestException("Cannot update an in-use connector");
        }

        connector.setStatus(status);
        stationRepository.save(station);
        return connector;
    }

    public synchronized void reserveConnector(String stationId, String connectorId) {
        ChargingStation station = getById(stationId);
        Connector connector = findConnector(station, connectorId);
        if (connector.getStatus() != ConnectorStatus.AVAILABLE) {
            throw new BadRequestException("Connector is not available: " + connectorId);
        }

        connector.setStatus(ConnectorStatus.IN_USE);
        stationRepository.save(station);
    }

    public synchronized void releaseConnector(String stationId, String connectorId) {
        ChargingStation station = getById(stationId);
        Connector connector = findConnector(station, connectorId);
        if (connector.getStatus() != ConnectorStatus.IN_USE) {
            throw new BadRequestException("Connector is not in use: " + connectorId);
        }

        connector.setStatus(ConnectorStatus.AVAILABLE);
        stationRepository.save(station);
    }

    private Connector findConnector(ChargingStation station, String connectorId) {
        return station.getConnectors().stream()
                .filter(c -> c.getId().equals(connectorId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Connector not found: " + connectorId));
    }
}
