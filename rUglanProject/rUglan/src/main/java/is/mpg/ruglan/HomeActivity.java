package is.mpg.ruglan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeActivity extends Activity {
    private static Context sContext;
    private CalEvent[] events = {};
    private Dabbi dabbi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        WebView wv = (WebView) findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(isURLMatching(url)) {
                    openCalEventActivity(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        Context sContext = getApplicationContext();

            @Override
            public void onPageFinished(WebView view, String url) {
                updateCalEventsInFullCalendar();
                WebView wv = (WebView) findViewById(R.id.webView);
                wv.loadUrl("javascript: $('#loading').hide();");
            }
        });
        wv.loadUrl("file:///android_asset/WebViewBase.html");

        try{
            dabbi = new Dabbi(this);
            this.events = dabbi.getAllCalEvents();
        } catch (Exception ex) {
            this.displayErrorMessage("Failed to load events.");
            Log.e("Dabbi failed to load data.", ex.getMessage());
        }
    }

    protected boolean isURLMatching(String url) {
        // some logic to match the URL
        return true;
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

    private String getJavascriptForCalEvents() {
        String javascriptEvents = "events: [";
        for(int i=0; i<this.events.length; i++) {
            if (i!=0) {
                javascriptEvents += ",";
            }
            javascriptEvents += "{"
                + "title: '" + this.events[i].getName() + "',"
                + "start: " +this.events[i].getFullCalendarStartDateString()+","
                + "end: " +this.events[i].getFullCalendarEndDateString() +","
                + "allDay: false,"
                + "backgroundColor: '" +this.events[i].getColor() + "',"
                + "borderColor: 'black',"
                + "url: '" + i + "'"
                + "}";
        }
        javascriptEvents += "]";

        return getTextFromAssetsTextFile("JavascriptBase.js")
                .replace("%%%EVENTS%%%", javascriptEvents);
    }

    private String getTextFromAssetsTextFile(String filename) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        InputStream inputStream = null;

        try{
            Context applicationContext = getApplicationContext();
            AssetManager assetManager = applicationContext.getAssets();
            inputStream = assetManager.open(filename);
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

            return outputStream.toString();
        } catch (IOException e){
            Log.e("TextFromAssets failed", e.getMessage());
        } catch (NullPointerException e){
            Log.e("NullPointerException in getApplicationContext", e.getMessage());
        }
        return "Failed to load file";
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
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateCalEventsInFullCalendar() {
        WebView wv = (WebView) findViewById(R.id.webView);
        wv.loadUrl("javascript:" + getJavascriptForCalEvents());
    }

    /**
     * @use updateLastUpdatedLabel();
     * @post The timestamp in lastUpdatedLabel has been
     * updated with info from Dabbi.
     */
    private void updateLastUpdatedLabel() {
        // TODO: Use Shared preferences to get lastUpdated string.
        String dabbiLastUpdated = "Label updated!!!!";
        TextView t= (TextView)findViewById(R.id.lastUpdatedLabel);
        t.setText(getString(R.string.lastUpdated) + dabbiLastUpdated);
    }

    /**
     * @use refresh();
     * @post The calendar content is refreshed.
     */
    private void refresh() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
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
                WebView wv = (WebView) findViewById(R.id.webView);
                wv.loadUrl("javascript:" + getJavascriptForCalEvents());
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
                updateLastUpdatedLabel();
            }
            else {
                displayErrorMessage("Failed to load new data.");
            }
            this.progress.dismiss();
        }
    }

    private void displayErrorMessage(String errorMessage) {
        System.out.println("test display");
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Error");
        alertDialogBuilder
                .setMessage(errorMessage)
                .setCancelable(false)
                .setPositiveButton("Ok",null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
