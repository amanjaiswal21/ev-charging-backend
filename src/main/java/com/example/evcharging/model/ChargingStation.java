package com.example.evcharging.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChargingStation {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private List<Connector> connectors = new ArrayList<>();

    @Builder
    public ChargingStation(String id, String name, double latitude, double longitude, List<Connector> connectors) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.connectors = connectors == null ? new ArrayList<>() : new ArrayList<>(connectors);
    }

}
