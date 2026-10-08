package com.example.evcharging.mapper;

import com.example.evcharging.dto.*;
import com.example.evcharging.model.*;
import com.example.evcharging.service.command.*;
import org.springframework.stereotype.Component;

@Component
public class ApiDtoMapper {
    public RegisterDriverCommand toCommand(RegisterDriverRequest request) {
        return new RegisterDriverCommand(request.name(), request.vehicleRegistrationNumber());
    }

    public RegisterStationCommand toCommand(RegisterStationRequest request) {
        return RegisterStationCommand.builder()
                .name(request.name())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .connectors(request.connectors().stream()
                        .map(connector -> new ConnectorCommand(connector.type()))
                        .toList())
                .build();
    }

    public StartSessionCommand toCommand(StartSessionRequest request) {
        return StartSessionCommand.builder()
                .driverId(request.driverId())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .radiusKm(request.radiusKm())
                .requestedConnectorType(request.requestedConnectorType())
                .promoCode(request.promoCode())
                .build();
    }

    public EndSessionCommand toCommand(EndSessionRequest request) {
        return new EndSessionCommand(request.energyDeliveredKwh());
    }

    public CreatePromoCommand toCommand(CreatePromoRequest request) {
        return new CreatePromoCommand(request.code(), request.percentageDiscount());
    }

    public DriverResponse toResponse(Driver driver) {
        return new DriverResponse(
                driver.getId(),
                driver.getName(),
                toResponse(driver.getVehicle())
        );
    }

    public VehicleResponse toResponse(Vehicle vehicle) {
        return new VehicleResponse(vehicle.getId(), vehicle.getRegistrationNumber());
    }

    public ChargingStationResponse toResponse(ChargingStation station) {
        return ChargingStationResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .latitude(station.getLatitude())
                .longitude(station.getLongitude())
                .connectors(station.getConnectors().stream()
                        .map(this::toResponse)
                        .toList())
                .build();
    }

    public ConnectorResponse toResponse(Connector connector) {
        return new ConnectorResponse(connector.getId(), connector.getType(), connector.getStatus());
    }

    public ChargingSessionResponse toResponse(ChargingSession session) {
        return ChargingSessionResponse.builder()
                .id(session.getId())
                .driverId(session.getDriverId())
                .stationId(session.getStationId())
                .connectorId(session.getConnectorId())
                .requestedConnectorType(session.getRequestedConnectorType())
                .actualConnectorType(session.getActualConnectorType())
                .billingConnectorType(session.getBillingConnectorType())
                .status(session.getStatus())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .energyDeliveredKwh(session.getEnergyDeliveredKwh())
                .finalCost(session.getFinalCost())
                .promoCode(session.getPromoCode())
                .promoDiscountPercentage(session.getPromoDiscountPercentage())
                .build();
    }

    public PromoCodeResponse toResponse(PromoCode promoCode) {
        return new PromoCodeResponse(
                promoCode.getCode(),
                promoCode.getType(),
                promoCode.getValue(),
                promoCode.isActive()
        );
    }
}
