# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Spring Boot 3.5 (milestone, pulled from `repo.spring.io/milestone`) REST backend for a small shop's POS/cash book ("iyarkaivanam"): orders/sales, expenses, cash movements and a daily cash balance. Clients are an Expo/React Native app (CORS allows `localhost:8081` and `localhost:19006`). Java 21 runtime, MySQL, Lombok + MapStruct.

Note: `README.md` is largely generic boilerplate (wrong package names, mentions Redis/Flyway/health endpoints that don't exist). Trust the code over it.

## Commands

Use the Maven wrapper (`./mvnw` or `mvnw.cmd` on Windows).

```bash
./mvnw clean package -DskipTests        # build jar (target/transaction-manager-0.0.1-SNAPSHOT.jar)
./mvnw spring-boot:run                  # run locally
./mvnw spring-boot:run -Dspring-boot.run.profiles=local   # use config/application-local.properties (localhost MySQL)
./mvnw test                             # tests (only a context-load test exists; it needs a reachable DB)
./mvnw test -Dtest=TransactionManagerApplicationTests     # single test class
./docker-build-push.sh <tag>            # build & push kishorek12/iyarkaivanam-backend:<tag>
```

`iyarkaivanam.postman_collection.json` at the repo root contains example requests for the API.

## Configuration & deployment

- Two sets of properties exist: `src/main/resources/application.properties` (packaged in the jar) and `config/*.properties`. When run from the repo root, Spring Boot automatically loads `./config/` and it **overrides** the classpath file. The Docker image copies only the jar, so in containers only the classpath file plus env vars apply.
- `spring.profiles.active=qa` is the default in both. Profiles: `local` (localhost MySQL, `ddl-auto=update`), `qa` (Clever Cloud MySQL, `ddl-auto=update`), `prod` (`DB_URL`/`DB_USERNAME`/`DB_PASSWORD` env vars, `ddl-auto=none`).
- No migration tool: schema is managed by Hibernate `ddl-auto=update` outside prod. Entity changes need manual DDL for prod.
- Pushing to `main` deploys to Fly.io (`.github/workflows/deploy.yml`, app `transaction-manager`, region `bom`) via the multi-stage `Dockerfile`.
- HikariCP pool is deliberately tiny (max 3) because of hosted-DB connection limits.

## Architecture

Package `com.transaction`, classic layering: `controller` → `service` → `repository` (Spring Data JPA) → `entity`, with `dto` + `dto/mapper` (MapStruct, `componentModel = "spring"`) in between. Controllers take/return DTOs, never entities.

- **Responses**: endpoints return `ApiResponse<T>` (`status`, `message`, `data`, `errorDetails`). `config/GlobalExceptionHandler` maps validation errors and `exception/BadRequestException` to 400 and everything else to 500, all in the same envelope. Throw `BadRequestException` for client errors.
- **Security** (`config/SecurityConfig`): stateless JWT. `JwtAuthenticationFilter` validates the bearer token; `/api/auth/**` (login/refresh, `AuthService`) is public. Authorization is entirely URL + HTTP-method based in `SecurityConfig` with roles `ROLE_EMPLOYEE`, `ROLE_ADMIN` and `ROLE_SUPERADMIN`: employees can mostly read and create orders/sales/expenses; admins manage master data and cash. **New endpoints must be added to the matcher list**, or Spring Security 6 denies them by default because there is no `anyRequest()` rule.
- **JWT keys** (`config/JwtKeyConfig`) are random per process, so every restart or redeploy (and each Fly machine) invalidates all access and refresh tokens. Refresh tokens are persisted in `RefreshToken`.
- **Auditing**: entities extend `BaseEntity` (`createdAt`, `updatedAt`, `createdBy`, `lastModifiedBy`) filled by JPA auditing via `AuditorAwareImpl` (the authenticated username, or `system-user`). Date-range queries (e.g. order summaries) filter on `createdAt`.
- **Domain**: `Order` 1→N `Sale` (cascade all; use `Order.addSale`), each sale referencing `Product` plus an optional `Flavour`/`AddOn`; an order has one `PaymentMethod`. Master data: `Category` (`CategoryType`), `Unit` (`UnitType`), `Product`, `Flavour`, `AddOn`, `PaymentMethod`. Several entities have hand-written getters/setters alongside or instead of Lombok.
- **Cash book**: `cron/CashBalanceCronService` runs daily at 00:10 and writes today's `DailyCashBalance` = yesterday's balance + yesterday's cash sales − cash expenses + net `CashMovement`s (by `MovementType`). It skips if a row for today already exists, so an admin-created opening balance takes precedence. Changes to how sales, expenses or movements are recorded must keep the repository aggregate queries (`getTotalSalesByDateAndCash` etc.) consistent.
- **Time zone**: the app pins the JVM default to `Asia/Kolkata` (`TransactionManagerApplication`, `JAVA_TOOL_OPTIONS` in `fly.toml`, `spring.jackson.time-zone`). `LocalDate.now()` and `LocalDateTime` values are IST, so be careful when changing date/time handling (recent commits touched UTC handling).
