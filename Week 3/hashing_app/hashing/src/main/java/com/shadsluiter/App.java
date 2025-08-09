package com.shadsluiter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException, InvalidKeySpecException, IOException {
        String[] alphabets = {
            "abcdefghijklmnopqrstuvwxyz",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "0123456789",
            "!@#$%^&*()-_=+[]{}|;:,.<>?",
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "abcdefghijklmnopqrstuvwxyz0123456789",
            "abcdefghijklmnopqrstuvwxyz!@#$%^&*()-_=+[]{}|;:,.<>?",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()-_=+[]{}|;:,.<>?",
            "0123456789!@#$%^&*()-_=+[]{}|;:,.<>?",
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()-_=+[]{}|;:,.<>?",
            "abcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:,.<>?",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+[]{}|;:,.<>?",
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+[]{}|;:,.<>?"
        };

        HashMap<String, String> textColors = new HashMap<>();
        textColors.put("RED", "\u001B[31m");
        textColors.put("GREEN", "\u001B[32m");
        textColors.put("YELLOW", "\u001B[33m");
        textColors.put("BLUE", "\u001B[34m");
        textColors.put("RESET", "\u001B[0m");

        HashUtil hashUtil = new HashUtil();
        Scanner scanner = new Scanner(System.in);

        System.out.println(textColors.get("BLUE") + "Welcome to the Password Hashing and Cracking Program!" + textColors.get("RESET"));
        while (true) {
            // Ask user to input a password
            System.out.println(textColors.get("YELLOW") + "Please enter a password to hash:" + textColors.get("RESET"));
            String clearTextPassword = scanner.nextLine();

            // Ask for hashing algorithm
            System.out.println(textColors.get("YELLOW") + "Which hash algorithm would you like to use? [MD5, SHA-1, SHA-256, SHA-512]. Leave blank for MD5:" + textColors.get("RESET"));
            String hashAlgorithm = scanner.nextLine().toUpperCase();
            if (hashAlgorithm.isEmpty()) {
                hashAlgorithm = "MD5";
            }

            String hashedPassword = hashUtil.hash(clearTextPassword, hashAlgorithm);
            System.out.println(textColors.get("GREEN") + "Hashed Password: " + textColors.get("RESET") + hashedPassword);
            System.out.println(textColors.get("GREEN") + "Algorithm used: " + textColors.get("RESET") + hashAlgorithm);
            System.out.println(textColors.get("BLUE") + "----------------------------------------------" + textColors.get("RESET"));

            // Ask for a hash to crack or use the generated one
            System.out.println(textColors.get("YELLOW") + "Enter a hash to crack (Leave blank to use the generated hash):" + textColors.get("RESET"));
            String hashedString = scanner.nextLine();
            if (hashedString.isEmpty()) {
                hashedString = hashedPassword;
            }

            // Ask for cracking algorithm
            System.out.println(textColors.get("YELLOW") + "Which algorithm should be used for cracking? [MD5, SHA-1, SHA-256, SHA-512]. Leave blank to use " + hashAlgorithm + ":" + textColors.get("RESET"));
            String crackingAlgorithm = scanner.nextLine().toUpperCase();
            if (crackingAlgorithm.isEmpty()) {
                crackingAlgorithm = hashAlgorithm;
            }

            System.out.println("Would you like to use a dictionary attack or a brute-force attack?");
            System.out.println("1: Dictionary Attack");
            System.out.println("2: Brute-force Attack");
            String attackChoice = scanner.nextLine();
            if (attackChoice.equals("1")) {
                System.out.println(textColors.get("YELLOW") + "Dictionary Attack Selected" + textColors.get("RESET"));
            } else {
                System.out.println(textColors.get("YELLOW") + "Brute-force Attack Selected" + textColors.get("RESET"));
            }

            if (attackChoice.equals("1")) {
                
                   // Perform dictionary attack
            String dictionaryResult = hashUtil.dictionary(hashedString, crackingAlgorithm);
            System.out.println(textColors.get("YELLOW") + "Dictionary Attack Result: " + textColors.get("RESET") + dictionaryResult);
            System.out.println(textColors.get("BLUE") + "----------------------------------------------" + textColors.get("RESET"));
            }
            else {
                // Brute-force attack 
                System.out.println(textColors.get("YELLOW") + "Select an alphabet for brute-force attack (Enter 0-" + (alphabets.length - 1) + ", Leave blank for 0):" + textColors.get("RESET"));
                for (int i = 0; i < alphabets.length; i++) {
                    System.out.println(i + ": " + alphabets[i]);
                }

                String alphabetChoice = scanner.nextLine();
                int alphabetIndex = 0;
                if (!alphabetChoice.isEmpty()) {
                    alphabetIndex = Integer.parseInt(alphabetChoice);
                }
                String alphabet = alphabets[alphabetIndex];

                String bruteForceResult = hashUtil.bruteForce(hashedString, 8, alphabet, crackingAlgorithm);
                System.out.println(textColors.get("RED") + "Brute Force Attack Result: " + textColors.get("GREEN") + bruteForceResult + textColors.get("RESET") + " is the password. " + textColors.get("YELLOW") + hashedString + textColors.get("RESET") + " is the hash value. Which was hashed with " + crackingAlgorithm);
                System.out.println(textColors.get("BLUE") + "----------------------------------------------" + textColors.get("RESET"));

            }
            // Ask if user wants to continue
            System.out.println(textColors.get("YELLOW") + "Would you like to hash another password? (yes/no):" + textColors.get("RESET"));
            String continueChoice = scanner.nextLine().toLowerCase();
            if (!continueChoice.equals("yes")) {
                break;
            }
        }

        System.out.println(textColors.get("GREEN") + "Thank you for using the Password Hashing and Cracking Program!" + textColors.get("RESET"));
        scanner.close();
    }
}
