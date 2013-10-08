package is.mpg.ruglan.test;

import android.test.AndroidTestCase;
import is.mpg.ruglan.iCalParser;

/**
 * Created by tritlo on 10/8/13.
 * @author Matti
 */
public class iCalParserTest extends AndroidTestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testUrlToCalEvents() throws Exception {

    }

    public void testUrlToString() throws Exception {
        String hws = "hello, world\n";
        String urlHWS = iCalParser.urlToString("https://notendur.hi.is/mpg3/helloworld.html");
        assertEquals(hws,urlHWS);
    }

    public void testParseCalendar() throws Exception {

    }

    public void testGetICalValue() throws Exception {

    }

    public void testRemoveCR() throws Exception {

    }

    public void testCalendarToEventList() throws Exception {

    }
}
