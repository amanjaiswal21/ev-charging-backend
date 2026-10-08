package com.example.evcharging.strategy;

import com.example.evcharging.model.ChargingStation;
import com.example.evcharging.dto.internal.SessionStartDto;

import java.util.Collection;
import java.util.Optional;

public interface StationSelectionStrategy {
    Optional<ConnectorAllocation> select(SessionStartDto input, Collection<ChargingStation> stations);
}
