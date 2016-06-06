package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Content Provider
 */
public class MovieProvider extends ContentProvider {
    // URI matcher to assist in allowed ContentProvider accesses
    public static final int MOVIES = 100;
    public static final int MOVIE_ID = 110;
    // URI for this MovieProvider
    private static final String CONTENT_AUTHORITY = "comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp";
    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY + "/" + MoviesContract.TABLE_MOVIES);
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(CONTENT_AUTHORITY, MoviesContract.TABLE_MOVIES, MOVIES);
        sURIMatcher.addURI(CONTENT_AUTHORITY, MoviesContract.TABLE_MOVIES + "/#", MOVIE_ID);
    }

    // SQLite database helper
    private MoviesDbHelper moviesDbHelper;

    @Override
    public boolean onCreate() {
        moviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Retrieve SQLite database
        SQLiteDatabase db = moviesDbHelper.getReadableDatabase();

        Cursor cursor = db.query(MoviesContract.TABLE_MOVIES, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Retrieve SQLite database
        SQLiteDatabase db = moviesDbHelper.getWritableDatabase();

        db.insert(MoviesContract.TABLE_MOVIES, null, values);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Retrieve SQLite database
        SQLiteDatabase db = moviesDbHelper.getWritableDatabase();

        return db.delete(MoviesContract.TABLE_MOVIES, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Retrieve SQLite database
        SQLiteDatabase db = moviesDbHelper.getWritableDatabase();

        db.update(MoviesContract.TABLE_MOVIES, values, selection, selectionArgs);
        return 0;
    }
}
