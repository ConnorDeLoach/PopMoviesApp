package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Manages the life cycle of the sqlite database
 */

public class DBAdapter {

    DBOpenHelper helper;

    public DBAdapter(Context context) {
        helper = new DBOpenHelper(context);
    }

    public long insertRow(ContentValues contentValues) {
        // Retrieve SQLite database
        SQLiteDatabase db = helper.getWritableDatabase();

        // Insert value-pair into database
        return db.insert(DBContract.TABLE_NAME, null, contentValues);
    }

    public Cursor queryDatabase(String type) {
        // Retrieve SQLite database
        SQLiteDatabase db = helper.getWritableDatabase();

        // Query the database
        return db.query(DBContract.TABLE_NAME, null, DBContract.TYPE + "=?", new String[]{type}, null, null, null);
    }

    public Cursor queryRow(String row, String[] columnNames) {
        // Retrieve SQLite database
        SQLiteDatabase db = helper.getWritableDatabase();

        // Query the database
        return db.query(DBContract.TABLE_NAME, columnNames, DBContract.UID + "=?", new String[]{row}, null, null, null);
    }

    public int deleteRows(String[] movieList, String movieType) {
        // Retrieve SQLite database
        SQLiteDatabase db = helper.getWritableDatabase();

        // Number of arguments
        String arguments = "";
        for (int i = 1; i < movieList.length; i++) {
            arguments = arguments + ", ?";
        }

        // Delete row
        return db.delete(DBContract.TABLE_NAME, DBContract.UID + " NOT IN (?" + arguments + ") AND " + DBContract.TYPE + " =\' " + movieType + "\'", movieList);
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
