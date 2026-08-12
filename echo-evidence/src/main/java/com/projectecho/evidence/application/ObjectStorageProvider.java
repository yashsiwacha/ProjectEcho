package com.projectecho.evidence.application;

import java.util.Map;

/** Port interface defining storage and object validation contracts (FD-0020). */
public interface ObjectStorageProvider {

    /**
     * Generates a pre-signed HTTP PUT URL to upload files directly from the browser.
     *
     * @param objectKey unique filename key.
     * @param metadata custom headers map.
     * @return signed upload URL string.
     */
    String generatePresignedUploadUrl(String objectKey, Map<String, String> metadata);

    /** Checks if the file exists and is validated. */
    boolean verifyUpload(String objectKey);
}
