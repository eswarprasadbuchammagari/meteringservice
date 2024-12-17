
# Metering Service

A Spring Boot application for calculating usage-based charges using different rate plans.

## Prerequisites
- Java 17
- Maven 3.8+
- MySQL 8.0+

## Setup
1. Create MySQL database:
```sql
CREATE DATABASE IF NOT EXISTS metering;
```

2. Update application.yml with your database credentials

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

## Testing
Run tests using:
```bash
mvn test
```

## API Documentation
The API will be available at: http://localhost:8080/api

### Endpoints
POST /api/metering/calculate - Calculate usage charges
GET /api/metering/rates - Get available rate plans