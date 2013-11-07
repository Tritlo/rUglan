package is.mpg.ruglan;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import is.mpg.ruglan.data.Dabbi;
import is.mpg.ruglan.utils.Utils;

/**
 * This class is an activity to facilitate changing of user settings.
 * It allows the user to change the settings that are used in the app,
 * and also provides a link for automatically fetching the iCal url.
 *
 * The settings are then stored in the apps default SharedPreferences.
 *
 * @author Matthias Pall Gissurarson
 */
public class SettingsActivity extends Activity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    static final int GETURLREQUEST = 0;
    private static Context sContext;
    TextView iCalInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sContext = this;
        setContentView(R.layout.activity_settings);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = this.prefs.edit();
        String iCalUrl = prefs.getString(Utils.iCalURLKey,"");
        iCalInput = (TextView) findViewById(R.id.iCalUrlInput);
        iCalInput.setText(iCalUrl);
        // Show the Up button in the action bar.
        setupActionBar();
        if (!prefs.contains(Utils.iCalURLKey)) {
            Utils.displayMessage(getString(R.string.note),
                    getString(R.string.iCalUrlRequired), this);
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
        if (iCalInput.getText().toString().equals("")) {
            Utils.displayMessage(getString(R.string.error),
                    getString(R.string.iCalUrlRequired), this);
        }
        else {
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle(getString(R.string.loading));
            progress.setMessage(getString(R.string.wait));
            progress.show();
            new saveSettings(progress).execute();
        }
    }

    private class saveSettings extends AsyncTask<Void, Void, Void> {

        private Boolean success = false;
        private ProgressDialog progress;

        public saveSettings(ProgressDialog progress) {
            this.progress = progress;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String current = prefs.getString(Utils.iCalURLKey,"");
                String iCalInputText = iCalInput.getText().toString();
                Boolean changed = !(current.equals(iCalInputText));
                if (changed){
                    editor.putString(Utils.iCalURLKey, iCalInputText);
                    //editor.commit();
                    Dabbi dabbi = new Dabbi(sContext);
                    dabbi.refreshEventsTable(iCalInputText);
                }
                editor.commit();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("eventsChanged",changed);
                setResult(RESULT_OK,resultIntent);
                success = true;
            } catch (Exception e) {
                Log.e("Failed renew data.", e.getMessage());
                this.progress.dismiss();
                // In onPostExecute an error alert message will be sent.
            }
            return null;
        }

        @Override
        public void onPreExecute() {
            this.progress.show();
        }

        @Override
        protected void onPostExecute(Void v) {
            if (this.success) {
                finish();
            } else {
                Utils.displayMessage(getString(R.string.error),
                        getString(R.string.failedToLoadNewData), sContext);
            }
            this.progress.dismiss();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
       if (requestCode == GETURLREQUEST){
           if (resultCode == RESULT_OK){
               String iCalUrl = data.getStringExtra(Utils.iCalURLKey);
               Log.e("res", iCalUrl);
               iCalInput.setText(iCalUrl);
               Utils.displayMessage(getString(R.string.note),
                       getString(R.string.successfullyGotiCalUrl), this);
           }
       }
    }

}
