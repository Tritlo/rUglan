package is.mpg.ruglan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.webkit.CookieManager;
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
                " /*$('html').empty().html(str);*/ " +
                " $(document).attr('title',str);" +
                "} else {" +
                " $(document).attr('title','waiting');" +
                "setTimeout(getIcal,500);" +
                "}" +
                "}" +
                " $(getIcal());";
        WebView wv = (WebView) findViewById(R.id.iCalUrlWebView);
        CookieManager.getInstance().removeAllCookie();
        wv.getSettings().setJavaScriptEnabled(true);
        wv.clearHistory();
        wv.clearCache(true);

        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                WebView wv = (WebView) findViewById(R.id.iCalUrlWebView);
                wv.loadUrl(javascr);
                String title = wv.getTitle();
                if (title.contains("tafla")){
                    sleep(500);
                }
                title = wv.getTitle();
                if (title.contains(".ics")){
                    Intent resultIntent = new Intent();
                    wv.loadUrl("https://ugla.hi.is/vk/logout.php");
                    resultIntent.putExtra("iCalUrl",title);
                    setResult(RESULT_OK,resultIntent);
                    finish();
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
