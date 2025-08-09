package com.demo.blockchain;

import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        List<Block> chain = new ArrayList<>();

        // 1) Genesis block (no previous hash)
        Block genesis = new Block("Genesis Block", "0");
        chain.add(genesis);

        // 2) Next blocks point to the previous block's hash
        Block b2 = new Block("Enroll Student", chain.get(chain.size() - 1).hash);
        chain.add(b2);

        Block b3 = new Block("Submit Assignment", chain.get(chain.size() - 1).hash);
        chain.add(b3);

        // Print the chain
        System.out.println("=== Toy Blockchain (3 blocks) ===");
        for (int i = 0; i < chain.size(); i++) {
            Block b = chain.get(i);
            System.out.println("Block #" + i);
            System.out.println("  Previous Hash: " + b.previousHash);
            System.out.println("  Data:          " + getData(b));
            System.out.println("  Hash:          " + b.hash);
            System.out.println();
        }

        // Validate chain
        boolean ok = isChainValid(chain);
        System.out.println("Blockchain valid: " + ok);
    }

    private static boolean isChainValid(List<Block> chain) {
        for (int i = 1; i < chain.size(); i++) {
            Block curr = chain.get(i);
            Block prev = chain.get(i - 1);

            // Recalculate and compare the current hash
            if (!curr.hash.equals(curr.calculateHash())) return false;
            // Check the previous hash pointer
            if (!curr.previousHash.equals(prev.hash)) return false;
        }
        return true;
    }

    // quick way to get the private data for printing (demo only)
    private static String getData(Block b) {
        try {
            var f = Block.class.getDeclaredField("data");
            f.setAccessible(true);
            return (String) f.get(b);
        } catch (Exception e) {
            return "<hidden>";
        }
    }
}
