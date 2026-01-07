package com.proveritus.cloudutility.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class FileUtils {

    private FileUtils() {
    }

    public static String readFileToString(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }
}
