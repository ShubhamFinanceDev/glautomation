package gl.automation.Service;

import gl.automation.Configuration.BlobStorageService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;

@Service
public class GlService {
    @Autowired
    private BlobStorageService blobStorageService;
    void invokeProcessOFGl()
    {

    }

    public String generateFile() throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("MIS_Report");
        int rowCount = 0;
        String fileName= "GL"+localDate()+".xlsx";
        String[] header = {"ApplicationNumber", "BranchName", "ApplicantName", "ChequeAmount", "ConsumerType", "HandoverDate", "LoanAmount", "UpdatedBy"};
        Row headerRow = sheet.createRow(rowCount++);
        int cellCount = 0;

        for (String headerValue : header) {
            headerRow.createCell(cellCount++).setCellValue(headerValue);
        }
        try {
            Path filePath = Paths.get("src/main/resources", fileName);
            Files.createDirectories(filePath.getParent());

            FileOutputStream fileOut = new FileOutputStream(filePath.toString());
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }
        uploadFileIntoStorage(fileName);
        return "success";
    }

    void uploadFileIntoStorage(String fileName) throws IOException {
        String folderPath = "src/main/resources";

        File folder = new File(folderPath);

            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().equals(fileName)) {
                        System.out.println(file.getName());
                        InputStream inputStream = new FileInputStream(file);
                       blobStorageService.uploadBlob("ilfsblob",fileName,inputStream,file.length());

                    }
                }
            } else {
                System.out.println("No files found in the directory.");
            }

    }


    public String localDate() {
        Calendar calendar = Calendar.getInstance();
        Timestamp futureTimestamp = new Timestamp(calendar.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String localDate = sdf.format(futureTimestamp);
        return localDate;
    }
}
