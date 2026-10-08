package com.example.evcharging.service;

import com.example.evcharging.exception.BadRequestException;
import com.example.evcharging.model.ChargingSession;
import com.example.evcharging.model.ChargingStation;
import com.example.evcharging.model.ConnectorStatus;
import com.example.evcharging.model.ConnectorType;
import com.example.evcharging.model.Driver;
import com.example.evcharging.model.SessionStatus;
import com.example.evcharging.repository.DriverRepository;
import com.example.evcharging.repository.PromoCodeRepository;
import com.example.evcharging.repository.SessionRepository;
import com.example.evcharging.repository.StationRepository;
import com.example.evcharging.dto.internal.ConnectorDto;
import com.example.evcharging.dto.internal.PromoCodeCreationDto;
import com.example.evcharging.dto.internal.SessionEndDto;
import com.example.evcharging.dto.internal.DriverRegistrationDto;
import com.example.evcharging.dto.internal.StationRegistrationDto;
import com.example.evcharging.dto.internal.SessionStartDto;
import com.example.evcharging.strategy.AcTariffStrategy;
import com.example.evcharging.strategy.DcTariffStrategy;
import com.example.evcharging.strategy.NearestAvailableStationSelectionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SessionServiceTest {
    private DriverService driverService;
    private StationService stationService;
    private PromoCodeService promoCodeService;
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        driverService = new DriverService(new DriverRepository());
        stationService = new StationService(new StationRepository());
        promoCodeService = new PromoCodeService(new PromoCodeRepository());
        BillingService billingService = new BillingService(List.of(
                new AcTariffStrategy(),
                new DcTariffStrategy()
        ));
        sessionService = new SessionService(
                new SessionRepository(),
                driverService,
                stationService,
                billingService,
                promoCodeService,
                new NearestAvailableStationSelectionStrategy()
        );
    }

    @Test
    void startsAcRequestOnDcFallbackAndBillsUsingAcTariff() {
        Driver driver = registerDriver();
        ChargingStation station = stationService.register(new StationRegistrationDto(
                "DC only",
                12.9716,
                77.5946,
                List.of(new ConnectorDto(ConnectorType.DC))
        ));

        ChargingSession active = sessionService.start(new SessionStartDto(
                driver.getId(),
                12.9716,
                77.5946,
                2.0,
                ConnectorType.AC,
                null
        ));

        assertEquals(SessionStatus.ACTIVE, active.getStatus());
        assertEquals(ConnectorType.AC, active.getRequestedConnectorType());
        assertEquals(ConnectorType.DC, active.getActualConnectorType());
        assertEquals(ConnectorType.AC, active.getBillingConnectorType());
        assertEquals(ConnectorStatus.IN_USE, station.getConnectors().get(0).getStatus());

        ChargingSession completed = sessionService.end(active.getId(), new SessionEndDto(25.0));

        assertEquals(SessionStatus.COMPLETED, completed.getStatus());
        assertEquals(240.0, completed.getFinalCost());
        assertEquals(ConnectorStatus.AVAILABLE, station.getConnectors().get(0).getStatus());
    }

    @Test
    void snapshotsPromoDiscountAtSessionStart() {
        Driver driver = registerDriver();
        stationService.register(new StationRegistrationDto(
                "DC station",
                12.9716,
                77.5946,
                List.of(new ConnectorDto(ConnectorType.DC))
        ));
        promoCodeService.create(new PromoCodeCreationDto("save10", 10.0));

        ChargingSession active = sessionService.start(new SessionStartDto(
                driver.getId(),
                12.9716,
                77.5946,
                2.0,
                ConnectorType.DC,
                "save10"
        ));
        promoCodeService.delete("save10");

        ChargingSession completed = sessionService.end(active.getId(), new SessionEndDto(20.0));

        assertEquals("SAVE10", completed.getPromoCode());
        assertEquals(10.0, completed.getPromoDiscountPercentage());
        assertEquals(306.0, completed.getFinalCost());
    }

    @Test
    void doesNotOfferOutOfServiceConnectors() {
        Driver driver = registerDriver();
        ChargingStation station = stationService.register(new StationRegistrationDto(
                "AC station",
                12.9716,
                77.5946,
                List.of(new ConnectorDto(ConnectorType.AC))
        ));
        stationService.updateConnectorStatus(
                station.getId(),
                station.getConnectors().get(0).getId(),
                ConnectorStatus.OUT_OF_SERVICE
        );

        assertThrows(BadRequestException.class, () -> sessionService.start(new SessionStartDto(
                driver.getId(),
                12.9716,
                77.5946,
                2.0,
                ConnectorType.AC,
                null
        )));
    }

    @Test
    void failsWhenNoStationIsWithinRadius() {
        Driver driver = registerDriver();
        stationService.register(new StationRegistrationDto(
                "Far station",
                13.0827,
                80.2707,
                List.of(new ConnectorDto(ConnectorType.DC))
        ));

        assertThrows(BadRequestException.class, () -> sessionService.start(new SessionStartDto(
                driver.getId(),
                12.9716,
                77.5946,
                1.0,
                ConnectorType.DC,
                null
        )));
    }

    private Driver registerDriver() {
        return driverService.register(new DriverRegistrationDto("Aman", "KA-01-EV-1234"));
    }
}
