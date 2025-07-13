# Card Cost API

This project is a REST API that calculates the clearing cost of a payment card based on the issuing country.

## Features

- CRUD operations for clearing costs
- Card cost calculation using an external BIN lookup API
- Multi-level caching (Caffeine, Redis, Database)
- Circuit breaker and retry mechanisms for the external API
- Dockerized solution with PostgreSQL and Redis
- API documentation with SwaggerUI
- Secure API with JWT authentication: All API endpoints (except authentication, health checks, and Swagger UI) require a valid JWT token.
- Rate limiting: Implemented using Bucket4j to control the number of requests to critical endpoints.
- Centralized error handling: Consistent error responses using `ApiErrorDTO` for validation, authentication, and unexpected errors.
- Health checks: Spring Boot Actuator endpoints provide insights into the application's health.

## API Documentation

Once the application is running, you can access the SwaggerUI documentation at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

### Endpoints

- `POST /api/v1/authenticate`: Authenticate with username and password to obtain a JWT token.
- `POST /api/v1/payment-cards-cost`: Calculate the cost of a payment card. (Requires JWT)
- `GET /api/v1/clearing-costs`: Get all clearing costs. (Requires JWT)
- `POST /api/v1/clearing-costs`: Create a new clearing cost. (Requires JWT)
- `PUT /api/v1/clearing-costs/{id}`: Update a clearing cost. (Requires JWT)
- `DELETE /api/v1/clearing-costs/{id}`: Delete a clearing cost. (Requires JWT)
- `GET /actuator/health`: Check the health of the application.

## Business Logic in CardCostService

The `CardCostService` is responsible for calculating the clearing cost of a given card number. It performs the following steps:
1. Extracts the BIN (first 6 digits) from the provided card number.
2. Uses the `BinLookupService` to query an external API (BinList.net) for country information based on the BIN.
3. Leverages a multi-level caching strategy (Caffeine, Redis, and a database cache) to minimize calls to the rate-limited external BIN lookup API.
4. Based on the country information, it looks up the corresponding clearing cost from the `clearing_costs` matrix.
5. If the country is 'US', 'GR', or 'OT' (Others), it applies the respective cost. If the country is not found or the BIN lookup fails, it defaults to the 'OT' (Others) cost.
6. Returns the country code and the calculated cost.

## Rate Limiting

Rate limiting is implemented using **Bucket4j** to protect the API from abuse and ensure fair usage.
- The `/api/v1/payment-cards-cost` endpoint is limited to **7000 requests per minute**.
- The `/api/v1/authenticate` endpoint is limited to **10 requests per minute** to prevent brute-force attacks.

## Authentication with JWT Token

The API uses JSON Web Tokens (JWT) for secure authentication.
- To access protected endpoints, clients must first obtain a JWT by sending a `POST` request to `/api/v1/authenticate` with a valid username and password.
- The authentication endpoint will return a JWT token with a customizable expiration.
- This token must then be included in the `Authorization` header of subsequent requests to protected endpoints in the format `Bearer <JWT_TOKEN>`.

**Test User Credentials:**
- **Username**: `test-user`
- **Password**: `password`

## Health Checks

Spring Boot Actuator provides various endpoints for monitoring and managing the application. The `/actuator/health` endpoint is exposed and accessible without authentication to check the application's health status.

## Profiles and Data Initialization

The application uses Spring Profiles to manage different configurations for various environments.

- **Default Profile**: Used when no specific profile is active (e.g., when running directly from your IDE). It uses an in-memory H2 database for data persistence.
- **Docker Profile**: Activated when running the application via `docker-compose`. It uses PostgreSQL for data persistence and Redis for distributed caching.

**Data Initialization:**
- For both H2 (default profile) and PostgreSQL (docker profile), initial data for the `clearing_costs` table and a `users` table with a test user are initialized using the `src/main/resources/data.sql` script.
- This script is executed automatically by Spring Boot on application startup.

## Configuration

### `application.properties` (Default Profile)

This file contains the default configuration for the application, primarily used when running the application directly (e.g., from an IDE).

### `application-docker.properties` (Docker Profile)

This file contains configuration overrides specifically for the `docker` profile, used when the application is run via Docker Compose.

### Logging Configuration (`log4j2.properties`)

The application uses Log4j2 for logging. The `log4j2.properties` file configures logging behavior, including log levels, appenders, and log file locations.

Logs are typically generated in the `logs/` directory relative to where the application is executed. You can modify `log4j2.properties` to change log levels, output formats, and destinations.

## Getting Started

### Prerequisites

- Java 17
- Docker
- Docker Compose

### Building and Running the Application

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/cmcl95/card-cost-api.git
    cd card-cost-api
    ```

2.  **Build and run with Docker Compose (Recommended for full environment):**

    ```bash
    docker-compose up --build
    ```

3.  **Run directly from project (using default H2 profile):**

    ```bash
    ./gradlew bootRun
    ```

4.  **Access the application:**

    The API will be available at [http://localhost:8080](http://localhost:8080).

## Running Tests

To run the tests, execute the following command:

```bash
./gradlew test
```