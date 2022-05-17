import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.text.MessageFormat.format;
import static java.util.function.Predicate.not;
import static org.hyperskill.hstest.testcase.CheckResult.correct;
import static org.hyperskill.hstest.testcase.CheckResult.wrong;

public class Scenario {
    private TestedProgram main;
    private String output = "";
    private final File dataFile;
    private final File scriptFile;

    Scenario(String name) {
        dataFile = new File("test/" + name + ".data.yaml");
        scriptFile = new File("test/" + name + ".script.yaml");
        System.out.println("Scenario '" + name + "' is started.");
        System.out.println();
    }

    CheckResult check() {

        final Object[][] data;
        final String[][] script;

        try {
            data = new YAMLMapper().readValue(dataFile, String[][].class);
            script = new YAMLMapper().readValue(scriptFile, String[][].class);
        } catch (IOException e) {
            e.printStackTrace();
            return wrong("Cannot find the files "
                    + dataFile.getName()
                    + " or "
                    + scriptFile.getName()
                    + ", looks like you deleted or renamed it. Please, revert the changes.");
        }

        for (var values : data) {
            for (var action : script) {
                final var command = action[0];
                switch (command) {
                    case "start":
                        main = new TestedProgram();
                        output = action.length == 1
                                ? main.start()
                                : main.start(format(action[1], values).split(" "));
                        output = output.trim().toLowerCase();
                        continue;
                    case "input":
                        output = main.execute(format(action[1], values)).trim().toLowerCase();
                        continue;
                    case "finish":
                        if (main.isFinished()) {
                            continue;
                        }
                        return wrong(format(action[1], values));
                    default:
                        final Map<String, Predicate<String>> validation = Map.of(
                                "contains", output::contains,
                                "not contains", not(output::contains),
                                "file exists", file -> new File(file).exists(),
                                "file delete", file -> new File(file).delete(),
                                "find", pattern -> Pattern.compile(pattern).matcher(output).find(),
                                "matches", output::matches);

                        final var expected = format(action[1], values);
                        if (validation.get(command).test(expected)) {
                            continue;
                        }
                        final var feedback = format(action[2], values) + System.lineSeparator()
                                + "Expected " + command + ": \"" + expected + "\"" + System.lineSeparator()
                                + "Actual data is: \"" + output + "\".";
                        return wrong(feedback);
                }
            }
        }
        return correct();
    }

}