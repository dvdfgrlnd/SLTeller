package com.maptest.Utils;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by david on 12/4/16.
 */

public class TimeParser {

    public static TimeHandler handler = new TimeHandler();

    public static class TimeHandler {
        public Calendar getNewObject() {
            return Calendar.getInstance();
        }
    }

    public static int parseMin(String min) {
        min = min.toLowerCase();
        int minutes = 1337;
        if (min.toLowerCase().equals("nu")) {
            minutes = 0;
        } else if (min.indexOf("min") != -1) {
            String s = "0";
            try {
                s = min.substring(0, min.indexOf("min")).trim();
                minutes = Integer.parseInt(s);
            } catch (StringIndexOutOfBoundsException e) {
            }
        } else if (min.indexOf(":") != -1) {
            DateFormat sdf = new SimpleDateFormat("hh:mm");
            try {
                Date date = sdf.parse(min);
                // "oldcal" is used to set the departure time, the date on the other hand will be 1970.1.1 etc. due to "date" will only have the time component
                Calendar oldcal = handler.getNewObject();
                oldcal.setTime(date);
                // Create the new Calendar object with current date, and set the time from the "oldcal" object
                Calendar cal = handler.getNewObject();
                cal.set(Calendar.HOUR, oldcal.get(Calendar.HOUR));
                cal.set(Calendar.MINUTE, oldcal.get(Calendar.MINUTE));
                Calendar now = handler.getNewObject();
                // If the departure time is the next day, increment the day by one. E.g. current time = 23.45, departure = 00.15
                if (cal.before(now)) {
                    cal.add(Calendar.DATE, 1);
                }
                minutes = (int) ((cal.getTimeInMillis() - now.getTimeInMillis()) / 60000);
            } catch (ParseException e) {
            }
        }
        return minutes;
    }

}
