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
 * Manages communication between moviesDB and internal app sqlite database
 */
public class MyAsyncTask extends AsyncTask<String, Void, String> {

    public static String jsonStr;
    // JSON Keys
    final String API_KEY = "api_key";
    final String RESULTS = "results";
    final String MOVIE_POSTER = "poster_path";
    final String MOVIE_ID = "id";
    final String MOVIE_TITLE = "title";
    final String MOVIE_RELEASE_DATE = "release_date";
    final String MOVIE_RATING = "vote_average";
    final String MOVIE_SYNOPSIS = "overview";
    String movieType = "";
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
                movieType = "popular";
                break;
            case "top_rated":
                params[0] = TOP_RATED_BASEURL;
                movieType = "top_rated";
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
        // Insert Data into SQLite DB
        try {
            // Initialize SQLite database
            DBAdapter dbAdapter = new DBAdapter(context);

            // Construct JSON object and extract movies array
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray moviesArray = jsonObject.getJSONArray(RESULTS);

            // List of current movies
            String[] moviesList = new String[moviesArray.length()];

            // Iterate through moviesArray and construct movies database
            for (int i = 0; i < moviesArray.length(); i++) {

                // Retrieve movie object from json
                JSONObject movie = moviesArray.getJSONObject(i);

                // Check if movie exists in database
                MovieObject movieObject = new MovieObject(context, movie.getString(MOVIE_ID));
                if (!movieObject.isMovieExists()) {

                    // Put values into ContentValues
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBContract.UID, movie.getString(MOVIE_ID));
                    contentValues.put(DBContract.POSTER_PATH, movie.getString(MOVIE_POSTER));
                    contentValues.put(DBContract.MOVIE_TITLE, movie.getString(MOVIE_TITLE));
                    contentValues.put(DBContract.RELEASE_DATE, getEasyDate(movie.getString(MOVIE_RELEASE_DATE)));
                    contentValues.put(DBContract.VOTE_AVERAGE, movie.getString(MOVIE_RATING));
                    contentValues.put(DBContract.SYNOPSIS, movie.getString(MOVIE_SYNOPSIS));
                    contentValues.put(DBContract.TYPE, movieType);

                    // Insert into database
                    dbAdapter.insertRow(contentValues);
                    moviesList[i] = movie.getString(MOVIE_ID);
                } else {
                    moviesList[i] = movie.getString(MOVIE_ID);
                }
            }
            // Delete old movies
            if (moviesList.length > 0) {
                int debug = dbAdapter.deleteRows(moviesList, movieType);
                Log.i("DEBUG", debug + "");
            }
        } catch (JSONException exc) {
            Log.e(MyAsyncTask.class.toString(), "Failed to insert data into database");
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
