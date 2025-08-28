# Week 7 – Cross-Site Script Injection (XSS)

This folder contains all files related to **Week 7** of CST-407: Application Security Foundations.

## Contents

- **Activity7_commentsApp_sourceCode.zip**  
  Source code for the vulnerable **Comments** application running on port `8080`.  
  Demonstrates how unsanitized user input rendered with `th:utext` can enable stored XSS.

- **Activity7_hackerApp_sourceCode.zip**  
  Source code for the **Hacker Listener** application running on port `8081`.  
  Receives keystrokes exfiltrated via malicious scripts injected into the Comments app.

- **Activity 7 Cross-Site Script Injection.pdf**  
  Final submitted version of Activity 7 with screenshots, explanations, and written responses following the instructor’s template.

## Summary

Activity 7 focused on demonstrating and mitigating a **stored cross-site scripting (XSS) attack**.  
Key learning outcomes included:

- Identifying **XSS vulnerabilities** in the Comments application caused by using `th:utext` to render raw HTML/JavaScript.  
- Crafting a **malicious `<script>` payload** that automatically executed in every visitor’s browser, exfiltrating keystrokes.  
- Setting up a **separate Hacker Listener app** to receive keystrokes via `/logKey` requests, logging them to both console and `keystrokes.txt`.  
- Using **browser DevTools (Network tab)** to confirm exfiltrated requests in real time.  
- Applying **fixes to eliminate the vulnerability**:  
  1. Replaced `th:utext` with `th:text` to ensure safe HTML escaping.  
  2. Added the **OWASP Java Encoder** dependency and encoded inputs in the `CommentsController` for defense in depth.  
- Verified the fix by confirming the malicious payload displayed as literal text instead of executing.

Three major sections were completed:

1. **Exploit Demonstration** – Injected a stored XSS payload into the Comments app and confirmed keystroke logging through the hacker listener.  
2. **Initial Fix (Template Update)** – Switched from `th:utext` to `th:text` to automatically escape HTML.  
3. **Stronger Fix (OWASP Encoder)** – Added encoder sanitization in the controller to ensure user input is safely handled before rendering.

## Submission Notes

- The `.pdf` file is the polished final submission for grading.  
- The `.zip` files contain the full Maven projects for both the vulnerable Comments app and the Hacker listener app.  
- Build artifacts (`/target`), IDE files, and OS metadata are excluded via `.gitignore`.  
- Projects were run locally with Maven (`mvn spring-boot:run`) on ports `8080` (Comments app) and `8081` (Hacker app).  
- Screenshots in the `.pdf` include exploit evidence (payload injection, DevTools, logs) and verification of the applied fixes.
