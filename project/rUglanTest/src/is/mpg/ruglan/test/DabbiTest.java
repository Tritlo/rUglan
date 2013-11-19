package is.mpg.ruglan.test;

import android.test.AndroidTestCase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
     * Checks if the database is empty, i.e. tests if the run private
     * method called in setUp works as expected.
     * @throws Exception
     */
    public void testDatabaseEmptyAfterTestSetUp() throws Exception {
        CalEvent[] calEventsDabbi = this.dabbi.getAllCalEvents();
        assertEquals("Number of events in Dabbi is " +calEventsDabbi.length + ", " +
                 "but should be 0.",
                 0, calEventsDabbi.length);
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

    /**
     * Tests if the refreshEventTable works as expected.
     * @throws Exception
     */
    public void testRefreshEventsTable() throws Exception {
    	CalEvent[] calEventsDabbiInit = dabbi.getAllCalEvents();
    	assertEquals("Number of events in Dabbi at start of test case is " 
    			+ calEventsDabbiInit.length + ", " + "but should be 0.",
                0, calEventsDabbiInit.length);
        String iCalUrl = "http://uc-media.rhi.hi.is/HTSProxies/6566792d312d36362e2f313436.ics"; //Matti
        dabbi.refreshEventsTable();
        CalEvent[] calEventsDabbi = dabbi.getAllCalEvents();
        CalEvent[] calEventsiCal = iCalParser.urlToCalEvents(iCalUrl);

        assertEquals("Number of events from iCal does not match events from " +
                "Dabbi after refresh. " +
                "Dabbi: " +calEventsDabbi.length + ", " +
                "iCalParser: " + calEventsiCal.length,
                calEventsiCal.length, calEventsDabbi.length);
        List<CalEvent> calEventsDabbiList = Arrays.asList(calEventsDabbi);
        Boolean areEqual = true;
        for(int i=0; i<calEventsiCal.length; i++) {
            if (!calEventsDabbiList.contains(calEventsiCal[i])) {
                areEqual = false;
            }
        }
        assertTrue("Events from iCal does not match events from " +
                "Dabbi after refresh", areEqual);
    }

    /**
     * Tests if the getCalEventsForRecurringEvents works as expected.
     * @throws Exception
     */
    public void testGetCalEventsForRecurringEvents() throws Exception {
    	CalEvent[] calEvents = new CalEvent[3];
    	Date[] date = new Date[calEvents.length*2];
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss", 
    			new Locale("UTC"));
        date[0] = format.parse("20140012T132000");
        date[1] = format.parse("20140012T140000");
        date[2] = format.parse("20140019T132000");
        date[3] = format.parse("20140019T140000");
        date[4] = format.parse("20140012T150000");
        date[5] = format.parse("20140012T154000");
    	
    	calEvents[0] = new CalEvent("Timi_1","f","Neshagi", date[0], date[1]);
    	calEvents[1] = new CalEvent("Timi_1","f","Neshagi", date[2], date[3]);
    	calEvents[2] = new CalEvent("Timi_1","d1","HT", date[4], date[5]);
    	
    	dabbi.addCalEvents(calEvents);
    	CalEvent[] result = dabbi.getCalEventsForRecurringEvents();
    	CalEvent[] expectedResult = new CalEvent[] {calEvents[1], calEvents[2]};
    	
    	assertEquals("Dabbi did not return the expected length of result.", 
    			expectedResult.length, result.length);

    	List<CalEvent> resultList = Arrays.asList(result);
        Boolean areEqual = true;
        for(int i=0; i<expectedResult.length; i++) {
            if (!resultList.contains(expectedResult[i])) {
                areEqual = false;
            }
        }
        assertTrue("Actual results not same as expected results", areEqual);
    }
    
    /**
     * Tests if the getCalEventsNames works as expected.
     * @throws Exception
     */
    public void testGetCalEventsNames() throws Exception {
    	Date startDate0 = new Date(999999998000L);
        Date endDate0 = new Date(999999999000L);
        Date startDate1 = new Date(99999912000L);
        Date endDate1 = new Date(99999919000L);
        CalEvent[] calEvents = new CalEvent[2];
        calEvents[0] = new CalEvent("test_timi1","Ekki til","Neshagi", 
        		startDate0, endDate0);
        calEvents[0] = new CalEvent("test_timi1","Ekki til","Neshagi", 
        		startDate1, endDate1);
        calEvents[1] = new CalEvent("test_timi2","Heldur ekki til","Laugarvatn", 
        		startDate1, endDate1);
        String[] calEventsNames = new String[2];
        calEventsNames[0] = "test_timi1";
        calEventsNames[1] = "test_timi2";
        Dabbi dabbi = new Dabbi(this.getContext());
        dabbi.addCalEvents(calEvents);
        
        String[] dabbiNames = dabbi.getCalEventsNames();
        assertEquals("Number of event names not as expected",
                calEventsNames.length, dabbiNames.length);
        List<String> dabbiNamesList = Arrays.asList(dabbiNames);
        Boolean areEqual = true;
        for(int i=0; i<calEvents.length; i++) {
            if (!dabbiNamesList.contains(calEventsNames[i])) {
                areEqual = false;
            }
        }
        assertTrue("Event names not as expected", areEqual);
    }
}