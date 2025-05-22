package com.tillDawn.utilities;

import javax.swing.*;
import java.io.File;

public class FileExplorer {
    public static File openFileExplorer() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}
