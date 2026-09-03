# rasta.uz

Online marketplace platform where sellers in Uzbekistan create their own shops. Built with Java 21, Spring Boot 3, PostgreSQL, and React 18.

## Tech Stack

| Layer      | Technology                                                    |
|------------|---------------------------------------------------------------|
| Backend    | Java 21, Spring Boot 3.3.5, Spring Security (JWT), Flyway    |
| Frontend   | React 18.3, Vite 5, React Router v6, Zustand, Axios          |
| Database   | PostgreSQL 16                                                 |
| Build      | Maven 3.9+, npm                                               |

## Prerequisites

- **Java 21** (e.g., Microsoft OpenJDK, Eclipse Temurin, or any JDK 21+)
- **Maven 3.9+** (or use IntelliJ IDEA's bundled Maven)
- **Node.js 18+** and npm
- **Docker** (for PostgreSQL, or a local PostgreSQL 16 installation)

## Quick Start

### 1. Start PostgreSQL with Docker

```bash
docker compose up -d postgres
```

This starts PostgreSQL 16 on port 5432 with:
- Database: `rasta_db`
- User: `postgres`
- Password: `postgres`

Wait for the health check to pass:

```bash
docker exec rasta-postgres pg_isready -U postgres -d rasta_db
```

### 2. Build and Run the Backend

```bash
# From the project root
mvn clean package -DskipTests
java -jar target/rasta-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

Or via Maven directly:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Or from IntelliJ IDEA: run `RastaApplication.java` with the `dev` profile.

The backend starts on **http://localhost:8080**. On first run, Flyway automatically creates all database tables.

### 3. Install and Run the Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend starts on **http://localhost:5173** and proxies `/api` requests to the backend at `localhost:8080`.

### 4. Open in Browser

Go to **http://localhost:5173** to see the landing page.

## Full Docker Compose (all services)

If your Docker has internet access, you can run everything in containers:

```bash
docker compose up -d --build
```

This builds and starts:
- `rasta-postgres` on port 5432
- `rasta-backend` on port 8080
- `rasta-frontend` on port 5173

> **Note:** If Docker build fails due to DNS issues (corporate proxy/VPN), use the local approach above (Docker only for PostgreSQL).

## API Endpoints

### Authentication

```
POST /api/auth/send-code    { "phone": "+998901234567" }
POST /api/auth/verify       { "phone": "+998901234567", "code": "123456" }
```

SMS codes are logged to the console in dev mode.

### Shops (public)

```
GET  /api/shops                          List all shops
GET  /api/shops/{handle}                 Get shop by handle
GET  /api/shops/{shopId}/config          Get shop theme/config
GET  /api/shops/{shopId}/products        List products
GET  /api/shops/{shopId}/reviews         List reviews
```

### Shops (authenticated)

```
POST   /api/shops                        Create shop
PUT    /api/shops/{id}                   Update shop
DELETE /api/shops/{id}                   Delete shop
PUT    /api/shops/{shopId}/config        Update shop config
```

### Products (authenticated)

```
POST   /api/shops/{shopId}/products          Create product
PUT    /api/shops/{shopId}/products/{id}     Update product
DELETE /api/shops/{shopId}/products/{id}     Delete product
POST   /api/shops/{shopId}/inventory/restock Restock variants
POST   /api/shops/{shopId}/inventory/adjust  Adjust stock
```

### Orders

```
POST /api/shops/{shopId}/orders              Create order (public)
GET  /api/shops/{shopId}/orders              List orders (auth)
PUT  /api/shops/{shopId}/orders/{id}/confirm Confirm order (auth)
PUT  /api/shops/{shopId}/orders/{id}/status  Update status (auth)
PUT  /api/shops/{shopId}/orders/{id}/cancel  Cancel order (public)
```

### Sales (authenticated)

```
GET  /api/shops/{shopId}/sales         List sales
POST /api/shops/{shopId}/sales         Create POS sale
```

### Reviews (public)

```
GET  /api/shops/{shopId}/reviews       List reviews
POST /api/shops/{shopId}/reviews       Add review
```

## Project Structure

```
rasta/
├── pom.xml                          Maven build
├── docker-compose.yml               Docker services
├── Dockerfile.backend               Backend Docker image
├── src/main/java/uz/rasta/
│   ├── RastaApplication.java
│   ├── config/                      Security, JWT, CORS
│   ├── entity/                      JPA entities (12)
│   ├── dto/                         Request/Response DTOs
│   ├── repository/                  Spring Data JPA repos
│   ├── service/                     Business logic
│   └── controller/                  REST controllers (7)
├── src/main/resources/
│   ├── application.yml              Configuration
│   └── db/migration/V1__init.sql    Flyway migration
└── frontend/
    ├── package.json
    ├── vite.config.js
    ├── Dockerfile                   Frontend Docker image
    ├── nginx.conf                   Production nginx config
    └── src/
        ├── App.jsx                  Router & layout
        ├── api/client.js            Axios API client
        ├── store/                   Zustand stores
        ├── i18n/                    EN/RU/UZ translations
        ├── data/                    Themes, palettes, types
        ├── components/              Reusable UI components
        ├── pages/                   Route pages
        │   ├── Landing.jsx
        │   ├── Auth.jsx
        │   ├── Onboarding.jsx
        │   ├── Marketplace.jsx
        │   ├── StorefrontPage.jsx
        │   └── dashboard/           Dashboard views (9)
        ├── hooks/                   Custom hooks
        └── styles/                  CSS (6 files)
```

## Configuration

### application.yml

| Property                       | Default                  | Description            |
|--------------------------------|--------------------------|------------------------|
| `spring.datasource.url`       | `jdbc:postgresql://localhost:5432/rasta_db` | Database URL |
| `spring.datasource.username`  | `postgres`               | DB username            |
| `spring.datasource.password`  | `postgres`               | DB password            |
| `app.jwt.secret`              | (base64 encoded)         | JWT signing key        |
| `app.jwt.expiration-ms`       | `86400000` (24h)         | Token expiration       |

Override with environment variables:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://myhost:5432/mydb java -jar target/rasta-0.0.1-SNAPSHOT.jar
```

## i18n

The app supports three languages: English (EN), Russian (RU), and Uzbek (UZ). Language can be switched via the pill selector in the navbar.
