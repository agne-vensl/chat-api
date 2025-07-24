# Chat API

A backend REST API for a simple public chat application.  
All users communicate in a single shared channel, with role-based functionality for **users** and **admins**.

---

## Features

### User Capabilities

- **Retrieve all messages** (author, timestamp, content) sorted by newest first.
- **Post new chat messages.**

### Admin Capabilities

- **Register new users** (fails if username exists).
- **Delete users** (their messages are reassigned to an `anonymous` user).
- **Fetch statistics** per user:
  - Username
  - Total messages sent
  - First and last message timestamps
  - Average message length
  - Content of the last message

---

## Tech Stack

- **Java 21**
- **Spring Boot** (REST API)
- **Gradle** (build & dependency management)
- **H2 Database** (in-memory DB)
- **JWT** (authentication & authorization)
- **OpenAPI 3 (Swagger)** (API documentation)
- **Native SQL queries** (no ORM abstraction)

---

## Getting Started

### Prerequisites

- Java 21
- Gradle

### Setup

1. Clone the repository.
2. Run with Gradle:

```
./gradlew bootRun
```

3. Open Swagger UI: http://localhost:8080/swagger-ui.html

### API Endpoints

The full API is documented in Swagger, but key routes include:

| Method | Endpoint            | Description                    | Role  |
| ------ | ------------------- | ------------------------------ | ----- |
| POST   | `/login`            | Authenticate and receive a JWT | Any   |
| GET    | `/messages`         | Fetch all chat messages        | Any   |
| POST   | `/messages`         | Post a new message             | Any   |
| POST   | `/admin/users`      | Create a new user              | Admin |
| DELETE | `/admin/users/{id}` | Delete a user                  | Admin |
| GET    | `/admin/statistics` | Fetch user statistics          | Admin |

### Running Tests

```
./gradlew test
```

---

## Known Limitations & Planned Improvements

- **Validation:** Role validation in DTOs is currently hardcoded; should be dynamic.
- **Configuration:** Separate Spring profile for tests (with its own JWT secret) would improve test isolation.
- **Testing:**
  - Expand automated test coverage.
  - Add Postman end-to-end test collection.
- **Authentication:**
  - Consider resolving multiple valid JWT tokens per user (currently allowed).
  - Include a valid sample JWT token in Swagger examples (or explain how to generate one).
  - Avoid hardcoding the admin user.
