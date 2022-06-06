import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.List;
import java.util.function.Function;


class TestClue {

    Function<String, CheckResult> callback;

    TestClue(Function<String, CheckResult> callback) {
        this.callback = callback;
    }
}


public class ContactsTest extends StageTest<TestClue> {

    private CheckResult splitActionsFeedback(int actualSize, int needSize) {
        if (actualSize < needSize) {
            return CheckResult.wrong(String.format("This test should contain at least %d actions, but you have only %d. " +
                            "You should separate your actions with an empty line.",
                    needSize,
                    actualSize));
        } else {
            return null;
        }
    }
    
    @Override
    public List<TestCase<TestClue>> generate() {
        return List.of (
            new TestCase<TestClue>()
                .setInput("exit")
                .setAttach(new TestClue(output -> {
                    output = output.strip().toLowerCase();
                    if (!output.contains("enter action")) {
                        return new CheckResult(false,
                            "No \"Enter action\" substring found in the output");
                    }
                    return CheckResult.correct();
                })),

            new TestCase<TestClue>()
                .setInput(
                    "count\n" +
                    "exit")
                .setAttach(new TestClue(output -> {
                    output = output.strip().toLowerCase();
                    if (!output.contains("0 records")) {
                        return new CheckResult(false,
                            "No \"0 records\" substring found in the output. " +
                                    "If you already have the database, try to delete it.");
                    }
                    return CheckResult.correct();
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "person\n" +
                        "John\n" +
                        "Smith\n" +
                        "\n" +
                        "\n" +
                        "123 456 789\n" +
                    "count\n" +
                    "exit")
                .setAttach(new TestClue(output -> {
                    output = output.strip().toLowerCase();
                    if (output.contains("0 records")) {
                        return new CheckResult(false,
                            "Can't add the person");
                    }
                    return CheckResult.correct();
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "person\n" +
                        "John\n" +
                        "Smith\n" +
                        "\n" +
                        "\n" +
                        "123 456 789\n" +
                    "list\n" +
                    "1\n" +
                    "menu\n" +
                    "exit")
                .setAttach(new TestClue(output -> {
                    String[] blocks = output.strip().split("(\\s*\\n\\s*){2,}");
                    var feedback = splitActionsFeedback(blocks.length, 3);
                    if (feedback != null) return feedback;

                    for (String infoBlock : blocks) {
                        if (infoBlock.contains("Name: John")
                                && infoBlock.contains("Surname: Smith")
                                && infoBlock.contains("Birth date: [no data]")
                                && infoBlock.contains("Gender: [no data]")
                                && infoBlock.contains("Number: 123 456 789")
                                && infoBlock.contains("Time created:")
                                && infoBlock.contains("Time last edit:")) {
                            return CheckResult.correct();
                        }
                    }
                    return new CheckResult(false,
                            "Something wrong with printing user info");
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "organization\n" +
                        "Pizza Shop\n" +
                        "Wall St. 1\n" +
                        "+0 (123) 456-789-9999\n" +
                    "list\n" +
                    "1\n" +
                    "menu\n" +
                    "exit")
                .setAttach(new TestClue(output -> {
                    String[] blocks = output.strip().split("(\\s*\\n\\s*){2,}");
                    var feedback = splitActionsFeedback(blocks.length, 3);
                    if (feedback != null) return feedback;

                    for (String infoBlock : blocks) {
                        if (infoBlock.contains("Organization name: Pizza Shop")
                                && infoBlock.contains("Address: Wall St. 1")
                                && infoBlock.contains("Number: +0 (123) 456-789-9999")
                                && infoBlock.contains("Time created:")
                                && infoBlock.contains("Time last edit:")) {
                            return CheckResult.correct();
                        }
                    }
                    return new CheckResult(false,
                            "Something wrong with printing organization info");
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "person\n" +
                        "John\n" +
                        "Smith\n" +
                        "\n" +
                        "\n" +
                        "123 456 789\n" +
                    "list\n" +
                    "1\n" +
                    "edit\n" +
                        "gender\n" +
                        "M\n" +
                    "menu\n" +
                    "list\n" +
                    "1\n" +
                    "menu\n" +
                    "exit")
                .setAttach(new TestClue(output -> {
                    String[] blocks = output.strip().split("(\\s*\\n\\s*){2,}");
                    var feedback = splitActionsFeedback(blocks.length, 6);
                    if (feedback != null) return feedback;

                    for (String infoBlock : blocks) {
                        if (infoBlock.contains("Name: John")
                                && infoBlock.contains("Surname: Smith")
                                && infoBlock.contains("Birth date: [no data]")
                                && infoBlock.contains("Gender: M")
                                && infoBlock.contains("Number: 123 456 789")
                                && infoBlock.contains("Time created:")
                                && infoBlock.contains("Time last edit:")) {
                            return CheckResult.correct();
                        }
                    }
                    return new CheckResult(false,
                            "Editing person is not working");
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "organization\n" +
                        "Pizza Shop\n" +
                        "Wall St. 1\n" +
                        "+0 (123) 456-789-9999\n" +
                    "list\n" +
                    "1\n" +
                    "edit\n" +
                        "address\n" +
                        "Wall St 2\n" +
                    "menu\n" +
                    "list\n" +
                    "1\n" +
                    "menu\n" +
                    "exit")
                .setAttach(new TestClue(output -> {
                    String[] blocks = output.strip().split("(\\s*\\n\\s*){2,}");
                    var feedback = splitActionsFeedback(blocks.length, 6);
                    if (feedback != null) return feedback;

                    for (String infoBlock : blocks) {
                        if (infoBlock.contains("Organization name: Pizza Shop")
                                && infoBlock.contains("Address: Wall St 2")
                                && infoBlock.contains("Number: +0 (123) 456-789-9999")
                                && infoBlock.contains("Time created:")
                                && infoBlock.contains("Time last edit:")) {
                            return CheckResult.correct();
                        }
                    }
                    return new CheckResult(false,
                            "Editing organization is not working");
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "organization\n" +
                        "Pizza Shop\n" +
                        "Wall St. 1\n" +
                        "+0 (123) 456-789-9999\n" +
                    "add\n" +
                        "person\n" +
                        "John\n" +
                        "Smith\n" +
                        "\n" +
                        "\n" +
                        "123 456 789\n" +
                    "add\n" +
                        "organization\n" +
                        "PizzaNuts\n" +
                        "Wall St. 6\n" +
                        "+0 (123) 456-789-9999\n" +
                    "search\n" +
                        "pizz\n" +
                    "1\n" +
                    "menu\n" +
                    "exit")
                .setAttach(new TestClue(output -> {
                    String[] blocks = output.strip().split("(\\s*\\n\\s*){2,}");
                    var feedback = splitActionsFeedback(blocks.length, 4);
                    if (feedback != null) return feedback;
    
                    
                    for (String infoBlock : blocks) {
                        if (infoBlock.contains("Pizza Shop")
                                && infoBlock.contains("PizzaNuts")
                                && !infoBlock.contains("John")) {
                            return CheckResult.correct();
                        }
                    }
                    return new CheckResult(false,
                            "Search is not working");
                })),

            new TestCase<TestClue>()
                .setInput(
                    "add\n" +
                        "organization\n" +
                        "Pizza Shop\n" +
                        "Wall St. 1\n" +
                        "+0 (123) 456-789-9999\n" +
                    "add\n" +
                        "person\n" +
                        "John\n" +
                        "Smith\n" +
                        "\n" +
                        "\n" +
                        "123 456 789\n" +
                    "add\n" +
                        "organization\n" +
                        "PizzaNuts\n" +
                        "Wall St. 6\n" +
                        "+0 (123) 456-789-9999\n" +
                    "search\n" +
                        "s\n" +
                    "1\n" +
                    "menu\n" +
                    "exit")
                .setAttach(new TestClue(output -> {
                    String[] blocks = output.strip().split("(\\s*\\n\\s*){2,}");
                    var feedback = splitActionsFeedback(blocks.length, 4);
                    if (feedback != null) return feedback;

                    for (String infoBlock : blocks) {
                        if (infoBlock.contains("Pizza Shop")
                                && infoBlock.contains("John Smith")
                                && infoBlock.contains("PizzaNuts")) {
                            return CheckResult.correct();
                        }
                    }
                    return new CheckResult(false,
                            "Search is not working");
                })),

        new TestCase<TestClue>()
            .setInput(
                "add\n" +
                    "organization\n" +
                    "Pizza Shop\n" +
                    "Wall St. 1\n" +
                    "+0 (123) 456-789-9999\n" +
                "add\n" +
                    "person\n" +
                    "John\n" +
                    "Smith\n" +
                    "\n" +
                    "\n" +
                    "123 456 789\n" +
                "add\n" +
                    "organization\n" +
                    "PizzaNuts\n" +
                    "Wall St. 6\n" +
                    "+0 (123) 456-781-9999\n" +
                "search\n" +
                    "789\n" +
                "1\n" +
                "menu\n" +
                "exit")
            .setAttach(new TestClue(output -> {
                String[] blocks = output.strip().split("(\\s*\\n\\s*){2,}");
                var feedback = splitActionsFeedback(blocks.length, 4);
                if (feedback != null) return feedback;

                for (String infoBlock : blocks) {
                    if (infoBlock.contains("Pizza Shop")
                            && infoBlock.contains("John Smith")
                            && !infoBlock.contains("PizzaNuts")) {
                        return CheckResult.correct();

                    } else if (infoBlock.contains("+0 (123) 456-789-9999")
                            && infoBlock.contains("123 456 789")
                            && !infoBlock.contains("+0 (123) 456-781-9999")) {
                        return CheckResult.correct();
                    }
                }
                return new CheckResult(false,
                        "Search by phone number is not working");
            }))
        );
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {
        try {
            return clue.callback.apply(reply);
        }
        catch (Exception ex) {
            return new CheckResult(false, "Can't check the answer");
        }
    }
}
