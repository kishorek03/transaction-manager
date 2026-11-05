---
name: docker-build
description: >-
  Use this skill when building and publishing Docker container images for the
  Iyarkaivanam backend. Explains local JAR packaging, Dockerfile.local usage,
  tagging, and resolving VPN/proxy build hanging issues.
---

# Docker Build & Push Guide

How to reliably build and push Docker images for `kishorek12/iyarkaivanam-backend`.

## Critical Rule: Always Use `Dockerfile.local`

> [!WARNING]
> Do NOT use the default multi-stage `Dockerfile` for local development builds!
> The default `Dockerfile` executes `./mvnw clean package` inside a Linux build container. When connected to a corporate VPN (such as FortiVPN), Docker containers fail to route through host proxy settings, causing Maven dependency downloads from `repo.maven.apache.org` and `repo.spring.io` to hang indefinitely (> 1000s).

### The Solution: `Dockerfile.local`
`Dockerfile.local` takes the pre-compiled JAR from the host's `target/` directory and builds in ~3–5 seconds:

```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/transaction-manager-0.0.1-SNAPSHOT.jar app.jar
ENV TZ=Asia/Kolkata
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## Build & Push Commands

### Option A: Using the Automated Scripts

**On Windows PowerShell**:
```powershell
.\docker-build-push.ps1 0.0.9
```

**On Linux / Git Bash / macOS**:
```bash
./docker-build-push.sh 0.0.9
```

### Option B: Manual Step-by-Step

1. **Package the executable JAR on host**:
   ```bash
   ./mvnw.cmd clean package -DskipTests
   ```
   Verify that `target/transaction-manager-0.0.1-SNAPSHOT.jar` exists.

2. **Build the image using `Dockerfile.local`**:
   ```bash
   docker build -f Dockerfile.local -t kishorek12/iyarkaivanam-backend:0.0.9 .
   ```

3. **Push to Docker Hub**:
   ```bash
   docker push kishorek12/iyarkaivanam-backend:0.0.9
   ```

---

## Authentication & Permissions

- Ensure the local Docker CLI is logged into Docker Hub:
  ```bash
  docker login
  ```
- If using a Personal Access Token (PAT), make sure it has **Read & Write** permissions (`repo:write`). An access token with only `repo:read` will fail on `docker push` with `access token has insufficient scopes`.
