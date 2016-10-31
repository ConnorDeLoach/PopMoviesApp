package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.R;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.activity.FavoritesActivity;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.async.MovieAdapter;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.db.MovieProvider;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.db.MoviesContract;

/**
 * Contains the gridview of movieposter views. Each clickable to start a details view.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int MOVIE_LOADER = 0;
    // MainFragment variables
    private OnMovieSelectedListener mOnMovieSelectedListener;
    private MovieAdapter mMovieAdapter;
    private String mMovieType = "popular";
    private SharedPreferences mPrefs;

    // This line makes it so this fragment can handle menu events.
    public MainFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnMovieSelectedListener = (OnMovieSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnMovieSelectedListener interface.");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        mPrefs = getActivity().getSharedPreferences("myData", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_layout, container, false);

        // Attach mMovieAdapter to GridView
        mMovieAdapter = new MovieAdapter(getContext(), null, 0);
        GridView gridView = (GridView) root.findViewById(R.id.mainfragment_gridview);
        gridView.setAdapter(mMovieAdapter);

        //Create toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // Create Spinner actions: sort by popular, or by top rated
        // Recover spinner state
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner_menu);
        if (mPrefs.getString("spinner_state", "popular").equals("top_rated")) {
            spinner.setSelection(1);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (!mMovieType.equals("popular")) {
                            mMovieType = "popular";
                            getLoaderManager().restartLoader(MOVIE_LOADER, null, MainFragment.this);
                        }
                        break;
                    case 1:
                        if (!mMovieType.equals("top_rated")) {
                            mMovieType = "top_rated";
                            getLoaderManager().restartLoader(MOVIE_LOADER, null, MainFragment.this);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set onClickListener to launch details fragment when a movie poster is touched
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movieId = setDetailsActivity(position);

                // use OnMovieSelectedListener callback to let MainActivity handle communication between fragments/activities
                mOnMovieSelectedListener.OnMovieSelected(movieId);
            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        //Setting action logic
        switch (itemId) {
            case R.id.action_settings:
                Toast.makeText(getActivity(), "Hello from toast", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_favorites:
                Intent intent = new Intent(getActivity(), FavoritesActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Store spinner state in shared preferences
        mPrefs.edit().putString("spinner_state", mMovieType).apply();
    }

    /**
     * Passes movieId from a touched view in GridView into an intent for DetailsFragment
     * @param position of each view in gridview
     * @return String with movie ID
     */
    private String setDetailsActivity(int position) {
        // Get poster path from the clicked view
        Cursor cursor = mMovieAdapter.getCursor();
        cursor.moveToPosition(position);
        return cursor.getString(0);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieProvider.CONTENT_URI, new String[]{MoviesContract.UID, MoviesContract.POSTER_PATH}, MoviesContract.TYPE + "=?", new String[]{mMovieType}, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the cursor in, framework will close the old cursor once the new data is in MovieAdapter.
        mMovieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Swap the cursor with null to make sure we are no longer using it before the cursor is closed.
        mMovieAdapter.swapCursor(null);
    }

    // Container Activity must implement this interface
    public interface OnMovieSelectedListener {
        void OnMovieSelected(String movieId);
    }
}