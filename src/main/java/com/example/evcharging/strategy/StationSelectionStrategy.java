package com.example.evcharging.strategy;

import com.example.evcharging.model.ChargingStation;
import com.example.evcharging.service.command.StartSessionCommand;

import java.util.Collection;
import java.util.Optional;

public interface StationSelectionStrategy {
    Optional<ConnectorAllocation> select(StartSessionCommand command, Collection<ChargingStation> stations);
}
