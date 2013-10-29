package is.mpg.ruglan;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by tritlo on 10/28/13.
 */
public class Utils {

    public static void displayErrorMessage(String errorMessage, Context ctx) {
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(ctx);
        alertDialogBuilder.setTitle("Error");
        alertDialogBuilder
                .setMessage(errorMessage)
                .setCancelable(false)
                .setPositiveButton("Ok",null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
