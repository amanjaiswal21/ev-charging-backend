# EV Charging Network Backend - Spring Boot Starter

This is a deliberately small, interview-friendly boilerplate for the EV charging machine-coding problem.

## Tech
- Java 17
- Spring Boot 3.3.5
- Maven
- In-memory repositories using `ConcurrentHashMap`
- REST APIs

## Already scaffolded
- Driver + vehicle registration
- Charging station + connectors registration
- Connector status API
- Promo create/delete API
- Driver/station session-history endpoints
- Domain models and enums
- Tariff Strategy seam
- Billing Service seam
- Session start/end endpoints
- Global exception handler
- Billing-test skeleton

## Intentionally TODO
The main interview logic is intentionally left for incremental implementation:
1. Nearby-station filtering using Haversine distance.
2. Connector selection.
3. AC -> DC fallback while billing at AC tariff.
4. Session creation/completion.
5. Tiered AC/DC tariff logic and minimum charge.
6. Billing tests.
7. Edge-case validations.

## Run
```bash
mvn spring-boot:run
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

## Design choices to explain
- In-memory repositories because the assignment explicitly allows them and the round is time-boxed.
- Strategy pattern only for tariff calculation because tariff rules are expected to change.
- Session stores requested, actual, and billing connector types to model the AC-requested/DC-served special rule clearly.
- No Kafka/Redis/database/microservices for this exercise.

## Suggested next commits
1. `feat: bootstrap spring boot domain and APIs`
2. `feat: implement station distance and connector selection`
3. `feat: implement session lifecycle`
4. `feat: add tariff and promo billing`
5. `test: cover billing edge cases`
6. `docs: document assumptions and tradeoffs`
