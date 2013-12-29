package is.mpg.ruglan.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import android.content.SharedPreferences;
import android.text.format.Time;

import java.util.Date;

import is.mpg.ruglan.HomeActivity;

/**
 * An interface for the database backend.
 * @author Jon
 */
public class Dabbi {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private Context context;

    /**
     * @use a = new Dabbi(context)
     * @pre context is a valid non null android context
     * @post a is a pointer to a new Dabbi objext with the
     * context context.
     * @param context A non null android Context object.
     */
    public Dabbi(Context context)
    {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
    }

    /**
     * @use a = new Dabbi()
     * @post a is a pointer to a new Dabbi objext with the
     * context of HomeActivity.
     */
    public Dabbi()
    {
        this(HomeActivity.getContext());
    }

    /**
     * @use addCalEvents(events)
     * @pre events is an array of valid CalEvent elements.
     * @post The CalEvents contained in events have been
     *  added to the database and leaves it in a consistent state.
     *
     * @param calEvents An array of CalEvents to be added.
     */
    public void addCalEvents(CalEvent[] calEvents)
    {
    	Log.d("Dabbi", "Adding " + calEvents.length + " events...");
        rDataBase DB;
        if(context == null)
        {
            System.out.println("warning Null context");
            return;
        }
        DB = new rDataBase(context);
        SQLiteDatabase qdb = DB.getWritableDatabase();
        if(qdb == null)
        {
            System.out.println("warning Null database pointer");
            return;
        }
        for(CalEvent event: calEvents)
        {
            //Check if the event is in the color table and if not add it to it.
            Cursor result = qdb.rawQuery("SELECT color FROM COLORS WHERE name = ?",
                    new String[]{event.getName()});
            if(result.getCount() == 0)
            {
                //Get the highest color value in the table so we know what value to
                //assign to this event.
                Cursor maxColorResult = qdb.rawQuery("SELECT MAX(color) FROM COLORS",null);
                int newColorValue = 0;
                if(maxColorResult.getCount() != 0)
                {
                	maxColorResult.moveToFirst();
                    newColorValue = maxColorResult.getInt(0)+1;
                }
                ContentValues colorValues = new ContentValues();
                colorValues.put("name",event.getName());
                colorValues.put("color", newColorValue);
                qdb.insert("COLORS",null,colorValues);
                Log.d("Dabbi",event.getName()+" got color "+newColorValue);
            }



            ContentValues values = new ContentValues();
            values.put("name", event.getName());
            values.put("description", event.getDescription());
            values.put("location", event.getLocation());
            values.put("start", event.getStart().getTime()/1000);
            values.put("finish", event.getEnd().getTime()/1000);
            values.put("hidden", event.isHidden()? "1" : "0");
            qdb.insert("CALEVENTS",null,values);
        }
        qdb.close();
        Log.d("Dabbi", "Adding done.");
    }

    /**
     * A function to get the color value of an event.
     * @use a = getColor(b);
     * @pre b is a name of an event that is in the database.
     * @post a is the color value of b.
     * @param name The event name we want the color for.
     * @return The color value of name or -1 in case of failure.
     */
    public int getColor(String name)
    {
        rDataBase DB;
        if(context == null)
        {
            System.out.println("warning Null context");
            return -1;
        }
        DB = new rDataBase(context);
        SQLiteDatabase qdb = DB.getWritableDatabase();
        Cursor result = qdb.rawQuery("SELECT color FROM COLORS WHERE name = ?",
                new String[]{name});
        result.moveToFirst();
        return result.getInt(0);
    }

    /**
     * @use events = getCalEvents(start,end)
     * @pre start and end are valid Date objects.
     * @post events contains all the CalEvents contained in
     *  the database that begin between start and end.
     *
     * @param start A Date object that is the earliest date
     *              we want to look at.
     * @param end A Date object that is the latest time an CalEvent
     *            can start at so it is included in the return value.
     */
    public CalEvent[] getCalEvents(Date start, Date end)
    {
        String query = "SELECT * FROM CALEVENTS "
        +"WHERE start BETWEEN ? AND ?";
        String queryArgs[] = new String[]{
                Long.toString(start.getTime()/1000),
                Long.toString(end.getTime()/1000)};
        return getCalEventsForQuery(query, queryArgs);
    }

    /**
     * @use events = getAllCalEvents()
     * @post events contains all the CalEvents contained in the database
     */
    public CalEvent[] getAllCalEvents()
    {
        
        String query ="SELECT * FROM CALEVENTS";
        String queryArgs[] = null;
        return getCalEventsForQuery(query, queryArgs);
    }
    
