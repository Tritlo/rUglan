package is.mpg.ruglan.test;

import android.test.AndroidTestCase;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import is.mpg.ruglan.data.CalEvent;
import is.mpg.ruglan.data.Dabbi;
import is.mpg.ruglan.data.iCalParser;

/**
 * Created by jon on 9.10.2013.
 */
public class DabbiTest extends AndroidTestCase {

    private Dabbi dabbi;

    /*
     * All unittests should start with an empty CALEVENTS table.
     */
    public void setUp() throws Exception {
        super.setUp();
        Dabbi dabbi = new Dabbi(this.getContext());
        dabbi.runPrivateMethod("there is no cow level");
        this.dabbi = dabbi;
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

    /**
     * Gets all CalEvents from database and verifies.
     * @throws Exception
     */
    public void testGetAllCalEvents() throws Exception {
        Date startDate0 = new Date(999999998000L);
        Date endDate0 = new Date(999999999000L);
        Date startDate1 = new Date(99999912000L);
        Date endDate1 = new Date(99999919000L);
        CalEvent[] calEvents = {
                new CalEvent("test_timi1","Prufu timi sem er ekki til",
                        "Neshagi", startDate0, endDate0),
                new CalEvent("test_timi2","Prufu timi sem er heldur ekki til",
                        "Laugarvatn", startDate1, endDate1)
        };
        dabbi.addCalEvents(calEvents);

        CalEvent[] currentEvents = dabbi.getAllCalEvents();
        assertTrue("Get all events did not return expected outcome",
                Arrays.deepEquals(calEvents, currentEvents));
    }

    /**
     *Tries to empty the event table in the database
     *@throws java.lang.Exception
     */
     public void testClearEventsTable() throws Exception
     {
         Dabbi dabbi = new Dabbi(this.getContext());

         Date startDate = new Date(999977000L);
         Date endDate = new Date(999999000L);
         CalEvent[] calEvents = new CalEvent[1];
         calEvents[0] = new CalEvent("test_timi4","Prufu timi sem er ekki til","Neshagi", startDate, endDate);
         dabbi.addCalEvents(calEvents);

         System.out.println(dabbi.getCalEvents(new Date(0),new Date(((long)Integer.MAX_VALUE)*1000))[0].toString());
         boolean success = dabbi.runPrivateMethod("there is no cow level");
         assertTrue("Failed to run private method.", success);
         assertEquals(0,dabbi.getCalEvents(new Date(0),new Date(((long)Integer.MAX_VALUE)*1000)).length);
     }

    public void testRefreshEventsTable() throws Exception {
        // TODO: get iCalUrl from shared preferences
        String iCalUrl = "http://uc-media.rhi.hi.is/HTSProxies/6566792d312d36362e2f313436.ics"; //Matti
        dabbi.refreshEventsTable();
        CalEvent[] calEventsDabbi = dabbi.getAllCalEvents();
        CalEvent[] calEventsiCal = new iCalParser().execute(iCalUrl).get();

        assertEquals("Number of events from iCal does not match events from " +
                "Dabbi after refresh",
                calEventsiCal.length, calEventsDabbi.length);
        List calEventsDabbiList = Arrays.asList(calEventsDabbi);
        Boolean areEqual = true;
        for(int i=0; i<calEventsiCal.length; i++) {
            if (!calEventsDabbiList.contains(calEventsiCal[i])) {
                areEqual = false;
            }
        }
        assertTrue("Events from iCal does not match events from " +
                "Dabbi after refresh", areEqual);
    }

}