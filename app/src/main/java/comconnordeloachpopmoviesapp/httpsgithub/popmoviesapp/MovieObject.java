package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Context;
import android.database.Cursor;

/**
 *
 */
public class MovieObject {

    // values
    private boolean movieExists;
    private String moviePoster = "http://image.tmdb.org/t/p/w342/";
    private String movieTitle;
    private String releaseDate;
    private String rating = "/10";
    private String synopsis;

    MovieObject(Context context, String movieId) {
        // Check if movie exists
        DBAdapter dbAdapter = new DBAdapter(context);
        Cursor cursor = dbAdapter.queryDatabase(movieId, DBContract.columnNames);
        if (!cursor.moveToFirst()) {
            movieExists = false;
        } else {
            // Movie exists, populating movie values
            movieExists = true;
            // Set movie poster
            moviePoster = moviePoster + cursor.getString(cursor.getColumnIndex(DBContract.POSTER_PATH));
            // Set movieTitle
            movieTitle = cursor.getString(cursor.getColumnIndex(DBContract.MOVIE_TITLE));
            // Set release date
            releaseDate = cursor.getString(cursor.getColumnIndex(DBContract.RELEASE_DATE));
            // Set rating
            rating = cursor.getString(cursor.getColumnIndex(DBContract.VOTE_AVERAGE)) + rating;
            // Set synopsis
            synopsis = cursor.getString(cursor.getColumnIndex(DBContract.SYNOPSIS));
        }
    }

    public boolean isMovieExists() {
        return movieExists;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRating() {
        return rating;
    }

    public String getSynopsis() {
        return synopsis;
    }
}
