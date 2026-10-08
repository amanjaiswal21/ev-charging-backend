package com.example.evcharging.service;

import lombok.RequiredArgsConstructor;

import com.example.evcharging.exception.NotFoundException;
import com.example.evcharging.model.Driver;
import com.example.evcharging.model.Vehicle;
import com.example.evcharging.repository.DriverRepository;
import com.example.evcharging.service.command.RegisterDriverCommand;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;

    public Driver register(RegisterDriverCommand command) {
        Vehicle vehicle = Vehicle.builder()
                .id(UUID.randomUUID().toString())
                .registrationNumber(command.vehicleRegistrationNumber())
                .build();
        Driver driver = Driver.builder()
                .id(UUID.randomUUID().toString())
                .name(command.name())
                .vehicle(vehicle)
                .build();
        return driverRepository.save(driver);
    }

    public Driver getById(String id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver not found: " + id));
    }
}
