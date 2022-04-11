package encryptdecrypt;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Extracting Arguments
        // Building Argument List
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));

        // Determine mode
        String mode = argList.contains("-mode") ?
                            argList.get(argList.indexOf("-mode") + 1) : "enc";

        // Extracting Data
        char[] data;
        if (argList.contains("-data")) {
            data = argList.get(argList.indexOf("-data") + 1).toCharArray();
        } else if (argList.contains("-in")) {
            File input = new File(argList.get(argList.indexOf("-in") + 1));
            try (Scanner scanner = new Scanner(input)) {
                String dataString = "";
                while (scanner.hasNextLine()) {
                    dataString += scanner.nextLine();
                }
                data = dataString.toCharArray();
            } catch (IOException e) {
                System.out.println("===Error===");
                e.printStackTrace();
                data = "".toCharArray();
            }
        } else {
            data = "".toCharArray();
        }

        // Extracting Key
        int key = argList.contains("-key") ?
                Integer.parseInt(argList.get(argList.indexOf("-key") + 1)) : 0;

        // Declare output
        String cryptedData;

        // Determine Algorithm
        Cryptor cryptor = new Cryptor();
        if (argList.contains("-alg")) {
            switch (argList.get(argList.indexOf("-alg") + 1)) {
                case "unicode":
                    cryptor.setAlgorithm(new unicode());
                    break;
                case "shift":
                    cryptor.setAlgorithm(new shift());
                    break;
                default:
                    cryptor.setAlgorithm(new shift());
            }
        } else {
            cryptor.setAlgorithm(new shift());
        }

        // Execute
        if (mode.equals("dec")) {
            cryptedData = cryptor.algorithm.decrypt(data, key);
        } else {
            cryptedData = cryptor.algorithm.encrypt(data, key);
        }

        // Output
        if (argList.contains("-out")) {
            try (FileWriter output = new FileWriter(argList.get(argList.indexOf("-out") + 1))) {
                output.write(cryptedData);
            } catch (Exception exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        } else {
            System.out.println(cryptedData);
        }
    }
}

interface CryptAlgorithm {
    String encrypt(char[] message, int key);
    String decrypt(char[] message, int key);
}

class Cryptor {
    CryptAlgorithm algorithm;

    public void setAlgorithm(CryptAlgorithm algorithm) {
        this.algorithm = algorithm;
    }
}

class unicode implements CryptAlgorithm {

    @Override
    public String encrypt(char[] message, int key) {
        StringBuilder encryptedText = new StringBuilder();
        for (char c : message) {
            char cipherChar = (char) (c + key);
            encryptedText.append(cipherChar);
        }
        return String.valueOf(encryptedText);
    }

    @Override
    public String decrypt(char[] message, int key) {
        StringBuilder decryptedText = new StringBuilder();
        for (char c : message) {
            char origChar = (char) (c - key);
            decryptedText.append(origChar);
        }
        return String.valueOf(decryptedText);
    }
}

class shift implements CryptAlgorithm {

    final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    @Override
    public String encrypt(char[] message, int key) {
        StringBuilder encryptedText = new StringBuilder();
        for (char c : message) {
            char lowerCasedC = Character.toLowerCase(c);
            if (!(alphabet.contains(Character.toString(lowerCasedC)))) {
                encryptedText.append(c);
                continue;
            }
            int newIndex = (alphabet.indexOf(lowerCasedC) + key) % alphabet.length();
            char cipherChar = alphabet.charAt(newIndex);
            if (Character.isUpperCase(c)) {
                cipherChar = Character.toUpperCase(cipherChar);
            }
            encryptedText.append(cipherChar);
        }
        return String.valueOf(encryptedText);
    }

    @Override
    public String decrypt(char[] message, int key) {
        StringBuilder decryptedText = new StringBuilder();
        for (char c : message) {
            char lowerCasedC = Character.toLowerCase(c);
            if (!(alphabet.contains(Character.toString(lowerCasedC)))) {
                decryptedText.append(c);
                continue;
            }
            int newIndex = (alphabet.indexOf(lowerCasedC) - key) % alphabet.length();
            while (newIndex < 0) {
                newIndex += alphabet.length();
            }
            char origChar = alphabet.charAt(newIndex);
            if (Character.isUpperCase(c)) {
                origChar = Character.toUpperCase(origChar);
            }
            decryptedText.append(origChar);
        }
        return String.valueOf(decryptedText);
    }
}