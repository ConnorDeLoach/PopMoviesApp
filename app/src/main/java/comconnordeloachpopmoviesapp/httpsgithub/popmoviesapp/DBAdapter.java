package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by connor on 5/6/16.
 */

public class DBAdapter {

    DBOpenHelper helper;

    public DBAdapter(Context context) {
        helper = new DBOpenHelper(context);
    }

    public long insertData(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBOpenHelper.TITLE, data);
        long id = db.insert(DBOpenHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    class DBOpenHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "PopMoviesAppDB";
        private static final String TABLE_NAME = "APPDATA";
        private static final int DATABASE_VERSION = 1;
        private static final String UID = "_id";
        private static final String TITLE = "Title";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public DBOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        // Called when the database is created for the first time. Tables and data are initialized here.
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
            } catch (SQLException exc) {
                Log.e(DBAdapter.class.toString(), "CREATE_TABLE failed");
                exc.printStackTrace();
            }

        }

        @Override
        // Called when the database version number changes. Do changes to the database here.
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException exc) {
                Log.e(DBAdapter.class.toString(), "DROP TABLE failed");
                exc.printStackTrace();
            }

        }
    }
}
