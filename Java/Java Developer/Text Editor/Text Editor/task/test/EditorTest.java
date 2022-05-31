import editor.TextEditor;
import org.assertj.swing.fixture.*;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import org.junit.After;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;

import static org.hyperskill.hstest.testcase.CheckResult.correct;

class OsCheck {
    /**
     * types of Operating Systems
     */
    public enum OSType {
        Windows, MacOS, Linux, Other
    };

    // cached result of OS detection
    protected static OSType detectedOS;

    public static OSType getOperatingSystemType() {
        if (detectedOS == null) {
            String OS = System.getProperty(
                "os.name", "generic")
                .toLowerCase(Locale.ENGLISH);
            if ((OS.contains("mac"))
                || (OS.contains("darwin"))) {
                detectedOS = OSType.MacOS;
            } else if (OS.contains("win")) {
                detectedOS = OSType.Windows;
            } else if (OS.contains("nux")) {
                detectedOS = OSType.Linux;
            } else {
                detectedOS = OSType.Other;
            }
        }
        return detectedOS;
    }
}

public class EditorTest extends SwingTest {
    public EditorTest() {
        super(new TextEditor());
    }

    static boolean notWindows =
        OsCheck.getOperatingSystemType() != OsCheck.OSType.Windows;

    private String dir = System.getProperty("user.dir") + File.separator;
    private File fileDir = new File(dir);

    @SwingComponent JTextComponentFixture textArea;
    @SwingComponent JTextComponentFixture searchField;
    @SwingComponent JButtonFixture saveButton;
    @SwingComponent JButtonFixture openButton;
    @SwingComponent JButtonFixture startSearchButton;
    @SwingComponent JButtonFixture previousMatchButton;
    @SwingComponent JButtonFixture nextMatchButton;
    @SwingComponent JCheckBoxFixture useRegExCheckbox;
    @SwingComponent JScrollPaneFixture scrollPane;
    @SwingComponent JMenuItemFixture menuFile;
    @SwingComponent JMenuItemFixture menuSearch;
    @SwingComponent JMenuItemFixture menuOpen;
    @SwingComponent JMenuItemFixture menuSave;
    @SwingComponent JMenuItemFixture menuExit;
    @SwingComponent JMenuItemFixture menuStartSearch;
    @SwingComponent JMenuItemFixture menuPreviousMatch;
    @SwingComponent JMenuItemFixture menuNextMatch;
    @SwingComponent JMenuItemFixture menuUseRegExp;
    @SwingComponent JFileChooserFixture fileChooser;

    String filename1 = "SomeFile.txt";
    String filename2 = "AnotherFile.txt";
    String noExistFile = "FileDoesNotExist";

    String searchText = "Sonnet";
    String regExSearchText = "[fr]uel";

