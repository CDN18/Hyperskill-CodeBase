package editor;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static editor.Constants.DEFAULT_FONT;

public class TextEditor extends JFrame {

    // Components

    public static JFileChooser fileChooser;

    // - Menu
    public static JMenuBar menuBar;
    public static JMenu fileMenu;
    public static JMenuItem openFile;
    public static JMenuItem saveFile;
    public static JMenuItem exit;
    public static JMenu searchMenu;
    public static JMenuItem find;
    public static JMenuItem findPrev;
    public static JMenuItem findNext;
    public static JMenuItem useRegex;

    // - Operation Panel
    public static JPanel operationPanel;
    public static JPanel inputPanel;
    public static JPanel fileOpPanel;
    public static JPanel textOpPanel;
    public static JTextField inputField;
    public static JButton openButton;
    public static JButton saveButton;
    public static JButton findButton;
    public static JButton findNextButton;
    public static JButton findPrevButton;
    public static JCheckBox regexCheckBox;

    // - Text
    public static JScrollPane scrollableTextArea;
    public static JPanel textPanel;
    public static JTextComponent textArea;

    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        // setLayout(null);
        setTitle("Basic Text Editor");
        setIconImage(ResourceUtil.getImage("assets/logo.png"));

        // Stage 3 Menu Implemented
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        openFile = new JMenuItem("Open");
        openFile.setMnemonic(KeyEvent.VK_O);
        openFile.addActionListener(e -> FileUtil.loadFile());
        fileMenu.add(openFile);
        saveFile = new JMenuItem("Save");
        saveFile.setMnemonic(KeyEvent.VK_S);
        saveFile.addActionListener(e -> FileUtil.saveFile());
        fileMenu.add(saveFile);
        fileMenu.addSeparator();
        exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_X);
        exit.addActionListener(e -> this.dispose());
        fileMenu.add(exit);
        searchMenu = new JMenu("Search");
        searchMenu.setMnemonic(KeyEvent.VK_S);
        menuBar.add(searchMenu);
        find = new JMenuItem("Start search");
        find.setMnemonic(KeyEvent.VK_F);
        find.setActionCommand("findStart");
        find.addActionListener(e -> new findWorker(e.getActionCommand()).execute());
        searchMenu.add(find);
        findPrev = new JMenuItem("Previous search");
        findPrev.setMnemonic(KeyEvent.VK_P);
        findPrev.setActionCommand("findPrev");
        findPrev.addActionListener(e -> new findWorker(e.getActionCommand()).execute());
        searchMenu.add(findPrev);
        findNext = new JMenuItem("Next search");
        findNext.setMnemonic(KeyEvent.VK_N);
        findNext.setActionCommand("findNext");
        findNext.addActionListener(e -> new findWorker(e.getActionCommand()).execute());
        searchMenu.add(findNext);
        useRegex = new JMenuItem("Use regex");
        useRegex.setMnemonic(KeyEvent.VK_R);
        useRegex.addActionListener(e -> regexCheckBox.setSelected(!regexCheckBox.isSelected()));
        searchMenu.add(useRegex);

        //

        // Operation Panel

        operationPanel = new JPanel();
        operationPanel.setLayout(new FlowLayout());
        inputPanel = new JPanel();
        fileOpPanel = new JPanel();
        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(200, 25));
        openButton = new JButton();
        openButton.setPreferredSize(new Dimension(25, 25));
        openButton.setFocusable(false);
        openButton.setIcon(new ImageIcon(ResourceUtil.getImage("assets/load.png")
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        openButton.addActionListener(e -> FileUtil.loadFile());
        saveButton = new JButton();
        saveButton.setPreferredSize(new Dimension(25, 25));
        saveButton.setFocusable(false);
        saveButton.setIcon(new ImageIcon(ResourceUtil.getImage("assets/save.png")
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        saveButton.addActionListener(e -> FileUtil.saveFile());
        inputPanel.add(inputField);
        fileOpPanel.add(openButton);
        fileOpPanel.add(saveButton);
        textOpPanel = new JPanel();
        findButton = new JButton();
        findButton.setPreferredSize(new Dimension(25, 25));
        findButton.setFocusable(false);
        findButton.setIcon(new ImageIcon(ResourceUtil.getImage("assets/find.png")
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        findButton.setActionCommand("findStart");
        findButton.addActionListener(e -> new findWorker(e.getActionCommand()).execute());
        textOpPanel.add(findButton);
        findPrevButton = new JButton();
        findPrevButton.setPreferredSize(new Dimension(25, 25));
        findPrevButton.setFocusable(false);
        findPrevButton.setIcon(new ImageIcon(ResourceUtil.getImage("assets/prev.png")
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        findPrevButton.setActionCommand("findPrev");
        findPrevButton.addActionListener(e -> new findWorker(e.getActionCommand()).execute());
        textOpPanel.add(findPrevButton);
        findNextButton = new JButton();
        findNextButton.setPreferredSize(new Dimension(25, 25));
        findNextButton.setFocusable(false);
        findNextButton.setIcon(new ImageIcon(ResourceUtil.getImage("assets/next.png")
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        findNextButton.setActionCommand("findNext");
        findNextButton.addActionListener(e -> new findWorker(e.getActionCommand()).execute());
        textOpPanel.add(findNextButton);
        regexCheckBox = new JCheckBox("Use regex");
        regexCheckBox.setFocusable(false);
        textOpPanel.add(regexCheckBox);
        operationPanel.add(fileOpPanel);
        operationPanel.add(inputPanel);
        operationPanel.add(textOpPanel);
        add(operationPanel, BorderLayout.NORTH);

        fileChooser = new JFileChooser();
        fileChooser.setVisible(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        add(fileChooser, BorderLayout.CENTER);

        // Text Panel
        textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        add(textPanel);
        textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setFont(DEFAULT_FONT);
        scrollableTextArea = new JScrollPane(textArea);
        textPanel.add(scrollableTextArea, BorderLayout.CENTER);

        // Set name for testing purposes
        textArea.setName("TextArea");
        inputField.setName("SearchField");
        openButton.setName("OpenButton");
        saveButton.setName("SaveButton");
        findButton.setName("StartSearchButton");
        findPrevButton.setName("PreviousMatchButton");
        findNextButton.setName("NextMatchButton");
        regexCheckBox.setName("UseRegExCheckbox");
        fileChooser.setName("FileChooser");
        scrollableTextArea.setName("ScrollPane");
        fileMenu.setName("MenuFile");
        searchMenu.setName("MenuSearch");
        openFile.setName("MenuOpen");
        saveFile.setName("MenuSave");
        exit.setName("MenuExit");
        find.setName("MenuStartSearch");
        findPrev.setName("MenuPreviousMatch");
        findNext.setName("MenuNextMatch");
        useRegex.setName("MenuUseRegExp");

        setVisible(true);
    }
}

class FontUtil {
    public static void setGlobalFont(Font font) {
        FontUIResource fontResource = new FontUIResource(font);
        for(Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if(value instanceof FontUIResource) {
                System.out.println(key);
                UIManager.put(key, fontResource);
            }
        }
    }
}

class FileUtil {
    public static void loadFile() {
        // TextEditor.fileChooser.setDialogTitle("Open file");
        TextEditor.fileChooser.setVisible(true);
        int result = TextEditor.fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = TextEditor.fileChooser.getSelectedFile();
            try {
                String text = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                TextEditor.textArea.setText(text);
                TextEditor.inputField.setText(file.getName());
                System.out.println("Loaded file: " + file.getPath());
            } catch (IOException e) {
                TextEditor.textArea.setText("");
                System.err.println("Error when loading file: " + e.getMessage());
            }
        }
    }

    public static void saveFile() {
        // TextEditor.fileChooser.setDialogTitle("Save file");

        TextEditor.fileChooser.setVisible(true);
        // System.out.println("DEBUG: Showing: " + TextEditor.fileChooser.isShowing() + " Visible:" + TextEditor.fileChooser.isVisible());
        int result = TextEditor.fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = TextEditor.fileChooser.getSelectedFile();
            try {
                Files.write(Paths.get(file.getAbsolutePath()), TextEditor.textArea.getText().getBytes());
                System.out.println("Saved file: " + file.getPath());
            } catch (IOException e) {
                System.err.println("Error when saving file: " + e.getMessage());
            }
        }
    }
}

class TextUtil {
    static String wd = "";
    static boolean regex = false;
    static List<Integer> indexList = new ArrayList<>();
    static List<Integer> wdLengthList = new ArrayList<>();
    static int index = -1;

    private static void updateWd() {
        wd = TextEditor.inputField.getText();

        regex = TextEditor.regexCheckBox.isSelected();
        index = -1;
        indexList.clear();
        wdLengthList.clear();
        String content = TextEditor.textArea.getText();
        if (regex) {
            Pattern pattern = Pattern.compile(wd);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                indexList.add(matcher.start());
                wdLengthList.add(matcher.group().length());
            }
        } else {
            int index = content.indexOf(wd);
            while (index != -1 && index + 1 < content.length()) {
                indexList.add(index);
                index = content.indexOf(wd, index + 1);
            }
        }
    }

    public static void findStart() {
        if (TextEditor.inputField.getText().isEmpty()) {
            return;
        }
        if (!TextEditor.inputField.getText().equals(wd)) {
            updateWd();
        } else {
            index = -1;
        }
        findNext();
    }

    public static void findNext() {
        if (TextEditor.inputField.getText().isEmpty()) {
            return;
        }
        if (!TextEditor.inputField.getText().equals(wd)) {
            updateWd();
        }
        if (index == indexList.size() - 1) {
            index = -1;
        }
        int startIndex = indexList.get(++index);
        int endIndex = startIndex + (regex ? wdLengthList.get(index) : wd.length());
        TextEditor.textArea.setCaretPosition(endIndex);
        // System.out.println("[DEBUG] Thread :" + Thread.currentThread().getName() + ": " + index);
        System.out.println("[DEBUG] Word: " + wd + " Index: " + index + " Start: " + startIndex + " End: " + endIndex);
        System.out.println("[DEBUG]Caret position: " + TextEditor.textArea.getCaretPosition());
        TextEditor.textArea.select(startIndex, regex ? startIndex + wdLengthList.get(index) : startIndex + wd.length());
        TextEditor.textArea.grabFocus();
    }

    public static void findPrev() {
        if (TextEditor.inputField.getText().isEmpty()) {
            return;
        }
        if (!TextEditor.inputField.getText().equals(wd)) {
            updateWd();
        }
        if (index == -1 || index == 0) {
            index = indexList.size();
        }
        int startIndex = indexList.get(--index);
        TextEditor.textArea.setCaretPosition(startIndex);
        TextEditor.textArea.select(startIndex, regex ? startIndex + wdLengthList.get(index) : startIndex + wd.length());
        TextEditor.textArea.grabFocus();
    }
}

class findWorker extends SwingWorker<Void, Void> {

    private final String command;

    findWorker(String actionCommand) {
        this.command = actionCommand;
    }

    @Override
    protected Void doInBackground() {
        switch (command) {
            case "findStart":
                TextUtil.findStart();
                break;
            case "findNext":
                TextUtil.findNext();
                break;
            case "findPrev":
                TextUtil.findPrev();
                break;
        }
        return null;
    }
}

class ResourceUtil {
    static Toolkit toolkit = Toolkit.getDefaultToolkit();
    public static Image getImage(String path) {
        Image result;
        File file = new File(path);
        if (file.exists()) {
            result = new ImageIcon(path).getImage();
        } else {
            URL iconUrl = TextEditor.class.getResource("/" + path);
            result = toolkit.getImage(iconUrl);
        }
        return result;
    }
}
