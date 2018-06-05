package pl.jakubpradzynski.crispus.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A utils-type class with helpful methods related to dates.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
public class DateUtils {

    /**
     * Method returns month name for specific month number.
     * You have to remember that the tables are numbered from zero, so you have to give the number of the month by one smaller.
     * @param month - month number
     * @return String (month name)
     */
    public static String theMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }

    /**
     * Method returns a date formatted from the given date as a string and format.
     * @param stringDate - date as string
     * @param format - date format
     * @return Date (formatted)
     * @throws ParseException - Exception is thrown when it is impossible to parse the date from the string.
     */
    public static Date stringToDate(String stringDate, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(stringDate);
        return date;
    }
}