    String textToSave1 = "Basic text editor\nType here too\nHere also\n\n";
    String textToSave2 = "          Sonnet I\n" +
        "\n" +
        "\n" +
        "FROM fairest creatures we desire increase,\n" +
        "That thereby beauty's rose might never die,\n" +
        "But as the riper should by time decease,\n" +
        "His tender heir might bear his memory:\n" +
        "But thou, contracted to thine own bright eyes,\n" +
        "Feed'st thy light'st flame with self-substantial fuel,\n" +
        "Making a famine where abundance lies,\n" +
        "Thyself thy foe, to thy sweet self too cruel.\n" +
        "Thou that art now the world's fresh ornament\n" +
        "And only herald to the gaudy spring,\n" +
        "Within thine own bud buriest thy content\n" +
        "And, tender churl, makest waste in niggarding.\n" +
        "Pity the world, or else this glutton be,\n" +
        "To eat the world's due, by the grave and thee.\n" +
        "\n" +
        " Sonnet II\n" +
        "       \n" +
        "         \n" +
        "When forty winters shall beseige thy brow,\n" +
        "And dig deep trenches in thy beauty's field,\n" +
        "Thy youth's proud livery, so gazed on now,\n" +
        "Will be a tatter'd weed, of small worth held:\n" +
        "Then being ask'd where all thy beauty lies,\n" +
        "Where all the treasure of thy lusty days,\n" +
        "To say, within thine own deep-sunken eyes,\n" +
        "Were an all-eating shame and thriftless praise.\n" +
        "How much more praise deserved thy beauty's use,\n" +
        "If thou couldst answer 'This fair child of mine\n" +
        "Shall sum my count and make my old excuse,'\n" +
        "Proving his beauty by succession thine!\n" +
        "This were to be new made when thou art old,\n" +
        "And see thy blood warm when thou feel'st it cold.\n" +
        "\n" +
        "Sonnet III\n" +
        "\n" +
        "\n" +
        "Look in thy glass, and tell the face thou viewest\n" +
        "Now is the time that face should form another;\n" +
        "Whose fresh repair if now thou not renewest,\n" +
        "Thou dost beguile the world, unbless some mother.\n" +
        "For where is she so fair whose unear'd womb\n" +
        "Disdains the tillage of thy husbandry?\n" +
        "Or who is he so fond will be the tomb\n" +
        "Of his self-love, to stop posterity?\n" +
        "Thou art thy mother's glass, and she in thee\n" +
        "Calls back the lovely April of her prime:\n" +
        "So thou through windows of thine age shall see\n" +
        "Despite of wrinkles this thy golden time.\n" +
        "But if thou live, remember'd not to be,\n" +
        "Die single, and thine image dies with thee.\n" +
        "\n" +
        "Sonnet IV\n" +
        "\n" +
        "\n" +
        "Unthrifty loveliness, why dost thou spend\n" +
        "Upon thyself thy beauty's legacy?\n" +
        "Nature's bequest gives nothing but doth lend,\n" +
        "And being frank she lends to those are free.\n" +
        "Then, beauteous niggard, why dost thou abuse\n" +
        "The bounteous largess given thee to give?\n" +
        "Profitless usurer, why dost thou use\n" +
        "So great a sum of sums, yet canst not live?\n" +
        "For having traffic with thyself alone,\n" +
        "Thou of thyself thy sweet self dost deceive.\n" +
        "Then how, when nature calls thee to be gone,\n" +
        "What acceptable audit canst thou leave?\n" +
        "Thy unused beauty must be tomb'd with thee,\n" +
        "Which, used, lives th' executor to be.";

    public void fileAction() {
        if(!fileChooser.target().isVisible()) {
            throw new AssertionError();
        }
        frame.setVisible(false);
        fileChooser.setCurrentDirectory(fileDir);

        if (OsCheck.getOperatingSystemType() == OsCheck.OSType.MacOS) {
            fileChooser.selectFile(new File(
                fileDir + File.separator + searchField.text()));
        } else {
            fileChooser.fileNameTextBox().setText(searchField.text());
        }

        fileChooser.approve();
        frame.setVisible(true);
    }

    @DynamicTest
    CheckResult test1() {
        requireEditable(textArea);
        requireEmpty(textArea, searchField);
        requireEnabled(
            saveButton, openButton,
            startSearchButton, nextMatchButton, previousMatchButton,
            menuOpen, menuSave, menuSave, menuFile, menuExit,
            menuStartSearch, menuPreviousMatch, menuNextMatch, menuUseRegExp
        );
        return correct();
    }

    @DynamicTest(feedback = "FileChooser doesn't appear on the second " +
        "press on SaveButton but should appear every time")
    CheckResult test2() {
        if (notWindows) {
            return correct();
        }

        searchField.setText(filename1);
        textArea.setText(textToSave1);

        saveButton.click();

        try {
            fileAction();
        } catch (IllegalStateException ex) {
            throw new AssertionError();
        }

        return correct();
    }

    @DynamicTest(feedback = "Text should be the same after saving and loading same file")
    CheckResult test3() {
        if (notWindows) {
            return correct();
        }

        String[] texts = {textToSave2, textToSave1};
        String[] files = {filename1, filename2};

        for (int i = 0; i < 2; i++) {

            String text = texts[i];
            String file = files[i];

            searchField.setText("");
            textArea.setText("");

            searchField.setText(file);
            textArea.setText(text);

            saveButton.click();
            fileAction();

            searchField.setText("");
            textArea.setText("");

            searchField.setText(file);
            openButton.click();
            fileAction();

            textArea.requireText(text);
        }

        return correct();
    }

