package editor;

import com.formdev.flatlaf.*;

public class ApplicationRunner {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        new TextEditor();
    }
}
