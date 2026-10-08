package com.example.evcharging.model;

import java.time.LocalDateTime;

public class ChargingSession {
    private String id;
    private String driverId;
    private String stationId;
    private String connectorId;

    // Keep both values because AC may be served on a DC connector but billed using AC tariff.
    private ConnectorType requestedConnectorType;
    private ConnectorType actualConnectorType;
    private ConnectorType billingConnectorType;

    private SessionStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double energyDeliveredKwh;
    private double finalCost;
    private String promoCode;

    public ChargingSession() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }
    public String getStationId() { return stationId; }
    public void setStationId(String stationId) { this.stationId = stationId; }
    public String getConnectorId() { return connectorId; }
    public void setConnectorId(String connectorId) { this.connectorId = connectorId; }
    public ConnectorType getRequestedConnectorType() { return requestedConnectorType; }
    public void setRequestedConnectorType(ConnectorType requestedConnectorType) { this.requestedConnectorType = requestedConnectorType; }
    public ConnectorType getActualConnectorType() { return actualConnectorType; }
    public void setActualConnectorType(ConnectorType actualConnectorType) { this.actualConnectorType = actualConnectorType; }
    public ConnectorType getBillingConnectorType() { return billingConnectorType; }
    public void setBillingConnectorType(ConnectorType billingConnectorType) { this.billingConnectorType = billingConnectorType; }
    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public double getEnergyDeliveredKwh() { return energyDeliveredKwh; }
    public void setEnergyDeliveredKwh(double energyDeliveredKwh) { this.energyDeliveredKwh = energyDeliveredKwh; }
    public double getFinalCost() { return finalCost; }
    public void setFinalCost(double finalCost) { this.finalCost = finalCost; }
    public String getPromoCode() { return promoCode; }
    public void setPromoCode(String promoCode) { this.promoCode = promoCode; }
}
