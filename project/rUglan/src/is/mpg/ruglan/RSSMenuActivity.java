package is.mpg.ruglan;

import is.mpg.ruglan.utils.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RSSMenuActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_rss_menu);
	    
	    LinearLayout listWrapper = 
               (LinearLayout) findViewById(R.id.rss_menu_activity_list_wrapper);
	    
	    try {
	    	String xmlUrl = Utils.rssMenuFeedUrl;
	    	// TODO: Parse xml
	    	
			for(int i=0; i<5; i++) {
		    	TextView tv = new TextView(this);
	            tv.setText("\n\n" + "Item " +i +": " + xmlUrl);
	            tv.setTextAppearance(this, android.R.style.TextAppearance_Small);
	            listWrapper.addView(tv);
		    }
	    }
	    catch (Exception e) {
	    	TextView tv = new TextView(this);
            tv.setText("\n\n" + getString(R.string.rss_menu_error_msg));
            tv.setTextAppearance(this, android.R.style.TextAppearance_Small);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
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