    /**
     * Deletes all the events in the events table of the database.
     * @use a = clearEventsTable();
     * @pre
     * @post The events table in the database is now empty if a is true else
     * something failed.
     */
    boolean clearEventsTable()
    {
        try
        {
            rDataBase DB = new rDataBase(context);
            SQLiteDatabase qdb = DB.getWritableDatabase();
            try {
                qdb.execSQL("DROP TABLE CALEVENTS");
            } catch (Exception ex) {
                Log.e("Failed to drop table CALEVENTS. " +
                        "Does the table even exist", ex.getMessage());
            }

            DB.executeSQLScript(qdb, "create.sql");
            qdb.close();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Runs a private method of the class if with the correct
     * password.
     * @use a = runPrivateMethod(password);
     * @pre
     * @post a is true if a method matched the password and it was run
     * else a is false
     */
    public boolean runPrivateMethod(String password)
    {
        if(password.equals("there is no cow level"))
        {
            return clearEventsTable();
        }
        return false;
    }

    /**
     * Refreshes the events in the CALEVENTS table.
     * @use refreshEventsTable(iCalUrl);
     * @pre iCalUrl is a path to a valid URL to a valid iCal
     * @post The CALEVENTS table in the database contains fresh data
     * from the iCal url in the iCalUrl setting in the SETTINGS table.
     */
    public void refreshEventsTable(String iCalUrl) throws Exception{
        CalEvent [] calEvents;
        Log.d("Dabbi","refreshing table");
        try{
            Log.d("Dabbi","Trying to fetch from parser");
            calEvents = iCalParser.urlToCalEvents(iCalUrl);
        } catch (Exception ex) {
        	Log.e("Dabbi", ex.getMessage());
            calEvents = null;
        }
        if (calEvents == null){
            Exception e = new Exception("iCal Parsing error");
            throw e;
        }
        clearEventsTable();
        addCalEvents(calEvents);
        Time now = new Time();
        now.setToNow();
        String t = now.format3339(false);
        String[] ts = t.split("T");
        String [] ts1 = ts[1].split(":");
        String tim = ts[0] + " " + ts1[0] + ":" + ts1[1];
        editor.putString("lastUpdate", tim);
        editor.commit();

    }

    public void refreshEventsTable() throws Exception
    {
        String iCalUrl = prefs.getString("iCalUrl","");
        refreshEventsTable(iCalUrl);
    }
    
    /**
     * @use CalEvent[] c = d.getCalEventsForRecurringEvents();
     * @pre d is an instance of Dabbi.
     * @return A list of CalEvents representing 
     * recurring events, on weekly basis
     */
    public CalEvent[] getCalEventsForRecurringEvents() {
    	
    	int secondsInAWeek = 604800;
        String query = "SELECT * FROM CALEVENTS "
        							+"GROUP BY name, location, start % ? "
        							+"ORDER BY description, start";
        String queryArgs[] = new String[]{Integer.toString(secondsInAWeek)};
        return getCalEventsForQuery(query, queryArgs);
    }
    
    /**
     * @use String[] names = d.getCalEventsNames();
     * @pre d is an instance of Dabbi.
     * @return A list of event names in Dabbi.
     */
    public String[] getCalEventsNames() {
    	rDataBase DB = new rDataBase(context);
        SQLiteDatabase qdb = DB.getWritableDatabase();
        if(qdb == null)
        {
            return new String[0];
        }
        Cursor result = qdb.rawQuery("SELECT name, max(start) AS ms FROM CALEVENTS " +
        		"GROUP BY name " +
        		"ORDER BY ms", null);

        //Iterate over the result.
        String[] names = new String[result.getCount()];
        result.moveToFirst();
        int i = 0;
        while(!result.isAfterLast())
        {
            names[i] = result.getString(0);
            i++;
            result.moveToNext();
        }
        qdb.close();
        return names;
    }

    /**
     * @param event is an instance of CalEvent
     * @return A list of CalEvents of same type as event, i.e. has the same
     * name and location and their start times are at the same time each week. 
     */
    public CalEvent[] getEventsLike(CalEvent event) {
    	rDataBase DB = new rDataBase(context);
        SQLiteDatabase qdb = DB.getWritableDatabase();
        if(qdb == null)
        {
            return new CalEvent[0];
        }
        int secondsInAWeek = 604800;
        String query = "SELECT * FROM CALEVENTS "
        							+ "WHERE name=? AND location=? " 
        							+ "AND (start-?)%?=0";
        String queryArgs[] = new String[]{
        									event.getName(),
        									event.getLocation(),
        									Long.toString(
        									   event.getStart().getTime()/1000),
        									Integer.toString(secondsInAWeek)
        							};
        return getCalEventsForQuery(query,queryArgs);
    }
    
    public void changeHiddenForEventsLike(CalEvent event, Boolean hidden) {
    	rDataBase DB = new rDataBase(context);
        SQLiteDatabase qdb = DB.getWritableDatabase();
        int secondsInAWeek = 604800;
        qdb.execSQL("UPDATE CALEVENTS "
        			+ "SET hidden=? "
					+ "WHERE name=? AND location=? " 
					+ "AND (start-?)%?=0",
					new String[]{
        					hidden? "1" : "0",
							event.getName(),
							event.getLocation(),
							Long.toString(
							   event.getStart().getTime()/1000),
							Integer.toString(secondsInAWeek)
					});
    }
    
    private CalEvent[] getCalEventsForQuery(String query, String[] queryArgs)
    {
    	rDataBase DB = new rDataBase(context);
        SQLiteDatabase qdb = DB.getWritableDatabase();
        if(qdb == null)
        {
            return new CalEvent[0];
        }
        Cursor result = qdb.rawQuery(query,queryArgs);

        //Iterate over the result.
        CalEvent[] events = new CalEvent[result.getCount()];
        result.moveToFirst();
        int i = 0;
        while(!result.isAfterLast())
        {
            CalEvent tmpEvent = new CalEvent(result.getString(0),
                                        result.getString(1),result.getString(2)
                    ,new Date(Long.parseLong(result.getString(3))*1000)
                    ,new Date(Long.parseLong(result.getString(4))*1000),
                    result.getInt(5)>0);
            events[i] = tmpEvent;
            i++;
            result.moveToNext();
        }
        qdb.close();
        return events;    
    }
}
