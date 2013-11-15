package is.mpg.ruglan;

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

public class HideActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_hide);
	    
	    LinearLayout listWrapper = 
	    		(LinearLayout) findViewById(R.id.hide_activity_list_wrapper);
	    
	    // TODO: Get data from Dabbi and display then. This can be done like so:
	    for(int i=0; i<3; i++) {
	    	TextView tv = new TextView(this);
	    	tv.setText("Namskeid " + i);
	    	tv.setTextAppearance(this, android.R.style.TextAppearance_Medium);
	    	listWrapper.addView(tv);
	    	for(int j=0; j<4; j++) {
	    		CheckBox cb = new CheckBox(this);
	    		cb.setText("Timi " + i + "." + j);
	    		cb.setChecked(true);
	    		listWrapper.addView(cb);
	    	}
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