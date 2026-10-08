package com.example.evcharging.repository;

import com.example.evcharging.model.ChargingSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionRepository {
    private final ConcurrentHashMap<String, ChargingSession> storage = new ConcurrentHashMap<>();

    public ChargingSession save(ChargingSession session) {
        storage.put(session.getId(), session);
        return session;
    }

    public Optional<ChargingSession> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<ChargingSession> findByDriverId(String driverId) {
        return storage.values().stream()
                .filter(s -> driverId.equals(s.getDriverId()))
                .toList();
    }

    public List<ChargingSession> findByStationId(String stationId) {
        return storage.values().stream()
                .filter(s -> stationId.equals(s.getStationId()))
                .toList();
    }
}
