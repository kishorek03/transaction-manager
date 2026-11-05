# Iyarkaivanam Transaction Manager

Spring Boot 3.5 REST backend for the **Iyarkaivanam** shop POS, sales, expenses, cash movements, and daily cash book.

- **Stack**: Java 21, Spring Boot 3.5.0-M3, Spring Data JPA, MySQL, Lombok, MapStruct.
- **Client**: Expo / React Native POS mobile/web app (CORS allowed: `localhost:8081`, `localhost:19006`).
- **Timezone**: Hardcoded to `Asia/Kolkata` (IST) across the JVM, Jackson, and database timestamps.

---

## Quick Commands

Use the Maven wrapper (`./mvnw.cmd` on Windows, `./mvnw` on Linux/macOS):

```bash
# Fast compile check (syntax & types, ~4s)
./mvnw.cmd test-compile -DskipTests

# Package executable JAR (target/transaction-manager-0.0.1-SNAPSHOT.jar)
./mvnw.cmd clean package -DskipTests

# Run locally with QA database (default)
./mvnw.cmd spring-boot:run

# Run locally with localhost MySQL
./mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local

# Run tests
./mvnw.cmd test -Dtest=DateRangeTest

# Build and push Docker image (uses Dockerfile.local)
.\docker-build-push.ps1 <tag>     # Windows PowerShell
./docker-build-push.sh <tag>      # Bash/Linux
```

---

## Configuration & Environments

Two configuration locations exist:
1. `src/main/resources/application.properties` (packaged in the JAR).
2. `config/*.properties` in project root (overrides classpath properties when run locally from the project root; not packaged in Docker image).

| Profile | Target Database | DDL Auto | Notes |
| :--- | :--- | :--- | :--- |
| **`local`** | `localhost:3306/iyarkaivanam_db` | `update` | Uses `config/application-local.properties`. |
| **`qa`** | Clever Cloud MySQL (default) | `update` | Shared QA testing environment. |
| **`prod`** | `143.244.134.163:3306/iyarkaivanam_db` | **`none`** | Schema changes **MUST** be executed manually via SQL. |

> [!CAUTION]
> **Production DDL Rule**: `ddl-auto=none` is set on production. Hibernate will NOT auto-create or alter columns/tables. Any entity changes require manual SQL DDL on the production database.

---

## Architecture & Code Conventions

Package root: `com.transaction`

```
com.transaction
├── config          # Security, JWT, WebConfig, AuditorAware, GlobalExceptionHandler
├── controller      # REST endpoints (return ApiResponse<T>, consume/produce DTOs)
├── cron            # CashBalanceCronService (runs daily at 00:10 IST)
├── dto             # Request/Response payloads + /mapper (MapStruct) + /projection
├── entity          # JPA Entities extending BaseEntity
├── exception       # Custom exceptions (BadRequestException)
├── repository      # Spring Data JPA repositories with custom JPQL queries
├── service         # Business logic layer
└── util            # Helper utilities
```

### Key Architectural Guidelines

1. **Layering & DTOs**:
   - Controllers **never** accept or return raw JPA entities. Always use DTOs.
   - All REST responses are wrapped in `ApiResponse<T>` (`status`, `message`, `data`, `errorDetails`).
   - Throw `BadRequestException` for user-facing validation errors (mapped to HTTP 400).

2. **Security (`SecurityConfig.java`)**:
   - Stateless JWT authentication via `JwtAuthenticationFilter`.
   - `/api/auth/**` is public; all other endpoints require authentication.
   - Roles: `ROLE_EMPLOYEE`, `ROLE_ADMIN`, `ROLE_SUPERADMIN`.
   - **CRITICAL**: Spring Security 6 has no open default rule (`anyRequest()`). **Every new endpoint path and method MUST be explicitly declared in `SecurityConfig.java`**, otherwise it will return `403 Forbidden` by default.

3. **Auditing & BaseEntity**:
   - Entities extend `BaseEntity` (`createdAt`, `updatedAt`, `createdBy`, `lastModifiedBy`).
   - Handled automatically by `AuditorAwareImpl`. Date-range filtering queries (sales, reports) filter on `createdAt`.

4. **MapStruct**:
   - Mappers use `@Mapper(componentModel = "spring")`.
   - If adding null checks or conditional mappings, verify generated MapStruct classes via `./mvnw.cmd test-compile -DskipTests`.

---

## Domain Concepts & Master Data Rules

- **Orders & Sales**:
  - `Order` 1→N `Sale` (cascade all; use helper `Order.addSale`).
  - An `Order` has exactly one `PaymentMethod`.
  - Each `Sale` references a `Product` and optional `Flavour` and `AddOn`.
- **Products**:
  - `Product` has an `is_active` (`Boolean`, default `true`) column.
  - `GET /api/products?activeOnly=true` filters active products; default `/api/products` returns all.
- **Expenses & Master Data Mapping**:
  - `Expense` can be linked to `expense_item_id` (`ExpenseItem`).
  - **Sugarcane Bundle**: `expense_item_id = 1`, `category_id = 6` (Sugarcane Stocks), `unit_id = 16` (Kilogram / kg), standardized item name: `'Sugarcane Bundle'`.
  - **Milk**: `expense_item_id = 5`, `unit_id = 22` (Litre / L), standardized item name: `'Milk'`.
  - **Caution**: Do NOT map non-stock expenses (e.g., peeling labour `கரும்பு சீவ`, waste disposal, wages, transportation) to stock expense items.
- **Daily Cash Book**:
  - `cron/CashBalanceCronService` runs daily at `00:10` IST.
  - Formula: `Today's Opening = Yesterday's Balance + Yesterday's Cash Sales - Cash Expenses + Net Cash Movements`.
  - If an admin creates a manual opening balance for today, the cron skips computation for that day.

---

## Docker & Production Deployment

### Docker Builds
- **Always use `Dockerfile.local`** (which packages the host-compiled `target/*.jar`).
- **Do NOT use multi-stage `Dockerfile`** for local builds: it attempts to fetch Maven dependencies inside the container and hangs indefinitely when behind VPN/corporate network proxies (FortiVPN).

### Production Server
- **Host**: `143.244.134.163` (SSH user: `root`)
- **Directory**: `/app/iyarkaivanam-java`
- **Docker Compose**: Uses `.env` for `TAG` (e.g. `TAG=0.0.8`).
- **Image**: `kishorek12/iyarkaivanam-backend:<tag>`
- **Port**: 8080 (container mapped to host port 8080).
- Detailed runbooks available in `.agents/skills/deploy-production` and `.agents/skills/database-operations`.
