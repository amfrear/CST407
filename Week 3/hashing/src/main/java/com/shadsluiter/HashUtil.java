package com.shadsluiter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.NumberFormat;
import java.util.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.mindrot.jbcrypt.BCrypt;
import com.lambdaworks.crypto.SCryptUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HashUtil {

    private final long countBlock = 5000001;
    private final Map<String, String> textColors = new HashMap<>();
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

    public HashUtil() {
        // colors for pretty console output
        textColors.put("RED", "\u001B[31m");
        textColors.put("GREEN", "\u001B[32m");
        textColors.put("YELLOW", "\u001B[33m");
        textColors.put("BLUE", "\u001B[34m");
        textColors.put("PURPLE", "\u001B[35m");
        textColors.put("CYAN", "\u001B[36m");
        textColors.put("WHITE", "\u001B[37m");
        textColors.put("RESET", "\u001B[0m");
    }

    public String hash(String clearTextPassword, String hashAlgorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        switch (hashAlgorithm) {
            case "MD5":
            case "SHA-1":
            case "SHA-256":
            case "SHA-512":
                return hashWithAlgorithm(clearTextPassword, hashAlgorithm);
            case "BCrypt":
                return hashWithBCrypt(clearTextPassword);
            case "SCrypt":
                return hashWithSCrypt(clearTextPassword);
            case "PBKDF2":
                return hashWithPBKDF2(clearTextPassword);
            case "Argon2":
                return hashWithArgon2(clearTextPassword);
            default:
                return "Invalid hash algorithm";
        }
    }

    public String dictionary(String hashedString, String hashAlgorithm) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String currentDirectory = System.getProperty("user.dir");
        List<String> passwords = readFileLines(currentDirectory + "/src/main/java/com/shadsluiter/10000.txt");
        List<String> prefixes = readFileLines(currentDirectory + "/src/main/java/com/shadsluiter/prefix.txt");
        List<String> suffixes = readFileLines(currentDirectory + "/src/main/java/com/shadsluiter/suffix.txt");
        List<String> names = readFileLines(currentDirectory + "/src/main/java/com/shadsluiter/names.txt");
        List<String> englishwords = readFileLines(currentDirectory + "/src/main/java/com/shadsluiter/englishwords.txt");

        Set<String> allSet = new HashSet<>();
        allSet.addAll(englishwords);
        allSet.addAll(names);
        allSet.addAll(passwords);
        List<String> allWords = new ArrayList<>(allSet);

        System.out.println("Trying all " + allWords.size() + " words in the dictionary");
        String result = testWords(allWords, hashedString, hashAlgorithm);
        if (result != null) return result;

        System.out.println(textColors.get("RED") + "--------------------------------------------" + textColors.get("RESET"));
        System.out.println("Trying suffixes with every word in the dictionary " + numberFormat.format(suffixes.size() * allWords.size())+ " combinations");
        result = testCombinations(allWords, suffixes, hashedString, hashAlgorithm, true);
        if (result != null) return result;

        System.out.println(textColors.get("RED") + "--------------------------------------------" + textColors.get("RESET"));
        System.out.println("Trying prefixes with every word in the dictionary " + numberFormat.format(prefixes.size() * allWords.size() ) + " combinations");
        result = testCombinations(allWords, prefixes, hashedString, hashAlgorithm, false);
        if (result != null) return result;


        long doubleCombinations = (long) allWords.size() * (long) allWords.size() * 4;
        System.out.println(textColors.get("RED") + "--------------------------------------------" + textColors.get("RESET"));
        System.out.println("Try combining all words in the dictionary in pairs. " + numberFormat.format(doubleCombinations ) + " combinations");
        result = combinePasswords(passwords, hashedString, hashAlgorithm);
        if (result != null) return result;


        long totalCombinations = (long) prefixes.size() * (long) suffixes.size() * (long) allWords.size() * 4;
        System.out.println(textColors.get("RED") + "--------------------------------------------" + textColors.get("RESET"));
        System.out.println("Addin prefixes and suffixes to all words in the dictionary. " + numberFormat.format( totalCombinations) + " combinations");
        result = testDoubleCombinations(prefixes, suffixes, allWords, hashedString, hashAlgorithm);
        if (result != null) return result;
 

      
        return "Password not found using dictionary attack.";
    }

    private String testWords(List<String> words, String hashedString, String hashAlgorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        long count = 0;
        for (String guess : words) {
            count++;
            if (count % 100000 == 0) {
                System.out.println("Tried " + count + " words.  Current word: " + guess);
            }
            String hashedGuess = hash(guess, hashAlgorithm);
            if (hashedGuess.equals(hashedString)) {
                return guess;
            }
            String capitalizedGuess = capitalize(guess);
            if (hash(capitalizedGuess, hashAlgorithm).equals(hashedString)) {
                return capitalizedGuess;
            }
        }
        return null;
    }

    // Test passwords with affixes including capitalized versions
    private String testCombinations(List<String> words, List<String> affixes, String hashedString, String hashAlgorithm, boolean isSuffix) throws NoSuchAlgorithmException, InvalidKeySpecException {
        long count = 0;
        long totalCombinations = (long) affixes.size() * (long) words.size() * 4;
        for (String affix : affixes) {
            for (String guess : words) { 
                String passwordWithAffix = isSuffix ? guess + affix : affix + guess; 
                count++;

                // Check if the generated guess matches the hashed password
                if (hash(passwordWithAffix, hashAlgorithm).equals(hashedString)) {
                    return passwordWithAffix;
                }
                if (count % countBlock == 0) {
                    System.out.println("Tried " + textColors.get("RED") + numberFormat.format(count) + textColors.get("RESET") + " words.  Current word: " + textColors.get("GREEN") + passwordWithAffix + textColors.get("RESET") + " hash value: " + textColors.get("CYAN")   +  hash(passwordWithAffix, hashAlgorithm) + textColors.get("RESET")  + " Percent complete " + textColors.get("BLUE") + (count * 100 / totalCombinations) + "%" + textColors.get("RESET"));
                }

                count++;
                // capitalize the guess with the affix unchanged
                String capitalizedPassword = isSuffix ? capitalize(guess) + affix : affix + capitalize(guess);
                if (hash(capitalizedPassword, hashAlgorithm).equals(hashedString)) {
                    return capitalizedPassword;
                }
                if (count % countBlock == 0) {
                    System.out.println("Tried " + textColors.get("RED") + numberFormat.format(count) + textColors.get("RESET") + " words.  Current word: " + textColors.get("GREEN") + capitalizedPassword + textColors.get("RESET") + " hash value: " + textColors.get("CYAN")   +  hash(capitalizedPassword, hashAlgorithm) + textColors.get("RESET")  + " Percent complete " + textColors.get("BLUE") + (count * 100 / totalCombinations) + "%" + textColors.get("RESET"));
                }

                count++;
                // capitalize the affix with the guess unchanged
                String passwordWithAffixCapitalized = isSuffix ? guess + capitalize(affix) : capitalize(affix) + guess;
                if (hash(passwordWithAffixCapitalized, hashAlgorithm).equals(hashedString)) {
                    return passwordWithAffixCapitalized;
                }
                if (count % countBlock == 0) {
                    System.out.println("Tried " + textColors.get("RED") + numberFormat.format(count) + textColors.get("RESET") + " words.  Current word: " + textColors.get("GREEN") + passwordWithAffixCapitalized + textColors.get("RESET") + " hash value: " + textColors.get("CYAN")   +  hash(passwordWithAffix, hashAlgorithm) + textColors.get("RESET")  + " Percent complete " + textColors.get("BLUE") + (count * 100 / totalCombinations) + "%" + textColors.get("RESET"));
                }
                count++;
                // capitalize both the guess and the affix
                String capitalizedPasswordWithAffix = isSuffix ? capitalize(guess) + capitalize(affix) : capitalize(affix) + capitalize(guess);
                if (hash(capitalizedPasswordWithAffix, hashAlgorithm).equals(hashedString)) {
                    return capitalizedPasswordWithAffix;
                }
                if (count % countBlock == 0) {
                    System.out.println("Tried " + textColors.get("RED") + numberFormat.format(count) + textColors.get("RESET") + " words.  Current word: " + textColors.get("GREEN") + capitalizedPasswordWithAffix + textColors.get("RESET") + " hash value: " + textColors.get("CYAN")   +  hash(capitalizedPasswordWithAffix, hashAlgorithm) + textColors.get("RESET")  + " Percent complete " + textColors.get("BLUE") + (count * 100 / totalCombinations) + "%" + textColors.get("RESET"));
                }
            }
        }
        return null;
    }

    // Test all combinations of prefixes, suffixes, and words including capitalized versions
    private String testDoubleCombinations(List<String> prefixes, List<String> suffixes, List<String> words, String hashedString, String hashAlgorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        long count = 0;
        long totalCombinations = (long) prefixes.size() * (long) suffixes.size() * (long) words.size() * 4;
        for (String prefix : prefixes) {

            for (String suffix : suffixes) {
                for (String guess : words) {
                   
                    String password = prefix + guess + suffix;
                    
                    if (hash(password, hashAlgorithm).equals(hashedString)) {
                        return password;
                    }
                    count++;
                    if (count % countBlock == 0) {
                        System.out.println("Tried " + textColors.get("RED") +  numberFormat.format(count) + textColors.get("RESET") + " words.  Current word: " + textColors.get("GREEN") + password + textColors.get("RESET") + " hash value: " + textColors.get("CYAN")   +  hash(password, hashAlgorithm) + textColors.get("RESET")  + " Percent complete " + textColors.get("BLUE") + (count * 100 / totalCombinations) + "%" + textColors.get("RESET")) ;
                    }

                 
                    String capitalizedPassword = prefix + capitalize(guess) + suffix;
                    if (hash(capitalizedPassword, hashAlgorithm).equals(hashedString)) {
                        return capitalizedPassword;
                    }

                    count++;
                    if (count % countBlock == 0) {
                        System.out.println("Tried " + textColors.get("RED") +  numberFormat.format(count) + textColors.get("RESET") + " words.  Current word: " + textColors.get("GREEN") + capitalizedPassword + textColors.get("RESET") + " hash value: " + textColors.get("CYAN")   +  hash(capitalizedPassword, hashAlgorithm) + textColors.get("RESET")  + " Percent complete " + textColors.get("BLUE") + (count * 100 / totalCombinations) + "%" + textColors.get("RESET")) ;
                    }

                    String passwordWithCapitalSuffix = prefix + guess + capitalize(suffix);
                    if (hash(passwordWithCapitalSuffix, hashAlgorithm).equals(hashedString)) {
                        return passwordWithCapitalSuffix;
                    }
                    count++;
                    if (count % countBlock == 0) {
                        System.out.println("Tried " + textColors.get("RED") +  numberFormat.format(count) + textColors.get("RESET") + " words.  Current word: " + textColors.get("GREEN") + passwordWithCapitalSuffix + textColors.get("RESET") + " hash value: " + textColors.get("CYAN")   +  hash(passwordWithCapitalSuffix, hashAlgorithm) + textColors.get("RESET")  + " Percent complete " + textColors.get("BLUE") + (count * 100 / totalCombinations) + "%" + textColors.get("RESET")) ;
                    }

                    String passwordWithCapitalPrefix = capitalize(prefix) + guess + suffix;
                    if (hash(passwordWithCapitalPrefix, hashAlgorithm).equals(hashedString)) {
                        return passwordWithCapitalPrefix;
                    }
                    count++;
                    if (count % countBlock == 0) {
                        System.out.println("Tried " + textColors.get("RED") +  numberFormat.format(count) + textColors.get("RESET") + " words.  Current word: " + textColors.get("GREEN") + passwordWithCapitalPrefix + textColors.get("RESET") + " hash value: " + textColors.get("CYAN")   +  hash(passwordWithCapitalPrefix, hashAlgorithm) + textColors.get("RESET")  + " Percent complete " + textColors.get("BLUE") + (count * 100 / totalCombinations) + "%" + textColors.get("RESET")) ;
                    }

                }
            }
        }
        return null;
    }

    // Combine all words in the dictionary in pairs
    private String combinePasswords(List<String> passwords, String hashedString, String hashAlgorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        long count = 0;
        long totalCombinations = (long) passwords.size() * (long) passwords.size() * 4;
        for (String guess1 : passwords) {
            for (String guess2 : passwords) {

              

                String password = guess1 + guess2;
                count++;
                if (count % countBlock == 0) {
                    System.out.println("Tried " + textColors.get("RED") + numberFormat.format(count) + textColors.get("RESET") + " words.  Current word: " + textColors.get("GREEN") + password + textColors.get("RESET") + " hash value: " + textColors.get("CYAN")   +  hash(password, hashAlgorithm) + textColors.get("RESET")  + " Percent complete " + textColors.get("BLUE") + (count * 100 / totalCombinations) + "%" + textColors.get("RESET")) ;    
                }
                if (hash(password, hashAlgorithm).equals(hashedString)) {
                    return password;
                }

                password = guess2 + guess1;
                count++;
                if (count % countBlock == 0) {
                    System.out.println("Tried " + textColors.get("RED") + numberFormat.format(count) + textColors.get("RESET") + " words.  Current word: " + textColors.get("GREEN") + password + textColors.get("RESET") +   " hash value: " +  hash(password, hashAlgorithm) +" Percent complete " + textColors.get("BLUE") + (count * 100 / totalCombinations) + "%" + textColors.get("RESET")) ;
                }

                // capitalize the first guess
                String capitalizedPassword = capitalize(guess1) + guess2;
                if (hash(capitalizedPassword, hashAlgorithm).equals(hashedString)) {
                    return capitalizedPassword;
                }

                count++;
                if (count % countBlock == 0) {
                    System.out.println("Tried " + textColors.get("RED") + numberFormat.format(count) + textColors.get("RESET") + " words.  Current word: " + textColors.get("GREEN") + capitalizedPassword + textColors.get("RESET") + " hash value: " + textColors.get("CYAN")   +  hash(capitalizedPassword, hashAlgorithm) + textColors.get("RESET")  + " Percent complete " + textColors.get("BLUE") + (count * 100 / totalCombinations) + "%" + textColors.get("RESET")) ;
                }

                // capitalize the second guess
                capitalizedPassword = guess1 + capitalize(guess2);
                if (hash(capitalizedPassword, hashAlgorithm).equals(hashedString)) {
                    return capitalizedPassword;
                }
                count++;
                if (count % countBlock == 0) {
                    System.out.println("Tried " + textColors.get("RED") + numberFormat.format(count) + textColors.get("RESET") + " words.  Current word: " + textColors.get("GREEN") + capitalizedPassword + textColors.get("RESET") + " hash value: " + textColors.get("CYAN")   +  hash(capitalizedPassword, hashAlgorithm) + textColors.get("RESET")  + " Percent complete " + textColors.get("BLUE") + (count * 100 / totalCombinations) + "%" + textColors.get("RESET")) ;
                }
            }
        }
        return null;
    }

    

    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public String bruteForce(String passwordHashed, int maxGuess, String alphabet, String algorithm) {
        String result = "No results found.";
        long count = 0;
        Date start = new Date();
    
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    
        for (int length = 1; length <= maxGuess; length++) {
            System.out.println(textColors.get("CYAN") + "==================================================" + textColors.get("RESET"));
            System.out.println("Trying length: " + length + " which will have " + numberFormat.format(Math.pow(alphabet.length(), length)) + " combinations");

            int[] indices = new int[length];
    
            while (true) {
                char[] charalphabet = alphabet.toCharArray();
                // Generate guess based on indices
                char[] guess = new char[length];
                for (int i = 0; i < length; i++) {
                    guess[i] = charalphabet[indices[i]];
                }
    
             
                String hash = "";
                // Check if the generated guess matches the hashed password
                try {
                    hash = hashWithAlgorithm(new String(guess), algorithm);
                    if (hash.equals(passwordHashed)) {
                        result = new String(guess);
                        return result;
                    }
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("Error: " + e.getMessage());
                }

                   // Increment the count and print if necessary
                   count++;
                   if (count % 5000000 == 0) {
                       printStatus(count, new String(guess), start, length, alphabet, (long) Math.pow(alphabet.length(), length), hash);
                   }
    
                // Increment the indices
                int incrementIndex = length - 1;
                while (incrementIndex >= 0 && indices[incrementIndex] == alphabet.length() - 1) {
                    indices[incrementIndex] = 0;
                    incrementIndex--;
                }
    
                // Break if all combinations have been tried
                if (incrementIndex < 0) {
                    break;
                }
     
                // Move to the next combination
                indices[incrementIndex]++;
            }
        }
    
        return result;
    }
    
    private String hashWithAlgorithm(String password, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return Base64.getEncoder().encodeToString(digest);
    }

    private String hashWithBCrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private String hashWithSCrypt(String password) {
        return SCryptUtil.scrypt(password, 16, 16, 16);
    }

    private String hashWithPBKDF2(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), "salt".getBytes(), 65536, 128);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    private String hashWithArgon2(String password) {
        Argon2 argon2 = Argon2Factory.create();
        return argon2.hash(2, 65536, 1, password.toCharArray());
    }

    private List<String> readFileLines(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    private void printStatus(long count, String guess, Date start, int length, String alphabet, long totalCombinations, String hash) {
        Date now = new Date();
        long elapsedTime = (now.getTime() - start.getTime()) / 1000; // total elapsed time in seconds

        double percentCompleted = ((double) count / totalCombinations) * 100;
        long hashesPerSecond = count / (elapsedTime > 0 ? elapsedTime : 1);

        System.out.println("Count: " + textColors.get("RED") + numberFormat.format(count) + textColors.get("RESET") +
                           " Guess: " + textColors.get("GREEN") + guess + textColors.get("RESET") +
                           " Hash: " + textColors.get("CYAN") + hash + textColors.get("RESET") +
                           " Time: " + textColors.get("BLUE") + elapsedTime + textColors.get("RESET") +
                           " seconds. Avg Hashes per second: " + textColors.get("YELLOW") + numberFormat.format(hashesPerSecond) +
                           textColors.get("RESET") + " Percent completed for length " +
                           textColors.get("PURPLE") + length + " " + textColors.get("CYAN") + String.format("%.2f", percentCompleted) +
                           textColors.get("RESET") + "%.");
    }
}
