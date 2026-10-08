package com.example.evcharging.repository;

import com.example.evcharging.model.ChargingStation;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class StationRepository {
    private final ConcurrentHashMap<String, ChargingStation> storage = new ConcurrentHashMap<>();

    public ChargingStation save(ChargingStation station) {
        storage.put(station.getId(), station);
        return station;
    }

    public Optional<ChargingStation> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Collection<ChargingStation> findAll() {
        return storage.values();
    }
}
