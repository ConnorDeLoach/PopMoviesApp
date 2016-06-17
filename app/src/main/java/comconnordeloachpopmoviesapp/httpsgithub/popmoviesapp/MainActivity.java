package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.Async.AsyncCallback;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.Async.MyAsyncTask;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.Async.ReviewAsyncTask;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.Async.TrailerAsyncTask;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.Utils.StringUtils;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.db.MovieProvider;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.db.MoviesContract;

public class MainActivity extends AppCompatActivity {

    private final String MAINFRAGMENTTAG = MainFragment.class.toString();
    private final int UID = 0;
    private final int TRAILER = 1;
    private final int REVIEWS = 2;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Query moviedb API for movies, trailers, and reviews
        MyAsyncTask myAsyncTask = new MyAsyncTask(this, new AsyncCallback() {
            @Override
            public void callback(String[] movieIds) {
                // Construct SQLite where args
                String idArgs = StringUtils.SQLiteWhereArgs(movieIds);
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(MovieProvider.CONTENT_URI, new String[]{MoviesContract.UID, MoviesContract.TRAILER, MoviesContract.REVIEWS}, MoviesContract.UID + " IN (" + idArgs + ")", null, null);
                    while (cursor.moveToNext()) {
                        // if no trailer, launch TrailerAsyncTask
                        if (cursor.getString(TRAILER).equals("")) {
                            TrailerAsyncTask trailerAsyncTask = new TrailerAsyncTask(mContext);
                            trailerAsyncTask.execute(cursor.getString(UID));
                        }
                        // Always look for new reviews
                        ReviewAsyncTask reviewAsyncTask = new ReviewAsyncTask(mContext);
                        reviewAsyncTask.execute(cursor.getString(UID));
                    }
                } catch (NullPointerException exc) {
                    Log.e(MainFragment.class.getSimpleName(), "Trailer AsyncTask cursor is null");
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        });
        myAsyncTask.execute("popular");

        // Create Fragment manager begin a transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Instantiate MainFragment and attach it to activity_main layout
        MainFragment mainFragment = new MainFragment();
        fragmentTransaction.add(R.id.fragment_container, mainFragment, MAINFRAGMENTTAG);
        fragmentTransaction.commit();

    }
}