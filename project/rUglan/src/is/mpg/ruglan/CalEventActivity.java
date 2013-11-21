package is.mpg.ruglan;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.TextView;

import is.mpg.ruglan.data.CalEvent;

public class CalEventActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_event);
        // Show the Up button in the action bar.
        setupActionBar();


        CalEvent e = (CalEvent) getIntent().getSerializableExtra("CAL_EVENT");
        if (e != null) {
            TextView name = (TextView) findViewById(R.id.name_label);
            TextView date = (TextView) findViewById(R.id.date_label);
            TextView time = (TextView) findViewById(R.id.time_label);
            TextView desc = (TextView) findViewById(R.id.desc_label);
            TextView loc = (TextView) findViewById(R.id.loc_label);

            name.setText(e.getName().replaceFirst(" ", "\n"));
            date.setText(e.getDateString());
            time.setText(e.getDurationString());
            desc.setText(e.getDescription());
            loc.setText(e.getLocation());
        }
        
        //Get the building if found and make the link
        String googleMapsLink = e.getGoogleMapsLink();
        if(googleMapsLink != "")
        {
        	LinearLayout buildingLayout =
                (LinearLayout) findViewById(R.id.building_location);
        
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cal_event, menu);
        return true;
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
