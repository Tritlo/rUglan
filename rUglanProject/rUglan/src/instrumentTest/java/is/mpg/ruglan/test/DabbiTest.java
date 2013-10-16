package is.mpg.ruglan.test;

import android.test.AndroidTestCase;

import java.util.Date;

import is.mpg.ruglan.CalEvent;
import is.mpg.ruglan.Dabbi;
import is.mpg.ruglan.rDataBase;

/**
 * Created by jon on 9.10.2013.
 */
public class DabbiTest extends AndroidTestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tries to add events to the database
     * @throws Exception
     */
    public void testAddCalEvents() throws Exception {

        Date startDate0 = new Date(999999998000L);
        Date endDate0 = new Date(999999999000L);
        Date startDate1 = new Date(99999912000L);
        Date endDate1 = new Date(99999919000L);
        CalEvent[] calEvents = new CalEvent[2];
        calEvents[0] = new CalEvent("test_timi1","Prufu timi sem er ekki til","Neshagi", startDate0, endDate0);
        calEvents[1] = new CalEvent("test_timi2","Prufu timi sem er heldur ekki til","Laugarvatn", startDate1, endDate1);
        Dabbi dabbi = new Dabbi(this.getContext());
        dabbi.addCalEvents(calEvents);

    }

    /**
     * Tries to add an event to the database and then extract
     * it back from the database and compare it with the original.
     * @throws Exception
     */
    public void testGetCalEvents() throws Exception {
        Dabbi dabbi = new Dabbi(this.getContext());

        Date startDate = new Date(99977000L);
        Date endDate = new Date(99999000L);
        CalEvent[] calEvents = new CalEvent[1];
        calEvents[0] = new CalEvent("test_timi3","Prufu timi sem er ekki til","Neshagi", startDate, endDate);
        dabbi.addCalEvents(calEvents);

        CalEvent[] events = dabbi.getCalEvents(startDate,endDate);
        assertEquals(events[0].toString(), calEvents[0].toString());

    }
}
