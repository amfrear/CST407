# Week 8 – SQL Injection and Prevention (Java Spring Boot)

This folder contains all files related to **Week 8** of CST-407: Application Security Foundations.

## Contents

- **Activity_8_SourceCode.zip**  
  Source code for the vulnerable **Orders** application built with Java Spring Boot.  
  Demonstrates how unsafe string concatenation in DAO methods allows SQL injection attacks, including UNION queries to extract schema, tables, columns, usernames, and passwords.

- **Activity 8 Submission.pdf**  
  Final submitted version of Activity 8 with screenshots, explanations, and written responses following the instructor’s template.

## Summary

Activity 8 focused on demonstrating and preventing **SQL injection attacks** in a Java Spring Boot application.  
Key learning outcomes included:

- Identifying **SQL injection vulnerabilities** in the Orders app where queries were constructed by concatenating untrusted input directly into SQL strings.  
- Crafting malicious **UNION SELECT payloads** to enumerate schemas, discover tables and columns, and extract sensitive data (usernames and passwords).  
- Replicating the injection both in MySQL Workbench and in the Orders app UI to show the real-world impact.  
- Applying a **fix by replacing string concatenation with PreparedStatements** and parameterized queries in the DAO layer.  
- Verifying the fix by confirming that injection attempts were treated as literal strings instead of executable SQL, and sensitive data could no longer be retrieved.  
- Reinforcing additional best practices such as input sanitization, principle of least privilege for DB accounts, and use of security testing tools (SQLMap, OWASP ZAP, BurpSuite) for defense in depth.

Three major sections were completed:

1. **Exploit Demonstration** – Performed UNION-based injection in MySQL Workbench and through the Orders app search box to leak data.  
2. **Initial Fix (PreparedStatements)** – Updated DAO methods to use `?` placeholders with bound parameters, eliminating the vulnerability.  
3. **Verification and Best Practices** – Retested injection to confirm protection and summarized additional defensive strategies.

## Submission Notes

- The `.pdf` file is the polished final submission for grading.  
- The `.zip` file contains the full Maven project for the vulnerable Orders application with DAO updates.  
- Build artifacts (`/target`), IDE files, and OS metadata are excluded via `.gitignore`.  
- The project was run locally with Maven (`mvn spring-boot:run`) on port `8080`.  
- Screenshots in the `.pdf` include baseline search results, injection payload outputs, DAO code before and after updates, and proof that injection attempts failed after the fix.
