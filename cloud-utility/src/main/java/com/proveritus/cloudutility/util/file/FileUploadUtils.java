package com.proveritus.cloudutility.util.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public final class FileUploadUtils {

    private FileUploadUtils() {
    }

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdir();
        }
        multipartFile.transferTo(new File(uploadDir + fileName));
    }
}
