package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public long insertData(String column, String data) {
        // Retrieve SQLite database
        SQLiteDatabase db = helper.getWritableDatabase();

        // Create the set of values to insert into database
        ContentValues contentValues = new ContentValues();
        contentValues.put(column, data);

        // Insert value-pair into database
        return db.insert(DBContract.TABLE_NAME, null, contentValues);
    }

    public Cursor queryData(String uid, String[] column) {
        // Retrieve SQLite database
        SQLiteDatabase db = helper.getWritableDatabase();

        // Query the database
        return db.query(DBContract.TABLE_NAME, column, uid, null, null, null, null);
    }

    class DBOpenHelper extends SQLiteOpenHelper {



        public DBOpenHelper(Context context) {
            super(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION);
        }

        @Override
        // Called when the database is created for the first time. Tables and data are initialized here.
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(DBContract.CREATE_TABLE);
            } catch (SQLException exc) {
                Log.e(DBAdapter.class.toString(), "CREATE_TABLE failed");
                exc.printStackTrace();
            }

        }

        @Override
        // Called when the database version number changes. Do changes to the database here.
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            try {
                db.execSQL(DBContract.DROP_TABLE);
                onCreate(db);
            } catch (SQLException exc) {
                Log.e(DBAdapter.class.toString(), "DROP TABLE failed");
                exc.printStackTrace();
            }

        }
    }
}
