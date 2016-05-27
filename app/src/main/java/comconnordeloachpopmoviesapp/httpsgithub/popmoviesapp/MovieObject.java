package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Context;

/**
 * Created by Connor on 5/25/2016.
 */
public class MovieObject {

    // values
    private Context context;
    private String movieId;

    MovieObject(Context context, String movieId) {
        this.context = context;
        this.movieId = movieId;
    }
}
