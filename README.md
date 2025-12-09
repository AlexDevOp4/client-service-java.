# Client Service – Java / Spring Boot Demo

## Overview

This project is a **Client Management Service** built with **Java** and **Spring Boot**.

It is designed to demonstrate:

- **Multithreading in Java** (background processing alongside request handling)
- **Creating REST APIs** with Spring Boot
- **Connecting to a database and performing CRUD operations** using Spring Data JPA
- **Data processing in Java** using the Stream API

The domain is simple: **clients** with a `name` and `sessionsRemaining`, similar to a personal training business.

---

## Tech Stack

- Java 17 (or your version)
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database (in-memory dev DB)
- Maven

---

## Main Features (Mapped to Requirements)

### 1. REST APIs

Implemented in `ClientController`:

- `GET /clients` – Get all clients
- `POST /clients` – Create a new client
- `GET /clients/{id}` – Get a single client by ID
- `PUT /clients/{id}` – Update a client’s name or sessions
- `DELETE /clients/{id}` – Delete a client
- `POST /clients/{id}/attend-session` – Decrement `sessionsRemaining` for a client
- `GET /clients/stats` – Compute and return aggregate stats

### 2. Database + CRUD

- `ClientEntity` is a JPA entity mapped to a `client_entity` table.
- `ClientRepository extends JpaRepository<ClientEntity, Long>` provides:
    - `findAll`, `findById`, `save`, `delete`, etc.
- All REST endpoints use `ClientRepository` for persistence.

### 3. Data Processing in Java

In the stats logic (now in `StatsService`):

- Loads all clients from the database.
- Uses **Java Streams** to compute:
    - `totalClients`
    - `totalSessions` (sum of `sessionsRemaining`)
    - `averageSessions` (rounded to 2 decimals)
    - `lowSessionClients` (clients with `sessionsRemaining <= 2`)

This is exposed via:

- `GET /clients/stats` (REST endpoint)
- And reused by a background job (see below).

### 4. Multithreading in Java (Inside Spring)

In `StatsService`:

- A method annotated with `@Scheduled(fixedRate = 10000)` runs every **10 seconds**.
- It calls the same stats computation logic and logs a summary line.

Spring runs this scheduled method on a **background thread**, while the main web thread pool handles HTTP requests concurrently.

This demonstrates:

- **Concurrent processing** using Spring’s scheduling mechanism.
- Shared use of the same `ClientRepository` and database from multiple threads.

---

## Project Structure (Key Files)

- `ClientServiceApplication.java`
    - Main Spring Boot application entry point (`@SpringBootApplication`, `@EnableScheduling`).

- `model/ClientEntity.java`
    - JPA entity for clients (`id`, `name`, `sessionsRemaining`).
    - Includes a `toString()` override for readable logging.

- `repository/ClientRepository.java`
    - `JpaRepository<ClientEntity, Long>` for CRUD operations.

- `controller/ClientController.java`
    - Defines all `/clients` REST endpoints (CRUD, attend-session, stats).

- `service/StatsService.java`
    - Contains `computeClientStats()` for data processing.
    - Contains `@Scheduled` method `logClientStats()` that periodically logs stats in a background thread.

---

## How to Run

### Prerequisites

- Java (JDK) installed
- Maven installed (if running from CLI)
- IntelliJ IDEA (project was built using IntelliJ)

### Running from IntelliJ

1. Open the project in IntelliJ.
2. Make sure Maven has imported dependencies (`pom.xml`).
3. Run `ClientServiceApplication` (right-click → Run).

The app will start on `http://localhost:8080`.

### Running from the Command Line

```bash
mvn clean install
mvn spring-boot:run
