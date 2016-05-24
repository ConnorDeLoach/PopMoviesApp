package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

/**
 * Class to control database scheme
 */
public class DBContract {

    static final String DATABASE_NAME = "PopMoviesAppDB";
    static final String TABLE_NAME = "APPDATA";
    static final int DATABASE_VERSION = 6;
    static final String UID = "_id";
    static final String FAVORITES = "Favorites";
    static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY NOT NULL, " + FAVORITES + " INTEGER DEFAULT 0);";
    static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
