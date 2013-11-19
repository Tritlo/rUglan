package is.mpg.ruglan;

import java.text.SimpleDateFormat;

import is.mpg.ruglan.data.CalEvent;
import is.mpg.ruglan.data.Dabbi;
import is.mpg.ruglan.utils.Utils;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Locale;

public class HideActivity extends Activity {
	private Dabbi dabbi;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_hide);
	    dabbi = new Dabbi(this);
	    
	    LinearLayout listWrapper = 
	    		(LinearLayout) findViewById(R.id.hide_activity_list_wrapper);
	    
	    CalEvent[] calEvents = dabbi.getCalEventsForRecurringEvents();
	    if (calEvents.length > 0) {
		    String[] coursesNames = dabbi.getCalEventsNames();
		    for(int i=0; i<coursesNames.length; i++) {
		    	TextView tv = new TextView(this);
		    	tv.setText(Utils.stripCourseNumberFromName(coursesNames[i]));
		    	tv.setTextAppearance(this, android.R.style.TextAppearance_Medium);
		    	listWrapper.addView(tv);
		    	for(int j=0; j<calEvents.length; j++) {
		    		if (calEvents[j].getName().equals(coursesNames[i])) {
		    			CheckBox cb = new CheckBox(this);
		    			String weekdayShort = 
		    					new SimpleDateFormat("EEE", new Locale("is")).
		    					format(calEvents[j].getStart());
			    		cb.setText(calEvents[j].getDescription() + " "
			    				+  weekdayShort + " " 
			    				+ calEvents[j].getDurationString());
			    		cb.setChecked(calEvents[j].isHidden());
			    		listWrapper.addView(cb);
		    		}
		    	}
		    }
	    }
	    else {
	    	TextView tv = new TextView(this);
	    	tv.setText(getString(R.string.no_cources_found));
	    	tv.setTextAppearance(this, android.R.style.TextAppearance_Medium);
	    	listWrapper.addView(tv);
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
    
    public void saveHidden(View view) {
    	// TODO: Implement the logic for saving
    	Utils.displayMessage("Ath!", "A eftir ad utfaera!!!", this);
    }
}