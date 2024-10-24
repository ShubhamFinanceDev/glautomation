package gl.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

@EnableAsync
@SpringBootApplication
public class GlAutomationApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlAutomationApplication.class, args);
    }

}
