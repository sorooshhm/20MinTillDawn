package com.tillDawn.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileSaver {
    public static void saveFile(File selectedFile, String targetFolder) {
        File targetFile = new File(targetFolder, selectedFile.getName());
        try {
            Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File saved to: " + targetFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
