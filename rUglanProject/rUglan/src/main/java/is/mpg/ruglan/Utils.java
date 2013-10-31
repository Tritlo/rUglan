package is.mpg.ruglan;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by tritlo on 10/28/13.
 */
public class Utils {

    public static String lastUpdateKey = "lastUpdate";
    public static String iCalURLKey = "iCalUrl";

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
}
