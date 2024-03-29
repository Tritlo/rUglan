package is.mpg.ruglan.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.lang.IllegalArgumentException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import is.mpg.ruglan.utils.Utils;

/**
 * Class representing Calendar Event for the project.
 * @author Siggi
 */
public class CalEvent implements Serializable, Comparable<CalEvent> {

    private static final long serialVersionUID = 1L;
    private String name, description, location;
    private Date start, end;
	public boolean isLecture;
    private Boolean hidden;
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
     *        -isLecture
     *        	A boolean which says whether this is a lecture or not.
     *      - hidden:
     *      	Has the value true IFF the event should be hidden.
     */


    /**
     * @use CalEvent e = new CalEvent("A", "B", "C", s, e, true);
     * @pre s and e have the same date and s < e.
     * @post e is a new instance of type CalEvent. e has the name
     *
     * @param name Name of the event.
     * @param description Description of the event.
     * @param location Location of the event.
     * @param start Start date of the event.
     * @param end End date of the event.
     * @param hidden true IFF the event should be hidden.
     */
    public CalEvent(String name, String description, String location, Date start, Date end, Boolean hidden) {
        Calendar calStart = Utils.dateToCalendar(start);
        Calendar calEnd = Utils.dateToCalendar(end);
        if (!Utils.isSameDay(calStart, calEnd)) {
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
        this.isLecture = Utils.isLecture(description);
        this.hidden = hidden;
    }
    
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
    	this(name, description, location, start, end, false);
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
     * @use Boolean b = e.isHidden();
     * @pre e is an instance of CalEvent.
     * @post b is true IFF e should be hidden.
     * @return
     */
    public Boolean isHidden() {
    	return this.hidden;
    }
    
    /**
     * @use e.setHidden(b);
     * @pre e is an instance of CalEvent. b is an instance of Boolean.
     * @post Sets the hidden property of e as b. 
     * @return
     */
    public void setHidden(Boolean hidden) {
    	this.hidden = hidden;
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
        Calendar startCal = Utils.dateToCalendar(this.getStart());
        Calendar endCal = Utils.dateToCalendar(this.getEnd());
        return startCal.get(Calendar.HOUR_OF_DAY) + ":"
                + String.format("%02d", startCal.get(Calendar.MINUTE))
                + " - " + endCal.get(Calendar.HOUR_OF_DAY) + ":"
                + String.format("%02d", endCal.get(Calendar.MINUTE));
    }

    /**
     * @use s = e.getDateString();
     * @pre e is an instance of CalEvent
     * @post s is a string on the form "d. M Y"
     *          describing the date of the event.
     *
     * @return A string on the form "d. M Y" describing the date of the event.
     */
    @SuppressLint("SimpleDateFormat")
    public String getDateString(){
        Calendar cal = Utils.dateToCalendar(this.getStart());
        SimpleDateFormat sdf = new SimpleDateFormat("d. MMM yyyy");
        return sdf.format(cal.getTime());
    }

    /**
     * @use s = a.equals(b)
     * @pre a,b is an instance of CalEvent
     * @post s is true if a and b have the same properties, false otherwise
     *
     * @return true if a and b have the same properties, false otherwise
     */
    @Override
    public boolean equals(Object aThat)
    {
        if ( this == aThat ) return true;
        if ( !(aThat instanceof CalEvent) ) return false;
        CalEvent that = (CalEvent) aThat;
        return  that.name.equals(this.name)  &&
                that.location.equals(this.location)  &&
                that.description.equals(this.description)  &&
                that.start.equals(this.start)  &&
                that.end.equals(this.end);
    }

    /**
     * @use s = e.getFullCalendarStartDateString();
     * @pre e is an instance of CalEvent
     * @return  s is a string on the form "new Date(y, m, d, H, M)" that can be used
     *          as a parameter for date in FullCalendar
     */
    public String getFullCalendarStartDateString() {
        Calendar startCal = Utils.dateToCalendar(this.getStart());
        return getFullCalendarDateString(startCal);
    }

    /**
     * @use s = e.getFullCalendarEndDateString();
     * @pre e is an instance of CalEvent
     * @return  s is a string on the form "new Date(y, m, d, H, M)" that can be used
     *          as a parameter for date in FullCalendar
     */
    public String getFullCalendarEndDateString() {
        Calendar endCal = Utils.dateToCalendar(this.getEnd());
        return getFullCalendarDateString(endCal);
    }

    /**
     * @use s = getFullCalendarEndDateString();
     * @return  s is a string on the form "new Date(y, m, d, H, M)" that can be used
     *          as a parameter for date in FullCalendar
     */
    private String getFullCalendarDateString(Calendar cal) {
        return "new Date("
                + cal.get(Calendar.YEAR) +", "
                + cal.get(Calendar.MONTH) +", "
                + cal.get(Calendar.DAY_OF_MONTH) +", "
                + cal.get(Calendar.HOUR_OF_DAY) +", "
                + cal.get(Calendar.MINUTE)
                + ")";
    }

    /**
     * @use s = getColor();
     * @param context
     * @return  s is a string representing a color that can be used
     *          as a parameter for backgroundColor in FullCalendar
     */
    public String getColor(Context context) {
    	if (this.hidden) {
    		return Utils.hiddenColor;
    	}
    	else {
    		Dabbi myDabbi = new Dabbi(context);
            return Utils.colors[myDabbi.getColor(this.name)%Utils.colors.length];
    	}
    }
    /**
     * @use s = getBuilding()
     * @post s contains the name of the building where the event takes place
     */
    public String getBuilding()
    {
    	try{
    		Log.d("DEBUG ",this.name+" has building "+this.location.split("\\\\, ")[1]);
    		return this.location.split(", ")[1];
    	}
    	catch(Exception e)
    	{
    		Log.d("DEBUG", "could not get a building name");
    		return "";
    	}
    }
    /**
     * Returns a link to google maps with the building where the event takes place selected
     * @use s = getGoogleMapsLink()
     * @post s contains a link to google maps with the building where the event takes place selected
     */
    public String getGoogleMapsLink()
    {
    	String building = getBuilding();
    	if (building == "")
    	{
    		return "";
    	}
    	if(Utils.googleMapsLink == null)
    	{
    		Utils.fillGoogleMapsLinkMap();
    	}
    	if(Utils.googleMapsLink.containsKey(building))
    	{
    		return Utils.googleMapsLink.get(building);
    	}
    	return "";
    }
    
     /** Compares the CalEvent to another CalEvent by their starting dates.
     * @use a = compareTo(b)
     * @post a is negative if b happens before this, 0 if they happen on the same time
     * and else its positive
     */
    public int compareTo(CalEvent event)
    {
    	return this.start.compareTo(event.start);
    }

}
