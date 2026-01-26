I have implemented the file upload functionality as a generic service within the `cloud-utility` module, adhering to its library nature. This functionality is now available for other microservices to integrate and use.

Here's a detailed summary of the changes made for this new feature:

**I. New Components Created for File Upload:**

1.  **`cloud-utility/src/main/java/com/tinash/cloud/utility/util/file/config/FileUploadProperties.java`**:
    *   A `@ConfigurationProperties` class to externalize file upload settings.
    *   Configurable properties include:
        *   `uploadDir`: The base directory for file storage (default: "uploads").
        *   `maxFileSizeMb`: Maximum allowed file size in MB (default: 10MB).
        *   `allowedFileTypes`: Comma-separated list of allowed file extensions (default: "*", allowing all).
    *   Includes a helper method `getMaxFileSizeInBytes()`.

2.  **`cloud-utility/src/main/java/com.tinash/cloud/utility/util/file/FileUploadException.java`**:
    *   A custom `RuntimeException` for specific file upload errors, allowing for more granular error handling.

3.  **`cloud-utility/src/main/java/com/tinash/cloud/utility/util/file/FileUploadResponse.java`**:
    *   A simple Data Transfer Object (DTO) to encapsulate the details of an uploaded file, including `fileName`, `fileDownloadUri`, `fileType`, and `size`.

**II. Existing Utilities Enhanced:**

1.  **`cloud-utility/src/main/java/com/tinash/cloud/utility/util/file/FileUploadUtils.java`**:
    *   The `saveFile` method was refactored and enhanced significantly:
        *   **Secure File Naming:** Generates a unique filename using `UUID` to prevent naming collisions and security issues.
        *   **File Validation:** Integrates with `FileUploadProperties` to validate against configured `maxFileSizeMb` and `allowedFileTypes`.
        *   **Directory Management:** Ensures the target upload directory exists.
        *   **Error Handling:** Throws `FileUploadException` for validation failures or I/O errors.
        *   Returns the securely generated unique filename.
    *   A new static helper method `resolveFilePath(String uploadDir, String fileName)` was added to construct the absolute path to an uploaded file.

**III. Core File Upload Service:**

1.  **`cloud-utility/src/main/java/com/tinash/cloud/utility/util/file/FileUploadService.java`**:
    *   A `@Service` class that orchestrates the file upload process.
    *   Injects `FileUploadProperties` for configuration.
    *   The `uploadFile(MultipartFile file)` method:
        *   Takes a `MultipartFile` as input.
        *   Uses `FileUploadUtils` to save the file securely.
        *   Constructs a `fileDownloadUri` (example URI, consuming services would typically expose a `/downloadFile/{fileName}` endpoint).
        *   Returns a `FileUploadResponse` with the details of the uploaded file.
    *   Provides a `getFilePath(String fileName)` method to easily retrieve the `Path` to a stored file.

This new functionality provides a robust and configurable way for microservices using `cloud-utility` to handle local file uploads, centralizing common logic and best practices. Consuming services would simply inject `FileUploadService` and expose their own REST endpoints to accept file uploads.