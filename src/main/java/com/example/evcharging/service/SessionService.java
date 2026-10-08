package com.example.evcharging.service;

import lombok.RequiredArgsConstructor;

import com.example.evcharging.exception.BadRequestException;
import com.example.evcharging.exception.NotFoundException;
import com.example.evcharging.model.ChargingSession;
import com.example.evcharging.model.SessionStatus;
import com.example.evcharging.repository.SessionRepository;
import com.example.evcharging.service.command.EndSessionCommand;
import com.example.evcharging.service.command.StartSessionCommand;
import com.example.evcharging.service.result.PromoDiscount;
import com.example.evcharging.strategy.ConnectorAllocation;
import com.example.evcharging.strategy.StationSelectionStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final DriverService driverService;
    private final StationService stationService;
    private final BillingService billingService;
    private final PromoCodeService promoCodeService;
    private final StationSelectionStrategy stationSelectionStrategy;

    public synchronized ChargingSession start(StartSessionCommand command) {
        driverService.getById(command.driverId());
        PromoDiscount promoDiscount = promoCodeService.resolveDiscount(command.promoCode());
        ConnectorAllocation allocation = stationSelectionStrategy
                .select(command, stationService.getAll())
                .orElseThrow(() -> new BadRequestException("No available connector found within radius"));

        stationService.reserveConnector(allocation.station().getId(), allocation.connector().getId());

        ChargingSession session = ChargingSession.builder()
                .id(UUID.randomUUID().toString())
                .driverId(command.driverId())
                .stationId(allocation.station().getId())
                .connectorId(allocation.connector().getId())
                .requestedConnectorType(command.requestedConnectorType())
                .actualConnectorType(allocation.connector().getType())
                .billingConnectorType(allocation.billingConnectorType())
                .status(SessionStatus.ACTIVE)
                .startTime(LocalDateTime.now())
                .promoCode(promoDiscount.code())
                .promoDiscountPercentage(promoDiscount.percentage())
                .build();

        return sessionRepository.save(session);
    }

    public synchronized ChargingSession end(String sessionId, EndSessionCommand command) {
        ChargingSession session = getById(sessionId);
        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new BadRequestException("Only active sessions can be ended");
        }

        double finalCost = billingService.calculate(
                session.getBillingConnectorType(),
                command.energyDeliveredKwh(),
                session.getPromoDiscountPercentage()
        );

        stationService.releaseConnector(session.getStationId(), session.getConnectorId());
        session.setEnergyDeliveredKwh(command.energyDeliveredKwh());
        session.setFinalCost(finalCost);
        session.setEndTime(LocalDateTime.now());
        session.setStatus(SessionStatus.COMPLETED);

        return sessionRepository.save(session);
    }

    public ChargingSession getById(String id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Session not found: " + id));
    }

    public List<ChargingSession> getDriverHistory(String driverId) {
        driverService.getById(driverId);
        return sortByStartTime(sessionRepository.findByDriverId(driverId));
    }

    public List<ChargingSession> getStationHistory(String stationId) {
        stationService.getById(stationId);
        return sortByStartTime(sessionRepository.findByStationId(stationId));
    }

    private List<ChargingSession> sortByStartTime(List<ChargingSession> sessions) {
        return sessions.stream()
                .sorted(Comparator.comparing(ChargingSession::getStartTime))
                .toList();
    }
}