    @DynamicTest(feedback = "TextArea should be empty if user tries to load file that doesn't exist")
    CheckResult test4() {
        if (notWindows) {
            return correct();
        }

        textArea.setText(textToSave1);
        searchField.setText(noExistFile);

        openButton.click();
        fileAction();
        textArea.requireText("");

        return correct();
    }

    @DynamicTest(feedback = "TextArea should correctly save and load an empty file")
    CheckResult test5() {
        if (notWindows) {
            return correct();
        }

        textArea.setText("");
        searchField.setText(filename1);

        saveButton.click();
        fileAction();
        textArea.setText(textToSave2);
        openButton.click();
        fileAction();
        textArea.requireText("");

        return correct();
    }

    // menu-related tests for save and load

    @DynamicTest(feedback = "Text should be the same after saving " +
        "and loading same file using MenuLoad")
    CheckResult test6() {
        if (notWindows) {
            return correct();
        }

        String[] texts = {textToSave2, textToSave1};
        String[] files = {filename1, filename2};

        for (int i = 0; i < 2; i++) {

            String text = texts[i];
            String file = files[i];

            searchField.setText("");
            textArea.setText("");

            searchField.setText(file);
            textArea.setText(text);

            menuSave.click();
            fileAction();

            searchField.setText("");
            textArea.setText("");

            searchField.setText(file);
            menuOpen.click();
            fileAction();

            textArea.requireText(text);
        }

        return correct();
    }

    @DynamicTest(feedback = "TextArea should be empty if user tries to " +
        "load file that doesn't exist using MenuLoad")
    CheckResult test7() {
        if (notWindows) {
            return correct();
        }

        textArea.setText(textToSave1);
        searchField.setText(noExistFile);

        menuOpen.click();
        fileAction();
        textArea.requireText("");

        return correct();
    }

    @DynamicTest(feedback = "TextArea should correctly save " +
        "and load an empty file using menu")
    CheckResult test8() {
        if (notWindows) {
            return correct();
        }

        textArea.setText("");
        searchField.setText(filename1);

        menuSave.click();
        fileAction();
        textArea.setText(textToSave2);
        menuOpen.click();
        fileAction();
        textArea.requireText("");

        return correct();
    }


    // search related tests

