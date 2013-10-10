package is.mpg.ruglan;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Date;

import is.mpg.ruglan.CalEvent;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        WebView wv = (WebView) findViewById(R.id.webView);

        String html =
                "<!doctype html>\n" +
                "<html lang=\"is\">\n" +
                "\t<head>\n" +
                "\t\t<meta charset=\"utf-8\">\n" +
                "\t\t<title>Transforms</title>\n" +
                "\t\t<style>\n" +
                "\t\tdiv { width: 300px; height: 300px; background: pink; text-align: center; font-size: 5em; padding-top: 100px; margin: 100px 0 0 100px;}\n" +
                "\n" +
                "\t\tdiv\n" +
                "\t\t{\n" +
                "\t\t\tborder: 2px solid #000;\n" +
                "\t\t\tborder-radius: 1em/1em;\n" +
                "\t\t}\n" +
                "\t\t</style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<div>Rúnuð horn!</div>\n" +
                "\t</body>\n" +
                "</html>";
        

        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadData(html, "text/html", "UTF-8");

        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(isURLMatching(url)) {
                    openNextActivity();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    protected boolean isURLMatching(String url) {
        // some logic to match the URL would be safe to have here
        return true;
    }

    protected void openNextActivity() {
        Intent intent = new Intent(this, CalEventActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
}
