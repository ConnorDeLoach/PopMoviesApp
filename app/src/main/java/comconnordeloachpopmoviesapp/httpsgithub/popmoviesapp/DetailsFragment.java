package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        // Get Intent data
        Intent intent = getActivity().getIntent();
        String movieTitle = intent.getStringExtra(Intent.EXTRA_TEXT);

        // Attach data to layout
        TextView textView = (TextView) root.findViewById(R.id.details_test_textview);
        textView.setText(movieTitle);
        return root;
    }
}
