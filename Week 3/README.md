# Week 3 – Password Hashing and Cracking

This folder contains all files related to **Week 3** of CST-407: Application Security Foundations.

## Contents

- **hashing_app/hashing/**  
  Java application to generate and compare password hashes using multiple algorithms (MD5, SHA-1, SHA-256, SHA-512, bcrypt, scrypt, pbkdf2, argon2). Includes source code, dictionaries, and hash result files.

- **passwordhasher/**  
  Basic Java password hashing example demonstrating SHA-256 hashing with and without salt.

- **toy-blockchain/**  
  Java application simulating a simple blockchain, included for related hashing concepts.

- **Activity 3 – Password Hashing and Cracking.pdf**  
  Final submitted version of Activity 3 in PDF format.

- **CST-407-RS-T3-Activity-3-Password-Hashing-and-Cracking.docx**  
  Instructor-provided assignment template with completed responses.

## Summary

Activity 3 focused on password hashing techniques and cracking methods using dictionary and brute-force approaches.  
Key learning outcomes included:

- Generating password hashes using Java and various secure hashing algorithms.
- Comparing strength, performance, and effectiveness of different hash types.
- Attempting to crack MD5 hashes using precomputed dictionaries.
- Analyzing hash formats and understanding salting mechanisms.
- Reflecting on password security and real-world vulnerabilities.

Three projects were developed:

- **hashing_app/hashing/** – Full suite that hashes a list of 10,000 passwords with multiple algorithms and generates `.txt` output files.
- **passwordhasher/** – Small example for generating salted and unsalted hashes for individual passwords.
- **toy-blockchain/** – Demonstrates how hashing applies in blockchain technology.

## Submission Notes

- The `.pdf` file is the final graded submission.
- The `.docx` file contains structured responses in the instructor’s original format.
- All Java project folders contain working Maven-based applications.
- Build artifacts (`/target`) were removed and excluded via `.gitignore`.
