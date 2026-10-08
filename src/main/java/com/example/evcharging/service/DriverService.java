package com.example.evcharging.service;

import com.example.evcharging.dto.RegisterDriverRequest;
import com.example.evcharging.exception.NotFoundException;
import com.example.evcharging.model.Driver;
import com.example.evcharging.model.Vehicle;
import com.example.evcharging.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DriverService {
    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver register(RegisterDriverRequest request) {
        Vehicle vehicle = new Vehicle(UUID.randomUUID().toString(), request.vehicleRegistrationNumber());
        Driver driver = new Driver(UUID.randomUUID().toString(), request.name(), vehicle);
        return driverRepository.save(driver);
    }

    public Driver getById(String id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver not found: " + id));
    }
}
