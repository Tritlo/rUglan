package is.mpg.ruglan;

import java.util.Calendar;
import java.util.Date;
import java.lang.IllegalArgumentException;

public class CalEvent {

    private String name, description, location;
    private Date start, end;
    /**
    *   CalEvent has the following attributes:
    *       - name:
    *           A string holding the name of the event.
    *       - location:
    *           A string holding the location of the event.
    *       - description:
    *           A string holding the description of the event.
    *       - start:
    *           A Date object holding start date and time of the event.
    *       - end:
    *           A Date object holding end date and time of the event.
    */


    /**
    *   Usage:  CalEvent e = new CalEvent("A", "B", "C", s, e);
    *   Before: s and e have the same date and s < e.
    *   After:  e is a new instance of type CalEvent. e has the
    *           name
    */
    public CalEvent(String name, String description, String location, Date start, Date end) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(start);
        cal2.setTime(end);
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        if (!sameDay) {
            throw new
            IllegalArgumentException("start date and end date need to be on the same day.");
        }
        if (start.compareTo(end) >= 0) {
            throw new
            IllegalArgumentException("end date needs to be after start date.");
        }

        this.name = name;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;

    }

    /**
    *   Usage:  String s = e.getName();
    *   Before: e is an instance of CalEvent.
    *   After:  s is the name of the CalEvent e.
    */
    public String getName() {
        return this.name;
    }

    /**
    *   Usage:  String s = e.getDescription();
    *   Before: e is an instance of CalEvent.
    *   After:  s is the description of the CalEvent e.
    */
    public String getDescription() {
        return this.description;
    }

    /**
    *   Usage:  String s = e.getLocation();
    *   Before: e is an instance of CalEvent.
    *   After:  s is the location of the CalEvent e.
    */
    public String getLocation() {
        return this.location;
    }

    /**
    *   Usage:  Date d = e.getStart();
    *   Before: e is an instance of CalEvent.
    *   After:  d is the start time of the CalEvent e.
    */
    public Date getStart() {
        return this.start;
    }

    /**
    *   Usage:  Date d = e.getEnd();
    *   Before: e is an instance of CalEvent.
    *   After:  d is the end time of the CalEvent e.
    */
    public Date getEnd() {
        return this.end;
    }
    
    /**
     * Usage: s = e.toString();
     * Pre: Nothing;
     * Post: s is a string that represents this event
     */
    public String toString(){
        return this.getName() + "" + this.getDescription() + " "
            + this.getLocation() + " " + this.getStart() + " " + this.getEnd();
    }
}
