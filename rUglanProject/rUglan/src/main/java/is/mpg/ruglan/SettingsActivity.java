package is.mpg.ruglan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends Activity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "rUglanSettings";
    static final int GETURLREQUEST = 0;
    TextView iCalInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        prefs = getSharedPreferences(this.PREFS_NAME, 0);
        editor = this.prefs.edit();
        String iCalUrl = prefs.getString("iCalUrl","");
        iCalInput = (TextView) findViewById(R.id.iCalUrlInput);
        iCalInput.setText(iCalUrl);
        // Show the Up button in the action bar.
        setupActionBar();
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
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getIcalUrl(View view)
    {
        Intent intent = new Intent(this, GetiCalUrlActivity.class);
        startActivityForResult(intent, GETURLREQUEST);
    }

    public void saveSettings(View view) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.show();
        new Thread() {
            public void run() {
                try{
                    editor.putString("iCalUrl",iCalInput.getText().toString());
                    editor.commit();
                    /* TODO: Refresh calendar if changed, when implemented. */
                    sleep(3000);
                } catch (Exception e) {
                    Log.e("iCalUrl", e.getMessage());
                }
                progress.dismiss();
                finish();
            }
        }.start();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
       if (requestCode == GETURLREQUEST){
           if (resultCode == RESULT_OK){
               String iCalUrl = data.getStringExtra("iCalUrl");
               Log.e("res", iCalUrl);
               iCalInput.setText(iCalUrl);
           }
       }
    }

}
