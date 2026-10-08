package com.example.evcharging.repository;

import com.example.evcharging.model.Driver;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DriverRepository {
    private final ConcurrentHashMap<String, Driver> storage = new ConcurrentHashMap<>();

    public Driver save(Driver driver) {
        storage.put(driver.getId(), driver);
        return driver;
    }

    public Optional<Driver> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Collection<Driver> findAll() {
        return storage.values();
    }
}
