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

    public void testAddCalEvents() throws Exception {

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\ntest1");
        Date startDate0 = new Date(999999998);
        Date endDate0 = new Date(999999999);
        Date startDate1 = new Date(99999912);
        Date endDate1 = new Date(99999919);
        CalEvent[] calEvents = new CalEvent[2];
        calEvents[0] = new CalEvent("test_timi1","Prufu timi sem er ekki til","Neshagi", startDate0, endDate0);
        calEvents[1] = new CalEvent("test_timi2","Prufu timi sem er heldur ekki til","Laugarvatn", startDate1, endDate1);
        Dabbi dabbi = new Dabbi();
        dabbi.addCalEvents(calEvents);

    }

    public void testGetCalEvents() throws Exception {

    }
}
