package is.mpg.ruglan;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Date;

import is.mpg.ruglan.CalEvent;

public class HomeActivity extends Activity {


    private static Context sContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sContext = getApplicationContext();

        ListView eventsListView = (ListView) findViewById(R.id.events_list);

        Date d1 = new Date(113, 9, 15, 12, 0);
        Date d2 = new Date(113, 9, 15, 13, 0);
        Date d3 = new Date(113, 9, 15, 14, 0);
        Date d4 = new Date(113, 9, 15, 15, 0);
        CalEvent [] events = new CalEvent[0];
        try{
            events = new iCalParser().execute("http://uc-media.rhi.hi.is/HTSProxies/6566792d312d36362e2f313436.ics").get();
        } catch (Exception ex) {
            events = new CalEvent[] {
                    new CalEvent("A", "d1", "VR-II", d1, d2),
                    new CalEvent("B", "f", "HT-104", d3, d4)
            };
        }

        String[] eventStrings = new String[events.length];
        for (int i=0; i<eventStrings.length; i++) {
            CalEvent e = events[i];
            eventStrings[i] = e.getName() + "\n" + e.getDescription() + " "
                    + e.getDurationString() + " @ " +e.getLocation();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, eventStrings);

        eventsListView.setAdapter(adapter);









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
    public static Context getContext() {
        return sContext;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
}
