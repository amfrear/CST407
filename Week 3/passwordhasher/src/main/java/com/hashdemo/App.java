package com.hashdemo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;

public class App {

    private static final int ITERATIONS = 10000; // for MD5/SHA-* timing

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a password to hash: ");
        String password = scanner.nextLine();
        scanner.close();

        System.out.println();
        System.out.println("=== One-pass hashes (single run) ===");
        onePass("MD5", password);
        onePass("SHA-1", password);
        onePass("SHA-256", password);
        onePass("SHA-512", password);
        onePassBCrypt(password, 10);

        System.out.println();
        System.out.println("=== Timed comparison (MD5/SHA-* looped " + ITERATIONS + "x vs one BCrypt) ===");
        timedLooped("MD5", password, ITERATIONS);
        timedLooped("SHA-1", password, ITERATIONS);
        timedLooped("SHA-256", password, ITERATIONS);
        timedLooped("SHA-512", password, ITERATIONS);
        timedBCrypt(password, 10);
    }

    private static void onePass(String algorithm, String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] digest = md.digest(password.getBytes());
        System.out.println(algorithm + ": " + toHex(digest));
    }

    private static void onePassBCrypt(String password, int cost) {
        String hash = BCrypt.hashpw(password, BCrypt.gensalt(cost));
        System.out.println("BCrypt (cost " + cost + "): " + hash);
    }

    private static void timedLooped(String algorithm, String password, int iterations) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        long start = System.nanoTime();
        byte[] last = null;
        for (int i = 0; i < iterations; i++) {
            last = md.digest(password.getBytes());
        }
        long end = System.nanoTime();
        double ms = (end - start) / 1_000_000.0;
        System.out.printf("%s x%,d: %.2f ms (last=%s)%n", algorithm, iterations, ms, toHex(last));
    }

    private static void timedBCrypt(String password, int cost) {
        long start = System.nanoTime();
        String hash = BCrypt.hashpw(password, BCrypt.gensalt(cost));
        long end = System.nanoTime();
        double ms = (end - start) / 1_000_000.0;
        System.out.printf("BCrypt (cost %d) x1: %.2f ms%n", cost, ms);
        System.out.println("BCrypt hash: " + hash);
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
