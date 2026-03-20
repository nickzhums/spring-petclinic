# Spring PetClinic – AI Agent Guide

## Architecture Overview

Spring Boot 4 / Java 17 MVC application with Thymeleaf templates. No service layer — controllers talk directly to Spring Data JPA repositories.

**Package layout** (each domain owns its full vertical slice):
```
org.springframework.samples.petclinic
├── model/         # Shared JPA base classes: BaseEntity, NamedEntity, Person
├── owner/         # Owner, Pet, Visit, PetType + controllers + repos + formatter
├── vet/           # Vet, Specialty + controller + repo (cached)
└── system/        # CacheConfiguration, WelcomeController, CrashController, WebConfiguration
```

**Entity hierarchy:** `BaseEntity` (id) → `NamedEntity` (name) → domain entities; `Person` (firstName, lastName) → `Owner`. All extend `BaseEntity`.

**Key data relationships:**
- `Owner` → `Pet` (OneToMany, EAGER, cascade ALL, JoinColumn `owner_id`)
- `Pet` → `Visit` (OneToMany, EAGER, cascade ALL)
- `Pet` → `PetType` (ManyToOne)
- `Vet` → `Specialty` (ManyToMany)

## Database & Profiles

Default profile uses **H2 in-memory** — schema/data loaded from `src/main/resources/db/h2/`.

Switch databases via Spring profile:
- `spring.profiles.active=mysql` → `application-mysql.properties`, scripts from `db/mysql/`
- `spring.profiles.active=postgres` → `application-postgres.properties`, scripts from `db/postgres/`

The `database` property in `application.properties` drives `spring.sql.init.*-locations` using `classpath*:db/${database}/schema.sql` — always keep in sync when adding a new DB profile.

`spring.jpa.hibernate.ddl-auto=none` — DDL is always managed by SQL init scripts, never by Hibernate.

## Build & Run

```bash
./mvnw spring-boot:run                   # H2 (default)
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql

./mvnw test                              # all tests
./mvnw package -P css                    # recompile SCSS → petclinic.css (Maven only)
./mvnw spring-boot:build-image           # OCI image (no Dockerfile needed)

./gradlew bootRun                        # Gradle alternative
```

**CSS:** Edit `src/main/scss/petclinic.scss`; the compiled output `src/main/resources/static/resources/css/petclinic.css` must be re-generated with `-P css`. Do not hand-edit the compiled CSS.

## Code Style & Formatting

- **spring-javaformat** enforces formatting on every `./mvnw validate` run. Apply fixes with: `./mvnw spring-javaformat:apply`
- **nohttp-checkstyle** enforces HTTPS URLs project-wide. Config: `src/checkstyle/nohttp-checkstyle.xml`.
- Field injection is not used — all dependencies are injected via constructor.
- Controllers are package-private classes (no `public` modifier); only `PetTypeFormatter` and model classes are `public`.

## Testing Patterns

| Test class | Profile | DB mechanism |
|---|---|---|
| `PetClinicIntegrationTests` | default | H2 in-memory |
| `MySqlIntegrationTests` | `mysql` | Testcontainers (`MySQLContainer`) — requires Docker; skipped without it |
| `PostgresIntegrationTests` | `postgres` | Docker Compose (`spring-boot-docker-compose`) |

Use `PetClinicIntegrationTests` as the fast-feedback loop during development (no Docker needed). The MySQL/Postgres tests are annotated `@DisabledInNativeImage` and `@DisabledInAotMode`.

## Caching

Only the `vets` cache exists, configured in `CacheConfiguration` via JCache/Caffeine. `VetRepository.findAll()` methods are annotated `@Cacheable("vets")`. If you add a new cacheable query, register the cache name in `CacheConfiguration.petclinicCacheConfigurationCustomizer()`.

## Frontend

Thymeleaf templates in `src/main/resources/templates/`. All pages use the `layout` fragment (`fragments/layout.html`) via `th:replace`. UI text is externalised in `src/main/resources/messages/messages*.properties` — use `#{key}` in templates; never hardcode UI strings.

Frontend dependencies (Bootstrap 5, Font Awesome 4) are served via **WebJars** — no npm/node toolchain needed.

## GraalVM Native Image

`PetClinicRuntimeHints` registers resource patterns (`db/*`, `messages/*`) and serialisation types required for AOT compilation. Update this class whenever new resources or serialisable types are added.

## Key Files

| Purpose | Path |
|---|---|
| DB init scripts | `src/main/resources/db/{h2,mysql,postgres}/` |
| Profile properties | `src/main/resources/application-{mysql,postgres}.properties` |
| Cache setup | `src/main/java/.../system/CacheConfiguration.java` |
| AOT hints | `src/main/java/.../PetClinicRuntimeHints.java` |
| SCSS source | `src/main/scss/petclinic.scss` |
| K8s manifests | `k8s/db.yml`, `k8s/petclinic.yml` |

