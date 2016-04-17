package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by connor on 4/16/16.
 */
public class DetailsFragment extends android.support.v4.app.Fragment {

    public DetailsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_details, container, false);

        // Create toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // Attach movie title
        TextView textView = (TextView) root.findViewById(R.id.details_test_textview);
        textView.setText(getMovieTitle());

        // Attach movie poster
        ImageView imageView = (ImageView) root.findViewById(R.id.details_image_view);
        Picasso.with(getActivity()).load(getMoviePoster()).into(imageView);
        return root;
    }

    private String getMovieTitle() {
        // Create JSON movie object from intent
        String title = null;
        try {
            JSONObject movie = new JSONObject(getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT));
            // Fetch movie title
            title = movie.getString("title");
        } catch (JSONException exc) {
            Log.e(DetailsFragment.class.toString(), exc.getMessage(), exc);
            exc.printStackTrace();
        }
        return title;
    }

    private String getMoviePoster() {
        // Create JSON movie object from intent
        final String BASEURL = "http://image.tmdb.org/t/p/w342/";
        String posterPath = null;
        try {
            JSONObject movie = new JSONObject(getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT));
            // Fetch movie title
            posterPath = movie.getString("poster_path");

        } catch (JSONException exc) {
            Log.e(DetailsFragment.class.toString(), exc.getMessage(), exc);
            exc.printStackTrace();
        }
        return BASEURL + posterPath;
    }
}
