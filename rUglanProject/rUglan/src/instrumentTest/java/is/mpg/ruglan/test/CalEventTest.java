package is.mpg.ruglan.test;

import is.mpg.ruglan.CalEvent;
import android.test.AndroidTestCase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Unittests for the CalEvent class
 * @author Siggi
 */
public class CalEventTest extends AndroidTestCase {
    private CalEvent event;
    private String name, description, location, durationString, fullCalStartString, fullCalEndString;
    private Date start, end;
    private SimpleDateFormat format;

    public void setUp() throws Exception {
        this.name = "Test Event";
        this.description = "Description for the test event.";
        this.location = "Location for the test event.";
        this.format = new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("UTC"));
        this.start = this.format.parse("20130515T132000");
        this.end = this.format.parse("20130515T140000");
        this.durationString = "13:20 - 14:00";
        this.fullCalStartString = "new Date(2013, 4, 15, 13, 20)";
        this.fullCalEndString = "new Date(2013, 4, 15, 14, 0)";
        this.event = new CalEvent(this.name, this.description, this.location, this.start, this.end);
        super.setUp();
    }

    /**
     * Creates a CalEvent with dates not on the same day. Expects IllegalArgumentException.
     * @throws Exception
     */
    public void testCalEventDatesNotSameDay() throws Exception {
        boolean exThrown = false;
        Date d1 = this.format.parse("20130514T132000");
        Date d2 = this.format.parse("20130515T133000");
        try {
            new CalEvent("A", "B", "C", d1, d2);
        } catch (IllegalArgumentException ex)
        {
            exThrown = true;
        }
        assertTrue("Unit test was able to create CalEvent with dates not on the same day.",
                    exThrown);
    }

    /**
     * Creates a CalEvent with end date before start date. Expects IllegalArgumentException.
     * @throws Exception
     */
    public void testCalEventEndBeforeStart() throws Exception {
        boolean exThrown = false;
        Date d1 = this.format.parse("20130515T132000");
        Date d2 = this.format.parse("20130514T133000");
        try {
            new CalEvent("A", "B", "C", d1, d2);
        } catch (IllegalArgumentException ex)
        {
            exThrown = true;
        }
        assertTrue("Unit test was able to create CalEvent with end date before start date.",
                    exThrown);
    }

    /**
     * Creates a CalEvent with start date same as end date. Expects IllegalArgumentException.
     * @throws Exception
     */
    public void testCalEventStartSameAsEnd() throws Exception {
        boolean exThrown = false;
        Date d1 = this.format.parse("20130515T132000");
        try {
            new CalEvent("A", "B", "C", d1, d1);
        } catch (IllegalArgumentException ex)
        {
            exThrown = true;
        }
        assertTrue("Unit test was able to create CalEvent with start date the same as end date.",
                    exThrown);
    }

    /**
     * Checks if getName returns the correct name.
     * @throws Exception
     */
    public void testGetName() throws Exception {
        assertEquals(this.name, this.event.getName());
    }

    /**
     * Checks if getDescription returns the correct description.
     * @throws Exception
     */
    public void testGetDescription() throws Exception {
        assertEquals(this.description, this.event.getDescription());
    }

    /**
     * Checks if getLocation returns the correct location.
     * @throws Exception
     */
    public void testGetLocation() throws Exception {
        assertEquals(this.location, this.event.getLocation());
    }

    /**
     * Checks if getStart returns the correct date.
     * @throws Exception
     */
    public void testGetStart() throws Exception {
        assertEquals(this.start, this.event.getStart());
    }

    /**
     * Checks if getEnd returns the correct date.
     * @throws Exception
     */
    public void testGetEnd() throws Exception {
        assertEquals(this.end, this.event.getEnd());
    }

    /**
     * Checks if getDurationString returns the correct string describing duration of the event.
     * @throws Exception
     */
    public void testGetDurationString() throws Exception {
        assertEquals(this.durationString, this.event.getDurationString());
    }

    /**
     * Checks if equals does correct checks
     * @throws Exception
     */
    public void testEquals() throws Exception {
        Date d4 = this.format.parse("20130515T131000");
        Date d1 = this.format.parse("20130515T132000");
        Date d2 = this.format.parse("20130515T133000");
        Date d3 = this.format.parse("20130515T134000");

        CalEvent a = new CalEvent("A", "B", "C", d1, d2);
        CalEvent b = new CalEvent("A", "B", "C", d1, d2);
        CalEvent [] wrong = {
        new CalEvent("B", "B", "C", d1, d2),
        new CalEvent("A", "A", "C", d1, d2),
        new CalEvent("A", "B", "D", d1, d2),
        new CalEvent("A", "B", "C", d1, d3),
        new CalEvent("A", "B", "C", d4, d2)};
        assertTrue(a.equals(b));
        for (CalEvent aWrong : wrong) {
            assertFalse(a.equals(aWrong));
        }
    }

    /**
     * Checks if getFullCalendarStartDateString returns the correct string of the event;
     * a string that can be used as a parameter in FullCalendar.
     * @throws Exception
     */
    public void testGetFullCalendarStartDateString() throws Exception {
        assertEquals(this.fullCalStartString, this.event.getFullCalendarStartDateString());
    }

    /**
     * Checks if getFullCalendarEndDateString returns the correct string of the event;
     * a string that can be used as a parameter in FullCalendar.
     * @throws Exception
     */
    public void testGetFullCalendarEndDateString() throws Exception {
        assertEquals(this.fullCalEndString, this.event.getFullCalendarEndDateString());
    }

    /**
     * Checks if getColor returns the correct color for a CalEvent.
     * @throws Exception
     */
    public void testGetColor() throws Exception {
        // TODO: Write this test when getColor has been implemented using rules with Dabbi.
        assertTrue("getColor has not yet been implemented with Dabbi's rules.", false);
    }
}
