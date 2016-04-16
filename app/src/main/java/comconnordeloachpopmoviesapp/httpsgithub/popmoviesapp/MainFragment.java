package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

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
import android.widget.GridView;
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

    // MainFragment global variables
    CustomAdapter mGridAdapter;
    Toolbar toolbar;

    public MainFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Run MyAsyncClass to fetch movie poster paths
        MyAsyncClass fetchMoviePosters = new MyAsyncClass();
        fetchMoviePosters.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_layout, container, false);

        //Create toolbar
        toolbar = (Toolbar)root.findViewById(R.id.app_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        // Create CustomAdapter connected to singlegrid imageview layout and moviePosterPaths data
        mGridAdapter = new CustomAdapter(getActivity(), R.layout.single_gridview, R.id.image_view);
        GridView gridView = (GridView) root.findViewById(R.id.grid_view);
        // Attach CustomAdapter to MainFragment's layout
        gridView.setAdapter(mGridAdapter);


        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate actions menu
        menu.clear();
        inflater.inflate(R.menu.fragment_main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        //Setting action
        if (itemId == R.id.action_settings) {
            Toast.makeText(getActivity(), "Hello from toast", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method that takes the JSON String from AsyncTask and extracts movie poster paths
     *
     * @param jsonString from MyAsyncTask
     * @throws JSONException
     */
    private void getImageDataFromJson(String jsonString)
            throws JSONException {
        // Names of JSON objects
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

    class MyAsyncClass extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            final String POPULAR_MOVIE_BASEURL = "http://api.themoviedb.org/3/movie/popular?";
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(POPULAR_MOVIE_BASEURL).buildUpon()
                    .appendQueryParameter(APPID_PARAM, "YOUR_API_KEY")
                    .build();

            // Variable declarations outside try block in order to close in Final block
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonString;
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