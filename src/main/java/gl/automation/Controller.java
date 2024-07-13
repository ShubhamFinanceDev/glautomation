package gl.automation;

import gl.automation.Configuration.BlobStorageService;
import gl.automation.Service.GlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RestController
public class Controller {
    @Autowired
    private GlService glService;
    @Autowired
    private BlobStorageService blobStorageService;

    @PostMapping("/invoke-gl-job")
    public ResponseEntity<?> createAndUploadGl(@RequestBody Map<String,String> request) throws IOException {
            String invokedBy = request.get("invokedBy");
            LocalDate processDate = LocalDate.parse(request.get("gl-date"));
        return glService.glJobInvoke(processDate, invokedBy);
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile() throws IOException {

        String blobName = "GL2024.06.24.xlsx";
        byte[] data = blobStorageService.downloadBlob("ilfsblob", blobName);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .contentLength(data.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + blobName + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);

    }
    @GetMapping("/create")
    public void createFile() throws IOException
    {
        glService.invokeProcessOfGl();
    }
}