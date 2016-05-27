package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by connor on 4/11/16.
 */
public class MainFragment extends Fragment {

    // MainFragment variables
    public static CustomAdapter mGridAdapter;

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
                        fetchPopMoviePosters.execute("popular");
                        break;
                    case 1:
                        MyAsyncTask fetchTopMoviePosters = new MyAsyncTask(getActivity());
                        fetchTopMoviePosters.execute("top_rated");
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
                try {
                    String movie = getDetailsActivity(position);
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, movie);
                    startActivity(intent);

                } catch (JSONException exc) {
                    Log.e(MainFragment.class.toString(), exc.getMessage(), exc);
                    exc.printStackTrace();
                }
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
     * Method to package specific movie JSON object when a movie poster is touched
     *
     * @param position of view in gridView
     * @return String representation of JSON object
     * @throws JSONException
     */
    private String getDetailsActivity(int position)
            throws JSONException {
        // Name of JSON Objects and arrays
        final String RESULTS = "results";

        // Construct JSON object and extract movie JSON object (as String)
        JSONObject jsonObject = new JSONObject(MyAsyncTask.jsonStr);
        JSONArray moviesArray = jsonObject.getJSONArray(RESULTS);
        JSONObject movie = moviesArray.getJSONObject(position);
        return movie.getString("id");
    }
}