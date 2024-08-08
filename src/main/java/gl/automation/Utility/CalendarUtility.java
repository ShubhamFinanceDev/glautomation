package gl.automation.Utility;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Service
public class CalendarUtility {
    int year=2023;
    int month=6;
    int day=8;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public LocalDate currentDate() {
        Calendar calendar = Calendar.getInstance();
//        calendar.set(year,month,day);
        Date currentDate = calendar.getTime();
        String formattedDate = dateFormat.format(currentDate);
        System.out.println(formattedDate);
        return LocalDate.parse(formattedDate);
    }

    public LocalDate glProcessDate(int prevDay) {

        Calendar calendar = Calendar.getInstance();
//        calendar.set(year,month,day);
        calendar.add(Calendar.DAY_OF_MONTH, -prevDay);
        Date currentDate = calendar.getTime();
        String formattedDate = dateFormat.format(currentDate);
        System.out.println(currentDate+"process date"+ formattedDate);
        return LocalDate.parse(formattedDate);
    }

    public LocalDate dateBasedOnDay(int day) {
        Calendar calendar = Calendar.getInstance();
//        calendar.set(year,month,day);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Date currentDate = calendar.getTime();
        String formattedDate = dateFormat.format(currentDate);
        return LocalDate.parse(formattedDate);
    }

}
