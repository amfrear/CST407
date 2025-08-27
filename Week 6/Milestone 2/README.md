# Week 6 — Milestone 2: Implement JWT and REST API (Events App)

This folder contains all deliverables for **CST-407 Milestone 2**. The project adds a **REST API** for the Events app and protects write operations with **JWT authentication**. Public reads are available without a token; **create/update/delete** require a valid **`Authorization: Bearer <token>`** header.

---

## Contents

- **Milestone 2 Implement JWT and REST API.pdf**  
  Final submission with screenshots, explanations, and the screencast link.

- **CST-407-RS-MilestoneAssignments.docx**  
  Instructor template completed with responses and embedded evidence.

- **Milestone_2_SourceCode.zip**  
  Full Maven project (no build artifacts). Includes:
  - `pom.xml` with Spring Web, Spring Security, JWT, Thymeleaf, JDBC, MySQL driver  
  - `src/main/java/...`
    - `security/` → `SecurityConfig`, `JwtAuthFilter`, `JwtUtil`, `CustomUserDetailsService`, `CorsConfig`
    - `controllers/` → `EventsApiController`, `UsersApiController`, plus MVC controllers (`UsersController`, `EventController`, `HomeController`)
    - `service/` → `EventService`, `UserService`
    - `data/` → `EventRepository`, `UserRepository` (+ interfaces)
    - `models/` → entities/models/converters
  - `src/main/resources/templates/` → `home.html`, `login.html`, `register.html`, `events.html`, `create-event.html`, `edit-event.html`, `searchForm.html`
  - `src/main/resources/static/css/styles.css`
  - `application.properties` (configure your local DB/JWT settings)

---

## Quick Start (Local)

**Prereqs:** Java 17, Maven (wrapper included), a running **MySQL**.

1) **Create database**
```sql
CREATE DATABASE eventsapp;
```

2) **Configure credentials** in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:8889/eventsapp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
app.jwt.secret=...your dev secret...
app.jwt.expirationMillis=3600000
```

3) **Run the app**
```bash
./mvnw clean spring-boot:run
# MVC: http://localhost:8080/events
# API: http://localhost:8080/api
```

4) **(MVC) Register & Login**
- Register at `/users/register`
- Login at `/users/loginForm`

---

## What’s New in Milestone 2

- **REST API** over `/api/**`
  - `GET /api/events` → public list (search with `?q=`), `GET /api/events/{id}` by id  
  - `POST /api/events`, `PUT /api/events/{id}`, `DELETE /api/events/{id}` → **JWT required**
- **User endpoints**
  - `POST /api/users/register` → create user  
  - `POST /api/users/login` → returns `{ "token": "<JWT>" }`
- **JWT Security**
  - `JwtAuthFilter` reads `Authorization: Bearer <token>`  
  - Valid tokens set Spring Security `Authentication` + expose the `uid` claim to controllers  
  - API chain is **stateless**; MVC chain still supports **form login**
- **CORS** for local front-ends (`localhost:5500` / `127.0.0.1:5500`)

---

## API Reference (Core Endpoints)

**Public**
- `GET /api/events` — list events (optional `?q=park`)  
- `GET /api/events/{id}` — single event (404 if missing)

**Auth**
- `POST /api/users/register` — create account  
- `POST /api/users/login` — returns `{ "token": "<JWT>" }`

**Protected (JWT required)**
- `POST /api/events` — create (organizer set from token’s `uid`)  
- `PUT /api/events/{id}` — update  
- `DELETE /api/events/{id}` — delete

**Headers**
```
Authorization: Bearer <your-jwt-here>
Content-Type: application/json
```

---

## Postman / cURL Smoke Tests

**Register**
```bash
curl -X POST http://localhost:8080/api/users/register   -H "Content-Type: application/json"   -d '{"userName":"alice","password":"pass123"}'
```

**Login → get token**
```bash
curl -X POST http://localhost:8080/api/users/login   -H "Content-Type: application/json"   -d '{"userName":"alice","password":"pass123"}'
# => {"token":"<JWT>"}
```

**Public list (no token)**
```bash
curl http://localhost:8080/api/events
```

**Create (requires token)**
```bash
TOKEN=<JWT>
curl -X POST http://localhost:8080/api/events   -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json"   -d '{"name":"Park Meetup","date":"2025-09-01","location":"City Park","description":"Bring snacks!"}'
```

**Update**
```bash
curl -X PUT http://localhost:8080/api/events/1   -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json"   -d '{"name":"Updated Meetup","date":"2025-09-02","location":"City Park","description":"Updated details"}'
```

**Delete**
```bash
curl -X DELETE http://localhost:8080/api/events/1   -H "Authorization: Bearer $TOKEN"
```

**Expected auth behavior**
- Missing/invalid token on protected routes → **401/403**
- Public GETs always allowed

---

## Security Overview

- **Two Security Chains**
  - **API** (`/api/**`): CORS enabled, CSRF disabled, **stateless** sessions; JWT required for writes  
  - **MVC**: form login for Thymeleaf pages; create/edit/delete pages require auth
- **JWT Flow**
  - `UsersApiController.login` issues tokens (subject = username; claims include `uid`, `roles`)  
  - `JwtAuthFilter` validates token per request and sets `Authentication`  
  - `EventsApiController` pulls `uid` from request to set `organizerid`
- **CORS**
  - `CorsConfig` allows `localhost:5500` / `127.0.0.1:5500`, exposes `Location` header, allows `Authorization`

---

## Test Matrix (what we verified)

**MVC**
- `/` home  
- `/events` list; search form `/events/search`  
- Auth-required pages: `/events/create`, `/events/edit/{id}`, `/events/delete/{id}`  
- Register → Login → Create/Edit/Delete

**API**
- `GET /api/events` (list), `GET /api/events?q=` (search), `GET /api/events/{id}` (single/404)  
- `POST /api/users/register` → 201  
- `POST /api/users/login` → `{ token }`  
- `POST /api/events` with Bearer → 201 + `Location`  
- `PUT /api/events/{id}`, `DELETE /api/events/{id}` with Bearer → 200/204  
- Missing/invalid token on protected routes → 401/403

---

## Rubric Mapping (at a glance)

- **Events API Controller**: complete CRUD + auth (Target)  
- **Users API Controller**: register + login, returns JWT (Target)  
- **JWT Authentication**: protected writes, public reads; correct 401/403 (Target)  
- **Postman Demonstration**: full flow including edge cases (Target)  
- **Format/Documentation**: structured PDF + this README + commented source (Target)

---

## Notes / Tips (dev only)

- The project returns the created resource location via `Location` header on `POST /api/events`.  
- `application.properties` is annotated for clarity; update DB creds as needed.  
- CORS is limited to local dev origins and allows the `Authorization` header.

---

## Submission Notes

- The PDF includes the screencast link, labeled screenshots, and an API test matrix.  
- The ZIP excludes `/target` and IDE/OS files; `mvnw`, `mvnw.cmd`, and `.mvn/` are included for portability.  
- Credentials and JWT secret are for **local development** only.
