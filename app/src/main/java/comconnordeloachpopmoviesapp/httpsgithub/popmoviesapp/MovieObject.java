package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Class to build an object containing all information for a movie
 */
public class MovieObject {

    // values
    private boolean movieExists;
    private String movieId;
    private int movieIsFavorite;
    private String moviePoster = "http://image.tmdb.org/t/p/w342/";
    private String movieTitle;
    private String releaseDate;
    private String rating = "/10";
    private String synopsis;
    private String trailer;

    public MovieObject(Context context, String movieId) {

        this.movieId = movieId;

        // Check if movie exists
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI, null, MoviesContract.UID + "=?", new String[]{movieId}, null);
            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    movieExists = false;
                } else {
                    // Movie exists, populating movie values
                    movieExists = true;
                    // Set movie isFavorite
                    movieIsFavorite = cursor.getInt(cursor.getColumnIndex(MoviesContract.FAVORITES));
                    // Set movie poster
                    moviePoster = moviePoster + cursor.getString(cursor.getColumnIndex(MoviesContract.POSTER_PATH));
                    // Set movieTitle
                    movieTitle = cursor.getString(cursor.getColumnIndex(MoviesContract.MOVIE_TITLE));
                    // Set release date
                    releaseDate = cursor.getString(cursor.getColumnIndex(MoviesContract.RELEASE_DATE));
                    // Set rating
                    rating = cursor.getString(cursor.getColumnIndex(MoviesContract.VOTE_AVERAGE)) + rating;
                    // Set synopsis
                    synopsis = cursor.getString(cursor.getColumnIndex(MoviesContract.SYNOPSIS));
                    // Set trailer
                    trailer = cursor.getString(cursor.getColumnIndex(MoviesContract.TRAILER));
                }
            }
        } catch (NullPointerException exc) {
            Log.e(MovieObject.class.getSimpleName(), "Cursor could not read database");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getMovieId() {
        return movieId;
    }

    public int getMovieIsFavorite() {
        return movieIsFavorite;
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

    public String getTrailer() {
        return trailer;
    }
}
