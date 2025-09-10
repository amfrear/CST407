# Week 8 — Milestone 5: Enforce HTTPS, Redirect HTTP, and Prove Encryption (Events App)

This folder contains all deliverables for **CST-407 Milestone 5**. The project enables **HTTPS** with a self-signed certificate, keeps **HTTP** only to **auto-redirect** to HTTPS, and proves with **Wireshark** that credentials are encrypted and no longer readable on the wire. It also emits **HSTS** so browsers prefer HTTPS.

---

## Contents

- **Milestone 5 Submission.pdf**  
  Final submission with screenshots, explanations, and (when added) the screencast link.

- **Milestone_5_SourceCode.zip**  
  Full Maven project (**no build artifacts**). Includes:  
  - `pom.xml`  
  - `src/main/java/...`  
    - `security/SecurityConfig.java` — two filter chains (API + MVC) updated to **require HTTPS** and send **HSTS**  
    - `HttpToHttpsRedirectConfig.java` — adds Tomcat HTTP connector on **8080** that **redirects** to HTTPS on **8443**  
    - All controllers, services, models, and JWT filter used by the app  
  - `src/main/resources/`  
    - `keystore.p12` — self-signed server cert (PKCS#12)  
    - `application.properties` — SSL and redirect properties  
  - `templates/` — Thymeleaf views including login at **`/users/loginForm`**

---

## Quick Start (Local)

**Prereqs:** Java 17, Maven (wrapper included), **MySQL** running.  
**DB:** `eventsapp` at `127.0.0.1:8889`, user **root** / pass **root** (adjust as needed).

1) **Create database**
```sql
CREATE DATABASE IF NOT EXISTS eventsapp;
```

2) **(First time)** Generate a self-signed certificate and create a PKCS#12 keystore (valid 10 years)  
Run from the project root:
```bash
keytool -genkeypair   -alias eventsapp   -keyalg RSA -keysize 2048   -storetype PKCS12   -keystore keystore.p12   -validity 3650   -storepass changeit -keypass changeit   -dname "CN=localhost, OU=Student, O=CST407, L=Local, S=AZ, C=US"
```
Move the file to `src/main/resources/keystore.p12`.

3) **Configure application properties** (`src/main/resources/application.properties`)
```properties
# --- DataSource ---
spring.datasource.url=jdbc:mysql://127.0.0.1:8889/eventsapp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# --- HTTPS ---
server.port=8443
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=changeit
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=eventsapp

# --- HTTP (redirect only) ---
server.http.port=8080
```
> If your base package is different, ensure the config classes use the correct package name.

4) **Run the app**
```bash
./mvnw clean spring-boot:run
# HTTPS: https://localhost:8443/
# HTTP redirect endpoint: http://localhost:8080/
```

5) **Login/Register**
- Register at **`/users/register`**
- Login at **`/users/loginForm`**
- Main page at **`/events`**

6) **Quick verification (command line)**  
Show HSTS and redirect:
```bash
# HSTS header on HTTPS (self-signed -> -k)
curl -I -k https://localhost:8443/users/loginForm

# HTTP -> HTTPS redirect
curl -I  http://localhost:8080/users/loginForm

# API also forced to HTTPS
curl -I  http://localhost:8080/api/events
```
You should see a `strict-transport-security` header on HTTPS and a `Location: https://...` header on HTTP.

---

## What Was Implemented

- **HTTPS enabled** on **8443** using a **PKCS#12** keystore (`keystore.p12`) under `src/main/resources`.
- **HTTP connector** on **8080** added solely to **redirect** to HTTPS (Tomcat `redirectPort`).
- **SecurityConfig** (two chains: API and MVC):
  - `requiresChannel().anyRequest().requiresSecure()` on **both** chains to enforce HTTPS
  - `headers().httpStrictTransportSecurity()` to send **HSTS** (1-year max-age, includeSubDomains)
- **Self-signed certificate** trusted locally as needed to avoid browser warnings during dev.

---

## How It Was Verified (Evidence Summary)

- **HTTP baseline (before SSL):** Wireshark captured a `POST /login` showing **clear-text** `username` and `password` fields.
- **HTTPS active:** TLS **ClientHello/ServerHello** observed; `Application Data` packets follow.
- **Encrypted payload:** **Follow TCP Stream** shows **garbled bytes**, not form fields; filtering for `http.request.method == "POST" && tcp.port == 8443` returns **no results**.
- **Redirect enforced:** Visiting `http://localhost:8080/...` bounces to `https://localhost:8443/...`; DevTools shows **302/307** with a **Location** header.
- **HSTS present:** Response contains `strict-transport-security: max-age=31536000; includeSubDomains`.

---

## File Highlights

- `src/main/java/.../HttpToHttpsRedirectConfig.java` — registers HTTP connector (8080) that redirects to 8443.  
- `src/main/java/.../security/SecurityConfig.java` — two separate filter chains (API + MVC) with **requiresSecure()** and **HSTS**.  
- `src/main/resources/application.properties` — datasource + SSL + redirect properties.  
- `src/main/resources/keystore.p12` — self-signed cert used by the server.

---

## Submission Notes

- The PDF includes labeled screenshots for HTTP baseline, TLS handshake, encrypted payload, redirect, and HSTS.  
- Add your screencast link (≤5 minutes) before final submission.  
- The source ZIP excludes `/target` and IDE/OS files; includes `mvnw`, `mvnw.cmd`, and `.mvn/`.  
- **Environment used:** Spring Boot 3 (port `8443`), HTTP redirect on `8080`, MySQL at `127.0.0.1:8889`, schema `eventsapp`, credentials `root/root`.
