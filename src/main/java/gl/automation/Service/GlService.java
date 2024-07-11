package gl.automation.Service;

import gl.automation.Configuration.BlobStorageService;
import gl.automation.Utility.CalendarUtility;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@EnableScheduling
@Service
public class GlService {
    private final Logger logger = LoggerFactory.getLogger(GlService.class);

    @Autowired
    private BlobStorageService blobStorageService;
    @Autowired
    private CalendarUtility calendar;

    @Scheduled(cron = "2 * * * * *")
    public void invokeProcessOfGl() {
        try {
            LocalDate currentDate=calendar.currentDate();
            logger.info("Gl Process start {}",currentDate);
            if ((currentDate.isAfter(calendar.dateBasedOnDay(4)) && (currentDate.isBefore(calendar.lastDateOfCurrentMonth())))) {
                LocalDate processDate = calendar.glProcessDate(1);
                logger.info("Gl Process invoke for {}",processDate);
                glJobInvoke(processDate,"scheduler");

            } else if (calendar.currentDate().equals(calendar.dateBasedOnDay(4))) {
                for (int i = 1; i <= 4; i++) {
                    LocalDate processDate = calendar.glProcessDate(i);
                    glJobInvoke(processDate,"scheduler");

                }
            } else {
                logger.info("Current date is less then of 4th day month or equal to last day of month.");
            }

        } catch (Exception e) {
            logger.error("Error while invoking Gl process", e.getMessage());
        }
        logger.info("Gl process completed. "+Calendar.getInstance().getTime());
    }


    public void glJobInvoke(LocalDate processDate, String invokedBy) throws IOException {
        logger.info("Process invoked for {}" , processDate +" at "+Calendar.getInstance().getTime()+ "By "+invokedBy);
        String fileName = "Gl-" + processDate + ".xlsx";
        generateFile(fileName);  //generateFileLocally
        uploadFileIntoStorage(fileName);
    }






    public void generateFile(String fileName) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("MIS_Report");
        int rowCount = 0;
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
        logger.info("File have been created {}");
    }


    void uploadFileIntoStorage(String fileName) throws IOException {
        String folderPath = "src/main/resources/";

        File folder = new File(folderPath);

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().equals(fileName)) {
                    System.out.println("uploaded successfully " + file.getName());
//                    InputStream inputStream = new FileInputStream(file);
//                    blobStorageService.uploadBlob("ilfsblob", fileName, inputStream, file.length());

                }
            }
        } else {
            System.out.println("No files found in the directory.");
        }

    }


}
