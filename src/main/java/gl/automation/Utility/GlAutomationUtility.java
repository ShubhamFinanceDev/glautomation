package gl.automation.Utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class GlAutomationUtility {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendMail(String fileName, byte[] file) {

        try {
            String successMsg = "Dear Sir/Madam,\n\nI would like to inform you that the GL Automation has been completed successfully at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\nPlease find the attached document for your reference. \n\n\nRegards,\nIT Support.";

            List<String> emailList = new ArrayList<>();
            emailList.add("naresh.chand@shubham.co");
            emailList.add("ravi.soni@shubham.co");
            emailList.add("Preeti.09721@shubham.co");
            emailList.add("abhishek.sharma@dbalounge.com");

            emailList.forEach(sendMail->{
                try {
                    MimeMessage message = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setFrom(sender);
                    helper.setTo(sendMail);
                    helper.setText(successMsg);
                    helper.setSubject("GL Automation notification");
                    InputStreamSource attachmentSource = new ByteArrayResource(file);
                    helper.addAttachment(fileName,attachmentSource);

                    javaMailSender.send(message);
                    System.out.println("Mail sent successfully to: " + sendMail);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
