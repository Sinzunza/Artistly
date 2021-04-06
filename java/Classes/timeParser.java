package Classes;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.time.format.DateTimeFormatter.ofPattern;

// Time parser for artistly DB
public class timeParser {

    // used to turn invert time so results are returned in descending order when querying
    public static String timeInvert(String timeStamp){
        timeStamp = timeStamp.replaceAll("\\s", "");
        timeStamp = timeStamp.replaceAll("-", "");
        timeStamp = timeStamp.replaceAll(":", "");
        return Long.toString(99999999999999l - Long.parseLong(timeStamp));
    }

    // used to revert time after timeInvert() has been used
    public static String timeRevert(String timeStamp){
        timeStamp = Long.toString(99999999999999l - Long.parseLong(timeStamp));
        timeStamp = timeStamp.substring(0, 4) + "-" + timeStamp.substring(4, 6) + "-" + timeStamp.substring(6, 8) + " " + timeStamp.substring(8, 10) + ":" + timeStamp.substring(10, 12) + ":" + timeStamp.substring(12, 14);

        SimpleDateFormat formatterSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formatterSDF.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date = formatterSDF.parse(timeStamp);
            formatterSDF.setTimeZone(TimeZone.getDefault());
            timeStamp = formatterSDF.format(date);

            DateTimeFormatter formatterDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String timeStampCurr = LocalDateTime.now().format(formatterDTF);

            if(timeStampCurr.equals(timeStamp.substring(0, 10))){
                return LocalTime.parse(timeStamp.substring(11, 16)).format(ofPattern("h:mm a"));
            }
            else {
                date = formatterSDF.parse(timeStamp);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).substring(0,3) + " " + cal.get(Calendar.DAY_OF_MONTH);
            }

        }
        catch(Exception e) {
            System.out.println(e);
        }

        return "Error";
    }
}
