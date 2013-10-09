package is.mpg.ruglan;
import android.os.AsyncTask;

import java.util.Scanner;
import java.net.URL;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ArrayList;
import java.text.ParseException;
import java.lang.IllegalArgumentException;

/**
 * Parser for iCalendars.
 * @author Matti
 */
public class iCalParser extends AsyncTask<String, Void, CalEvent []>{

    public CalEvent [] doInBackground(String... url){
        return urlToCalEvents(url[0]);
    }

    /**
     * @use events = urlToCalEvents(url)
     * @pre url is a valid url pointing to a iCalendar file
     * @post events is the list of the events in the calendar cal,
     *  null where parsing or arguments were wrong
     *
     * @param url The url to the iCal calendar
     * @return a list of the events in the calendar located at the url
     */
    public static CalEvent [] urlToCalEvents(String url){
        return parseCalendar(urlToString(url));
    }

    /**
     * Takes an url and fetches its contents into a string
     *
     * @use s = urlToString(url)
     * @pre url is a valid url
     * @post s is the string that the url contained if
     *  successful, empty string otherwise.
     *
     * @param url the url of the website to be downloaded
     * @return the string with the content of the url
     */
    public static String urlToString(String url){
        try {
            String ofTheKing = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
            return ofTheKing;
        }
        catch (IOException e){
            return "";
        }
    }

    /**
     * Takes a string containing an iCal calendar and returns a list of the events in the calendar
     *
     * @use events = parseCalendar(cal)
     * @pre cal is a calendar on the ical format,
     * @post events is the list of the events in the calendar cal,
     *  null where parsing or arguments were wrong
     *
     * @param calendar A string of an iCal calendar
     * @return a list of the events in the calendar cal
     */
    public static CalEvent [] parseCalendar(String calendar){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("UTC"));
        String [][] events = calendarToEventList(calendar);
        CalEvent [] calEvents = new CalEvent[events.length-1];//The first in events is calendar info.
        for(int i = 1; i < events.length; i++){
            String name = getICalValue(events[i], "SUMMARY");
            String desc = getICalValue(events[i], "DESCRIPTION");
            String loc  = getICalValue(events[i], "LOCATION");
            String st   = getICalValue(events[i], "DTSTART");
            String en   = getICalValue(events[i], "DTEND");
            //Could be a single block, but android studio warns that
            //
            try {
                Date start = format.parse(st);
                Date end = format.parse(en);
                try {
                calEvents[i-1] = new CalEvent(name,desc,loc,start,end);
                } catch (IllegalArgumentException ex)
                {
                    calEvents[i-1] = null;
                }
            }
            catch (ParseException  ex) {
                calEvents[i-1] = null;
            }
        }
        return calEvents;

    }

    /**
     * Takes an string array containing an iCal event, and returns the
     *  value of the type
     *
     * @use val = getICalValue(e,t)
     * @pre e is a iCal event
     * @post the value of the type t of the iCal event e.
     *
     * @param event the event to be parsed
     * @param type the type of the value to be fished out
     * @return the value of the type type in the event
     */
    public static String getICalValue(String [] event, String type){
        for(int i = 0; i < event.length;i++) {
            if (event[i].startsWith(type)) {
                String[] spli = event[i].split(":");
                return spli.length == 2 ? spli[1] : "";
            }
        }
        return "";
    }

    /**
     * Removes carriage returns in all string in an array of strings
     *
     * @use l = removeCR(ls)
     * @pre ls is a array of strings
     * @post l is the same array of strings with all CR removed
     *
     * @param lines the lines for which the carriage return is to be removed from
     * @return
     */

    public static String [] removeCR(String [] lines){
        String [] ret = new String[lines.length];
        for(int i = 0; i < lines.length; i++){
            ret[i] = lines[i].replaceAll("\r","");
        }
        return ret;
    }

    /**
     * Takes an iCal calendar string, and returns an array of string arrays each
     *  containing an event
     *
     * @use calE = calendarToEvenList(cal)
     * @pre cal is a string that contains an iCal calendar
     * @post calE is an array of array of strings, where each entry is an string array of an iCal
     *  of the events in the calendar cal
     *
     * @param calendar a string that contains an iCal calendar
     * @return an array of the events in the iCal calendar calendar.
     */

    public static String [][] calendarToEventList(String calendar){
        int i = -1, j = 0;
        String [][] ret = new String[1][1];
        ArrayList<String []> events = new ArrayList<String []>();
        while(calendar.indexOf("BEGIN:VEVENT",i+1) >= 0){
            i = calendar.indexOf("BEGIN:VEVENT",i+1);
            j = calendar.indexOf("END:VEVENT",i);
            String substr = calendar.substring(i,j);
            events.add(removeCR(substr.split("\n")));
        }
        return events.toArray(ret);
    }
	
	    
	

}
