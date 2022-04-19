import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.TestCase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class TestClue {
    String input;
    String fileContent;

    TestClue(String input, String fileContent) {
        this.input = input;
        this.fileContent = fileContent;
    }
}

public class CorrecterTest extends StageTest<TestClue> {

    private static File received = null;
    private static File encoded = null;
    private static File decoded = null;

    @Override
    public List<TestCase<TestClue>> generate() {
        TestClue[] testClues = new TestClue[]{
            new TestClue("encode", "Eat more of these french buns!"),
            new TestClue("send",   "Eat more of these french buns!"),
            new TestClue("decode", "Eat more of these french buns!"),

            new TestClue("encode", "$ome rand0m messAge"),
            new TestClue("send",   "$ome rand0m messAge"),
            new TestClue("decode", "$ome rand0m messAge"),

            new TestClue("encode", "better call Saul 555-00-73!"),
            new TestClue("send",   "better call Saul 555-00-73!"),
            new TestClue("decode", "better call Saul 555-00-73!"),

            new TestClue("encode", "5548172 6548 225147 23656595 5155"),
            new TestClue("send",   "5548172 6548 225147 23656595 5155"),
            new TestClue("decode", "5548172 6548 225147 23656595 5155"),
        };

        List<TestCase<TestClue>> result = new ArrayList<>();

        for (int i = 0; i < testClues.length; i++) {
            result.add(new TestCase<TestClue>()
                .setAttach(testClues[i])
                .setInput(testClues[i].input)
                .addFile("send.txt", testClues[i].fileContent));
        }

        return result;
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {
        String path = System.getProperty("user.dir");

        received = null;
        encoded = null;
        decoded = null;

        searchReceived();
        searchEncoded();
        searchDecoded();

        String correctFileBinary = toBinary(clue.fileContent.getBytes());
        String correctFileEncoded = encodeFile(correctFileBinary);

        String action = clue.input;

        if (action.equals("encode")) {

            if (encoded == null) {
                return new CheckResult(false,
                    "Can't find encoded.txt file. " +
                        "Make sure your program writes it down or " +
                        "make sure the name of file is correct.");
            }

            byte[] encodedContent;
            FileInputStream encodedStream;

            try {
                encodedStream = new FileInputStream(encoded);
            } catch (FileNotFoundException e) {
                return new CheckResult(false,
                    "Can't find received.txt file. " +
                        "Make sure your program writes it down or " +
                        "make sure the name of file is correct.");
            }

            try {
                encodedContent = encodedStream.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Can't read the file");
            }

            String encodedBinary = toBinary(encodedContent);

            return new CheckResult(encodedBinary.equals(correctFileEncoded));
        }

        if (action.equals("send")) {

            if (received == null) {
                return new CheckResult(false,
                    "Can't find received.txt file. " +
                        "Make sure your program writes it " +
                        "down or make sure the name of file is correct.");
            }

            byte[] receivedContent;

            FileInputStream receivedStream;

            try {
                receivedStream = new FileInputStream(received);
            } catch (FileNotFoundException e) {
                return new CheckResult(false,
                    "Can't find received.txt file. " +
                        "Make sure your program writes it down or " +
                        "make sure the name of file is correct.");
            }

            try {
                receivedContent = receivedStream.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Can't read the file");
            }

            String receivedBinary = toBinary(receivedContent);


            return checkDifference(receivedBinary, correctFileEncoded);
        }



        if (action.equals("decode")) {

            if (decoded == null) {
                return new CheckResult(false,
                    "Can't find decoded.txt file. " +
                        "Make sure your program writes it down or " +
                        "make sure the name of file is correct.");
            }

            byte[] decodedContent;


            FileInputStream decodedStream;

            try {
                decodedStream = new FileInputStream(decoded);
            } catch (FileNotFoundException e) {
                return new CheckResult(false,
                    "Can't find received.txt file. " +
                        "Make sure your program writes it down or " +
                        "make sure the name of file is correct.");
            }

            try {
                decodedContent = decodedStream.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Can't read the file");
            }

            String decodedBinary = toBinary(decodedContent);

            if (!decodedBinary.equals(correctFileBinary)) {
                return new CheckResult(false, "The decoded text must match initial text!");
            }

            return CheckResult.correct();
        }

        throw new RuntimeException("Can't check the program");
    }

    private static String toBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++) {
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        }
        return sb.toString();
    }

    private static byte[] fromBinary(String s) {
        int sLen = s.length();
        byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
        char c;
        for (int i = 0; i < sLen; i++)
            if ((c = s.charAt(i)) == '1')
                toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
            else if (c != '0')
                throw new IllegalArgumentException();
        return toReturn;
    }

    private static void searchReceived() {
        File file = new File("received.txt");
        if (file.exists()) {
            received = file;
        }
    }

    private static void searchEncoded() {
        File file = new File("encoded.txt");
        if (file.exists()) {
            encoded = file;
        }
    }

    private static void searchDecoded() {
        File file = new File("decoded.txt");
        if (file.exists()) {
            decoded = file;
        }
    }

    private String encodeFile(String binaryString) {

        String encoded = "";

        for (int i = 0; i < binaryString.length(); i += 4) {

            if (i + 4 > binaryString.length()) {
                throw new RuntimeException("Can't decode binary data");
            }

            int startSubIndex = i;
            int stopSubIndex = i + 4;

            String currSub = binaryString.substring(startSubIndex, stopSubIndex);

            String encodedPart;

            int parityBit1 = 0;
            int parityBit2 = 0;
            int parityBit4 = 0;

            if (currSub.charAt(0) == '1') {
                parityBit1++;
                parityBit2++;
            }

            if (currSub.charAt(1) == '1') {
                parityBit1++;
                parityBit4++;
            }

            if (currSub.charAt(2) == '1') {
                parityBit2++;
                parityBit4++;
            }

            if (currSub.charAt(3) == '1') {
                parityBit1++;
                parityBit2++;
                parityBit4++;
            }

            encodedPart =
                (parityBit1 % 2 == 1? "1": "0") +
                    (parityBit2 % 2 == 1? "1": "0") +
                    currSub.charAt(0) +
                    (parityBit4 % 2 == 1? "1": "0") +
                    currSub.charAt(1) +
                    currSub.charAt(2) +
                    currSub.charAt(3) +
                    "0";

            encoded += encodedPart;
        }

        return encoded;
    }

    private CheckResult checkDifference(String output, String correct) {
        if (output.isEmpty() && correct.isEmpty()) return CheckResult.correct();

        if (output.length() != correct.length()) {
            return new CheckResult(false,
                "The program was expected to output " +
                    correct.length() / 8 +
                    " bytes, but output " +
                    output.length() / 8);
        }

        for (int i = 0; i < output.length(); i += 8) {
            String currOutputByte = output.substring(i, i+8);
            String currCorrectByte = correct.substring(i, i+8);

            int difference = 0;
            for (int j = 0; j < currCorrectByte.length(); j++) {
                char currOutputBit = currOutputByte.charAt(j);
                char currCorrectBit = currCorrectByte.charAt(j);

                if (currCorrectBit != currOutputBit) {
                    difference++;
                }
            }

            if (difference == 0) {
                return new CheckResult(false,
                    "One of bytes from the input stayed the same but should be changed");
            }

            if (difference != 1) {
                return new CheckResult(false,
                    "One of bytes from the input was changes in more than one bit");
            }
        }

        return CheckResult.correct();
    }
}

