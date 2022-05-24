import org.hyperskill.hstest.exception.outcomes.WrongAnswer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String findId(String output) {
        Pattern pattern = Pattern.compile("ID = (\\d+)");
        Matcher matcher = pattern.matcher(output);

        if (matcher.find()) {
            int count = matcher.groupCount();
            if (count != 1) {
                throw new WrongAnswer("Can't find ID of the file in the output!\nMake sure you print ID like in examples!");
            }
            return matcher.group(1);
        } else {
            throw new WrongAnswer("Can't find ID of the file in the output!\nMake sure you print ID like in examples!");
        }
    }

    public static void deleteTestFiles() {
        File dir = new File(FileServerTest.serverDataPath);

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith("test_purpose_")) {
                    boolean isDeleted = file.delete();
                    if (!isDeleted) {
                        throw new WrongAnswer("Can't delete test files. Maybe they are not closed!");
                    }
                }
            }
        }

        File cdir = new File(FileServerTest.clientDataPath);
        files = cdir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith("test_purpose_")) {
                    boolean isDeleted = file.delete();
                    if (!isDeleted) {
                        throw new WrongAnswer("Can't delete test files. Maybe their input streams are not closed!");
                    }
                }
            }
        }
    }

    public static void createFiles(String clientDataPath) {

        for (int i = 0; i < 2; i++) {
            try {
                File file = new File(clientDataPath + String.format("test_purpose_test%d.txt", i + 1));
                if (!file.exists()) file.createNewFile();
                FileWriter writer = new FileWriter(file, false);
                writer.write(String.format("test%d", i + 1));
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException("Can't create test files!");
            }

        }
    }

    public static int numExistingFiles(File file) {
        File[] files = file.listFiles();
        if (files == null) {
            return 0;
        }
        return (int) Arrays.stream(files).filter(File::exists).count();
    }

    private static boolean isFileExists(String path) {
        return Files.exists(Paths.get(path)) && !Files.isDirectory(Paths.get(path));
    }

    public static boolean isClientFileExists(String fileName) {
        return isFileExists(FileServerTest.clientDataPath + fileName);
    }

    public static boolean isServerFileExists(String fileName) {
        return isFileExists(FileServerTest.serverDataPath + fileName);
    }

    public static String getServerFileContent(String fileName) {
        return getFileContent(FileServerTest.serverDataPath + fileName);
    }

    public static String getClientFileContent(String fileName) {
        return getFileContent(FileServerTest.clientDataPath + fileName);
    }

    private static String getFileContent(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName))).trim();
        } catch (IOException e) {
        }
        throw new WrongAnswer("Can't read files content.\n" +
            "Make sure you close input/output streams after reading or writing files!");
    }

}


