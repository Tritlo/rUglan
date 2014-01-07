package is.mpg.ruglan.utils;

import is.mpg.ruglan.data.CalEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by tritlo on 10/28/13.
 */
public class Utils {
	
	public static HashMap<String,String> googleMapsLink = null;

    public static String lastUpdateKey = "lastUpdate";
    public static String iCalURLKey = "iCalUrl";
    public static String showHiddenKey = "showHiddenEvents";

    public static boolean showHiddenDefaultValue = false;
    public static String hiddenColor = "rgba(20, 22, 24, 0.2)"; 
    public static String[] colors = new String[]{
    	"rgb(165,8,8)",
    	"rgb(8,54,165)",
    	"rgb(100,165,8)",
    	"rgb(165,8,146)",
    	"rgb(8,165,139)",
    	"rgb(165,93,8)",
    	"rgb(47,8,165)",
    	"rgb(14,165,8)",
    	"rgb(165,8,60)",
    	"rgb(8,106,165)",
    	"rgb(152,165,8)",
    	"rgb(132,8,165)",
    	"rgb(8,165,86)",
    	"rgb(165,40,8)",
    	"rgb(8,21,165)",
    	"rgb(67,165,8)",
    	"rgb(165,8,113)",
    	"rgb(8,159,165)",
    	"rgb(165,126,8)",
    	"rgb(80,8,165)",
    	"rgb(8,165,34)",
    	"rgb(165,8,28)",
    	"rgb(8,74,165)",
    	"rgb(120,165,8)",
    	"rgb(165,8,165)",
    	"rgb(8,165,119)",
    	"rgb(165,73,8)",
    	"rgb(27,8,165)",
    	"rgb(35,165,8)",
    	"rgb(165,8,81)",
    	"rgb(8,126,165)",
    	"rgb(165,158,8)",
    	"rgb(112,8,165)",
    	"rgb(8,165,66)",
    	"rgb(165,20,8)",
    	"rgb(8,41,165)",
    	"rgb(87,165,8)",
    	"rgb(165,8,133)",
    	"rgb(8,165,151)",
    	"rgb(165,105,8)",
    	"rgb(59,8,165)",
    	"rgb(8,165,14)",
    	"rgb(165,8,48)",
    	"rgb(8,94,165)",
    	"rgb(140,165,8)",
    	"rgb(145,8,165)",
    	"rgb(8,165,99)",
    	"rgb(165,53,8)",
    	"rgb(8,9,165)",
    	"rgb(55,165,8)",
    	"rgb(165,8,101)",
    	"rgb(8,147,165)",
    	"rgb(165,138,8)",
    	"rgb(92,8,165)",
    	"rgb(8,165,46)",
    	"rgb(165,8,15)",
    	"rgb(8,61,165)",
    	"rgb(107,165,8)",
    	"rgb(165,8,153)",
    	"rgb(8,165,131)",
    	"rgb(165,85,8)",
    	"rgb(39,8,165)",
    	"rgb(22,165,8)",
    	"rgb(165,8,68)",
    	"rgb(8,114,165)",
    	"rgb(160,165,8)",
    	"rgb(125,8,165)",
    	"rgb(8,165,79)",
    	"rgb(165,33,8)",
    	"rgb(8,29,165)",
    	"rgb(75,165,8)",
    	"rgb(165,8,121)",
    	"rgb(8,165,164)",
    	"rgb(165,118,8)",
    	"rgb(72,8,165)",
    	"rgb(8,165,26)",
    	"rgb(165,8,36)",
    	"rgb(8,82,165)",
    	"rgb(127,165,8)",
    	"rgb(157,8,165)",
    	"rgb(8,165,111)",
    	"rgb(165,65,8)",
    	"rgb(19,8,165)",
    	"rgb(42,165,8)",
    	"rgb(165,8,88)",
    	"rgb(8,134,165)",
    	"rgb(165,150,8)",
    	"rgb(104,8,165)",
    	"rgb(8,165,58)",
    	"rgb(165,13,8)",
    	"rgb(8,49,165)",
    	"rgb(95,165,8)",
    	"rgb(165,8,141)",
    	"rgb(8,165,144)",
    	"rgb(165,98,8)",
    	"rgb(52,8,165)",
    	"rgb(10,165,8)",
    	"rgb(165,8,56)",
    	"rgb(8,102,165)",
    	"rgb(148,165,8)"};

   /** 
    * @param description
    * @return whether the description matches the description of a lecture.
    */
    public static Boolean isLecture(String description){
    	return (description.length() == 0 || description.startsWith("f"));
    }
    
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
    
