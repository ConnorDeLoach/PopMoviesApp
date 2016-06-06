package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
public class MainFragment extends Fragment {

    // MainFragment variables
    public static CustomAdapter mGridAdapter;
    private String movieType;

    public MainFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_layout, container, false);

        //Create toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.app_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        // Create Spinner actions: sort by popular, or by top rated
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner_menu);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        MyAsyncTask fetchPopMoviePosters = new MyAsyncTask(getActivity());
                        movieType = "popular";
                        setGridView();
                        fetchPopMoviePosters.execute(movieType);
                        break;
                    case 1:
                        MyAsyncTask fetchTopMoviePosters = new MyAsyncTask(getActivity());
                        movieType = "top_rated";
                        setGridView();
                        fetchTopMoviePosters.execute(movieType);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Create CustomAdapter connected to single grid imageview layout and moviePosterPaths data
        mGridAdapter = new CustomAdapter(getActivity(), R.layout.single_gridview, R.id.image_view);
        GridView gridView = (GridView) root.findViewById(R.id.grid_view);

        // Attach CustomAdapter to MainFragment's layout
        gridView.setAdapter(mGridAdapter);

        // Set onClickListener to launch details fragment when a movie poster is touched
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movie = setDetailsActivity(position);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, movie);
                startActivity(intent);
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
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Accesses database using the posterpath in each view in GridView to retrieve movie ID for details view
     * @param position of each view in gridview
     * @return String with movie ID
     */
    private String setDetailsActivity(int position) {
        // Get poster path from the clicked view
        String posterPath = mGridAdapter.getItem(position);

        // Use the poster path to identify the movie and retrieve the movie's ID
        Cursor cursor = getContext().getContentResolver().query(MovieProvider.CONTENT_URI, new String[]{MoviesContract.UID}, MoviesContract.POSTER_PATH + "=?", new String[]{posterPath}, null);
        cursor.moveToFirst();
        String data = cursor.getString(cursor.getColumnIndex(MoviesContract.UID));
        cursor.close();
        return data;
    }

    /**
     * Accesses database to retrieve movie poster URI for GridView
     */
    private void setGridView() {
        // Clear Gridview
        mGridAdapter.clear();

        // Run Cursor over database
        Cursor cursor = getContext().getContentResolver().query(MovieProvider.CONTENT_URI, null, MoviesContract.TYPE + "=?", new String[]{movieType}, null);
        Log.i("DEBUG", cursor.getCount() + "");
        while (cursor.moveToNext()) {
            // Add poster path to mGridAdapter
            mGridAdapter.add(cursor.getString(cursor.getColumnIndex(MoviesContract.POSTER_PATH)));
        }
        cursor.close();
        mGridAdapter.notifyDataSetChanged();
    }
}