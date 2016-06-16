package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

/**
 * Class to control database and provider scheme
 */
public class MoviesContract {
    // Column names
    public static final String UID = "_id";
    public static final String FAVORITES = "favorites";
    public static final String POSTER_PATH = "poster_path";
    public static final String MOVIE_TITLE = "movie_title";
    public static final String RELEASE_DATE = "release_date";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String SYNOPSIS = "synopsis";
    public static final String TYPE = "type";
    public static final String TRAILER = "trailer";
    static final String TABLE_MOVIES = "movies";
    // SQLite methods
    static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_MOVIES;
    static final String CREATE_TABLE = "CREATE TABLE " + TABLE_MOVIES + " (" + UID + " INTEGER PRIMARY KEY NOT NULL, "
            + FAVORITES + " INTEGER DEFAULT 0,"
            + POSTER_PATH + " TEXT, "
            + MOVIE_TITLE + " TEXT, "
            + RELEASE_DATE + " TEXT, "
            + VOTE_AVERAGE + " TEXT, "
            + SYNOPSIS + " TEXT, "
            + TYPE + " TEXT, "
            + TRAILER + " TEXT NOT NULL DEFAULT '');";
}
