package com.example.evcharging.model;

public class Vehicle {
    private String id;
    private String registrationNumber;

    public Vehicle() {}

    public Vehicle(String id, String registrationNumber) {
        this.id = id;
        this.registrationNumber = registrationNumber;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
}
