package com.tilk.utils;

import java.util.Calendar;

/**
 * Created by YPierru on 11/01/2017.
 */

public class DateTimeUtils {

    public static int getMinuteSinceMidnight(){
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        return ((hour * 60) + minute);
    }

    public static int getMinutesSpentHour(){
        Calendar now = Calendar.getInstance();

        return now.get(Calendar.MINUTE);
    }

}
