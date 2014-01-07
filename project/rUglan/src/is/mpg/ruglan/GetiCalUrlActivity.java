package is.mpg.ruglan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import static android.os.SystemClock.sleep;


/**
 * This class is an activity that facilitates fetching of an iCal url from Ugla.
 * It works by opening a webview,  waiting until the user logs in,
 * and then using jQuery to find the iCal url of the user on his Ugla.
 * this is then passed back to the app via the webpage title.
 * The activity then returns this as a result.
 *
 * @author Matthias Pall Gissurarson
 */
public class GetiCalUrlActivity extends Activity {

	String scheduleUrl;
	
    @TargetApi(19)
	@SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acvitvity_getical);
        scheduleUrl = "https://ugla.hi.is/HTS/taflan_min.php?";
        final String js = "("
        		+ "function getIcal() {"
        		+ " if ($(':contains(.ics)').length > 0){ "
        		+ "	 var str =  $(':contains(.ics)').last().text().split('. ')[1];"
        		+ "	 return str;"
        		+ "	} else {"
        		+ "	 return 'waiting';"
        		+ "	} "
        		+ "})();";
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
                if (!wv.getUrl().startsWith(scheduleUrl)) {
                	wv.loadUrl(scheduleUrl);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	    	        wv.evaluateJavascript(js, new ValueCallback<String>() {
						@Override
					    public void onReceiveValue(String res) {
								if(res.contains(".ics"))
								{
									res = res.replace("\"", "");
						            Intent resultIntent = new Intent();
						            resultIntent.putExtra("iCalUrl",res);
						            setResult(RESULT_OK,resultIntent);
						            WebView wv = (WebView) findViewById(R.id.iCalUrlWebView);
						            wv.loadUrl("https://ugla.hi.is/vk/logout.php");
						            finish();
									
								}
						    } 
						});
                } else {
			        runOnUiThread(new Runnable() {
			            @Override
			            public void run() {
			                // Code for WebView goes here
			            	WebView wv = (WebView) findViewById(R.id.iCalUrlWebView);
			                wv.loadUrl(javascr);
			                String title = wv.getTitle();
			                if (title.contains("tafla")){
			                    sleep(1000);
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
                	
                }
            }
        });
       

        wv.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBariCal);
                pBar.setProgress(progress);
                if (progress == 100){
                    pBar.setVisibility(View.GONE);
                }
                else {
                    pBar.setVisibility(View.VISIBLE);
                }
            }
        });
        wv.loadUrl(scheduleUrl);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.geti_cal_url, menu);
        return true;
    }
}
