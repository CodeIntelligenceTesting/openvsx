# Open VSX Server

Spring Boot backend providing the REST API, extension storage, search, and background jobs.

## Tech Stack

- **Java 25**, Spring Boot 3.5.10, Jetty
- **PostgreSQL** with Flyway migrations and JooQ
- **Elasticsearch** 8.x (optional — DB search fallback exists)
- **Redis** cluster (rate limiting, caching)
- **JobRunr** for background job scheduling
- **Hibernate** 6.x / Spring Data JPA

## Build & Run

```bash
./gradlew assemble          # Compile (skip tests)
./gradlew build             # Compile + all tests
./gradlew runServer         # Start the server (needs DB + ES running)
```

## Testing

```bash
./gradlew unitTests             # Unit tests only (fast, no containers needed)
./gradlew test                  # All tests including integration tests
./gradlew s3IntegrationTests    # S3 tests against LocalStack (needs Docker)
```

`unitTests` excludes: `IntegrationTest`, `CacheServiceTest`, `RepositoryServiceSmokeTest`, `AwsStorageServiceIntegrationTest`.

Integration tests use **Testcontainers** — Docker must be running.

## Architecture

Layered architecture under `org.eclipse.openvsx`:

| Layer | Key Classes | Purpose |
|-------|-------------|---------|
| **API** | `RegistryAPI`, `UserAPI` | REST controllers |
| **Service** | `LocalRegistryService`, `ExtensionService`, `UserService` | Business logic |
| **Repository** | `repositories/` package | Spring Data JPA repositories |
| **Storage** | `storage/` package | File/asset storage abstraction |
| **Search** | `search/` package | Extension search abstraction |

### Key Packages

```
adapter/        — Upstream registry adapter (mirroring from marketplace.visualstudio.com)
admin/          — Admin API endpoints
cache/          — Spring cache configuration
eclipse/        — Eclipse Foundation API integration (ECA, publisher agreement)
entities/       — JPA entities (Extension, ExtensionVersion, FileResource, etc.)
json/           — JSON DTOs for API responses
mail/           — Email notification service
metrics/        — Prometheus metrics
migration/      — Data migration handlers (not DB schema — see Flyway)
mirror/         — Extension mirroring logic
publish/        — Extension publish pipeline
ratelimit/      — Rate limiting (Redis-backed)
repositories/   — Spring Data repositories
scanning/       — Extension virus/malware scanning
search/         — Search service (Elasticsearch + DB fallback)
security/       — OAuth2 security configuration
storage/        — Storage backends (S3, GCS, Azure, local filesystem)
util/           — Shared utilities
web/            — Web configuration
```

### Storage Abstraction

`IStorageService` interface with implementations:
- `AWSStorageService` — Amazon S3
- `GoogleCloudStorageService` — GCS
- `AzureBlobStorageService` — Azure Blob Storage
- `LocalStorageService` — Local filesystem

### Search Abstraction

`ISearchService` interface with implementations:
- `ElasticSearchService` — Elasticsearch (production)
- `DatabaseSearchService` — PostgreSQL full-text search (fallback/dev)

## Database

### Flyway Migrations

Schema migrations live in `src/main/resources/db/migration/` (V1 through V1_62). Follow the naming convention `V1_{next}__Description.sql` when adding new migrations.

### JooQ Generated Code

`src/main/jooq-gen/` contains JooQ-generated classes. **Do not edit these files manually** — they are regenerated from the database schema.

## Code Style

No formatter config exists. Follow existing patterns:
- Standard Java conventions
- Constructor injection (not field injection)
- Existing test patterns use JUnit 5 + Spring Boot Test + Mockito
