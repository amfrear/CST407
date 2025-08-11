# Week 4 – Wireshark Network Packet Capture Demonstration

This folder contains all files related to **Week 4** of CST-407: Application Security Foundations.

## Contents

- **loginapp/**  
  Java-based login web application used to demonstrate HTTP and HTTPS/TLS network traffic. Includes source code and Maven configuration.

- **Activity 4 Secure Network Communication.pdf**  
  Final submitted version of Activity 4 in PDF format.

- **CST-407-RS-T4-Activity-4-Wireshark-Network-Packet-Capture-Demonstration.docx**  
  Instructor-provided assignment template with completed responses and screenshots.

## Summary

Activity 4 focused on building, testing, and securing a basic login application to observe how network traffic changes between HTTP and HTTPS using Wireshark.  
Key learning outcomes included:

- Building and running a Java-based login web app locally.
- Capturing HTTP traffic in Wireshark to view unencrypted login requests and responses.
- Following HTTP streams to analyze GET and POST requests.
- Configuring HTTPS/TLS with a self-signed certificate and repeating the test to observe encrypted traffic.
- Comparing HTTP and HTTPS captures to highlight the importance of TLS encryption.
- Documenting other common protocols observed during network analysis.

Three major sections were completed:

1. **Build and Test the Login Application** – Verified application functionality with successful and failed login attempts.  
2. **Enable HTTPS and Repeat with TLS** – Configured the application to use HTTPS on port 8443 and validated TLS encryption in Wireshark.  
3. **Summary of Key Concepts & Protocols Table** – Reflected on the differences between HTTP and HTTPS, along with protocol details.

## Submission Notes

- The `.pdf` file is the final graded submission.
- The `.docx` file contains structured responses in the instructor’s original format, including all required screenshots.
- The `loginapp/` folder contains the full Maven project for the web application.
- Build artifacts (`/target`) were removed and excluded via `.gitignore`.