    @DynamicTest
    CheckResult test9() {
        searchField.setText(searchText);
        textArea.setText(textToSave2);
        startSearchButton.click();

        if (!Objects.equals(textArea.target().getSelectedText(), searchText)) {
            throw new WrongAnswer("After clicking StartSearchButton should " +
                "be selected founded text");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test10() {
        searchField.setText(searchText);
        textArea.setText(textToSave2);
        startSearchButton.click();
        nextMatchButton.click();

        if (!Objects.equals(textArea.target().getSelectedText(), searchText)) {
            throw new WrongAnswer("After clicking NextMatchButton should " +
                "be selected founded text");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test11() {
        searchField.setText(searchText);
        textArea.setText(textToSave2);
        startSearchButton.click();
        previousMatchButton.click();

        if (!Objects.equals(textArea.target().getSelectedText(), searchText)) {
            throw new WrongAnswer("After clicking PreviousMatchButton should " +
                "be selected founded text");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test12() {
        searchField.setText(searchText);
        textArea.setText(textToSave2);
        menuStartSearch.click();

        if (!Objects.equals(textArea.target().getSelectedText(), searchText)) {
            throw new WrongAnswer("After clicking MenuStartSearch should " +
                "be selected founded text");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test13() {
        searchField.setText(searchText);
        textArea.setText(textToSave2);
        startSearchButton.click();
        menuNextMatch.click();

        if (!Objects.equals(textArea.target().getSelectedText(), searchText)) {
            throw new WrongAnswer("After clicking MenuNextMatch should " +
                "be selected founded text");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test14() {
        searchField.setText(searchText);
        textArea.setText(textToSave2);
        startSearchButton.click();
        menuPreviousMatch.click();

        if (!Objects.equals(textArea.target().getSelectedText(), searchText)) {
            throw new WrongAnswer("After clicking MenuPreviousMatch should " +
                "be selected founded text");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test15() {
        searchField.setText(searchText);
        textArea.setText(textToSave2);

        startSearchButton.click();

        // confirmed with working program AND provided text
        if (textArea.target().getCaretPosition() != 16) {
            throw new WrongAnswer("Wrong caret position after clicking StartSearchButton " +
                "(should be at the end of founded text)");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test16() {
        searchField.setText(searchText);
        textArea.setText(textToSave2);

        menuStartSearch.click();

        // confirmed with working program AND provided text
        if (textArea.target().getCaretPosition() != 16) {
            throw new WrongAnswer("Wrong caret position after clicking MenuStartSearch " +
                "(should be at the end of founded text)");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test17() {
        searchField.setText(searchText);
        textArea.setText(textToSave2);

        startSearchButton.click();
        nextMatchButton.click();

        // confirmed with working program AND provided text
        if (textArea.target().getCaretPosition() != 640) {
            throw new WrongAnswer("Wrong caret position after clicking NextMatchButton");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test18() {
        searchField.setText(searchText);
        textArea.setText(textToSave2);

        menuStartSearch.click();
        menuNextMatch.click();

        // confirmed with working program AND provided text
        if (textArea.target().getCaretPosition() != 640) {
            throw new WrongAnswer("Wrong caret position after clicking MenuNextMatch");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test19() {
        searchField.setText(searchText);
        textArea.setText(textToSave2);

        startSearchButton.click();
        previousMatchButton.click();

        // confirmed with working program AND provided text
        if (textArea.target().getCaretPosition() != 1921) {
            throw new WrongAnswer("Wrong caret position after clicking PreviousMatchButton");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test20() {
        searchField.setText(searchText);
        textArea.setText(textToSave2);

        startSearchButton.click();
        menuPreviousMatch.click();

        // confirmed with working program AND provided text
        if (textArea.target().getCaretPosition() != 1921) {
            throw new WrongAnswer("Wrong caret position after clicking MenuPreviousMatch");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test21() {
        useRegExCheckbox.check(true);
        if (!useRegExCheckbox.target().isSelected()) {
            throw new WrongAnswer( "Checkbox is not clickable");
        }
        return correct();
    }

    @DynamicTest
    CheckResult test22() {
        useRegExCheckbox.check(false);
        menuUseRegExp.click();
        if (!useRegExCheckbox.target().isSelected()) {
            throw new WrongAnswer( "MenuUseRegExp does't work");
        }
        return correct();
    }

    @DynamicTest
    CheckResult test23() {
        searchField.setText(regExSearchText);
        textArea.setText(textToSave2);

        startSearchButton.click();

        // confirmed with working program AND provided text
        if (textArea.target().getCaretPosition() != 288) {
            throw new WrongAnswer("Wrong caret position after clicking StartSearchButton " +
                "using regular expression");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test24() {
        searchField.setText(regExSearchText);
        textArea.setText(textToSave2);

        menuStartSearch.click();
        nextMatchButton.click();

        // confirmed with working program AND provided text
        if (textArea.target().getCaretPosition() != 372) {
            throw new WrongAnswer("Wrong caret position after clicking NextMatchButton " +
                "using regular expression");
        }

        return correct();
    }

    @DynamicTest
    CheckResult test25() {
        searchField.setText(regExSearchText);
        textArea.setText(textToSave2);

        menuStartSearch.click();
        previousMatchButton.click();

        // confirmed with working program AND provided text
        if (textArea.target().getCaretPosition() != 372) {
            throw new WrongAnswer("Wrong caret position after clicking PreviousMatchButton " +
                "using regular expression");
        }

        return correct();
    }

    @After
    public void deleteFiles() {
        try {
            Files.deleteIfExists(Paths.get(filename1));
            Files.deleteIfExists(Paths.get(filename2));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
