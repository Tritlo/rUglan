package is.mpg.ruglan;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * An interface for the database backend.
 * @author Jón
 */
public class Dabbi {

    /**
     * @use addCalEvents(events)
     * @pre events is an array of valid CalEvent elements.
     * @post The CalEvents contained in events have been
     *  added to the database and leaves it in a consistent state.
     *
     * @param events An array of CalEvents to be added.
     */
    public void addCalEvents(CalEvent[] calEvents)
    {
        System.out.println("test1");
        rDataBase DB;
        if(HomeActivity.getContext() == null)
        {
            System.out.println("\n\n\n\n Null context\n\n\n\n\n");
            return;
        }
        DB = new rDataBase(HomeActivity.getContext());
        System.out.println("test1");
        SQLiteDatabase qdb = DB.getWritableDatabase();
        System.out.println("test3");
        if(qdb == null)
        {
            System.out.println("\n\n\n\n Null database pointer\n\n\n\n\n");
            return;
        }
        System.out.println("test4");
        for(CalEvent event: calEvents)
        {
            qdb.rawQuery("INSERT INTO CalEvents VALUE(?,?,?,?,?)",
                         new String[]{event.getName()
                                      ,event.getDescription()
                                      ,event.getLocation()
                                      ,Long.toString(event.getStart().getTime()/1000)
                                      ,Long.toString(event.getEnd().getTime()/1000)});
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
    public static CalEvent[] getCalEvents(Date start, Date end)
    {
        rDataBase DB = new rDataBase(HomeActivity.getContext());
        SQLiteDatabase qdb = DB.getWritableDatabase();
        if(qdb == null)
        {
            return new CalEvent[0];
        }
        Cursor result = qdb.rawQuery("SELECT * FROM CalEvents"
        +"WHERE start >= ?"
        +"AND ? >= start",new String[]{Long.toString(start.getTime()/1000),
                    Long.toString(start.getTime()/1000)});

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
        }

        return events;
    }
}
