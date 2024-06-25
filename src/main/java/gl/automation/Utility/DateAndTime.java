package gl.automation.Utility;

import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class DateAndTime {
    Calendar calendar = Calendar.getInstance();
    int lastDay = calendar.get(Calendar.DAY_OF_MONTH);

}
