# 📚 Library Book Reservation System

This is a backend service for a modern digital library system that allows users to reserve books, manage their reservations, and ensures that business rules are enforced.

## 🧩 Features

- Reserve an available book
- Cancel a reservation
- Retrieve a reservation by ID
- List all reservations for a specific user
- Reservations automatically expire after 7 days if not picked up
- User registration and authentication via JWT

## 🚦 Business Rules

- A user cannot have more than 3 active reservations at the same time
- A book can only be reserved if there are available copies
- Canceling a reservation restores the book's availability
- Reservations expire automatically after 7 days if not picked up

## ⚙️ Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Security + JWT
- JPA / Hibernate
- PostgreSQL
- Redis (for caching)
- Docker

## 🗃️ API Endpoints

### Authentication

`POST /api/v1/auth/login`  
Authenticate and obtain a JWT

`POST /api/v1/auth/register`  
Register a new user

### Reservations

`POST /api/v1/reservations`  
Reserve a book

`GET /api/v1/reservations/{reservationId}`  
Get reservation by ID

`GET /api/v1/reservations/users/{libraryUserId}`  
Get all reservations for a user

`DELETE /api/v1/reservations/{reservationId}`  
Cancel a reservation

## ⏳ Reservation Expiry

A scheduled job runs in the background to automatically mark reservations as `EXPIRED` after 7 days if they haven't been picked up.

## 🧪 Testing

- Unit tests for service and repository layers
- Tests written with JUnit 5 and Mockito
- Covers all public logic and exception flows

## 📬 Postman Testing

To test the API endpoints, import the Postman collection from `postman/library-api.postman_collection.json`.

You can also import the environment file from `postman/library-api.postman_environment.json`.

## 🐳 Docker Support

This application is Docker-ready. To run it:

```bash
docker-compose up --build
