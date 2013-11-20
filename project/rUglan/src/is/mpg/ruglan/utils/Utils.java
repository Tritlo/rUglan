package is.mpg.ruglan.utils;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.webkit.WebView;

/**
 * Created by tritlo on 10/28/13.
 */
public class Utils {
	
	public static HashMap<String,String> googleMapsLink = null;

    public static String lastUpdateKey = "lastUpdate";
    public static String iCalURLKey = "iCalUrl";

    public static String[] colors = new String[] {
            "red",
            "#999",
            "#b39",
            "brown"
    };


    public static void displayMessage(String messageHeader, String messageBody, Context ctx) {
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(ctx);
        alertDialogBuilder.setTitle(messageHeader);
        alertDialogBuilder
                .setMessage(messageBody)
                .setCancelable(false)
                .setPositiveButton("Ok",null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private static String hsvToRgb(float hue, float saturation, float value) {

        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0: return rgbToString(value, t, p);
            case 1: return rgbToString(q, value, p);
            case 2: return rgbToString(p, value, t);
            case 3: return rgbToString(p, q, value);
            case 4: return rgbToString(t, p, value);
            case 5: return rgbToString(value, p, q);
            default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
    }
    private static String rgbToString(float r, float g, float b) {
        String rs = Integer.toHexString((int)(r * 256));
        String gs = Integer.toHexString((int)(g * 256));
        String bs = Integer.toHexString((int)(b * 256));
        return "#"+rs + gs + bs;
    }

    /**
     * Fills the Utils.colors array with in some sense good colors.
     * @use fillColorsArray();
     * @pre
     * @post The Utils.colors array contains strings that css takes as colors and
     * are in some sense good.
     */
    public static void fillColorsArray(){
        int hue_steps = 50;
        int sat_steps = 1;
        double golden_ratio_conjugate = 0.618033988749895;
        colors = new String[hue_steps*sat_steps];
        float sat = 0.5F;
        for(int j = 0; j < sat_steps; j++)
        {
            float hue = 0F;
            for(int i = 0;i<hue_steps;i++)
            {
                String rgb =hsvToRgb(hue,sat,0.95F);
                colors[i+hue_steps*j] = rgb;
                //hue += 1.0F/(float)(hue_steps);
                hue += golden_ratio_conjugate;
                hue %= 1;
                //System.out.println(rgb);
            }
            //We want the sat range to 0.5 long
            sat += (1.0F/(float)sat_steps)*0.5F;
        }
    }
    
    /**
     * 
     * @param c The Current context of the application.
     * @param wv The WebView that contains the FullCalendar object.
     * 
     * Sets the agenda view as Week if the orientation is landscape
     * and to Day if the orientation is portrait.
     */
    public static void setCalendarViewByOrientation(Context context, WebView wv) {
    	int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == 2) {
        	// Landscape
        	wv.loadUrl("javascript: $('.fc-button-agendaWeek').click();");
        }
        if (orientation == 1) {
        	// Portrait
        	wv.loadUrl("javascript: $('.fc-button-agendaDay').click();");
        }
    }
    
    /**
     * Initializes the googleMapsLink map.
     * @use fillGoogleMapsLinkMap()
     * @post the utils.googleMapsLink map now contains the google maps
     * links for all known buildings.
     */
    public static void fillGoogleMapsLinkMap()
    {
    	googleMapsLink = new HashMap<String,String>();
    	googleMapsLink.put("Askja","https://maps.google.com/maps?q=64.137259,-21.945772&hl=en&ll=64.137263,-21.94577&spn=0.002743,0.009871&sll=37.0625,-95.677068&sspn=40.681389,80.859375&t=m&z=17");
    	googleMapsLink.put("VR-1","https://maps.google.com/maps?q=64.139028,-21.956618&hl=en&ll=64.139032,-21.956627&spn=0.001371,0.004935&sll=37.0625,-95.677068&sspn=40.681389,80.859375&t=m&z=18");
    	googleMapsLink.put("VR-2", "https://maps.google.com/maps?q=64.139376,-21.957529&hl=en&ll=64.139376,-21.957529&spn=0.001371,0.004935&sll=37.0625,-95.677068&sspn=40.681389,80.859375&t=m&z=18");
    	googleMapsLink.put("Haskolatorg","https://maps.google.com/maps?q=64.140085,-21.950278&hl=en&ll=64.139968,-21.949997&spn=0.002742,0.009871&sll=37.0625,-95.677068&sspn=40.681389,80.859375&t=m&z=17");
    }
}
