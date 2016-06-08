package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

/**
 * Class to control database and provider scheme
 */
public class MoviesContract {
    static final String TABLE_MOVIES = "movies";

    // Column names
    static final String UID = "_id";
    static final String FAVORITES = "favorites";
    static final String POSTER_PATH = "poster_path";
    static final String MOVIE_TITLE = "movie_title";
    static final String RELEASE_DATE = "release_date";
    static final String VOTE_AVERAGE = "vote_average";
    static final String SYNOPSIS = "synopsis";
    static final String TYPE = "type";
    static final String[] columnNames = {UID, FAVORITES, POSTER_PATH, MOVIE_TITLE, RELEASE_DATE, VOTE_AVERAGE, SYNOPSIS};

    // SQLite methods
    static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_MOVIES;
    static final String CREATE_TABLE = "CREATE TABLE " + TABLE_MOVIES + " (" + UID + " INTEGER PRIMARY KEY NOT NULL, "
            + FAVORITES + " INTEGER DEFAULT 0,"
            + POSTER_PATH + " TEXT, "
            + MOVIE_TITLE + " TEXT, "
            + RELEASE_DATE + " TEXT, "
            + VOTE_AVERAGE + " TEXT, "
            + SYNOPSIS + " TEXT, "
            + TYPE + " TEXT);";
}