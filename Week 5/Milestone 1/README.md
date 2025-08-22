# Week 5 — Milestone 1: Implement Spring Boot Security (Events App)

This folder contains all deliverables for **CST-407 Milestone 1**. The project secures a simple Events application with **Spring Security**, **BCrypt** password hashing, **role-based authorization (USER/ADMIN)**, and **owner-only CRUD**.

## Contents

- **Milestone 1– Implement Spring Boot Security.pdf**
  Final submission with screenshots, explanations, and the screencast link.

- **CST-407-RS-MilestoneAssignments.docx**
  Instructor template completed with responses and embedded evidence.

- **Milestone_1_sourceCode.zip**
  Full Maven project (no build artifacts). Includes:
  - `pom.xml` with Spring Security, Thymeleaf, JDBC/JPA, MySQL driver
  - `src/main/java/...`
    - `security/` → `SecurityConfig`, `CustomUserDetailsService`
    - `controllers/` → `UsersController`, `EventController`
    - `service/`, `models/`, `data/`
  - `src/main/resources/templates/` → `layout.html`, `login.html`, `register.html`, `events.html`, `create-event.html`, `edit-event.html`, `searchForm.html`
  - `src/main/resources/static/css/styles.css`
  - `application.properties` (configure your local DB credentials)

---

## Quick Start (Local)

Prereqs: Java 17, Maven (wrapper included), a running MySQL server.

1) Create database
   CREATE DATABASE eventsapp;

2) Configure credentials in `src/main/resources/application.properties`:
   spring.datasource.url=jdbc:mysql://localhost:3306/eventsapp
   spring.datasource.username=YOUR_USER
   spring.datasource.password=YOUR_PASSWORD
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

3) Run the app
   ./mvnw clean spring-boot:run
   Open: http://localhost:8080/events

4) Register & login
   - Register a user at /users/register
   - Login at /users/loginForm

---

## Features Implemented

- Spring Security 6
  - Custom SecurityFilterChain with form login and logout
  - BCrypt password encoding
  - Database-backed auth via UserDetailsService

- RBAC & Ownership
  - Roles: ROLE_USER, ROLE_ADMIN (stored in roles table)
  - Owner-only edit/delete on events
  - Admin can edit/delete any event

- Thymeleaf Security
  - Navbar adapts by auth state & role (Register/Login vs. Create Event/Hello/Logout)
  - Conditional display of action buttons per row

- Persistence
  - Users & roles stored in MySQL; passwords hashed ($2… BCrypt)
  - JPA/Hibernate creates/updates schema (ddl-auto=update)

- Screens & Flows
  - Custom Register and Login pages
  - Events list, create, edit, delete, and search

---

## Promote an Admin (optional)

1) Register a user named `admin`, then run:
   INSERT INTO eventsapp.roles (user_id, role)
   SELECT u.id, 'ROLE_ADMIN'
   FROM eventsapp.users u
   WHERE u.login_name = 'admin'
     AND NOT EXISTS (
         SELECT 1 FROM eventsapp.roles r
         WHERE r.user_id = u.id AND r.role = 'ROLE_ADMIN'
     );

2) Verify:
   SELECT r.user_id, u.login_name, r.role
   FROM eventsapp.roles r
   JOIN eventsapp.users u ON u.id = r.user_id
   ORDER BY r.user_id, r.role;

---

## Submission Notes

- The PDF includes the screencast link, all screenshots, and a test matrix (all ✅).
- The ZIP excludes `/target` and IDE/OS files; `mvnw`, `mvnw.cmd`, and `.mvn/` are included for portability.
- Database credentials are local and should be updated before running.
