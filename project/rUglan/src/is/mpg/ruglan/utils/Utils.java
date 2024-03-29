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
    	"rgb(242,12,12)",
    	"rgb(12,79,242)",
    	"rgb(146,242,12)",
    	"rgb(242,12,213)",
    	"rgb(12,242,203)",
    	"rgb(242,136,12)",
    	"rgb(69,12,242)",
    	"rgb(21,242,12)",
    	"rgb(242,12,89)",
    	"rgb(12,156,242)",
    	"rgb(223,242,12)",
    	"rgb(193,12,242)",
    	"rgb(12,242,126)",
    	"rgb(242,59,12)",
    	"rgb(12,31,242)",
    	"rgb(98,242,12)",
    	"rgb(242,12,166)",
    	"rgb(12,233,242)",
    	"rgb(242,184,12)",
    	"rgb(117,12,242)",
    	"rgb(12,242,49)",
    	"rgb(242,12,41)",
    	"rgb(12,108,242)",
    	"rgb(175,242,12)",
    	"rgb(241,12,242)",
    	"rgb(12,242,174)",
    	"rgb(242,107,12)",
    	"rgb(40,12,242)",
    	"rgb(51,242,12)",
    	"rgb(242,12,118)",
    	"rgb(12,185,242)",
    	"rgb(242,231,12)",
    	"rgb(164,12,242)",
    	"rgb(12,242,97)",
    	"rgb(242,30,12)",
    	"rgb(12,61,242)",
    	"rgb(128,242,12)",
    	"rgb(242,12,195)",
    	"rgb(12,242,221)",
    	"rgb(242,154,12)",
    	"rgb(87,12,242)",
    	"rgb(12,242,20)",
    	"rgb(242,12,70)",
    	"rgb(12,138,242)",
    	"rgb(205,242,12)",
    	"rgb(212,12,242)",
    	"rgb(12,242,144)",
    	"rgb(242,77,12)",
    	"rgb(12,13,242)",
    	"rgb(80,242,12)",
    	"rgb(242,12,147)",
    	"rgb(12,215,242)",
    	"rgb(242,202,12)",
    	"rgb(135,12,242)",
    	"rgb(12,242,68)",
    	"rgb(242,12,23)",
    	"rgb(12,90,242)",
    	"rgb(157,242,12)",
    	"rgb(242,12,224)",
    	"rgb(12,242,192)",
    	"rgb(242,125,12)",
    	"rgb(58,12,242)",
    	"rgb(33,242,12)",
    	"rgb(242,12,100)",
    	"rgb(12,167,242)",
    	"rgb(234,242,12)",
    	"rgb(182,12,242)",
    	"rgb(12,242,115)",
    	"rgb(242,48,12)",
    	"rgb(12,42,242)",
    	"rgb(110,242,12)",
    	"rgb(242,12,177)",
    	"rgb(12,242,240)",
    	"rgb(242,172,12)",
    	"rgb(105,12,242)",
    	"rgb(12,242,38)",
    	"rgb(242,12,52)",
    	"rgb(12,119,242)",
    	"rgb(187,242,12)",
    	"rgb(230,12,242)",
    	"rgb(12,242,163)",
    	"rgb(242,96,12)",
    	"rgb(28,12,242)",
    	"rgb(62,242,12)",
    	"rgb(242,12,129)",
    	"rgb(12,196,242)",
    	"rgb(242,220,12)",
    	"rgb(153,12,242)",
    	"rgb(12,242,86)",
    	"rgb(242,19,12)",
    	"rgb(12,72,242)",
    	"rgb(139,242,12)",
    	"rgb(242,12,206)",
    	"rgb(12,242,210)",
    	"rgb(242,143,12)",
    	"rgb(76,12,242)",
    	"rgb(14,242,12)",
    	"rgb(242,12,82)",
    	"rgb(12,149,242)",
    	"rgb(216,242,12)"};

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
                + "title: '" + Utils.stripCourseNumberFromName(
                						event.getName()) + "',"
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
