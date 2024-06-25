package gl.automation.Configuration;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class BlobStorageService {

        private final BlobServiceClient blobServiceClient;

        @Autowired
        public BlobStorageService(BlobServiceClient blobServiceClient) {
            this.blobServiceClient = blobServiceClient;
        }

        public void uploadBlob(String containerName, String blobName, InputStream data, long length) {
            BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName)
                    .getBlobClient(blobName);
            blobClient.upload(data, length, true); // true to overwrite existing blobs
        }
    public byte[] downloadBlob(String containerName, String blobName) throws IOException {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            blobClient.download(outputStream);
            return outputStream.toByteArray();
        }
    }
        // Other methods (downloadBlob, listBlobs, deleteBlob) can remain as is
    }

