package is.mpg.ruglan;
import java.util.Scanner;
import java.net.URL;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.ParseException;
import java.lang.IllegalArgumentException;
import is.mpg.ruglan.CalEvent;
//import java.util.regex.Pattern;

/**
 * Parser for iCalendars.
 */
public class iCalParser {

    /**
    * Use: events = urlToCalEvents(url)
    * Pre: url is a valid url pointing to a iCalendar file
    * 	where the first event starts on line 8,
    * 	each event is 10 lines,
    * 	and the last line is not part of an event
    * Post: events is the list of the events in the calendar cal,
    *  null where parsing or arguments were wrong
    */
    public static CalEvent [] urlToCalEvents(String url){
        return parseCalendar(urlToString(url));
    }

    /**
    * Use: s = urlToString(url)
    * Pre: url is a valid url
    * Post: s is the string that the url contained if
    *  successful, empty string otherwise.
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
     * Use: events = parseCalendar(cal)
     * Pre: cal is a calendar on the ical format,
     * 	where the first event starts on line 8,
     * 	each event is 10 lines,
     * 	and the last line is not part of an event
     * Post: events is the list of the events in the calendar cal,
     *  null where parsing or arguments were wrong
     */
    public static CalEvent [] parseCalendar(String calendar){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("UTC"));
        String [] lines = calendar.split("\n");
		/*Each event takes 10 lines, 7 lines at start and
		 * 1 line at finish define the calendar. */
        int numEvents = (lines.length - 8)/10;
        CalEvent [] calEvents = new CalEvent[numEvents];
        for(int i = 7; i < lines.length - 1; i+= 10){
            String name = lines[i+7].split(":")[1].replaceAll("\r","");
            String desc = lines[i+1].split(":")[1].replaceAll("\r","");
            String loc = lines[i+5].split(":")[1].replaceAll("\r","");
            String st = lines[i+4].split(":")[1].replaceAll("\r","");
            String en = lines[i+2].split(":")[1].replaceAll("\r","");
            try {
                Date start = format.parse(st);
                Date end = format.parse(en);
                try{
                    calEvents[(i-7)/10] = new CalEvent(name,desc,loc,start,end);
                }
                catch (IllegalArgumentException e) {
                    calEvents[(i-7)/10]  = null;
                }
            }
            catch (ParseException ex) {
                calEvents[(i-7)/10] = null;
            }
        }
        return calEvents;

    }

}
