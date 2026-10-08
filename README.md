# EV Charging Network Backend

Small Spring Boot backend for the EV charging machine-coding problem. It uses in-memory storage, REST APIs, and deliberately scoped seams for the areas most likely to change during a live extension.

## Tech
- Java 17
- Spring Boot 3.3.5
- Maven
- In-memory repositories using `ConcurrentHashMap`
- REST APIs

## Implemented
- Driver + vehicle registration.
- Charging station + connector registration.
- Connector availability, in-use, and out-of-service handling.
- Start-session flow with radius filtering and connector allocation.
- AC request fallback to a DC connector when no AC connector is free, while billing at the AC tariff.
- End-session flow with energy delivered and final price calculation.
- Driver and station session history for active and completed sessions.
- Promo create/delete APIs, with valid promo discounts snapshotted when a session starts.
- Tiered AC/DC tariffs with minimum session charges.
- Separate API DTOs and internal DTO records so controllers do not expose internal domain models directly.
- Automated tests for billing slabs, minimum charges, promo discounts, AC-billed-on-DC, out-of-service connectors, radius filtering, and promo snapshot behavior.

## Run
```bash
mvn spring-boot:run
```

## Test
```bash
mvn test
```

## Core APIs
- `POST /api/drivers`
- `POST /api/stations`
- `PATCH /api/stations/{stationId}/connectors/{connectorId}/status?status=OUT_OF_SERVICE`
- `POST /api/sessions/start`
- `POST /api/sessions/{sessionId}/end`
- `GET /api/drivers/{driverId}/sessions`
- `GET /api/stations/{stationId}/sessions`
- `POST /api/promos`
- `DELETE /api/promos/{code}`

## Assumptions
- Station search uses the latitude/longitude supplied in the start-session request as the driver's current location.
- When multiple stations qualify, the default selection strategy chooses the nearest station with the requested connector type. AC-to-DC fallback is attempted only after no available AC connector exists inside the radius.
- DC tariff follows the problem statement example: minimum INR 150; first 10 kWh at INR 20/kWh; 11-25 kWh at INR 14/kWh; beyond 25 kWh at INR 9/kWh.
- AC tariff is an explicit local assumption: minimum INR 50; first 20 kWh at INR 10/kWh; beyond 20 kWh at INR 8/kWh.
- Minimum session charge is applied before promo discounts.
- Promo codes are percentage discounts only. A valid promo is checked and snapshotted when a session starts, so later deletion does not change an active session's discount.
- Connector status `IN_USE` is managed by session lifecycle only. The status API is for taking connectors out of service and bringing them back.

## Design Decisions
- Controllers expose API response DTOs and use `ApiDtoMapper.toInternal` to convert API requests into immutable DTOs in `dto.internal`. Services and station-selection strategies depend on these internal DTOs rather than HTTP request classes. Domain models remain internal and are not returned directly by REST controllers.
- Lombok builders construct domain models and larger internal/response DTOs with named fields. Constructor-level builders preserve connector availability defaults and the station's defensive connector-list copy. Session lifecycle updates still modify the stored session through generated setters.
- Tariff calculation uses the Strategy pattern. Adding a new connector tariff should mean adding a new `TariffStrategy`, not changing session orchestration.
- Station selection also uses a strategy interface. The current implementation is nearest-available selection, leaving room for cheapest or highest-power strategies later.
- `SessionService` orchestrates the use case and depends on abstractions or focused services rather than reaching into repository internals.
- In-memory repositories are used because the assignment allows them and they keep the exercise focused on domain behavior.
- Session state stores requested, actual, and billing connector types separately to make the AC-requested/DC-served rule explicit.

## Trade-offs
- Concurrency protection is intentionally simple: session start/end and connector reservation/release are synchronized around the in-memory service objects. A real deployment would move this into database constraints or distributed locking.
- Tariff values are hard-coded in strategies for readability during a machine-coding round. Production code would likely externalize them to configuration.
- Promo support is currently percentage-only because that is the mandatory scope. The `PromoType` enum is ready for a flat-amount extension.
- History responses are sorted by start time but not paginated.

## With More Time
- Add a configurable station-selection strategy setting.
- Add flat-amount promos and promo expiry windows.
- Add cancellation with a no-show fee policy.
- Add integration tests through MockMvc for the REST boundary.
- Replace in-memory repositories with persistent storage and optimistic locking.

## AI Use
- AI was used to accelerate repository inspection, requirement extraction, implementation scaffolding, and test expansion.
- Generated ideas were narrowed to the existing Spring Boot shape instead of adding unnecessary infrastructure.
- The main rewrites were around DTO boundaries, strategy placement, and promo discount ownership so the design stayed explainable and easy to extend.