    /**
     * @use	Calendar c = dateToCalendar(d);
     * @pre d is a Date object.
     * @post c has the same date as d.
     * 
     * @param date Date object.
     * @return A calendar object with the same date as the date parameter.
     */
    public static Calendar dateToCalendar(Date date) {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
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
    	//links are something thats opened with the default app for
    	//that type of URI like a http url or a geo uri
    	//geo uri is on the form
    	//geo:latitude,longitude?z=zoom&q=QUERY where zoom is in [1:23] and 1 is
    	//the entire earth and 23 is as close as it gets.
    	//QUERY is a string to put in the search bar of google maps when it is opened
    	//That should do the trick by it self but if you have the map loaded and no Internet
    	//you still get the map zoomed at the correct building if you include the coordinates.
    	String zoom = "18";
    	googleMapsLink = new HashMap<String,String>();
    	googleMapsLink.put("Askja","geo:64.137273,-21.945709?z="+zoom+"&q=64.137273,-21.945709");
    	googleMapsLink.put("VR-1","geo:64.138883,-21.954482?z="+zoom+"&q=64.138883,-21.954482");
    	googleMapsLink.put("VR-2", "geo:64.138736, -21.955587?z="+zoom+"&q=Haskoli Islands VRII");
    	googleMapsLink.put("Haskolatorg","geo:64.139949,-21.950432?z="+zoom+"&q=64.139949,-21.950432");
    	googleMapsLink.put("Haskolabio","geo:64.140333,-21.953967?z="+zoom+"&q=64.140333,-21.953967");
    	googleMapsLink.put("Adalbygging","geo:64.140475,-21.949050?z="+zoom+"&q=64.140475,-21.949050");
    	googleMapsLink.put("Arnagardur","geo:64.138883,-21.951367?z="+zoom+"&q=Arnagardur, Haskoli islands");
    	googleMapsLink.put("Gimli","geo:64.139117,-21.950117?z="+zoom+"&q=64.139117,-21.950117");
    	googleMapsLink.put("Ithrottahus, Saemundargotu 6","geo:64.139517,-21.951083?z="+zoom+"&q=64.139517,-21.951083");
    	googleMapsLink.put("Logberg","geo:64.139483,-21.949667?z="+zoom+"&q=64.139483,-21.949667");
    	googleMapsLink.put("Nyi Gardur","geo:64.13905,-21.94915?z="+zoom+"&q=64.13905,-21.94915");
    	googleMapsLink.put("Oddi","geo:64.138677,-21.950405?z="+zoom+"&q=64.138677,-21.950405");
    	googleMapsLink.put("Stapi","geo:64.141567,-21.947083?z="+zoom+"&q=64.141567,-21.947083"); //TODO: fix this uri
    	googleMapsLink.put("Endurmenntun","geo:64.139717,-21.95585?z="+zoom+"&q=64.139717,-21.95585");
    	googleMapsLink.put("Hagi","geo:64.14355,-21.961883?z="+zoom+"&q=64.14355,-21.961883");
    	googleMapsLink.put("Neshagi 16","geo:64.143283,-21.962260?z="+zoom+"&q=64.143283,-21.962260");
    	googleMapsLink.put("Raunvisindastofnun","geo:64.139917,-21.95555?z="+zoom+"&q=64.139917,-21.95555");
    	googleMapsLink.put("Taeknigardur","geo:64.139483,-21.955217?z="+zoom+"&q=64.139483,-21.955217");
    	googleMapsLink.put("VR-3","geo:64.138883,-21.953517?z="+zoom+"&q=64.138883,-21.953517"); //TODO: This is slightly wrong but
    	googleMapsLink.put("Eirberg","geo:64.1383,-21.925333?z="+zoom+"&q=Eirberg, Eiriksgata 34");  // Haskoli Islands {VRIII, VR3}
    	googleMapsLink.put("Laeknagardur","geo:64.136783,-21.929167?z="+zoom+"&q=64.136783,-21.929167");//is way off.
    }
     /**	
     * Removes the course number from of the course name
     * @param courseName to remove course number from
     * @return the course name but with the course name removed.
     */
    public static String stripCourseNumberFromName(String courseName) {
    	return courseName.replaceAll("^[A-Za-z]+[0-9]{3}[A-Z]?-[0-9]{5}\\s", "");

    }
    
    public static String getJavascriptForCalEvents(CalEvent[] events, Context context) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String javascriptEvents = "events: [";
        for(int i=0; i<events.length; i++) {
        	CalEvent event = events[i];
        	if (event.isHidden() && 
        			!prefs.getBoolean(Utils.showHiddenKey, 
        							Utils.showHiddenDefaultValue)) {
    			// Skip this event
    			continue;
        	}
            if ( !javascriptEvents.endsWith("[") ) {
                javascriptEvents += ",";
            }
            String className = ""; 
            if (!event.isHidden()) {
            	className = event.isLecture ?  "lecture" : "tutorial";
            }
            javascriptEvents += "{"
                + "title: '" + Utils.stripCourseNumberFromName(event.getName()) 
                			 + "\\n" + event.getDescription() + "',"
                + "start: " +event.getFullCalendarStartDateString()+","
                + "end: " +event.getFullCalendarEndDateString() +","
                + "allDay: false,"
                + "backgroundColor: '" +event.getColor(context) + "',"
                + "borderColor: 'black',"
                + "className: '" + className + "',"
                + "url: '" + i + "'"
                + "}";
        }
        javascriptEvents += "]";

        return getTextFromAssetsTextFile("JavascriptBase.js", context)
                .replace("%%%EVENTS%%%", javascriptEvents);
    }
    
    public static String getTextFromAssetsTextFile(String filename, Context context) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        InputStream inputStream = null;

        try{
            Context applicationContext = context;
            AssetManager assetManager = applicationContext.getAssets();
            inputStream = assetManager.open(filename);
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

            return outputStream.toString();
        } catch (IOException e){
            Log.e("TextFromAssets failed", e.getMessage());
        } catch (NullPointerException e){
            Log.e("NullPointerException in getApplicationContext", e.getMessage());
        }
        return "Failed to load file";
    }
    
    /**
     * @param cal1
     * @param cal2
     * @return True IFF cal1 and cal2 are the same day.
     */
    public static Boolean isSameDay(Calendar cal1, Calendar cal2) {
    	return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
