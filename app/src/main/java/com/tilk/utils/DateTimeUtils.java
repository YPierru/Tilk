package com.tilk.utils;

import java.util.Calendar;

/**
 * Created by YPierru on 11/01/2017.
 */

public class DateTimeUtils {

    public static int getAmountOf6hInCurrentMonth(){
        return getNumberOfDayInMonth() * 4;
    }

    public static int getNumberOfDayInMonth(){
        Calendar now = Calendar.getInstance();
        return now.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getNumberOfDayInYear(){
        Calendar now = Calendar.getInstance();
        return now.getActualMaximum(Calendar.DAY_OF_YEAR);
    }

    public static String getCurrentYear(){
        Calendar now = Calendar.getInstance();
        return String.valueOf(now.get(Calendar.YEAR));
    }

    public static String getPreviousYear(){
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR,-1);
        return String.valueOf(now.get(Calendar.YEAR));
    }

    public static String getCurrentMonth(){
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        Calendar now = Calendar.getInstance();
        return months[now.get(Calendar.MONTH)];
    }

    public static String getCurrentMonthName() {
        String[] monthsFrench = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        Calendar now = Calendar.getInstance();
        return monthsFrench[now.get(Calendar.MONTH)];
    }

    public static String getPreviousMonthName() {
        String[] monthsFrench = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        Calendar now = Calendar.getInstance();
        if(getCurrentMonth().equals("01")){
            return monthsFrench[11];
        }else {
            return monthsFrench[now.get(Calendar.MONTH)];
        }
    }


}
