import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import org.hyperskill.hstest.testing.TestedProgram;
import org.junit.AfterClass;

import java.io.File;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.hyperskill.hstest.common.Utils.sleep;

public class FileServerTest extends StageTest<String> {

    private static final String onConnectExceptionMessage = "A client can't connect to the server!\n" +
        "Make sure the server handles connections and doesn't stop after one client connected.";

    public static final String serverDataPath = System.getProperty("user.dir") +
        File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator;

    public static final String clientDataPath = System.getProperty("user.dir") +
        File.separator + "src" + File.separator + "client" + File.separator + "data" + File.separator;

    private static String id;

    @Override
    public List<TestCase<String>> generate() {
        return List.of(
            new TestCase<String>()
                .feedbackOnException(ConnectException.class, onConnectExceptionMessage)
                .setDynamicTesting(this::checkServerStop),
            new TestCase<String>()
                .feedbackOnException(ConnectException.class, onConnectExceptionMessage)
                .setDynamicTesting(this::checkPaths),
            new TestCase<String>()
                .feedbackOnException(ConnectException.class, onConnectExceptionMessage)
                .setDynamicTesting(this::testSaveAndGet),
            new TestCase<String>()
                .feedbackOnException(ConnectException.class, onConnectExceptionMessage)
                .setDynamicTesting(this::testGetAfterServerRestart),
            new TestCase<String>()
                .feedbackOnException(ConnectException.class, onConnectExceptionMessage)
                .setDynamicTesting(this::testDeleteFiles)
        );
    }

    // Test #1. Check if server stops
    CheckResult checkServerStop() {

        TestedProgram server = getServer();
        TestedProgram client;

        server.startInBackground();

        client = getClient();
        client.start();
        client.execute("exit");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!server.isFinished()) {
            return CheckResult.wrong("You should stop the server if a client sends 'exit'");
        }

