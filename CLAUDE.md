# rasta.uz

Online marketplace for sellers in Uzbekistan. Spring Boot 3.3.5 + Java 21 + PostgreSQL + React.

## Prerequisites

### PostgreSQL
```
Database: rasta_db
User: postgres
Password: postgres
Port: 5432
```

### MinIO (Object Storage for images)
```bash
docker run -d --name rasta-minio \
  -p 9000:9000 -p 9001:9001 \
  -e MINIO_ROOT_USER=minioadmin \
  -e MINIO_ROOT_PASSWORD=minioadmin \
  -v minio-data:/data \
  minio/minio server /data --console-address ":9001"
```

- API: http://localhost:9000
- Console: http://localhost:9001 (login: minioadmin / minioadmin)
- Bucket `rasta-media` is auto-created on app startup with public read policy
- Upload endpoint: `POST /api/media` (multipart, auth required) returns `{ "url": "http://localhost:9000/rasta-media/<uuid>.jpg" }`

## Running

### Backend
```bash
./mvnw spring-boot:run
```
Runs on http://localhost:8080

### Frontend
```bash
cd frontend && npm install && npm run dev
```
Runs on http://localhost:5173, proxies `/api` to backend.

## Project structure

```
src/main/java/uz/rasta/
  config/       - Security, JWT, CORS, exception handling
  controller/   - REST endpoints
  dto/          - Request/response objects
  entity/       - JPA entities
  repository/   - Spring Data repositories
  service/      - Business logic
  media/        - MinIO media module (upload/download)
```

## Key commands

- Compile: `./mvnw compile`
- Run: `./mvnw spring-boot:run`
- Frontend build: `cd frontend && npm run build`
