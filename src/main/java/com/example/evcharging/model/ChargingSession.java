package com.example.evcharging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private double promoDiscountPercentage;

}
