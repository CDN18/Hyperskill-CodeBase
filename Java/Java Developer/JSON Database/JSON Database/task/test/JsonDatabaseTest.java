import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isArray;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isObject;

public class JsonDatabaseTest extends StageTest<String> {

    private static final String OK_STATUS = "OK";
    private static final String ERROR_STATUS = "ERROR";
    private static final String NO_SUCH_KEY_REASON = "No such key";

    private static final String WRONG_EXIT = "The server should stop when client sends 'exit' request";

    private static final String fileName = "data.json";
    private static final String serverFolderPath = System.getProperty("user.dir") + File.separator +
        "src" + File.separator +
        "server" + File.separator +
        "data";
    private static final String serverJsonFileName = serverFolderPath + File.separator + fileName;

    private static final String clientFolderPath = System.getProperty("user.dir") + File.separator +
        "src" + File.separator +
        "client" + File.separator +
        "data";

    private static final Gson gson = new Gson();

    private static final String setFileContent = JsonBuilder.newBuilder()
        .addValue("type", "set")
        .addValue("key", "person")
        .addJsonObject("value", JsonBuilder.newBuilder()
            .addValue("name", "Elon Musk")
            .addJsonObject("car",
                JsonBuilder.newBuilder()
                    .addValue("model", "Tesla Roadster")
                    .addValue("year", "2018")
                    .getAsJsonObject()
            )
            .addJsonObject("rocket",
                JsonBuilder.newBuilder()
                    .addValue("name", "Falcon 9")
                    .addValue("launches", "87")
                    .getAsJsonObject())
            .getAsJsonObject())
        .getAsString();

    private static final String getFileContent = JsonBuilder.newBuilder()
        .addValue("type", "get")
        .addValue("key", gson.fromJson("[person, name]", JsonArray.class))
        .getAsString();

    private static final String secondGetFileContent = JsonBuilder.newBuilder()
        .addValue("type", "get")
        .addValue("key", gson.fromJson("[person]", JsonArray.class))
        .getAsString();

    private static final String deleteFileContent = JsonBuilder.newBuilder()
        .addValue("type", "delete")
        .addValue("key", gson.fromJson("[person, car, year]", JsonArray.class))
        .getAsString();

    private static final String updateFileContent = JsonBuilder.newBuilder()
        .addValue("type", "set")
        .addValue("key", gson.fromJson("[person, rocket, launches]", JsonArray.class))
        .addValue("value", "88")
        .getAsString();

    private static int threadsCount;

    @DynamicTest(order = 1)
    CheckResult checkExit() {

        TestedProgram server = getServer();
        server.startInBackground();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TestedProgram client = getClient();
        client.start("-t", "exit");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!server.isFinished()) {
            server.stop();
            return CheckResult.wrong(WRONG_EXIT);
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 2)
    CheckResult checkJsonFile() throws IOException {

        if (!Files.exists(Paths.get(serverFolderPath))) {
            return CheckResult.wrong("Can't find /server/data folder");
        }

        if (!Files.exists(Paths.get(serverJsonFileName))) {
            return CheckResult.wrong("Can't find " + fileName + " file in the /server/data folder");
        }

        if (!Files.exists(Paths.get(clientFolderPath))) {
            return CheckResult.wrong("Can't find /client/data folder");
        }

        createJsonFiles();

        return CheckResult.correct();
    }

    private static void createJsonFiles() throws IOException {
        Files.write(Paths.get(clientFolderPath + File.separator + "setFile.json"),
            List.of(setFileContent),
            StandardCharsets.UTF_8);
        Files.write(Paths.get(clientFolderPath + File.separator + "getFile.json"),
            List.of(getFileContent),
            StandardCharsets.UTF_8);
        Files.write(Paths.get(clientFolderPath + File.separator + "secondGetFile.json"),
            List.of(secondGetFileContent),
            StandardCharsets.UTF_8);
        Files.write(Paths.get(clientFolderPath + File.separator + "deleteFile.json"),
            List.of(deleteFileContent),
            StandardCharsets.UTF_8);
        Files.write(Paths.get(clientFolderPath + File.separator + "updateFile.json"),
            List.of(updateFileContent),
            StandardCharsets.UTF_8);
    }

