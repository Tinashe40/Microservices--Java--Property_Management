package com.tinash.cloud.utility.util.file;

import org.springframework.web.multipart.MultipartFile;

public final class FileValidator {

    private FileValidator() {
    }

    public static boolean isValid(MultipartFile file) {
        return file != null && !file.isEmpty();
    }
}
