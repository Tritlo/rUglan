package is.mpg.ruglan.test;

import android.test.AndroidTestCase;

import java.text.SimpleDateFormat;
import java.util.Locale;

import is.mpg.ruglan.data.CalEvent;
import is.mpg.ruglan.data.iCalParser;

/**
 * Created by tritlo on 10/8/13.
 * @author Matti
 */
public class iCalParserTest extends AndroidTestCase {

    private String calendarString;
    private CalEvent hbv;
    private String url;

    public void setUp() throws Exception {
        super.setUp();
        this.url = "https://notendur.hi.is/mpg3/ugla.ics";
        this.calendarString = iCalParser.urlToString(this.url);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("UTC"));
        this.hbv = new CalEvent("HBV501G-20136 Hugbunadarverkefni 1", " d1", "V02-152\\, VR-2",
                format.parse("20131017T100000"), format.parse("20131017T113000"));
    }

    public void testUrlToCalEvents() throws Exception {
        CalEvent [] events = iCalParser.urlToCalEvents(this.url);
        assertTrue(this.hbv.toString(),this.hbv.equals(events[0]));
    }

    public void testUrlToString() throws Exception {
        String hws = "hello, world\n";
        String urlHWS = iCalParser.urlToString("https://notendur.hi.is/mpg3/helloworld.html");
        assertEquals(hws,urlHWS);
    }

    public void testParseCalendar() throws Exception {
        CalEvent [] events = iCalParser.parseCalendar(this.calendarString);
        assertTrue(events[0].toString(),this.hbv.equals(events[0]));
    }

    public void testGetICalValue() throws Exception {
        String [] event = iCalParser.calendarToEventList(this.calendarString)[0];
        String name = iCalParser.getICalValue(event, "SUMMARY");
        assertTrue(name,this.hbv.getName().equals(name));
    }

    public void testRemoveCR() throws Exception {
        String [] wCr = {"hey\r"};
        String [] res = iCalParser.removeCR(wCr);
        assertTrue(res[0].equals("hey"));
    }

    public void testCalendarToEventList() throws Exception {
       String [][] events = iCalParser.calendarToEventList(this.calendarString);
       assertTrue(events[0][1].equals("DESCRIPTION: d1"));
        assertTrue(events[1][1].equals("DESCRIPTION: d3"));

    }
}
