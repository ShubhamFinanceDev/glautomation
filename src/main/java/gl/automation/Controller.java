package gl.automation;

import gl.automation.Configuration.BlobStorageService;
import gl.automation.Service.GlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class Controller {
    @Autowired
    private GlService glService;
    @Autowired
    private BlobStorageService blobStorageService;

    @PostMapping("/upload")
    public String addFile(@RequestParam("file") MultipartFile file) throws IOException {

        blobStorageService.uploadBlob("ilfsblob", file.getOriginalFilename(), file.getInputStream(), file.getSize());
        return "Success";
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
    public String createFile() throws IOException
    {
       return glService.generateFile();
    }
}