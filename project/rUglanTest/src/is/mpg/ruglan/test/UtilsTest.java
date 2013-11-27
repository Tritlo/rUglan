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
	
    
    /**
     * Checks if the stripCourseNumberFromName works as expected.
     * @throws Exception
     */
    public void testStripCourseNumberFromName() throws Exception {
    	String[] input = new String[]{
    			"EDL102G-20136 Edlisfraedi 1 V",
    			"IDN503G-20136 Verkefnastjornun",
    			"TOL301G-20136 Formleg mal og reiknanleiki",
    			"HBV501G-20136 Hugbunadarverkefni 1"
    	};
    	String[] output = new String[]{
    			"Edlisfraedi 1 V",
    			"Verkefnastjornun",
    			"Formleg mal og reiknanleiki",
    			"Hugbunadarverkefni 1"
    	};
    	for(int i=0; i<input.length; i++) {
    		assertTrue("Result did not match expected result.",
    			Utils.stripCourseNumberFromName(input[i]).equals(output[i]));
    	}
    }
}
