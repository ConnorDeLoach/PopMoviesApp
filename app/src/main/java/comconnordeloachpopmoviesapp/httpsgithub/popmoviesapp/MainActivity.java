package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.Async.AsyncCallback;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.Async.MyAsyncTask;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.Async.ReviewAsyncTask;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.Async.TrailerAsyncTask;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.Utils.StringUtils;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.db.MovieProvider;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.db.MoviesContract;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMovieSelectedListener {

    private final String MAINFRAGMENTTAG = MainFragment.class.toString();
    private final String DETAILFRAGMENT_TAG = DetailsFragment.class.toString();
    private final int UID = 0;
    private final int TRAILER = 1;
    private final int REVIEWS = 2;
    private boolean mTwoPane;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Query moviedb API for popular movies, trailers, and reviews
        MyAsyncTask popAsyncTask = new MyAsyncTask(this, new AsyncCallback() {
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
        popAsyncTask.execute("popular");

        // Query moviesdb for top rated movies, trailers, and reviews
        MyAsyncTask topAsyncTask = new MyAsyncTask(this, new AsyncCallback() {
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
        topAsyncTask.execute("top_rated");

        // Check for 2Pane state
        // if 2pane state, dynamically initialize details fragment
        if (findViewById(R.id.detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                // Create Details Fragment and attach movieId argument(s)
                // Get movieId
                Cursor cursor = getContentResolver().query(MovieProvider.CONTENT_URI, new String[]{MoviesContract.UID}, null, null, null);
                String movieId = null;
                try {
                    cursor.moveToFirst();
                    movieId = cursor.getString(0);
                } catch (NullPointerException exc) {
                    Log.e(this.getClass().getSimpleName(), "TwoPane DetailsFragment failed to retrieve movieId");
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                if (movieId == null) {
                    // passing a null value will crash the app
                    // run app in 1-pane mode instead
                    mTwoPane = false;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("movieid", movieId);

                    // Put bundle with movieId into DetailFragment object
                    DetailsFragment df = new DetailsFragment();
                    df.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detail_container, df, DETAILFRAGMENT_TAG)
                            .commit();
                }
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void OnMovieSelected(String movieId) {
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putString("movieid", movieId);

            // Put bundle with movieId into DetailFragment object
            DetailsFragment df = new DetailsFragment();
            df.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, df, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            // Get movieId to pass to details fragment via DetailsActivity
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, movieId);
            startActivity(intent);
        }
    }
}