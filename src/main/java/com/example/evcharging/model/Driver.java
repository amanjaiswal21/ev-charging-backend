package com.example.evcharging.model;

public class Driver {
    private String id;
    private String name;
    private Vehicle vehicle;

    public Driver() {}

    public Driver(String id, String name, Vehicle vehicle) {
        this.id = id;
        this.name = name;
        this.vehicle = vehicle;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
}
