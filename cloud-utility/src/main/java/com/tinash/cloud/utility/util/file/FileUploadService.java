package com.tinash.cloud.utility.util.file;

import com.tinash.cloud.utility.util.file.config.FileUploadProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final FileUploadProperties fileUploadProperties;

    /**
     * Uploads a file to the configured local storage directory.
     *
     * @param file The MultipartFile to upload.
     * @return FileUploadResponse containing details of the uploaded file.
     * @throws FileUploadException if the file cannot be stored or if validation fails.
     */
    public FileUploadResponse uploadFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new FileUploadException("Original filename is missing.");
        }

        // Use FileUploadUtils to save the file and get the secure file name
        String secureFileName = FileUploadUtils.saveFile(
                fileUploadProperties.getUploadDir(),
                originalFileName,
                file,
                fileUploadProperties
        );

        // Build the file download URI (example - consuming service needs to expose an endpoint)
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/") // Assuming a download endpoint
                .path(secureFileName)
                .toUriString();

        return new FileUploadResponse(
                secureFileName,
                fileDownloadUri,
                file.getContentType(),
                file.getSize()
        );
    }

    /**
     * Retrieves the file path for a given filename.
     *
     * @param fileName The name of the file to retrieve.
     * @return The Path to the file.
     */
    public Path getFilePath(String fileName) {
        return FileUploadUtils.resolveFilePath(fileUploadProperties.getUploadDir(), fileName);
    }
}
