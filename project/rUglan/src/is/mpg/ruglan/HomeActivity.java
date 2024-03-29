package is.mpg.ruglan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

import is.mpg.ruglan.data.CalEvent;
import is.mpg.ruglan.data.Dabbi;
import is.mpg.ruglan.utils.Utils;

public class HomeActivity extends Activity {
    //CalEvent[] events;
    SharedPreferences prefs;
    private static Context sContext;
    private CalEvent[] events = {};
    static final int SETTINGSREQUEST = 0;
    static final int HIDEREQUEST = 1;
    private Dabbi dabbi;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setting variables and other settings
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sContext = this;
        WebView wv = (WebView) findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                WebView wv = (WebView) findViewById(R.id.webView);
                TextView loadingText = (TextView)
                        findViewById(R.id.loadingCalendarView);
                ProgressBar pBar = (ProgressBar)
                        findViewById(R.id.progressBarCalendarView);
                pBar.setProgress(progress);
                if (progress == 100){
                    wv.setVisibility(View.VISIBLE);
                    pBar.setVisibility(View.GONE);
                    loadingText.setVisibility(View.GONE);
                    Utils.setCalendarViewByOrientation(HomeActivity.this, wv);
                }
                else {
                    wv.setVisibility(View.GONE);
                    pBar.setVisibility(View.VISIBLE);
                    loadingText.setVisibility(View.VISIBLE);
                }
            }
        });

        // Load data from Dabbi
        try{
            dabbi = new Dabbi(this);
            this.events = dabbi.getAllCalEvents();
        } catch (Exception ex) {
            this.events = null;
            Utils.displayMessage("Error", "Failed to load events.", getApplicationContext());
            Log.e("Dabbi failed to load data.", ex.getMessage());
        }
        //To do: Error handling, dialog or otherwise
        if (this.events == null){
            /* TODO: Handle more gracefully (design question) */
            Utils.displayMessage("Error", getString(R.string.Invalid_iCal_alert), this);
            this.events = new CalEvent[0];
        }
        updateCalEventsInFullCalendar();


        // After loading is done, handle events in WebView
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                openCalEventActivity(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                WebView wv = (WebView) findViewById(R.id.webView);
                wv.loadUrl("javascript: $('#loading').hide();");
                if (!prefs.contains(Utils.lastUpdateKey)) {
                    wv.loadUrl("javascript: $('#no-ical').show();");
                    Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivityForResult(intent,SETTINGSREQUEST);
                }
                else {
                    wv.loadUrl("javascript: $('#no-ical').hide();");
                    wv.loadUrl("javascript:" + Utils.getJavascriptForCalEvents(
                    			events, HomeActivity.this));
                    updateLastUpdatedLabel();
                    Utils.setCalendarViewByOrientation(HomeActivity.this, wv);
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        WebView wv = (WebView) findViewById(R.id.webView);
        Utils.setCalendarViewByOrientation(this, wv);
    }
    
    protected void openCalEventActivity(String url) {
        Intent intent = new Intent(this, CalEventActivity.class);
        try {
            URL u = new URL(url);
            String index = u.getFile().substring(u.getFile().lastIndexOf('/')+1,
                                            u.getFile().length());
            intent.putExtra("CAL_EVENT", this.events[Integer.parseInt(index)]);
            startActivity(intent);
        } catch (MalformedURLException e) {
            Log.e("MalformedURLException", e.getMessage());
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivityForResult(settingsIntent, SETTINGSREQUEST);
                return true;
            case R.id.action_hide:
                Intent hideIntent = new Intent(this, HideActivity.class);
                startActivityForResult(hideIntent, HIDEREQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateCalEventsInFullCalendar() {
        WebView wv = (WebView) findViewById(R.id.webView);
        wv.loadUrl("file:///android_asset/WebViewBase.html");
    }

    /**
     * @use updateLastUpdatedLabel();
     * @post The timestamp in lastUpdatedLabel has been
     * updated with info from Dabbi.
     */
    private void updateLastUpdatedLabel() {
        String dabbiLastUpdated = prefs.getString(Utils.lastUpdateKey,"");
        WebView wv = (WebView) findViewById(R.id.webView);
        wv.loadUrl("javascript: $('#last-updated-label').html('"
                + getString(R.string.last_updated_prefix) + " " + dabbiLastUpdated
                + "');");
    }

    /**
     * @use refresh();
     * @post The calendar content is refreshed.
     */
    private void refresh() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.loading));
        progress.setMessage(getString(R.string.wait));
        new refreshData(progress).execute();
    }

    private class refreshData extends AsyncTask<Void, Void, Void> {

        private Boolean success = false;
        private ProgressDialog progress;

        public refreshData(ProgressDialog progress) {
            this.progress = progress;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                dabbi.refreshEventsTable();
                events = dabbi.getAllCalEvents();
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
               updateCalEventsInFullCalendar();
            } else {
                Utils.displayMessage(getString(R.string.error),
                getString(R.string.Invalid_iCal_alert), sContext);
            }
            this.progress.dismiss();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == SETTINGSREQUEST || requestCode == HIDEREQUEST){
            if (resultCode == RESULT_OK){
                Boolean eventsChanged = data.getBooleanExtra("eventsChanged", false);
                if (eventsChanged)
                {
                    //Have to refresh screen after return from
                    //settings, as view might have been updated
                    events = dabbi.getAllCalEvents();
                    updateCalEventsInFullCalendar();
                }
            }
        }
    }

}
