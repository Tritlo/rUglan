package is.mpg.ruglan;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class HomeActivity extends Activity {
    CalEvent[] events;


    private static Context sContext;

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
                WebView wv = (WebView) findViewById(R.id.webView);
                wv.loadUrl("javascript:" + getJavascriptForCalEvents());
                wv.loadUrl("javascript: $('#loading').hide();");
            }
        });
        wv.loadUrl("file:///android_asset/WebViewBase.html");
        Date d1 = new Date(113, 9, 15, 12, 0);
        Date d2 = new Date(113, 9, 15, 13, 0);
        Date d3 = new Date(113, 9, 15, 14, 0);
        Date d4 = new Date(113, 9, 15, 15, 0);
        CalEvent [] events = new CalEvent[0];
        try{
            this.events = new iCalParser().execute("http://uc-media.rhi.hi.is/HTSProxies/6566792d312d36362e2f313436.ics").get();
        } catch (Exception ex) {
            this.events = new CalEvent[] {
                    new CalEvent("A", "d1", "VR-II", d1, d2),
                    new CalEvent("B", "f", "HT-104", d3, d4)
            };
        }
    }

    protected boolean isURLMatching(String url) {
        // some logic to match the URL
        return true;
    }

    protected void openCalEventActivity(String url) {
        Intent intent = new Intent(this, CalEventActivity.class);
        String index = "asdf";
        try {
            URL u = new URL(url);
            index = u.getFile().substring(u.getFile().lastIndexOf('/')+1, u.getFile().length());
        } catch (MalformedURLException e) {
            System.out.println("test failed to load url");
        }
        intent.putExtra("CAL_EVENT", this.events[Integer.parseInt(index)]);
        startActivity(intent);
    }

    private String getJavascriptForCalEvents() {
        String javascriptEvents = "events: [";
        for(int i=0; i<this.events.length; i++) {
            if (i!=0) {
                javascriptEvents += ",";
            }
            javascriptEvents += "{"
                    + "title: '" + this.events[i].getName() + "',"
                    + "start: " +this.events[i].getFullCalendarStartDateString() +","
                    + "end: " +this.events[i].getFullCalendarEndDateString() +","
                    + "allDay: false,"
                    + "backgroundColor: '" +this.events[i].getColor() + "',"
                    + "borderColor: 'black',"
                    + "url: '" + i + "'"
                    + "}";
        }
        javascriptEvents += "]";

        return getTextFromAssetsTextFile("JavascriptBase.js").replace("%%%EVENTS%%%",
                                                                        javascriptEvents);
    }
    private String getTextFromAssetsTextFile(String filename) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        AssetManager assetManager = getApplicationContext().getAssets();
        // TODO: Replace getApplicationContext with this.getContext after merging dabbi branch.
        InputStream inputStream = null;

        try{
            inputStream = assetManager.open(filename);
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

            return outputStream.toString();
        } catch (IOException e){
            // TODO: Handle file failed to Load
            System.out.println("test failed to load file");
            return "Failed to load file";
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
    
}
