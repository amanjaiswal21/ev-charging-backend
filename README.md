## Assumptions

- The location supplied when starting a session represents the driver's current location. Search radius is in kilometres, and distances are calculated using the Haversine formula.
- The nearest station with an available requested connector is selected. For AC requests, DC fallback is considered only when no AC connector is available within the radius; billing still uses the AC tariff.
- DC pricing follows the assignment: minimum INR 150, first 10 kWh at INR 20/kWh, next 15 kWh at INR 14/kWh, and remaining energy at INR 9/kWh.
- Since AC pricing was unspecified, I assumed a minimum of INR 50, first 20 kWh at INR 10/kWh, and remaining energy at INR 8/kWh.
- Minimum charges are applied before percentage discounts. The final amount is rounded to two decimal places.
- Promo codes are validated at session start, and the discount is stored on the session. Deleting a promo later does not affect an active session.
- Delivered energy is supplied when ending a session; live charging and meter readings are outside the current scope. In-memory data is lost when the application restarts.

## Key Design Decisions and Trade-offs

- Controllers, services, repositories, and DTOs have separate responsibilities. API DTOs keep the REST contract independent of internal domain models, at the cost of additional mapping code.
- Tariff calculation and station selection use strategy interfaces so their rules can change independently of session orchestration.
- Sessions store requested, actual, and billing connector types separately to represent AC-to-DC fallback explicitly.
- In-memory repositories keep setup simple. Synchronized session operations serialize start/end requests within one application instance, but limit throughput and do not support multiple instances.
- AC/DC minimum charges, slab limits, and rates are configured in `src/main/resources/application.properties` under `charging.tariffs`. Limits are cumulative kWh boundaries; the final slab omits `up-to-kwh` to cover all remaining energy. Configuration is validated at startup and changes require a restart.
- Promos support percentage discounts only, and history is not paginated. These choices keep the implementation focused on the required flows.

## With More Time

- Add persistent storage with transactions and concurrency control for connector allocation.
- Add API integration tests and concurrent session-start tests.
- Add tariff versioning so active sessions retain their original pricing rules when tariffs change.
- Add configurable station selection, flat-amount promos, expiry rules, and paginated history.

## AI Use

- I prompted AI to help interpret the requirements, scaffold Spring Boot components, and suggest tests for billing slabs, minimum charges, connector fallback, and promo behaviour.
- I rejected suggestions that introduced infrastructure beyond the exercise's scope, keeping the implementation focused on the existing Spring Boot structure and in-memory storage.
- I reviewed and rewrote generated code around DTO boundaries, strategy  and promo discount ownership. These changes kept HTTP details out of business logic, separated pricing and selection rules, and preserved discounts for sessions already started.
