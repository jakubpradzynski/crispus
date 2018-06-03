package pl.jakubpradzynski.crispus.utils;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

public class DateUtilsTest {

    @Test
    public void theMonthTest() {
        String month = DateUtils.theMonth(5 - 1);
        Assert.assertTrue(month.equals("May"));
    }

    @Test
    public void stringToDateTest() {
        Date date = null;
        try {
            date = DateUtils.stringToDate("1997-05-21", "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date1 = Date.from(Instant.now());
        Assert.assertTrue(date.before(date1));
    }

}
