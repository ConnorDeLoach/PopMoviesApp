package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Connor on 5/25/2016.
 */
public class MyAsyncTask extends AsyncTask<String, Void, String> {

    public static String jsonStr;
    // JSON Keys
    final String API_KEY = "api_key";
    final String RESULTS = "results";
    final String MOVIE_POSTER = "poster_path";
    final String MOVIE_ID = "id";
    final String MOVIE_TITLE = "title";
    private Context context;

    MyAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        final String TOP_RATED_BASEURL = "http://api.themoviedb.org/3/movie/top_rated";
        final String POPULAR_MOVIE_BASEURL = "http://api.themoviedb.org/3/movie/popular";
        final String APPID_PARAM = API_KEY;

        //Assign MyAsyncTask params to top_rated URL or popular URL
        switch (params[0]) {
            case "popular":
                params[0] = POPULAR_MOVIE_BASEURL;
                break;
            case "top_rated":
                params[0] = TOP_RATED_BASEURL;
                break;
            default:
                Log.e(MyAsyncTask.class.toString(), "Invalid params");
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
            jsonStr = buffer.toString();

        } catch (java.io.IOException exc) {
            Log.e(MyAsyncTask.class.toString(), exc.getMessage(), exc);
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
                    Log.e(MyAsyncTask.class.toString(), exc.getMessage(), exc);
                }
            }
        }

        return jsonStr;
    }

    @Override
    protected void onPostExecute(String jsonString) {
        // Catch null returns
        if (jsonString == null) {
            Log.e(MyAsyncTask.class.toString(), "AsyncTask failed to retrieve data");
            return;
        }
        // Execute getImageDataFromJson task
        try {
            getImageDataFromJson(jsonString);
            // Notify mGridAdapter to refresh
            MainFragment.mGridAdapter.notifyDataSetChanged();
        } catch (JSONException exc) {
            Log.e(MyAsyncTask.class.toString(), exc.getMessage(), exc);
        }
        // Insert Data into SQLite DB
        try {
            // Initialize SQLite database
            DBAdapter dbAdapter = new DBAdapter(context);

            // Construct JSON object and extract movies array
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray moviesArray = jsonObject.getJSONArray(RESULTS);

            // Iterate through moviesArray and construct movies database
            for (int i = 0; i < moviesArray.length(); i++) {
                // Values to hold
                String movieId;
                String moviePoster;
                String title;
                String date;
                String rating;
                String synopsis;

                // Check to see if movie exists in database
                JSONObject movie = moviesArray.getJSONObject(i);
                if (dbAdapter.queryData(movie.getString(MOVIE_ID), new String[]{DBContract.UID}).getCount() == 0) {

                    // Retrieve _id
                    movieId = movie.getString(MOVIE_ID);
                    // Retrieve movie poster path
                    moviePoster = movie.getString(MOVIE_POSTER);
                    // Retrieve movie title
                    title = movie.getString(MOVIE_TITLE);
                    // Retrieve movie release date
                    date = getEasyDate(movie.getString("release_date"));
                    // Retrieve movie rating
                    rating = movie.getString("vote_average");
                    // Retrieve movie synopsis
                    synopsis = movie.getString("overview");

                    // Put values into ContentValues
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBContract.UID, movieId);
                    contentValues.put(DBContract.POSTER_PATH, moviePoster);
                    contentValues.put(DBContract.MOVIE_TITLE, title);
                    contentValues.put(DBContract.RELEASE_DATE, date);
                    contentValues.put(DBContract.VOTE_AVERAGE, rating);
                    contentValues.put(DBContract.SYNOPSIS, synopsis);

                    // Insert into database
                    dbAdapter.insertRow(contentValues);
                }
            }
        } catch (JSONException exc) {
            Log.e(MyAsyncTask.class.toString(), "Failed to insert data into database");
        }
    }

    /**
     * Takes the JSON String from AsyncTask and extracts movie poster paths
     * @param jsonString from MyAsyncTask
     * @throws JSONException
     */
    private void getImageDataFromJson(String jsonString)
            throws JSONException {

        // Construct JSON object and extract movies array
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray moviesArray = jsonObject.getJSONArray(RESULTS);

        // Clear mGridAdapter
        MainFragment.mGridAdapter.clear();

        // Go through every index of moviesArray and extract posterPath
        for (int i = 0; i < moviesArray.length(); i++) {
            // Retrieve movie JSON object
            JSONObject movie = moviesArray.getJSONObject(i);

            // Add poster path to mGridAdapter
            MainFragment.mGridAdapter.add(movie.getString(MOVIE_POSTER));
        }
    }

    /**
     * Parse and return a formatted movie release date
     *
     * @param date from moviesdb json
     * @return formatted date for sqlite database
     */
    private String getEasyDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy", Locale.US);
        try {
            date = formatter.format(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
