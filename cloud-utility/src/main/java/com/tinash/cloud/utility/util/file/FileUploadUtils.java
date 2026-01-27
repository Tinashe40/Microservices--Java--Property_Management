package com.tinash.cloud.utility.util.file;

import com.tinash.cloud.utility.util.file.config.FileUploadProperties;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public final class FileUploadUtils {

    private FileUploadUtils() {
    }

    /**
     * Saves a multipart file to the specified upload directory with a secure, unique filename.
     * Performs basic validation (file empty, size, allowed types).
     *
     * @param uploadDir The base directory to save the file.
     * @param fileName The original file name (used to extract extension).
     * @param multipartFile The file to save.
     * @param properties FileUploadProperties for validation and configuration.
     * @return The securely generated unique filename (including extension).
     * @throws FileUploadException if the file is empty, exceeds size limits, has a disallowed type, or an I/O error occurs.
     */
    public static String saveFile(String uploadDir, String fileName, MultipartFile multipartFile, FileUploadProperties properties) {
        if (multipartFile.isEmpty()) {
            throw new FileUploadException("Failed to store empty file " + fileName);
        }

        // Validate file size
        if (multipartFile.getSize() > properties.getMaxFileSizeInBytes()) {
            throw new FileUploadException(String.format("File size exceeds the maximum limit of %d MB", properties.getMaxFileSizeMb()));
        }

        // Validate file type
        String fileExtension = StringUtils.getFilenameExtension(fileName);
        if (fileExtension == null || fileExtension.isEmpty()) {
            throw new FileUploadException("Could not determine file extension for " + fileName);
        }
        if (!properties.getAllowedFileTypes().equals("*") &&
                !containsIgnoreCase(properties.getAllowedFileTypes().split(","), fileExtension)) {
            throw new FileUploadException(String.format("File type '%s' is not allowed", fileExtension));
        }

        // Generate a unique file name
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
        Path copyLocation = Paths.get(uploadDir + File.separator + uniqueFileName);

        try {
            Files.createDirectories(copyLocation.getParent()); // Ensure directory exists
            Files.copy(multipartFile.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            return uniqueFileName;
        } catch (IOException e) {
            throw new FileUploadException("Could not store file " + fileName + ". Please try again!", e);
        }
    }

    /**
     * Helper to check if a string array contains a value ignoring case.
     */
    private static boolean containsIgnoreCase(String[] array, String value) {
        for (String element : array) {
            if (element.trim().equalsIgnoreCase(value.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the full path for a given filename in the upload directory.
     * @param uploadDir The base upload directory.
     * @param fileName The name of the file.
     * @return The absolute Path to the file.
     */
    public static Path resolveFilePath(String uploadDir, String fileName) {
        return Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName).normalize();
    }
}
