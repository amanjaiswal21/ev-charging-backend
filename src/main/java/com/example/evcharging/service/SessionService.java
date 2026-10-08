package com.example.evcharging.service;

import com.example.evcharging.dto.EndSessionRequest;
import com.example.evcharging.dto.StartSessionRequest;
import com.example.evcharging.exception.NotFoundException;
import com.example.evcharging.model.ChargingSession;
import com.example.evcharging.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final DriverService driverService;
    private final StationService stationService;
    private final BillingService billingService;

    public SessionService(SessionRepository sessionRepository,
                          DriverService driverService,
                          StationService stationService,
                          BillingService billingService) {
        this.sessionRepository = sessionRepository;
        this.driverService = driverService;
        this.stationService = stationService;
        this.billingService = billingService;
    }

    public ChargingSession start(StartSessionRequest request) {
        // First validate driver exists.
        driverService.getById(request.driverId());

        /*
         * TODO happy flow:
         * 1. Get all stations.
         * 2. Filter stations within radius using Haversine distance.
         * 3. Find AVAILABLE connector of requested type.
         * 4. If AC requested and unavailable, allow AVAILABLE DC fallback.
         * 5. requestedType = AC, actualType = DC, billingType = AC for fallback case.
         * 6. Mark connector IN_USE.
         * 7. Create ACTIVE ChargingSession and save it.
         */
        throw new UnsupportedOperationException("Start-session flow not implemented yet");
    }

    public ChargingSession end(String sessionId, EndSessionRequest request) {
        ChargingSession session = getById(sessionId);

        /*
         * TODO happy flow:
         * 1. Validate session is ACTIVE.
         * 2. Calculate price via billingService using billingConnectorType.
         * 3. Set energy, price, endTime, COMPLETED.
         * 4. Mark allocated connector AVAILABLE again.
         * 5. Save and return session.
         */
        throw new UnsupportedOperationException("End-session flow not implemented yet");
    }

    public ChargingSession getById(String id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Session not found: " + id));
    }

    public List<ChargingSession> getDriverHistory(String driverId) {
        driverService.getById(driverId);
        return sessionRepository.findByDriverId(driverId);
    }

    public List<ChargingSession> getStationHistory(String stationId) {
        stationService.getById(stationId);
        return sessionRepository.findByStationId(stationId);
    }
}
