package util;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date addDuration(Date date, Duration duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, (int) duration.getSeconds());
        return calendar.getTime();
    }
}
