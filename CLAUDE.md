# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) and AI assistants working in this repository.

See [GEMINI.md](file:///C:/Users/Prithvi.Prakash/Daytona/LearningProjects/transaction-manager/GEMINI.md) for full project architecture and domain notes.

## Overview

Spring Boot 3.5 (milestone, pulled from `repo.spring.io/milestone`) REST backend for the **Iyarkaivanam** shop POS/cash book: orders/sales, expenses, cash movements, and daily cash balance. Clients are an Expo/React Native app (CORS allows `localhost:8081` and `localhost:19006`). Java 21 runtime, MySQL, Lombok + MapStruct.

## Commands

Use the Maven wrapper (`./mvnw.cmd` on Windows, `./mvnw` on Linux/macOS):

```bash
./mvnw.cmd test-compile -DskipTests             # fast compile check (~4s)
./mvnw.cmd clean package -DskipTests            # build jar (target/transaction-manager-0.0.1-SNAPSHOT.jar)
./mvnw.cmd spring-boot:run                      # run locally with QA database (default)
./mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local   # use config/application-local.properties (localhost MySQL)
./mvnw.cmd test -Dtest=DateRangeTest            # run specific test
.\docker-build-push.ps1 <tag>                  # build & push Docker image using Dockerfile.local (Windows PowerShell)
./docker-build-push.sh <tag>                   # build & push Docker image using Dockerfile.local (Bash)
```

`iyarkaivanam.postman_collection.json` at the repo root contains example requests for the API.

## Configuration & Deployment

- Two sets of properties exist: `src/main/resources/application.properties` (packaged in the jar) and `config/*.properties`. When run from the repo root, Spring Boot automatically loads `./config/` and it overrides the classpath file. The Docker image copies only the jar, so in containers only the classpath file plus env vars apply.
- Profiles: `local` (localhost MySQL, `ddl-auto=update`), `qa` (Clever Cloud MySQL, `ddl-auto=update`), `prod` (`143.244.134.163:3306/iyarkaivanam_db`, `ddl-auto=none`).
- **Production DDL**: `ddl-auto=none` is set on prod. Hibernate will NOT auto-create or alter columns/tables. Manual SQL DDL is required for any entity/schema changes in production.
- **Production Server**: `143.244.134.163` at `/app/iyarkaivanam-java`. Deployed via Docker Compose pulling `kishorek12/iyarkaivanam-backend:<tag>`.
- **Docker builds**: Always use `Dockerfile.local` with pre-built jar. Do not use multi-stage `Dockerfile` locally because VPN/proxies cause Maven dependency download timeouts.
- HikariCP pool is deliberately small (max 3) because of hosted-DB connection limits.

## Architecture

Package `com.transaction`, classic layering: `controller` → `service` → `repository` (Spring Data JPA) → `entity`, with `dto` + `dto/mapper` (MapStruct, `componentModel = "spring"`) in between. Controllers take/return DTOs, never raw entities.

- **Responses**: endpoints return `ApiResponse<T>` (`status`, `message`, `data`, `errorDetails`). `config/GlobalExceptionHandler` maps validation errors and `exception/BadRequestException` to 400 and everything else to 500, all in the same envelope. Throw `BadRequestException` for client errors.
- **Security** (`config/SecurityConfig`): stateless JWT. `JwtAuthenticationFilter` validates the bearer token; `/api/auth/**` (login/refresh, `AuthService`) is public. Authorization is entirely URL + HTTP-method based in `SecurityConfig` with roles `ROLE_EMPLOYEE`, `ROLE_ADMIN` and `ROLE_SUPERADMIN`. **New endpoints must be added to the matcher list**, or Spring Security 6 denies them by default (403 Forbidden).
- **JWT keys** (`config/JwtKeyConfig`) are random per process, so restarts invalidate active tokens. Refresh tokens are persisted in `RefreshToken`.
- **Auditing**: entities extend `BaseEntity` (`createdAt`, `updatedAt`, `createdBy`, `lastModifiedBy`) filled by JPA auditing via `AuditorAwareImpl`. Date-range queries filter on `createdAt`.
- **Domain**: `Order` 1→N `Sale` (cascade all; use `Order.addSale`), each sale referencing `Product` plus an optional `Flavour`/`AddOn`; an order has one `PaymentMethod`. Master data: `Category` (`CategoryType`), `Unit` (`UnitType`), `Product` (`is_active`), `Flavour`, `AddOn`, `PaymentMethod`, `ExpenseItem`.
- **Master Data Mappings**:
  - Sugarcane Bundle: `unit_id = 16` (kg), `category_id = 6` (Sugarcane Stocks), `expense_item_id = 1`, item = `'Sugarcane Bundle'`.
  - Milk: `unit_id = 22` (Litre), `expense_item_id = 5`, item = `'Milk'`.
  - Exclude wages, peeling labour, transport from stock expense items.
- **Cash book**: `cron/CashBalanceCronService` runs daily at 00:10 IST and writes today's `DailyCashBalance` = yesterday's balance + yesterday's cash sales − cash expenses + net `CashMovement`s. Skips if a row for today already exists.
- **Time zone**: JVM default is pinned to `Asia/Kolkata` (`TransactionManagerApplication`, `spring.jackson.time-zone`). All dates and times are IST.
