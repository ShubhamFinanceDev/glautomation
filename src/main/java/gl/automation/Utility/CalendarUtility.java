package gl.automation.Utility;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Service
public class CalendarUtility {
    int year=2024;
    int month=06;
    int day=30;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public LocalDate currentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        Date currentDate = calendar.getTime();
        String formattedDate = dateFormat.format(currentDate);
        System.out.println(formattedDate);
        LocalDate date = LocalDate.parse(formattedDate);
        return date;
    }

    public LocalDate glProcessDate(int prevDay) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        calendar.add(Calendar.DAY_OF_MONTH, -prevDay);
        Date currentDate = calendar.getTime();
        String formattedDate = dateFormat.format(currentDate);
        System.out.println(currentDate+"process date"+ formattedDate);
        LocalDate date = LocalDate.parse(formattedDate);
        return date;
    }

    public LocalDate dateBasedOnDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Date currentDate = calendar.getTime();
        String formattedDate = dateFormat.format(currentDate);
        LocalDate date = LocalDate.parse(formattedDate);
        return date;
    }

    public LocalDate lastDateOfCurrentMonth()
    {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date currentDate = calendar.getTime();
        String formattedDate = dateFormat.format(currentDate);
        LocalDate date = LocalDate.parse(formattedDate);
        return date;
    }
}
