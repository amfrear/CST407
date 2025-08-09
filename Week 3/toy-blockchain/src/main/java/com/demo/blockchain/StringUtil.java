package com.demo.blockchain;

import java.security.MessageDigest;

public class StringUtil {

    // Returns SHA-256 hash of input as a lowercase hex string
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 error", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
