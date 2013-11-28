package is.mpg.ruglan.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An helper class for an sqlite datebase
 * All credit to  Gaddo F. Benedetti
 * @author  Gaddo F. Benedetti
 * 
 * All we did here is to change values for database name and schema file.
 */
public class rDataBase extends SQLiteOpenHelper {
    final static int DB_VERSION = 1;
    final static String DB_NAME = "rUglaDB.s3db";
    Context context;

    public rDataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        // Store the context for later use
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //This is the first version so there is nothing to update from.
    }

    public void onCreate(SQLiteDatabase database) {
        executeSQLScript(database, "create.sql");
        executeSQLScript(database, "create_settings.sql");
    }

    public void executeSQLScript(SQLiteDatabase database, String dbname) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;

        try{
            inputStream = assetManager.open(dbname);
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

            String[] createScript = outputStream.toString().split(";");
            for (int i = 0; i < createScript.length; i++) {
                String sqlStatement = createScript[i].trim();
                // TODO You may want to parse out comments here
                if (sqlStatement.length() > 0) {
                    database.execSQL(sqlStatement + ";");
                }
            }
        } catch (IOException e){
            // TODO Handle Script Failed to Load
            System.out.println("test failed to load schema");
        } catch (SQLException e) {
            // TODO Handle Script Failed to Execute
            System.out.println("test failed to run schema");
        }
    }
}
