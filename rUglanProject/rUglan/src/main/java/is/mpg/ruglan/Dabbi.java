package is.mpg.ruglan;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * An interface for the database backend.
 * @author Jón
 */
public class Dabbi {

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
    }

    /**
     * @use a = new Dabbi()
     * @post a is a pointer to a new Dabbi objext with the
     * context of HomeActivity.
     */
    public Dabbi()
    {
        this.context = HomeActivity.getContext();
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
            ContentValues values = new ContentValues();
            values.put("name", event.getName());
            values.put("description", event.getDescription());
            values.put("location", event.getLocation());
            values.put("start", event.getStart().getTime()/1000);
            values.put("finish", event.getEnd().getTime()/1000);
            qdb.insert("CALEVENTS",null,values);
        }
        qdb.close();
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
        rDataBase DB = new rDataBase(context);
        SQLiteDatabase qdb = DB.getWritableDatabase();
        if(qdb == null)
        {
            return new CalEvent[0];
        }
        Cursor result = qdb.rawQuery("SELECT * FROM CALEVENTS "
        +"WHERE start BETWEEN ? AND ?" ,new String[]{Long.toString(start.getTime()/1000),
                    Long.toString(end.getTime()/1000)});

        //Iterate over the result.
        CalEvent[] events = new CalEvent[result.getCount()];
        result.moveToFirst();
        int i = 0;
        while(!result.isAfterLast())
        {
            CalEvent event = new CalEvent(result.getString(0),result.getString(1),result.getString(2)
                    ,new Date(Long.parseLong(result.getString(3))*1000)
                    ,new Date(Long.parseLong(result.getString(4))*1000));
            events[i] = event;
            i++;
            result.moveToNext();
        }

        return events;
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
            qdb.execSQL("DROP TABLE CALEVENTS");
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
     * Sets the current iCal url and resets the database
     * if it is different from the old url.
     * @use setiCalUrl(url);
     * @pre
     * @post url is the the database now has only events from
     * the url iCal url.
     */
    public void setiCalUrl(String url)
    {
        rDataBase DB = new rDataBase(context);
        SQLiteDatabase qdb = DB.getWritableDatabase();
        Cursor result = qdb.rawQuery("SELECT val FROM SETTINGS "
                +"WHERE setting = 'iCalUrl'" ,null);
        if(result.getCount() == 1)
        {
            result.moveToFirst();
            if(result.getString(0).equals(url))
            {
                return;
            }
        }
        else if(result.getCount() == 0)
        {
            ContentValues values = new ContentValues();
            values.put("setting", "iCalUrl");
            values.put("val", url);
            qdb.insert("SETTINGS",null,values);
        }
        else
        {
            ContentValues values = new ContentValues();
            values.put("val", url);
            qdb.update("SETTINGS",values,"setting = 'iCalUrl'",null);
        }
        refreshEventsTable();
    }

    /**
     * Gets the iCal url that is currently beeing used from the database
     * @use a = getiCalUrl();
     * @pre
     * @post a is the current iCal url.
     */
    public String getiCalUrl()
    {
        rDataBase DB = new rDataBase(context);
        SQLiteDatabase qdb = DB.getWritableDatabase();
        Cursor result = qdb.rawQuery("SELECT val FROM SETTINGS "
                +"WHERE setting = 'iCalUrl'" ,null);
        if(result.getCount() == 0)
        {
            return null;
        }

        result.moveToFirst();
        return result.getString(0);
    }

    /**
     * Refreshes the events in the CALEVENTS table.
     * @use refreshEventsTable();
     * @pre
     * @post The CALEVENTS table in the database contains fresh data
     * from the iCal url in the iCalUrl setting in the SETTINGS table.
     */
    public void refreshEventsTable()
    {

    }

}
