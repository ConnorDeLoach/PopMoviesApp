package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.R;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.activity.DetailsActivity;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.async.MovieAdapter;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.db.MovieProvider;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.db.MoviesContract;

/**
 * Hold view for favorites
 */
public class FavoritesFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int MOVIE_LOADER = 1;
    // member variables
    private MovieAdapter mFavoritesAdapter;

    // This line makes it so this fragment can handle menu events.
    public FavoritesFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);

        // Attach mFavoritesAdapter to GridView
        mFavoritesAdapter = new MovieAdapter(getContext(), null, 0);
        GridView gridView = (GridView) root.findViewById(R.id.favorites_gridview);
        gridView.setAdapter(mFavoritesAdapter);

        // Create loader
        getLoaderManager().initLoader(MOVIE_LOADER, null, FavoritesFragment.this);

        //Create toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.app_bar);
        toolbar.findViewById(R.id.spinner_menu).setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        // Enable home navigation button
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            case R.id.action_favorites:
                mFavoritesAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Passes movieId from a touched view in GridView into an intent for DetailsFragment
     *
     * @param position of each view in gridview
     * @return String with movie ID
     */
    private String setDetailsActivity(int position) {
        // Get poster path from the clicked view
        Cursor cursor = mFavoritesAdapter.getCursor();
        cursor.moveToPosition(position);
        return cursor.getString(0);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieProvider.CONTENT_URI, new String[]{MoviesContract.UID, MoviesContract.POSTER_PATH}, MoviesContract.FAVORITES + "=?", new String[]{"1"}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the cursor in, framework will close the old cursor once the new data is in MovieAdapter.
        mFavoritesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Swap the cursor with null to make sure we are no longer using it before the cursor is closed.
        mFavoritesAdapter.swapCursor(null);
    }
}