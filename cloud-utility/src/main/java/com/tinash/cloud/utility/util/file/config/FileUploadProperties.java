package com.tinash.cloud.utility.util.file.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import jakarta.validation.constraints.NotBlank;

@Component
@ConfigurationProperties(prefix = "app.file.upload")
@Getter
@Setter
public class FileUploadProperties {

    @NotBlank(message = "File upload directory must not be blank")
    private String uploadDir = "uploads"; // Default upload directory

    private long maxFileSizeMb = 10; // Default max file size in MB
    private String allowedFileTypes = "*"; // Default to allow all file types (e.g., "png,jpg,jpeg,pdf")

    public long getMaxFileSizeInBytes() {
        return maxFileSizeMb * 1024 * 1024;
    }
}
