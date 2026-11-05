---
name: deploy-production
description: >-
  Use this skill when deploying backend Docker container updates to the production
  server (143.244.134.163). Provides SSH commands, docker-compose steps, .env tag updates,
  and container log verification.
---

# Production Deployment Runbook

Deploy backend Docker updates to the Iyarkaivanam production server.

## Server Connection Details

- **Host IP**: `143.244.134.163`
- **SSH User**: `root`
- **Password**: `2025*Rbeipaqct`
- **Application Directory**: `/app/iyarkaivanam-java`
- **Container Name**: `iyarkaivanam-backend` (service `backend-app`)
- **Docker Image**: `kishorek12/iyarkaivanam-backend:<tag>`

---

## Deployment Steps

Execute the following remote commands over SSH or using a deployment script:

### 1. Update the Target Tag in `.env`
Inspect and update `/app/iyarkaivanam-java/.env` with the target release tag:

```bash
cd /app/iyarkaivanam-java
# Check current tag
cat .env

# Update to target tag (e.g., 0.0.8)
sed -i 's/^TAG=.*/TAG=0.0.8/' .env
```

### 2. Pull the New Docker Image
Pull the newly published image from Docker Hub:

```bash
docker pull kishorek12/iyarkaivanam-backend:0.0.8
```

### 3. Recreate the Container
Restart the application container with the new image:

```bash
cd /app/iyarkaivanam-java
docker-compose down && docker-compose up -d
```

### 4. Verify Application Health and Logs
Wait 15–20 seconds for Spring Boot startup, then verify:

```bash
# Verify container is running and healthy
docker ps --filter "name=iyarkaivanam-backend"

# Check startup logs
docker logs iyarkaivanam-backend --tail 60
```

Expected log output:
`Started TransactionManagerApplication in XX.XXX seconds (process running for XX.XXX)`

---

## Troubleshooting

- **Image pull fails / authentication required**: Ensure the image was already pushed to Docker Hub by the user locally.
- **Port 8080 collision**: If another process is holding port 8080, run `lsof -i :8080` or `netstat -tulpn | grep 8080`.
- **Database connection failure on startup**: Check that `/app/iyarkaivanam-java/.env` has valid `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`. Production MySQL is running locally on the same host or accessible on port 3306.
