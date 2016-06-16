package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Intent;
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

/**
 * Contains the gridview of movieposter views. Each clickable to start a details view.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int MOVIE_LOADER = 0;
    // MainFragment variables
    private MovieAdapter mMovieAdapter;
    private String movieType = "popular";

    // This line makes it so this fragment can handle menu events.
    public MainFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_layout, container, false);

        // Attach mMovieAdapter to GridView
        mMovieAdapter = new MovieAdapter(getContext(), null, 0);
        GridView gridView = (GridView) root.findViewById(R.id.grid_view);
        gridView.setAdapter(mMovieAdapter);

        //Create toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // Create Spinner actions: sort by popular, or by top rated
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner_menu);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (!movieType.equals("popular")) {
                            movieType = "popular";
                            getLoaderManager().restartLoader(MOVIE_LOADER, null, MainFragment.this);
                        }
                        break;
                    case 1:
                        if (!movieType.equals("top_rated")) {
                            movieType = "top_rated";
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

                // Get movieId to pass to details fragment
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, movieId);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
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
        }
        return super.onOptionsItemSelected(item);
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
        return new CursorLoader(getActivity(), MovieProvider.CONTENT_URI, new String[]{MoviesContract.UID, MoviesContract.POSTER_PATH}, MoviesContract.TYPE + "=?", new String[]{movieType}, null);

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
}