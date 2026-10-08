package com.example.evcharging.strategy;

import com.example.evcharging.model.ChargingStation;
import com.example.evcharging.model.ConnectorStatus;
import com.example.evcharging.model.ConnectorType;
import com.example.evcharging.dto.internal.SessionStartDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Component
public class NearestAvailableStationSelectionStrategy implements StationSelectionStrategy {
    private static final double EARTH_RADIUS_KM = 6371.0088;

    @Override
    public Optional<ConnectorAllocation> select(SessionStartDto input, Collection<ChargingStation> stations) {
        Optional<ConnectorAllocation> exactMatch = findNearest(
                input,
                stations,
                input.requestedConnectorType(),
                input.requestedConnectorType()
        );

        if (exactMatch.isPresent() || input.requestedConnectorType() != ConnectorType.AC) {
            return exactMatch;
        }

        return findNearest(input, stations, ConnectorType.DC, ConnectorType.AC);
    }

    private Optional<ConnectorAllocation> findNearest(SessionStartDto input,
                                                      Collection<ChargingStation> stations,
                                                      ConnectorType actualConnectorType,
                                                      ConnectorType billingConnectorType) {
        return stations.stream()
                .map(station -> candidateForStation(input, station, actualConnectorType, billingConnectorType))
                .flatMap(Optional::stream)
                .min(Comparator.comparingDouble(ConnectorAllocation::distanceKm));
    }

    private Optional<ConnectorAllocation> candidateForStation(SessionStartDto input,
                                                              ChargingStation station,
                                                              ConnectorType actualConnectorType,
                                                              ConnectorType billingConnectorType) {
        double distanceKm = distanceKm(
                input.latitude(),
                input.longitude(),
                station.getLatitude(),
                station.getLongitude()
        );
        if (distanceKm > input.radiusKm()) {
            return Optional.empty();
        }

        return station.getConnectors().stream()
                .filter(connector -> connector.getType() == actualConnectorType)
                .filter(connector -> connector.getStatus() == ConnectorStatus.AVAILABLE)
                .findFirst()
                .map(connector -> new ConnectorAllocation(station, connector, billingConnectorType, distanceKm));
    }

    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double startLat = Math.toRadians(lat1);
        double endLat = Math.toRadians(lat2);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(startLat) * Math.cos(endLat)
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
