package is.mpg.ruglan.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.test.AndroidTestCase;

import is.mpg.ruglan.utils.Utils;

/**
 * Created by jon on 1.11.2013.
 */
public class UtilsTest extends AndroidTestCase {
	/**
	 * Tests if the conversion from Date to Calendar preserves 
	 * the actual date/time 
	 * @throws Exception
	 */
	public void testDateToCalendar() throws Exception {
		SimpleDateFormat format = 
				new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("UTC"));
        Date start = format.parse("20130515T132000");
        Date end = format.parse("20130515T140000");
        Calendar calStart = Utils.dateToCalendar(start);
        Calendar calEnd = Utils.dateToCalendar(end);
        assertEquals("Calendar object did not match date object.", 
        		0, calStart.getTime().compareTo(start));
        assertEquals("Calendar object did not match date object.",
        		0, calEnd.getTime().compareTo(end));
	}
	
    public void testFillColorsArray() throws Exception {

        Utils.fillColorsArray();
        assertTrue(Utils.colors.length > 10);
        assertEquals(7, Utils.colors[10].length());
        assertEquals("#",Utils.colors[10].substring(0, 1));
        Long.decode(Utils.colors[10]);
    }
}
