# Spring PetClinic — Agent Guide

## Architecture Overview

Spring Boot 4 / Java 17 MVC application using Thymeleaf templates, Spring Data JPA, and a multi-database strategy. No service layer — controllers talk directly to repositories.

**Package layout** (`src/main/java/.../petclinic/`):
- `model/` — JPA base classes only: `BaseEntity` (id), `NamedEntity` (name), `Person` (firstName/lastName)
- `owner/` — domain + web for owners, pets, visits (all co-located in one package)
- `vet/` — domain + web for vets and specialties
- `system/` — `CacheConfiguration`, `WebConfiguration` (i18n), `WelcomeController`, `CrashController`

**Data model**: `Owner` → `Pet[]` → `Visit[]` (cascade ALL, fetch EAGER). `Pet` has a `PetType` (via `types` table). `Vet` has `Specialty[]` (many-to-many via `vet_specialties`).

**REST dual exposure**: `VetController` serves both HTML (`/vets.html`) and JSON/XML (`/vets`) from the same controller method — no separate REST layer.

## Databases & Profiles

Default profile uses in-memory **H2** (schema/data auto-loaded from `src/main/resources/db/h2/`). Switch with `spring.profiles.active`:

| Profile | Datasource | SQL scripts |
|---------|-----------|-------------|
| *(default)* | H2 in-memory | `db/h2/` |
| `mysql` | MySQL | `db/mysql/` |
| `postgres` | PostgreSQL | `db/postgres/` |

Profile properties live in `application-mysql.properties` / `application-postgres.properties`. Env vars override: `MYSQL_URL`, `MYSQL_USER`, `MYSQL_PASS` / `POSTGRES_URL`, `POSTGRES_USER`, `POSTGRES_PASS`.

`spring.jpa.hibernate.ddl-auto=none` — the app **never** auto-generates DDL; SQL scripts are always the source of truth.

## Key Developer Commands

```bash
# Run (H2, default)
./mvnw spring-boot:run

# Run tests (H2)
./mvnw test

# Recompile SCSS → CSS (required after any .scss edit)
./mvnw package -P css

# Build OCI container image (no Dockerfile needed)
./mvnw spring-boot:build-image

# Run with MySQL via Docker Compose
docker compose up mysql
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

MySQL integration tests use **Testcontainers** (requires Docker); Postgres tests use **Docker Compose** (`docker-compose.yml`). Both are skipped automatically without Docker.

## Testing Patterns

- `PetClinicIntegrationTests` — full `@SpringBootTest` with H2; also has a `main()` for running as a dev app with DevTools.
- `MySqlIntegrationTests` — `@ActiveProfiles("mysql")` + `@Testcontainers`; `@DisabledInNativeImage`.
- `PostgresIntegrationTests` — uses `docker-compose.yml` service `postgres`.
- Unit tests live under `src/test/java/.../owner/`, `vet/`, `system/`, `model/`.

## Code Conventions

- **No service layer**: controllers inject repositories directly (e.g., `OwnerController(OwnerRepository owners)`).
- **`@InitBinder` blocks `id`** in every controller — never bind `id` from form input.
- **`@ModelAttribute` pre-loads entities** — `OwnerController.findOwner()` and `VisitController.loadPetWithVisit()` fetch from DB before every handler, throwing `IllegalArgumentException` on missing records (not 404).
- **`PetTypeFormatter`** converts PetType name ↔ object for form binding; registered as a `@Component` (Spring auto-detects it).
- **Validation messages** use message keys (e.g., `{telephone.invalid}`) resolved from `src/main/resources/messages/messages*.properties` (9 languages). Add new constraint messages there.
- **Snake-case column naming**: `PhysicalNamingStrategySnakeCaseImpl` — Java fields map automatically (e.g., `birthDate` → `birth_date`).
- **Caching**: only the `vets` cache exists, configured via JCache/Caffeine in `CacheConfiguration`. Add new caches there.
- **i18n**: language switched via `?lang=de` URL param (see `WebConfiguration`); session-scoped.
- **CSS**: edit `.scss` files under `src/main/scss/`, not the generated `petclinic.css` directly.
- **Commits** require a `Signed-off-by` trailer (DCO).

## Key Files

| Purpose | Path |
|---------|------|
| App entry point | `src/main/java/.../PetClinicApplication.java` |
| GraalVM native hints | `src/main/java/.../PetClinicRuntimeHints.java` |
| DB schema (H2) | `src/main/resources/db/h2/schema.sql` |
| Cache config | `src/main/java/.../system/CacheConfiguration.java` |
| i18n messages | `src/main/resources/messages/messages.properties` |
| Thymeleaf layout | `src/main/resources/templates/fragments/layout.html` |

