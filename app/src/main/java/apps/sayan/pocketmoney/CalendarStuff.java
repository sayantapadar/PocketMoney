package apps.sayan.pocketmoney;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by SAYAN on 17-05-2016.
 */
public class CalendarStuff {
    Calendar calendar;
    public CalendarStuff(){
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        this.calendar=cal;
    }
    public static String getMonth()
    {
        CalendarStuff calendarStuff=new CalendarStuff();
        switch (calendarStuff.calendar.get(Calendar.MONTH))
        {
            case 0: return "January";
            case 1: return "February";
            case 2: return "March";
            case 3: return "April";
            case 4: return "May";
            case 5: return "June";
            case 6: return "July";
            case 7: return "August";
            case 8: return "September";
            case 9: return "October";
            case 10: return "November";
            case 11: return "December";
        }
        return null;
    }
    public static String getDetail(){
        CalendarStuff calendarStuff=new CalendarStuff();
        return (String.valueOf(calendarStuff.calendar.get(Calendar.DATE))+ "-"+
                String.valueOf(calendarStuff.calendar.get(Calendar.MONTH)+1)+ " "+
                String.valueOf(calendarStuff.calendar.get(Calendar.HOUR_OF_DAY))+ ":"+
                String.valueOf(calendarStuff.calendar.get(Calendar.MINUTE))
        );
    }
}
