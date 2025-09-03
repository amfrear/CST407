# Week 7 — Milestone 3: Implement Data Sanitization to Prevent XSS Attacks (Events App)

This folder contains all deliverables for **CST-407 Milestone 3**. The project demonstrates how the Events app was vulnerable to **stored cross-site scripting (XSS)**, implements data sanitization and escaping to prevent it, and verifies the fix with a full test plan.

---

## Contents

- **Milestone 3 Submission.pdf**  
  Final submission with screenshots, explanations, test results, and the screencast link.

- **Milestone_3_SourceCode.zip**  
  Full Maven project (no build artifacts). Includes:  
  - `pom.xml` updated with **jsoup** sanitization dependency  
  - `src/main/java/...`  
    - `util/HtmlSanitizer.java` — strips unsafe HTML using jsoup  
    - `service/EventService.java` — sanitizes description before saving  
    - All other services, controllers, repositories, and models  
  - `src/main/resources/templates/` → updated `events.html` using `th:text` (escaped output)  
  - `application.properties` for local DB config

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
```

3) **Run the app**
```bash
./mvnw clean spring-boot:run
# Visit: http://localhost:8080/events
```

4) **Login/Register**
- Register at `/users/register`
- Login at `/users/loginForm`

---

## What’s New in Milestone 3

- **Vulnerability Identified**  
  - `events.html` used `th:utext`, which rendered raw HTML and allowed stored XSS.  
  - Demonstrated by inserting `<script>alert("XSS-M3")</script>` into event descriptions.

- **Fix Implemented**  
  - Updated `events.html` to use `th:text` for safe HTML escaping.  
  - Added **jsoup** dependency in `pom.xml`.  
  - Created `HtmlSanitizer` Spring component (strips all HTML tags).  
  - Updated `EventService` to sanitize descriptions before saving to the database.

- **Testing**  
  - Verified script and image payloads no longer execute.  
  - Confirmed normal text renders safely.  
  - Mixed input results in only safe text being displayed.

---

## Test Cases (Summary)

| Test Case      | Input                                | Result                                     |
|----------------|--------------------------------------|--------------------------------------------|
| Script Payload | `<script>alert("XSS")</script>`      | Payload stripped, no alert executed         |
| Image Payload  | `<img src=x onerror=alert(1)>`       | Payload stripped, no alert executed         |
| Normal Input   | `Company picnic at the park`         | Text displayed normally                     |
| Mixed Input    | `Lunch <script>alert("XSS")</script>`| Displays only "Lunch"; script stripped      |

---

## Security Overview

- **Defense-in-Depth**  
  - Sanitize on write: `HtmlSanitizer` strips HTML before saving to DB.  
  - Escape on render: Thymeleaf `th:text` ensures output is HTML-escaped.  
  - Together, these stop stored XSS at both persistence and presentation layers.

- **Sanitization Rules**  
  - `Safelist.none()` removes all HTML tags.  
  - This ensures only plain text is stored and displayed.

---

## Submission Notes

- PDF includes the screencast link and labeled screenshots.  
- ZIP excludes `/target` and IDE/OS files; `mvnw`, `mvnw.cmd`, and `.mvn/` are included.  
- Database credentials are for **local development** only.
