package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.Async;

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

import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.db.MovieProvider;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.db.MoviesContract;

/**
 * Query moviesdb for movie reviews URL
 */
public class ReviewAsyncTask extends AsyncTask<String, Void, String> {

    private Context mContext;
    private String mMovieId;

    public ReviewAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
        mMovieId = params[0];

        // Construct Uri string
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(mMovieId)
                .appendPath("reviews")
                .appendQueryParameter("api_key", "YOUR_API_KEY");
        String myUrl = builder.build().toString();

        // Variable declarations outside try block in order to close in Final block
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr;
        try {
            // Create a URL from the built Uri string above and open a connection to MoviesDB
            URL url = new URL(myUrl);
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

        // Get review(s) from json
        String reviews = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultsArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject reviewsJson = resultsArray.getJSONObject(i);
                // demarcate reviews with "connordeloach.popMoviesApp"
                if (reviewsJson.length() != 0) {
                    String author = reviewsJson.getString("author");
                    String review = reviewsJson.getString("content");
                    reviews = reviews + author + ": " + review + "connordeloach.popMoviesApp";
                } else {
                    // no reviews so return nothing
                    return;
                }
            }

            // Truncate last comma
            if (reviews.length() >= 1) {
                reviews = reviews.substring(0, reviews.length() - 26);
            }

            // Put trailer into database
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.REVIEWS, reviews);
            mContext.getContentResolver().update(MovieProvider.CONTENT_URI, contentValues, MoviesContract.UID + "=?", new String[]{mMovieId});
        } catch (JSONException exc) {
            Log.e(MyAsyncTask.class.toString(), "Failed to insert data into database");
        }
    }
}
