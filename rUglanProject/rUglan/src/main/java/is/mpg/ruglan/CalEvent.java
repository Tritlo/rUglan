package is.mpg.ruglan;

import java.util.Calendar;
import java.util.Date;
import java.lang.IllegalArgumentException;

/**
 * Class representing Calendar Event for the project.
 * @author Siggi
 */
public class CalEvent {

    private String name, description, location;
    private Date start, end;
    /**
     *  CalEvent has the following attributes:
     *      - name:
     *          A string holding the name of the event.
     *      - location:
     *          A string holding the location of the event.
     *      - description:
     *          A string holding the description of the event.
     *      - start:
     *          A Date object holding start date and time of the event.
     *      - end:
     *          A Date object holding end date and time of the event.
     */


    /**
     * @use CalEvent e = new CalEvent("A", "B", "C", s, e);
     * @pre s and e have the same date and s < e.
     * @post e is a new instance of type CalEvent. e has the name
     *
     * @param name Name of the event.
     * @param description Description of the event.
     * @param location Location of the event.
     * @param start Start date of the event.
     * @param end End date of the event.
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
     * @use String s = e.getName();
     * @pre e is an instance of CalEvent.
     * @post s is the name of the CalEvent e.
     *
     * @return Name of the event.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @use String s = e.getDescription();
     * @pre e is an instance of CalEvent.
     * @post s is the description of the CalEvent e.
     *
     * @return Description of the event.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @use String s = e.getLocation();
     * @pre e is an instance of CalEvent.
     * @post s is the location of the CalEvent e.
     *
     * @return Location of the event.
     */
    public String getLocation() {
        return this.location;
    }

     /**
      * @use Date d = e.getStart();
      * @pre e is an instance of CalEvent.
      * @post d is the start time of the CalEvent e.
      *
      * @return Start date of the event.
      */
    public Date getStart() {
        return this.start;
    }

    /**
     * @use Date d = e.getEnd();
     * @pre e is an instance of CalEvent.
     * @post d is the end time of the CalEvent e.
     *
     * @return End date of the event.
     */
    public Date getEnd() {
        return this.end;
    }
    
    /**
     * @use s = e.toString();
     * @pre Nothing
     * @post s is a string that represents this event
     *
     * @return A string representation of the event.
     */
    public String toString(){
        return this.getName() + " " + this.getDescription() + " "
            + this.getLocation() + " " + this.getStart() + " " + this.getEnd();
    }

    /**
     * @use s = e.getDurationString();
     * @pre e is an instance of CalEvent
     * @post s is a string on the form "HH:MM - HH:MM" describing the duration of the event.
     *
     * @return A string on the form "HH:MM - HH:MM" describing the duration of the event.
     */
    public String getDurationString(){
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setTime(this.getStart());
        endCal.setTime(this.getEnd());
        return startCal.get(Calendar.HOUR_OF_DAY) + ":"
                + String.format("%02d", startCal.get(Calendar.MINUTE))
                + " - " + endCal.get(Calendar.HOUR_OF_DAY) + ":"
                + String.format("%02d", endCal.get(Calendar.MINUTE));
    }
}