    @DynamicTest(order = 3)
    CheckResult testInputs() throws InterruptedException {
        
        threadsCount = getThreadCount();

        TestedProgram server = getServer();
        server.startInBackground();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TestedProgram client;
        String output;

        String requestJson;
        String responseJson;
        
        client = getClient();
        output = client.start("-t", "set", "-k", "1", "-v", "Hello world!");
        requestJson = JsonFinder.findRequestJsonObject(output);
        expect(requestJson)
            .asJson()
            .check(isObject()
                .value("type", "set")
                .value("key", "1")
                .value("value", "Hello world!")
            );
        responseJson = JsonFinder.findResponseJsonObject(output);
        expect(responseJson)
            .asJson()
            .check(isObject()
                .value("response", OK_STATUS)
            );

        // Files
        client = getClient();
        output = client.start("-in", "setFile.json");
        requestJson = JsonFinder.findRequestJsonObject(output);
        expect(requestJson)
            .asJson()
            .check(isObject()
                .value("key", "person")
                .value("value", isObject()
                    .value("name", "Elon Musk")
                    .value("car", isObject()
                        .value("model", "Tesla Roadster")
                        .anyOtherValues()
                    )
                    .anyOtherValues()
                )
                .anyOtherValues()
            );
        responseJson = JsonFinder.findResponseJsonObject(output);
        expect(responseJson)
            .asJson()
            .check(isObject()
                .value("response", OK_STATUS)
            );

        client = getClient();
        output = client.start("-in", "getFile.json");
        requestJson = JsonFinder.findRequestJsonObject(output);
        expect(requestJson)
            .asJson()
            .check(isObject()
                .value("type", "get")
                .value("key", isArray("person", "name"))
            );
        responseJson = JsonFinder.findResponseJsonObject(output);
        expect(responseJson)
            .asJson()
            .check(
                isObject()
                    .value("response", OK_STATUS)
                    .value("value", "Elon Musk")
            );


        client = getClient();
        output = client.start("-in", "updateFile.json");
        requestJson = JsonFinder.findRequestJsonObject(output);
        expect(requestJson)
            .asJson()
            .check(isObject()
                .value("type", "set")
                .value("key", isArray("person", "rocket", "launches"))
                .value("value", "88")
            );
        responseJson = JsonFinder.findResponseJsonObject(output);
        expect(responseJson)
            .asJson()
            .check(
                isObject()
                    .value("response", OK_STATUS)
            );


        client = getClient();
        output = client.start("-in", "secondGetFile.json");
        requestJson = JsonFinder.findRequestJsonObject(output);
        expect(requestJson)
            .asJson()
            .check(isObject()
                .value("type", "get")
                .value("key", isArray("person"))
            );
        responseJson = JsonFinder.findResponseJsonObject(output);
        expect(responseJson)
            .asJson()
            .check(
                isObject()
                    .value("response", OK_STATUS)
                    .value("value", isObject()
                        .value("rocket", isObject()
                            .value("name", "Falcon 9")
                            .value("launches", "88")
                        )
                        .anyOtherValues()
                    )
            );


        client = getClient();
        output = client.start("-in", "deleteFile.json");
        requestJson = JsonFinder.findRequestJsonObject(output);
        expect(requestJson)
            .asJson()
            .check(isObject()
                .value("type", "delete")
                .value("key", isArray("person", "car", "year"))
            );
        responseJson = JsonFinder.findResponseJsonObject(output);
        expect(responseJson)
            .asJson()
            .check(
                isObject()
                    .value("response", OK_STATUS)
            );


        client = getClient();
        output = client.start("-in", "secondGetFile.json");
        requestJson = JsonFinder.findRequestJsonObject(output);
        expect(requestJson)
            .asJson()
            .check(isObject()
                .value("type", "get")
                .value("key", isArray("person"))
            );
        responseJson = JsonFinder.findResponseJsonObject(output);
        expect(responseJson)
            .asJson()
            .check(
                isObject()
                    .value("response", OK_STATUS)
                    .value("value", isObject()
                        .value("rocket", isObject()
                            .value("name", "Falcon 9")
                            .value("launches", "88")
                        )
                        .value("car", isObject()
                            .value("model", "Tesla Roadster")
                        )
                        .anyOtherValues()
                    )
            );
        checkIfThreadWasCreated();


        client = getClient();
        client.start("-t", "exit");

        return CheckResult.correct();
    }

    private static TestedProgram getClient() {
        return new TestedProgram("client");
    }

    private static TestedProgram getServer() {
        return new TestedProgram("server");
    }

    private static int getThreadCount() {
        return (int) ManagementFactory.getThreadMXBean().getTotalStartedThreadCount();
    }

    private static void checkIfThreadWasCreated() {

        int total = getThreadCount();

        /*8 threads: 1 server thread and 7 client threads created during the test.
         If the server doesn't handle clients in a new thread then the difference between number of threads
         before and after the test should be equal 8 */
        if (total - threadsCount == 8) {
            throw new WrongAnswer("Looks like you don't process client connection in another thread.\n" +
                "Every client request should be parsed and handled in a separate thread!\n" +
                (total - threadsCount));
        }
    }

    @AfterClass
    public static void deleteFiles() {
        try {
            Files.delete(Paths.get(clientFolderPath + File.separator + "setFile.json"));
            Files.delete(Paths.get(clientFolderPath + File.separator + "getFile.json"));
            Files.delete(Paths.get(clientFolderPath + File.separator + "secondGetFile.json"));
            Files.delete(Paths.get(clientFolderPath + File.separator + "deleteFile.json"));
            Files.delete(Paths.get(clientFolderPath + File.separator + "updateFile.json"));
        } catch (IOException ignored) {
        }

        String filePath = serverFolderPath + File.separator + fileName;
        String tempFilePath = serverFolderPath + File.separator + "temp.json";

        try {
            Files.copy(Paths.get(tempFilePath), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            Files.delete(Paths.get(tempFilePath));
        } catch (IOException ignored) {
        }
    }

    @BeforeClass
    public static void copyFiles() {

        String filePath = serverFolderPath + File.separator + fileName;
        String tempFilePath = serverFolderPath + File.separator + "temp.json";

        try {
            Files.createFile(Paths.get(tempFilePath));
        } catch (IOException ignored) {
        }

        try {
            Files.copy(Paths.get(filePath), Paths.get(tempFilePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ignored) {
        }

        try {
            Files.write(Paths.get(filePath), "{}".getBytes());
        } catch (IOException ignored) {
        }
    }
}
