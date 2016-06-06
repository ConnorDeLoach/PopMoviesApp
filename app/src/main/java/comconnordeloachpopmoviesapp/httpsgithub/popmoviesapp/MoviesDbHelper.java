package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Manages the life cycle of the sqlite database
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    // Database declaration
    static final int DATABASE_VERSION = 16;
    static final String DATABASE_NAME = "PopMoviesAppDB";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    // Called when the database is created for the first time. Tables and data are initialized here.
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(MoviesContract.CREATE_TABLE);
        } catch (SQLException exc) {
            Log.e(MoviesDbHelper.class.toString(), "CREATE_TABLE failed");
            exc.printStackTrace();
        }
    }

    @Override
    // Called when the database version number changes. Do changes to the database here.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL(MoviesContract.DROP_TABLE);
            onCreate(db);
        } catch (SQLException exc) {
            Log.e(MoviesDbHelper.class.toString(), "DROP TABLE failed");
            exc.printStackTrace();
        }
    }
}