# Copilot / AI agent instructions — products-ms

Purpose: Short, actionable notes to help an AI agent contribute safely and productively to this Spring Boot microservice.

## Big picture (what this service is)
- Spring Boot 3.3.5, Java 21 microservice that exposes a REST API for managing products. See `ProductApplication.java`.
- Persistence: JPA repositories + MySQL (datasource configured in `application.properties` / `application-dev.properties`). Repositories live in `com.apogee.product.repositories`.
- API surface: controllers under `com.apogee.product.controllers` → each controller delegates to a *backingService* (e.g. `ProductsBackingService`) which orchestrates domain *services* and *utilities*.
- Layers:
  - Controllers (HTTP, OpenAPI annotations) → BackingService (API orchestration) → Service (business logic, transactions) → Repository (DB).
  - DTOs: input in `dtos.inputs`, output in `dtos.output`. Models in `models/`, entities in `entities/`.

## Project-specific conventions & important patterns
- Backing services are the canonical place to adapt controller DTOs into domain objects and to assemble Response DTOs. Example: `ProductsBackingService`.
- Use the custom reflective `Mapper` utility (see `utilities/Mapper.java`) for DTO↔entity↔model conversions. Mapper requires a no-arg constructor and copies fields by name (nested collections supported). Avoid introducing alternative mapping libraries without coordination.
- Use `Utilities.transformCollection/transform` helpers for mapping collections and applying complementary functions (see `utilities/Utilities.java`).
- Error handling: throw `RecordNotFoundException` or `DBException` from services. Messages are usually i18n keys (see `src/main/resources/errors_en.properties` and `errors_ar.properties`). `GlobalExceptionHandler` converts these to `FailureResponse` (English + Arabic message extraction by ResourceBundle).
- Logging: AOP-based request/response logging via `LoggerAspect` (sets a `requestId` request attribute and logs a JSON-like MapMessage). Be careful when changing controller signatures — logging relies on parameter annotations (`@PathVariable`, `@RequestBody`).
- API docs: OpenAPI config in `configs/OpenApiConfigurations.java` — default server url `/` and `bearerAuth` scheme are declared. Response schemas like `FailureResponseDto` are added in components.
- Response pattern: controllers return `ResponseEntity<Response>` where specific DTOs extend `Response` or `SuccessfulResponse` (see `dtos/output/Response.java` and `SuccessfulResponse`). Set appropriate HTTP status codes.

## Build / run / test (developer workflows)
- Build: `./mvnw clean package` (use `-DskipTests` if you only want the artifact).
- Run locally: `./mvnw spring-boot:run` or `java -jar target/products-ms-0.0.1-SNAPSHOT.jar`.
- Tests: `./mvnw test`. Unit tests use JUnit 5 + Mockito (see `src/test/java/...`).
- Docker: image built via provided `DockerFile` (multi-stage). DB connection is supplied via env vars used by ENTRYPOINT (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`). Default port: 9090.
- Sonar: `sonar-maven-plugin` configured; Sonar token via `${env.SONAR_TOKEN}`.

## Small but important rules for code changes
- Keep controllers thin: orchestration, DTO→model conversion, and response assembly belong in *backing services* (e.g. `ProductsBackingService`).
- Mapping: use the project `Mapper` (see `src/main/java/com/apogee/product/utilities/Mapper.java`). It:
  - Requires a no-arg constructor for destination types; will throw if missing.
  - Copies fields by name and supports nested collections and maps.
  - Caches constructors/fields for performance.
  - **Important:** adding a new field requires updates on both sides (DTO/model/entity) because unmatched fields are silently ignored.
- Utilities: prefer `Utilities.transformCollection` / `Utilities.transform` helpers for collection mapping and applying complementary functions (see `Utilities.java`). Note there is a deprecated overload — prefer the non-deprecated variants.
- Error handling / i18n: prefer throwing `RecordNotFoundException` or `DBException` with i18n keys (e.g. `throw new RecordNotFoundException(RECORD_NOT_FOUND, id)`) so `GlobalExceptionHandler` can create localized `FailureResponse` from `errors_*.properties`. Be aware some services use literal messages (e.g., `"Tag not found"` in `ProductServiceImpl`) — new code should use i18n keys and add strings to `errors_en.properties` and `errors_ar.properties`.
- AOP logging: `LoggerAspect` sets a `requestId` attribute and constructs request/response logs using `@PathVariable` and `@RequestBody` annotations. Keep controller method parameter annotations intact to preserve logging details and `requestId` behavior.
- Transactions: Service implementations are typically `@Transactional` — maintain transaction boundaries in services, not controllers or backing services.
- Unit testing patterns (recommended):
  - Use JUnit 5 + Mockito (`@ExtendWith(MockitoExtension.class)`).
  - Mock repositories/services (`@Mock`) and inject SUT with `@InjectMocks`.
  - Use `Optional.of(...)` / `Optional.empty()` for repository responses and `when(...).thenThrow(...)` to simulate errors.
  - Use `ArgumentCaptor` to assert saved entities and `verify(...)` for interaction assertions.
  - Avoid loading Spring context in service unit tests — keep them fast and isolated.
  - See `src/test/java/com/apogee/product/services/impl/CategoryServiceImplTest.java` for a working reference.
- Docker & runtime: Dockerfile is multi-stage. Runtime ENTRYPOINT expects DB env vars: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`. Default app port: 9090 (dev profile uses 9091 and enables SQL logging).
- CI & quality: `sonar-maven-plugin` is configured in `pom.xml`; Sonar token must be provided via the `SONAR_TOKEN` environment variable.
- Common pitfalls to watch for:
  - `Mapper` silently ignores unmatched fields — add tests to ensure new fields are mapped.
  - Throw i18n keys consistently so `GlobalExceptionHandler` can resolve localized messages.
  - Changing controller method signatures or removing annotations may break logging or OpenAPI docs; keep signatures stable.



## Example snippets (reference patterns)
- Controller → backing service pattern: `ProductController` delegates to `ProductsBackingService` and returns `ResponseEntity<FindProductResponseDto>`.
- Throwing a not-found error from service: `throw new RecordNotFoundException(RECORD_NOT_FOUND, productId);` (ensure `errors_*.properties` contains the key).
- Map entity to model with complementary properties: `Utilities.transformCollection(productEntities, Product.class, this::getProduct)` (see `ProductServiceImpl#getProduct`).

## Where to look first (quick navigation)
- App entry: `src/main/java/com/apogee/product/ProductApplication.java`
- Controllers: `src/main/java/com/apogee/product/controllers` (API surface)
- Backing services: `src/main/java/com/apogee/product/backingService` (orchestration)
- Services: `src/main/java/com/apogee/product/services` and `services/impl`
- Repositories: `src/main/java/com/apogee/product/repositories`
- Utilities: `src/main/java/com/apogee/product/utilities` (`Mapper`, `Utilities`)
- Error messages: `src/main/resources/errors_en.properties`, `errors_ar.properties`
- Logging: `src/main/java/com/apogee/product/aop/LoggerAspect.java`

---
If any section needs more detail (examples for mapping edge-cases, how to add OpenAPI schemas, or test stubs), tell me which area to expand and I will iterate. Please review and point out anything unclear or missing. 
