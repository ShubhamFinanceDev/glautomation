package gl.automation.Service;

import gl.automation.Configuration.BlobStorageService;
import gl.automation.Dto.ReportModel;
import gl.automation.Utility.CalendarUtility;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
import java.time.format.DateTimeFormatter;
import java.util.*;

@EnableScheduling
@Service
public class GlService {
    private final Logger logger = LoggerFactory.getLogger(GlService.class);

    @Autowired
    private BlobStorageService blobStorageService;
    @Autowired
    private CalendarUtility calendar;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0 0/1 * * * *")
    public void invokeProcessBySchedule() {
        try {
            LocalDate currentDate = calendar.currentDate();
            logger.info("Gl Process start {}", currentDate);

            if (currentDate.isAfter(calendar.dateBasedOnDay(4))) {
                LocalDate processDate = calendar.glProcessDate(1);
                logger.info("Gl Process invoke for {}", processDate);
                glJobInvoke(processDate, "scheduler");
            } else if (currentDate.equals(calendar.dateBasedOnDay(4))) {
                int i = 1;
                while (i <= 4) {
                    glJobInvoke(calendar.glProcessDate(i), "scheduler");
                    i++;
                }
            } else {
                logger.info("Current date is less than 4th day of month");
            }
        } catch (Exception e) {
            logger.error("Error while invoking Gl process: {}", e.getMessage());
        }
        logger.info("Gl process completed. {}", Calendar.getInstance().getTime());
    }


    public ResponseEntity<?> glJobInvoke(LocalDate processDate, String invokedBy) throws IOException {
        logger.info("Process invoked for {}", processDate + " at " + Calendar.getInstance().getTime() + " By " + invokedBy);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMYYYY");
        String formattedDate = processDate.format(formatter);

        String fileName = "Handsoff " + formattedDate + ".xlsx";
        List<ReportModel> reportModels = new ArrayList<>();
        try {

            reportModels= readData(processDate);
            if(!reportModels.isEmpty()){
            byte[] file = generateFile(fileName, reportModels);  //generateFileLocally
            uploadFileIntoStorage(fileName, file);
            }
            else {
                logger.info("Records not available to write in file");
            }
        } catch (Exception e) {
            logger.error("Error while invoking gl Job by invokedBy {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok("Success");
    }


    private List<ReportModel> readData(LocalDate processDate) throws Exception {
        String query = "SELECT *\n" +
                "FROM neo_cas_lms_sit1_sh.gl_handsoff_view_new_prod1_pan\n" +
                "WHERE \"Voucher_Date\" = TO_DATE('" + processDate + "', 'YYYY-MM-DD')";

        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ReportModel.class));
    }


    public byte[] generateFile(String fileName, List<ReportModel> reportModels) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("MIS_Report");
        int rowCount = 0;
        String[] header = {"VOUCHER_DTL_ID", "Voucher_Number", "Branch", "GL_Code", "DrCr_Flag", "Amount", "NARRATION", "Reference_Id", "Loan_Id", "Value_Date", "Voucher_Date", "Voucher_Creation_Date", "Product_Code", "Entity_ID", "SCHEME_CODE", "CUST_ID", "CUST_NAME", "PRODUCT_TYPE", "PRODUCT_NAME", "SANCTIONED_LOAN_AMOUNT", "CAS_APPLICATION_NUMBERt", "CHEQUE_NUMBER", "LOAN_BRANCH_STATE_CODE", "CUSTOMER_ADDRESS_STATE_CODE", "PAN"};
        Row headerRow = sheet.createRow(rowCount++);
        int cellCount = 0;

        for (String headerValue : header) {
            headerRow.createCell(cellCount++).setCellValue(headerValue);
        }
        for (ReportModel readData : reportModels) {
            Row row = sheet.createRow(rowCount++);
            cellCount = 0;
            row.createCell(cellCount++).setCellValue(readData.getVoucherDtlId());
            row.createCell(cellCount++).setCellValue(readData.getVoucherNumber());
            row.createCell(cellCount++).setCellValue(readData.getBranch());
            row.createCell(cellCount++).setCellValue(readData.getGlCode());
            row.createCell(cellCount++).setCellValue(readData.getDrCrFlag());
            row.createCell(cellCount++).setCellValue(readData.getAmount());
            row.createCell(cellCount++).setCellValue(readData.getNarration());
            row.createCell(cellCount++).setCellValue(readData.getReferenceId());
            row.createCell(cellCount++).setCellValue(readData.getLoanId());
            row.createCell(cellCount++).setCellValue(readData.getValueDate());
            row.createCell(cellCount++).setCellValue(readData.getVoucherDate());
            row.createCell(cellCount++).setCellValue(readData.getVoucherCreationDate());
            row.createCell(cellCount++).setCellValue(readData.getProductCode());
            row.createCell(cellCount++).setCellValue(readData.getEntityId());
            row.createCell(cellCount++).setCellValue(readData.getSchemeCode());
            row.createCell(cellCount++).setCellValue(readData.getCustId());
            row.createCell(cellCount++).setCellValue(readData.getCustName());
            row.createCell(cellCount++).setCellValue(readData.getProductType());
            row.createCell(cellCount++).setCellValue(readData.getProductName());
            row.createCell(cellCount++).setCellValue(readData.getSanctionLoanAmount());
            row.createCell(cellCount++).setCellValue(readData.getCasApplicationNumber());
            row.createCell(cellCount++).setCellValue(readData.getChequeNumber());
            row.createCell(cellCount++).setCellValue(readData.getLoanBranchStateCode());
            row.createCell(cellCount++).setCellValue(readData.getCustomerAddressStateCode());
            row.createCell(cellCount++).setCellValue(readData.getPan());

        }
        logger.info("No of records insert in file {}", rowCount);
        byte[] excelData;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            excelData = outputStream.toByteArray();
        } finally {
            workbook.close();
        }
        logger.info("File have been created");
        return excelData;
    }


    void uploadFileIntoStorage(String fileName, byte[] file) throws IOException {

        InputStream inputStream = new ByteArrayInputStream(file);
        blobStorageService.uploadBlob("ilfsblob", fileName, inputStream, file.length);
        logger.info("File uploaded: {}", fileName);


    }


}
