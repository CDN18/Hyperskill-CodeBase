package correcter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Write a mode: ");
        switch (scanner.next()) {
            case "encode":
                encode();
                break;
            case "send":
                send();
                break;
            case "decode":
                decode();
                break;
            default:
        }
    }

    private static void decode() {
        try (FileInputStream receive = new FileInputStream("received.txt")) {
            ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
            StringBuilder decodedBinary = new StringBuilder();
            byte[] receivedBytes = receive.readAllBytes();
            for (byte receivedByte : receivedBytes) {
                String binary = Integer.toBinaryString(receivedByte & 0xff);
                while (binary.length() < Byte.SIZE) {
                    binary = String.format("0%s", binary);
                }
                char[] binaryArray = binary.toCharArray();
                int[] binaryBits = new int[binaryArray.length];
                for (int j = 0; j < binaryBits.length; j++) {
                    binaryBits[j] = Character.getNumericValue(binaryArray[j]);
                }
                int group1 = binaryBits[2] ^ binaryBits[4] ^ binaryBits[6];
                int group2 = binaryBits[2] ^ binaryBits[5] ^ binaryBits[6];
                int group3 = binaryBits[4] ^ binaryBits[5] ^ binaryBits[6];
                boolean[] correctIndex = new boolean[4];
                correctIndex[0] = (group1 ^ binaryBits[0]) == 1 && (group2 ^ binaryBits[1]) == 1;
                correctIndex[1] = (group1 ^ binaryBits[0]) == 1 && (group3 ^ binaryBits[3]) == 1;
                correctIndex[2] = (group2 ^ binaryBits[1]) == 1 && (group3 ^ binaryBits[3]) == 1;
                correctIndex[3] = correctIndex[0] && correctIndex[1] && correctIndex[2];
                if (correctIndex[3]) {
                    Arrays.fill(correctIndex, 0, correctIndex.length - 1, false);
                }
                int index = -1;
                for (int i = 0; i < correctIndex.length; i++) {
                    if (correctIndex[i]) {
                        index = i;
                    }
                }
                switch (index) {
                    case 0:
                        binaryBits[2] = binaryBits[0] ^ binaryBits[4] ^ binaryBits[6];
                        break;
                    case 1:
                        binaryBits[4] = binaryBits[0] ^ binaryBits[2] ^ binaryBits[6];
                        break;
                    case 2:
                        binaryBits[5] = binaryBits[1] ^ binaryBits[2] ^ binaryBits[6];
                        break;
                    case 3:
                        binaryBits[6] = binaryBits[0] ^ binaryBits[2] ^ binaryBits[4];
                        break;
                    default:
                }
                decodedBinary.append(binaryBits[2]).append(binaryBits[4]).
                            append(binaryBits[5]).append(binaryBits[6]);
            }
            byte[] decodedBytes = new byte[decodedBinary.length() / Byte.SIZE];
            for (int i = 0; i < decodedBytes.length; i+= 1) {
                decodedBytes[i] = (byte) (Integer.parseUnsignedInt(decodedBinary.
                                substring(i * Byte.SIZE, (i + 1) * Byte.SIZE), 2) & 0xff);
            }
            outputBuffer.write(decodedBytes);
            try (FileOutputStream decoded = new FileOutputStream("decoded.txt")) {
                decoded.write(outputBuffer.toByteArray());
            }
        } catch (FileSystemNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void send() {
        try (FileInputStream encode = new FileInputStream("encoded.txt")) {
            ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
            byte[] encodedBytes = encode.readAllBytes();
            Random generator = new Random();
            for (int i = 0; i < encodedBytes.length; i++) {
                encodedBytes[i] ^= 1 << generator.nextInt(Byte.SIZE);
            }
            outputBuffer.writeBytes(encodedBytes);
            try (FileOutputStream received = new FileOutputStream("received.txt")) {
                received.write(outputBuffer.toByteArray());
            }
        } catch (FileSystemNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void encode() {
        try (FileInputStream send = new FileInputStream("send.txt")) {
            ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
            byte[] input = send.readAllBytes();
            StringBuilder inputBinary = new StringBuilder();
            StringBuilder encodeBinary = new StringBuilder();
            for (byte b : input) {
                String binary = Integer.toBinaryString(b & 0xff);
                while (binary.length() < 8) {
                    binary = String.format("0%s", binary);
                }
                inputBinary.append(binary);
            }
            for (int i = 0; i < inputBinary.length(); i += Byte.SIZE / 2) {
                char[] subString = inputBinary.substring(i, i + Byte.SIZE / 2).toCharArray();
                int[] subBits = new int[subString.length];
                for (int j = 0; j < subBits.length; j++) {
                    subBits[j] = Character.getNumericValue(subString[j]);
                }
                encodeBinary.append(subBits[0] ^ subBits[1] ^ subBits[3]).
                            append(subBits[0] ^ subBits[2] ^ subBits[3]).append(subBits[0]).
                            append(subBits[1] ^ subBits[2] ^ subBits[3]).
                            append(subString, 1, 3).append(0);
            }
            int byteSize = encodeBinary.length() / Byte.SIZE;
            byte[] encodeBytes = new byte[byteSize];
            for (int i = 0; i < byteSize; i ++) {
                encodeBytes[i] = (byte) (Integer.parseUnsignedInt(encodeBinary.substring(i * Byte.SIZE, (i + 1) * Byte.SIZE), 2) & 0xff);
            }
            outputBuffer.writeBytes(encodeBytes);
            try (FileOutputStream encoded = new FileOutputStream("encoded.txt")) {
                encoded.write(outputBuffer.toByteArray());
            }
        } catch (FileSystemNotFoundException | IOException fnf) {
            throw new RuntimeException(fnf);
        }
    }
}