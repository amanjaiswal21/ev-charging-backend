package com.example.evcharging.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChargingStationTest {
    @Test
    void builderKeepsAnEmptyConnectorListWhenOmittedOrNull() {
        ChargingStation omitted = ChargingStation.builder().build();
        ChargingStation explicitNull = ChargingStation.builder().connectors(null).build();

        assertNotNull(omitted.getConnectors());
        assertTrue(omitted.getConnectors().isEmpty());
        assertNotNull(explicitNull.getConnectors());
        assertTrue(explicitNull.getConnectors().isEmpty());
    }

    @Test
    void builderCopiesTheConnectorList() {
        Connector connector = Connector.builder().id("connector-1").type(ConnectorType.AC).build();
        List<Connector> connectors = new ArrayList<>(List.of(connector));
        ChargingStation station = ChargingStation.builder().connectors(connectors).build();

        connectors.clear();

        assertEquals(List.of(connector), station.getConnectors());
        station.getConnectors().clear();
        assertTrue(station.getConnectors().isEmpty());
    }
}
