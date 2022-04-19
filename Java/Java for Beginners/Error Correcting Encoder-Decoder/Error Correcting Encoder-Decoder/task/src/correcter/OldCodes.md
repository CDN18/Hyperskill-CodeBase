# Codes before stage 3
```java
package correcter;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final StringBuilder dictionary = new StringBuilder("aAbBcCdDeEfFgGhHiIjJkKlL" +
                "mMnNoOpPqQrRsStTuUvVwWxXyYzZ 0123456789");
        Scanner scanner = new Scanner(System.in);
        String originalInput = scanner.nextLine();
        System.out.println(originalInput);
        StringBuilder input = new StringBuilder();
        final int times = 3;
        for (char text : originalInput.toCharArray()) {
            input.append(String.valueOf(text).repeat(times));
        }
        System.out.println(input);
        Random generator = new Random();
        for (int i = 0; i < input.length(); i += 3) {
            int index = i + generator.nextInt(3); // Index of the char to be changed
            int original = dictionary.indexOf(String.valueOf(input.charAt(index))); // Find the char in the dictionary
            int distorted = (original + generator.nextInt()) % dictionary.length();
            distorted = Math.abs(distorted);
            while (original == distorted) {
                distorted = (original + generator.nextInt()) % dictionary.length();
                distorted = Math.abs(distorted);
            } // Make sure that the distorted char is different from the original one
            input.setCharAt(index, dictionary.charAt(distorted));
        }
        System.out.println(input);
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i += 3) {
            String text = input.substring(i, i + 3);
            boolean pair1 = text.substring(0, 1).equals(text.substring(1, 2));
            boolean pair2 = text.substring(0, 1).equals(text.substring(2, 3));
            if (pair1 || pair2) {
                output.append(text.charAt(0));
            } else {
                output.append(text.charAt(1));
            }
        }
        System.out.println(output);
    }
}

```

# Codes before stage 4

```java
package correcter;

import java.io.*;
import java.nio.file.FileSystemNotFoundException;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        try (FileInputStream send = new FileInputStream("send.txt")) {
            BufferedInputStream inputBuffer = new BufferedInputStream(send);
            ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
            byte currentByte = (byte) inputBuffer.read();
            Random generator = new Random();
            while (currentByte != -1) {
                int index = generator.nextInt(Byte.SIZE);
                byte upper = (byte) (currentByte >>> (index + 1) << (index + 1));
                int lowerTemp = (currentByte << (Byte.SIZE - index)) & 0xff;
                byte lower = (byte) (lowerTemp >>> (Byte.SIZE - index));
                byte current = (byte) ((currentByte >>> (index)) & 0xff);
                current = (byte) ((current << (Byte.SIZE - 1)) & 0xff);
                current = (byte) ((current >>> (Byte.SIZE - index - 1)) & 0xff);
                current = (byte) (current == 0 ? 1 : 0);
                current = (byte) (current << index);
                byte newByte = (byte) (upper + lower + current);
                outputBuffer.write(newByte);
                currentByte = (byte) inputBuffer.read();
            }
            try (FileOutputStream receive = new FileOutputStream("received.txt")) {
                receive.write(outputBuffer.toByteArray());
            }
        } catch (FileSystemNotFoundException | IOException fnf) {
            throw new RuntimeException(fnf);
        }

    }
}

```

# Codes before stage 5
- Part of the original decode() function
```java
    /*
                for (int j = 0; j < binary.length(); j += 2) {
                    int num1 = Integer.parseUnsignedInt(binary.substring(j, j + 1));
                    int num2 = Integer.parseUnsignedInt(binary.substring(j + 1, j + 2));
                    if ((num1 ^ num2) == 1) {
                        int correct;
                        switch (j) {
                            case 0:
                                correct = Integer.parseUnsignedInt(binary.substring(6, 7)) ^
                                                Integer.parseUnsignedInt(binary.substring(4, 5)) ^
                                                Integer.parseUnsignedInt(binary.substring(2, 3));
                                binary = String.valueOf(correct).repeat(2) +
                                                binary.substring(2);
                                decodedBinary.append(correct).append(binary.charAt(2))
                                                .append(binary.charAt(4));
                                break;
                            case 2:
                                correct = Integer.parseUnsignedInt(binary.substring(6, 7)) ^
                                        Integer.parseUnsignedInt(binary.substring(4, 5)) ^
                                        Integer.parseUnsignedInt(binary.substring(0, 1));
                                binary = binary.substring(0, 2) + String.valueOf(correct).repeat(2) +
                                        binary.substring(4);
                                decodedBinary.append(binary.charAt(0)).append(correct).append(binary.charAt(4));
                                break;
                            case 4:
                                correct = Integer.parseUnsignedInt(binary.substring(6, 7)) ^
                                        Integer.parseUnsignedInt(binary.substring(2, 3)) ^
                                        Integer.parseUnsignedInt(binary.substring(0, 1));
                                binary = binary.substring(0, 4) + String.valueOf(correct).repeat(2);
                                decodedBinary.append(binary.charAt(0)).append(binary.charAt(2)).append(correct);
                                break;
                            default:
                                decodedBinary.append(binary.charAt(0)).append(binary.charAt(2)).append(binary.charAt(4));
                        }
                        break;
                    }
                }
                receivedBytes[i] = (byte) Integer.parseUnsignedInt(binary, 2);
     */
```

- Part of the original encode function

```java
            for (int i = 0; i < inputBinary.length(); i += 3) {
                byte[] binaries = new byte[3];
                encodeBinary.append(String.valueOf(inputBinary.charAt(i)).repeat(2));
                binaries[0] = (byte) Integer.parseUnsignedInt(String.valueOf(inputBinary.charAt(i)));
                if (i + 1 < inputBinary.length()) {
                    encodeBinary.append(String.valueOf(inputBinary.charAt(i + 1)).repeat(2));
                    binaries[1] = (byte) Integer.parseUnsignedInt(String.valueOf(inputBinary.charAt(i + 1)));
                } else {
                    encodeBinary.append("0".repeat(2));
                    binaries[1] = 0;
                }
                if (i + 2 < inputBinary.length()) {
                    encodeBinary.append(String.valueOf(inputBinary.charAt(i + 2)).repeat(2));
                    binaries[2] = (byte) Integer.parseUnsignedInt(String.valueOf(inputBinary.charAt(i + 2)));
                } else {
                    encodeBinary.append("0".repeat(2));
                    binaries[2] = 0;
                }
                int correctCode = binaries[0] ^ binaries[1] ^ binaries[2];
                encodeBinary.append(String.valueOf(correctCode).repeat(2));
            }
            int byteSize = encodeBinary.length() / Byte.SIZE;
            if (encodeBinary.length() % Byte.SIZE != 0) {
                byteSize++;
            }
            byte[] encodeBytes = new byte[byteSize];
            for (int i = 0; i < byteSize; i ++) {
                encodeBytes[i] = (byte) Integer.parseUnsignedInt(encodeBinary.substring(i * Byte.SIZE, (i + 1) * Byte.SIZE), 2);
            }
```

