package is.mpg.ruglan;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by tritlo on 10/28/13.
 */
public class Utils {

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
        int hue_steps = 150;
        int sat_steps = 4;
        colors = new String[hue_steps*sat_steps];
        float sat = 0.4F;
        for(int j = 0; j < sat_steps; j++)
        {
            float hue = 0F;
            for(int i = 0;i<hue_steps;i++)
            {
                String rgb =hsvToRgb(hue,sat,0.95F);
                colors[i+hue_steps*j] = rgb;
                hue += 1.0F/(float)(hue_steps);
                //System.out.println(rgb);
            }
            //We want the sat range to 0.6 long
            sat += (1.0F/(float)sat_steps)*0.6F;
        }
    }
}
