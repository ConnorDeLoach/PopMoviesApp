package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by connor on 4/11/16.
 */
public class MainFragment extends Fragment {

    // MainFragment variables
    private CustomAdapter mGridAdapter;
    private String jsonString = null;

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
                        MyAsyncClass fetchPopMoviePosters = new MyAsyncClass();
                        fetchPopMoviePosters.execute("popular");
                        break;
                    case 1:
                        MyAsyncClass fetchTopMoviePosters = new MyAsyncClass();
                        fetchTopMoviePosters.execute("top_rated");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Create CustomAdapter connected to singlegrid imageview layout and moviePosterPaths data
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
     * Method to package specific movie JSON object when a movie poster to touched
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
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray moviesArray = jsonObject.getJSONArray(RESULTS);
        return moviesArray.getJSONObject(position).toString();
    }

    /**
     * Method that takes the JSON String from AsyncTask and extracts movie poster paths
     *
     * @param jsonString from MyAsyncTask
     * @throws JSONException
     */
    private void getImageDataFromJson(String jsonString)
            throws JSONException {
        // Names of JSON objects and arrays
        final String RESULTS = "results";
        final String MOVIEPOSTER = "poster_path";

        // Construct JSON object and extract movies array
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray moviesArray = jsonObject.getJSONArray(RESULTS);

        // Clear mGridAdapter
        mGridAdapter.clear();

        // Go through every index of moviesArray and extract posterPath
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);
            // Add posterpath to mGridAdapter
            mGridAdapter.add(movie.getString(MOVIEPOSTER));
        }
    }

    class MyAsyncClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            final String TOP_RATED_BASEURL = "http://api.themoviedb.org/3/movie/top_rated";
            final String POPULAR_MOVIE_BASEURL = "http://api.themoviedb.org/3/movie/popular";
            final String APPID_PARAM = "api_key";

            //Assign MyAsyncTask params to top_rated URL or popular URL
            switch (params[0]) {
                case "popular":
                    params[0] = POPULAR_MOVIE_BASEURL;
                    break;
                case "top_rated":
                    params[0] = TOP_RATED_BASEURL;
                    break;
                default:
                    Log.e(MyAsyncClass.class.toString(), "Invalid params");
                    return null;
            }

            Uri builtUri = Uri.parse(params[0]).buildUpon()
                    .appendQueryParameter(APPID_PARAM, "YOUR_API_KEY")
                    .build();

            // Variable declarations outside try block in order to close in Final block
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                // Create a URL from the built Uri string above and open a connection to MoviesDB
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                // Setup the urlConnection to be read into a buffered reader
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                // Read the JSON from MoviesDB
                String line;
                while ((line = reader.readLine()) != null) {
                    // Format JSON String for debugging purposes
                    buffer.append(line + "\n");
                }
                // Get JSON out of try block
                jsonString = buffer.toString();

            } catch (java.io.IOException exc) {
                Log.e(MyAsyncClass.class.toString(), exc.getMessage(), exc);
                exc.printStackTrace();
                // If error break out of method
                return null;
            } finally {
                // Cleanup open streams
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException exc) {
                        Log.e(MyAsyncClass.class.toString(), exc.getMessage(), exc);
                    }
                }
            }

            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            // Catch null returns
            if(jsonString == null) {
                Log.e(MyAsyncClass.class.toString(), "jsonString failed to retrieve data");
                return;
            }
            // Execute getImageDataFromJson task
            try {
                getImageDataFromJson(jsonString);
                // Notify mGridAdapter to refresh
                mGridAdapter.notifyDataSetChanged();
            } catch (JSONException exc) {
                Log.e(MyAsyncClass.class.toString(), exc.getMessage(), exc);
            }
        }
    }
}