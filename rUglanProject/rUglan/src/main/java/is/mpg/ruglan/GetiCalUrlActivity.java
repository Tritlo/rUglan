package is.mpg.ruglan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.os.SystemClock.sleep;

public class GetiCalUrlActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acvitvity_getical);
        final String javascr = "javascript: function getIcal() {" +
                " if ($(':contains(.ics)').length > 0){ " +
                "var str =  $(':contains(.ics)').last().text().split('. ')[1]; " +
                "$('html').empty().html(str); " +
                " $(document).attr('title',str);" +
                "} else {" +
                " $(document).attr('title','waiting');" +
                "setTimeout(getIcal,1000);" +
                "}" +
                "}" +
                " $(getIcal());";
        WebView wv = (WebView) findViewById(R.id.iCalUrlWebView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.clearCache(true);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                WebView wv = (WebView) findViewById(R.id.iCalUrlWebView);
                wv.loadUrl(javascr);
                String title = wv.getTitle();
                Log.e("title", title);
                while (!(title.contains(".ics"))){
                    if (title.contains(".ics")){
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("iCalUrl",title);
                        finish();
                    }
                    title = wv.getTitle();
                    sleep(2000);
                }
            }
        });
        wv.loadUrl("https://ugla.hi.is/HTS/taflan_min.php?");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.geti_cal_url, menu);
        return true;
    }
}
