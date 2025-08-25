# Week 6 – Implementing REST API Security with JWT

This folder contains all files related to **Week 6** of CST-407: Application Security Foundations.

## Contents

- **Activity6_SourceCode_OrdersApp.zip**  
  Final project source code for the secured Orders REST API. Includes `pom.xml`, `src/`, and `resources/` configuration. Build artifacts (`/target`) and IDE files are excluded.

- **Activity6_ApiClient_SourceCode.zip**  
  Source code for the simple jQuery/HTML client used to test the API. Demonstrates login, token storage, and authorized CRUD requests against the secured Orders API.

- **CST-407 Activity 6 Implementing REST API Security with Java Web Tokens (JWT).pdf**  
  Final submitted version of Activity 6 with screenshots, explanations, and written responses following the instructor’s template.

## Summary

Activity 6 focused on securing a **REST API with JSON Web Tokens (JWT)**.  
Key learning outcomes included:

- Configuring **JWT properties** (`app.jwtSecret`, `app.jwtExpirationInMs`) and building a `JwtTokenProvider` for token creation/validation.
- Creating a **JWT authentication filter** and updating the `SecurityConfig` to enforce stateless sessions and permit only `/register` and `/login` without authentication.
- Implementing **`CustomUserDetailsService` and `CustomUserDetails`** to integrate Spring Security with a database-backed user model.
- Verifying **role-based access control (RBAC)** using `USER` and `ADMIN` roles embedded in JWT claims.
- Testing **negative scenarios** (no token, invalid token) and confirming 401/403 responses.
- Demonstrating **CRUD operations with JWT protection** in Postman:  
  - GET `/api/orders/{customerid}`  
  - PUT `/api/orders/{id}` (ADMIN only)  
  - DELETE `/api/orders/{id}` (ADMIN only)
- Building a lightweight **jQuery client** that logs in, saves the token, and makes authorized requests.

Three major sections were completed:

1. **JWT Configuration and Security Setup** – Added JWT dependencies, properties, and integrated a custom filter into Spring Security.  
2. **UserDetails Integration and RBAC** – Implemented user lookup from the database, issued tokens with role claims, and verified role enforcement.  
3. **Client + CRUD Verification** – Tested endpoints with Postman and a jQuery client to confirm secured behavior for both USER and ADMIN roles.

## Submission Notes

- The `.pdf` file is the polished final submission for grading.  
- The `.zip` files contain the full Maven Orders API and the separate client source code.  
- Build artifacts (`/target`), IDE files, and OS metadata are excluded via `.gitignore`.  
- The Orders API project requires MySQL (MAMP in this case) and includes schema definitions for `users` and `orders`.  
- A sample `users` table entry with role `ADMIN` was used to demonstrate secured PUT and DELETE operations.
