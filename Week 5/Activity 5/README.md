# Week 5 – Spring Security Configuration

This folder contains all files related to **Week 5** of CST-407: Application Security Foundations.

## Contents

- **Activity_5_sourceCode.zip**  
  Final project source code for the secured Orders application. Includes `pom.xml`, `src/`, `templates/`, and the SQL schema (`ordersapproles.sql`). Build artifacts and IDE files are excluded.

- **CST-407 Activity 5 Spring Boot Security Configuration.pdf**  
  Final submitted version of Activity 5 with screenshots, explanations, and the written summary (Part 4).

- **CST-407-RS-T5-Activity-5-Spring-Security-Configuration.docx**  
  Instructor-provided assignment template with completed responses and embedded screenshots.

## Summary

Activity 5 focused on implementing **Spring Security** in a web application to enforce authentication, role-based authorization, and secure user handling.  
Key learning outcomes included:

- Adding `spring-boot-starter-security` and configuring the `SecurityConfig` class with a custom `SecurityFilterChain`.
- Defining role-based access rules (USER vs. ADMIN) for controller endpoints and verifying 403/allowed behavior.
- Implementing a **BCrypt password encoder** and integrating with a database-backed `UserDetailsService`.
- Creating a **custom login page**, logout functionality, and a dedicated 403 access denied page.
- Enabling **method-level security** with `@EnableMethodSecurity` and `@PreAuthorize` annotations.
- Using **Thymeleaf Security Extras** to conditionally display navigation and UI actions based on user roles.

Three major sections were completed:

1. **Baseline and Basic Security** – Verified unsecured vs. secured app, added Spring Security defaults, and introduced `SecurityConfig`.  
2. **Custom Authentication and RBAC** – Implemented database-backed authentication, role checks, and custom login/logout/403 pages.  
3. **Thymeleaf and Method Security** – Integrated security tags in templates and reinforced controller access with `@PreAuthorize`.

## Submission Notes

- The `.pdf` file is the polished final submission for grading.  
- The `.docx` file shows the assignment in the instructor’s original template.  
- The `Activity_5_sourceCode.zip` file contains the full Maven project with source code and templates.  
- Build artifacts (`/target`), IDE files, and OS metadata are excluded via `.gitignore`.  
- A `README_SETUP.md` inside the project provides quick setup instructions and an example SQL insert for granting an admin role.
