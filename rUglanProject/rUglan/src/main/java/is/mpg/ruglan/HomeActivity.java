package is.mpg.ruglan;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Parcelable;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Date;

import is.mpg.ruglan.CalEvent;

public class HomeActivity extends Activity {
    CalEvent[] events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Date d1 = new Date(113, 9, 15, 12, 0);
        Date d2 = new Date(113, 9, 15, 13, 0);
        Date d3 = new Date(113, 9, 15, 14, 0);
        Date d4 = new Date(113, 9, 15, 15, 0);
        try{
            this.events = new iCalParser().execute("http://uc-media.rhi.hi.is/HTSProxies/6566792d312d36362e2f313436.ics").get();
        } catch (Exception ex) {
            this.events = new CalEvent[] {
                    new CalEvent("A", "d1", "VR-II", d1, d2),
                    new CalEvent("B", "f", "HT-104", d3, d4)
            };
        }

        String html =
                "<!doctype html>\n" +
                "<html lang=\"is\">\n" +
                "\t<head>\n" +
                "\t\t<meta charset=\"utf-8\">\n" +
                "\t\t<title>Events</title>\n" +
                "<style>\n" +
                "\t\tp { background: lightgreen;}\n" +
                "\t\t</style>" +
                "\t</head>\n" +
                "\t<body>\n";

        for(int i=0; i<this.events.length; i++) {
            html += "\t\t<p><a href=\"" + i + "\">" + this.events[i].toString() + "</a></p>\n";
        }
        html += "</body></html>";

        WebView wv = (WebView) findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadData(html, "text/html", "UTF-8");
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(isURLMatching(url)) {
                    openCalEventActivity(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    protected boolean isURLMatching(String url) {
        // some logic to match the URL
        return true;
    }

    protected void openCalEventActivity(String eventIndex) {
        Intent intent = new Intent(this, CalEventActivity.class);
        intent.putExtra("CAL_EVENT", this.events[Integer.parseInt(eventIndex)]);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
}
