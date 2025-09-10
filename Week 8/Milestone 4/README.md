# Week 8 — Milestone 4: Demonstrate & Mitigate SQL Injection (Events App)

This folder contains all deliverables for **CST-407 Milestone 4**. The project shows how the Events app’s search feature was vulnerable to **SQL injection (SQLi)**, then fixes it by converting the repository query to a **parameterized (prepared) statement**, and verifies that the attack no longer works.

---

## Contents

- **Milestone 4 Submission.pdf**  
  Final submission with screenshots, explanations, and (when added) the screencast link.

- **Milestone_4_SourceCode.zip**  
  Full Maven project (**no build artifacts**). Includes:  
  - `pom.xml`  
  - `src/main/java/...`  
    - `data/EventRepository.java` — `findByDescription(...)` rewritten to use a **parameterized `LIKE ?`** query  
    - All controllers, services, models, and config used by the app  
  - `src/main/resources/templates/` — search page reachable at **`/events/search`**  
  - `application.properties` for local DB config

---

## Quick Start (Local)

**Prereqs:** Java 17, Maven (wrapper included), running **MySQL**.

1) **Create database**
```sql
CREATE DATABASE IF NOT EXISTS eventsapp;
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
- Open the search page at **`/events/search`** (note: the guide’s `/searchForm` is not used in this codebase)

---

## How the Vulnerability Was Demonstrated

- **Baseline search**: On `/events/search`, a normal keyword like `music` returns only matching events.  
- **Boolean-true payload**: Submitting  
  ```
  ' OR '1'='1' -- 
  ```  
  caused the app to return **all events**, proving that user input was concatenated into the SQL string.  
- **Error-based proof**: Submitting just a single quote `'` produced a SQL syntax error prior to the fix, further confirming injection.

---

## What Changed to Fix It

**Before (vulnerable):**
```java
// EventRepository.java
@Override
public List<EventEntity> findByDescription(String description) {
    String sql = "SELECT * FROM events WHERE description LIKE '%" + description + "%'";
    return jdbcTemplate.query(sql, new EventModelRowMapper());
}
```

**After (safe, parameterized):**
```java
// EventRepository.java
@Override
public List<EventEntity> findByDescription(String description) {
    String term = (description == null) ? "" : description.trim();
    String pattern = "%" + term + "%";
    String sql = "SELECT * FROM events WHERE description LIKE ?";

    return jdbcTemplate.query(sql, new EventModelRowMapper(), pattern);
}
```

- The SQL uses **`LIKE ?`** and the user input is passed as a **bind parameter**.  
- No string concatenation in the SQL statement → the payload can’t change the query structure.

---

## How the Fix Was Verified

- A normal keyword search still returns expected results (no feature regression).  
- Re-submitting the boolean-true payload **`' OR '1'='1' -- `** returns **0 results** (unless an event literally contains that text).  
- Submitting a lone `'` no longer produces a SQL syntax error; the app handles it safely.

---

## Security Overview

- **Root cause:** Concatenating untrusted input into SQL (`"... LIKE '%" + description + "%'"`).  
- **Mitigation:** Parameterized queries ensure the DB driver treats user input as **data**, not **code**.  
- **Outcome:** Injection attempts are interpreted literally and no longer alter the SQL logic.

---

## Submission Notes

- The PDF includes labeled screenshots and a link to the ≤5-minute screencast (add the link before final submission).  
- The source ZIP excludes `/target` and IDE/OS files; includes `mvnw`, `mvnw.cmd`, and `.mvn/`.  
- **Environment used:** Spring Boot on port `8080`; MySQL at `127.0.0.1:8889`; schema `eventsapp`; credentials `root/root`.

---
