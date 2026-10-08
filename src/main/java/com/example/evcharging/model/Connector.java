package com.example.evcharging.model;

public class Connector {
    private String id;
    private ConnectorType type;
    private ConnectorStatus status;

    public Connector() {}

    public Connector(String id, ConnectorType type) {
        this.id = id;
        this.type = type;
        this.status = ConnectorStatus.AVAILABLE;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public ConnectorType getType() { return type; }
    public void setType(ConnectorType type) { this.type = type; }
    public ConnectorStatus getStatus() { return status; }
    public void setStatus(ConnectorStatus status) { this.status = status; }
}