        return CheckResult.correct();
    }

    CheckResult checkPaths() {
        // Test #2. Check folders with data
        if (!Files.exists(Paths.get(serverDataPath)) || !Files.isDirectory(Paths.get(serverDataPath))) {
            return CheckResult.wrong("Can't find '/server/data' folder. You should store all saved files in it!\n" +
                "The folder should be created even if the server wasn't started!");
        }

        if (!Files.exists(Paths.get(clientDataPath)) || !Files.isDirectory(Paths.get(clientDataPath))) {
            return CheckResult.wrong("Can't find '/client/data' folder. You should store all files you want to " +
                "store on the server in it!\n" +
                "The folder should be created even if the client wasn't started!");
        }
        return CheckResult.correct();
    }


    CheckResult testSaveAndGet() {

        TestedProgram server;
        TestedProgram client;

        Utils.createFiles(clientDataPath);

        // Test #3. Check saving file on the server
        server = getServer();
        server.startInBackground();

        File folder = new File(serverDataPath);
        int numOfFilesBeforeAdding = Utils.numExistingFiles(folder);

        client = getClient();
        client.start();
        client.execute("2\ntest_purpose_test1.txt");
        String clientOutput = client.execute("");

        if (!clientOutput.contains("Response says that file is saved! ID =")) {
            return CheckResult.wrong("After saving a file on the server you should print:\n" +
                "Response says that file is saved! ID = **, where ** is an id of the file!");
        }

        id = Utils.findId(clientOutput);

        int numOfFilesAfterAdding = Utils.numExistingFiles(folder);

        if (numOfFilesAfterAdding == numOfFilesBeforeAdding) {
            return CheckResult.wrong("Once a client saved a file on the server number of files in /server/data/ should be changed!");
        }

        client = getClient();
        client.start();
        clientOutput = client.execute("2\ntest_purpose_test2.txt\ntest_purpose_newFile.txt");

        if (!clientOutput.contains("Response says that file is saved! ID =")) {
            return CheckResult.wrong("After saving a file on the server you should print:\n" +
                "Response says that file is saved! ID = **, where ** is an id of the file!");
        }

        if (!Utils.isServerFileExists("test_purpose_newFile.txt")) {
            return CheckResult.wrong("Can't find a file after saving on the server." +
                "You should save client's files in /server/data/ folder!");
        }

        String savedFileContent = Utils.getServerFileContent("test_purpose_newFile.txt");

        if (!savedFileContent.equals("test2")) {
            return CheckResult.wrong("A file after saving has wrong content!");
        }

        // Test #4. Check getting files
        client = getClient();
        client.start();
        clientOutput = client.execute("1\n1\ntest_purpose_notExist.txt");

        if (!clientOutput.contains("The response says that this file is not found!")) {
            return CheckResult.wrong("When client tries to get a file by name that doesn't exist you should print:\n" +
                "\"The response says that this file is not found!\"");
        }

        client = getClient();
        client.start();
        clientOutput = client.execute("1\n2\n" + (id + "511"));

        if (!clientOutput.contains("The response says that this file is not found!")) {
            return CheckResult.wrong("When client tries to get a file by ID that doesn't exist you should print:\n" +
                "\"The response says that this file is not found!\"");
        }

        client = getClient();
        client.start();
        client.execute("1\n1\ntest_purpose_newFile.txt\ntest_purpose_get.txt");

        if (!Utils.isClientFileExists("test_purpose_get.txt")) {
            return CheckResult.wrong("Can't find a file after getting it from the server by name.\n" +
                "You should store all downloaded files from the server in /client/data/ folder.");
        }

        String downloadedByNameFileContent = Utils.getClientFileContent("test_purpose_get.txt");
        if (!downloadedByNameFileContent.equals("test2")) {
            return CheckResult.wrong("After getting a file from the server by name it has wrong content!");
        }

        client = getClient();
        client.start();
        client.execute("1\n2\n" + id + "\ntest_purpose_get_id.txt");

        if (!Utils.isClientFileExists("test_purpose_get_id.txt")) {
            return CheckResult.wrong("Can't find a file after getting it from the server by ID.\n" +
                "You should store all downloaded files from the server in /client/data/ folder.");
        }

        String downloadedByIdFileContent = Utils.getClientFileContent("test_purpose_get_id.txt");
        if (!downloadedByIdFileContent.equals("test1")) {
            return CheckResult.wrong("After getting a file from the server by ID it has wrong content!");
        }

        client = getClient();
        client.start();
        client.execute("exit");

        return CheckResult.correct();
    }

    CheckResult testGetAfterServerRestart() {

        TestedProgram server = getServer();
        TestedProgram client = getClient();

        server.startInBackground();
        client.start();
        client.execute("1\n1\ntest_purpose_newFile.txt\ntest_purpose_get_after_restart.txt");

        if (!Utils.isClientFileExists("test_purpose_get_after_restart.txt")) {
            return CheckResult.wrong("Can't find a file after getting it from the server by name.\n" +
                "Looks like your server lose all stored files after restart.\n" +
                "You should store all downloaded files from the server in /client/data/ folder.");
        }

        client = getClient();
        client.start();
        client.execute("1\n2\n" + id + "\ntest_purpose_get_by_id_after_restart.txt");

        if (!Utils.isClientFileExists("test_purpose_get_by_id_after_restart.txt")) {
            return CheckResult.wrong("Can't find a file after getting it from the server by ID.\n" +
                "Looks like your server lose all stored files after restart.\n" +
                "You should store all downloaded files from the server in /client/data/ folder.");
        }

        client = getClient();
        client.start();
        client.execute("exit");

        return CheckResult.correct();
    }

    CheckResult testDeleteFiles() {

        TestedProgram server = getServer();
        TestedProgram client = getClient();

        File folder = new File(serverDataPath);
        int numOfFilesBeforeDeleting = Utils.numExistingFiles(folder);

        server.startInBackground();
        client.start();
        client.execute("3\n1\ntest_purpose_newFile.txt");

        sleep(2000);
        int numOfFilesAfterDeletingByName = Utils.numExistingFiles(folder);
        if (numOfFilesBeforeDeleting == numOfFilesAfterDeletingByName) {
            return CheckResult.wrong("Once a client deleted a file by name from the server, " +
                "number of files in /server/data/ should be fewer!");
        }

        client = getClient();
        client.start();
        client.execute("3\n2\n" + id);

        sleep(2000);
        int numOfFilesAfterDeletingById = Utils.numExistingFiles(folder);
        if (numOfFilesAfterDeletingByName == numOfFilesAfterDeletingById) {
            return CheckResult.wrong("Once a client deleted a file by ID from the server, " +
                "number of files in /server/data/ should be fewer!");
        }

        client = getClient();
        client.start();
        client.execute("exit");

        return CheckResult.correct();
    }


    @AfterClass
    public static void afterTestDeleteFiles() {
        Utils.deleteTestFiles();
    }

    public static TestedProgram getClient() {
        return new TestedProgram("client");
    }

    public static TestedProgram getServer() {
        return new TestedProgram("server");
    }
}


