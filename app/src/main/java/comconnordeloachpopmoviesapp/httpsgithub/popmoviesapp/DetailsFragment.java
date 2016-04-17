package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        View root = inflater.inflate(R.layout.activity_details, container, false);

        // Attach data to layout
        TextView textView = (TextView) root.findViewById(R.id.details_test_textview);
        textView.setText(getMovieTitle());
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
}
