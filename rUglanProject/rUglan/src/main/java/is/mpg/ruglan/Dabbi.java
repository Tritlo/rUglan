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
}
