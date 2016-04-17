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

    final String BASEURL = "http://image.tmdb.org/t/p/w342/";

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
        textView.setText(getData("title"));

        // Attach movie poster
        ImageView imageView = (ImageView) root.findViewById(R.id.details_image_view);
        Picasso.with(getActivity()).load(BASEURL + getData("poster_path")).into(imageView);
        return root;
    }

    private String getData(String param) {
        // Create JSON movie object from intent
        String data;
        try {
            JSONObject movie = new JSONObject(getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT));
            return movie.getString(param);
        } catch (JSONException exc) {
            Log.e(DetailsFragment.class.toString(), exc.getMessage(), exc);
            exc.printStackTrace();
        }
        return null;
    }
}
