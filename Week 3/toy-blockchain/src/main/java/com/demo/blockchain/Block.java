package com.demo.blockchain;

import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String data; // Our data will be a simple message.
    private long timeStamp; // Number of milliseconds since 1/1/1970.

    // Block Constructor.
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    // Calculate new hash based on block contents
    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256(
                previousHash +
                Long.toString(timeStamp) +
                data
        );
        return calculatedhash;
    }
}
