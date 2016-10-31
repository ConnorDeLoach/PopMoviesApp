package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.async;

/**
 * After movie database is updated, callback to initiate Trailer and Review AsyncTasks
 */
public interface AsyncCallback {

    void callback(String[] movieIds);
}
