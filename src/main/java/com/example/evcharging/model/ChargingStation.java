package com.example.evcharging.model;

import java.util.ArrayList;
import java.util.List;

public class ChargingStation {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private List<Connector> connectors = new ArrayList<>();

    public ChargingStation() {}

    public ChargingStation(String id, String name, double latitude, double longitude, List<Connector> connectors) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.connectors = connectors == null ? new ArrayList<>() : new ArrayList<>(connectors);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public List<Connector> getConnectors() { return connectors; }
    public void setConnectors(List<Connector> connectors) { this.connectors = connectors; }
}
