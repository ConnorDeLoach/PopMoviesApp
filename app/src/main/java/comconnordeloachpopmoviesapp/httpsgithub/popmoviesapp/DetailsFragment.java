package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

        // Setup favorites logic
        DBAdapter dbAdapter = new DBAdapter(getActivity());
        String[] isFavorite = {DBContract.FAVORITES};
        Cursor cursor = dbAdapter.queryData("_id=" + getData("id"), isFavorite);

        // Check to see if movie is favorite or not
        if (cursor.getCount() != 1) {
            Log.e(DetailsFragment.class.getSimpleName(), "Did not retrieve exactly 1 row during favorites query");
            cursor.close();
        } else {
            // Move cursor to column
            cursor.moveToFirst();

            // Load correct toolbar depending on whether movie is favorited or not
            if (cursor.getInt(0) == 0) {
                // Create toolbar
                Toolbar toolbar = (Toolbar) root.findViewById(R.id.details_toolbar);
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
                // Enable home navigation button
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                CheckBox checkBox = (CheckBox) root.findViewById(R.id.favorite_border);
            } else {
                // Create toolbar
                Toolbar toolbar = (Toolbar) root.findViewById(R.id.details_toolbar_favorite);
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
                // Enable home navigation button
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                CheckBox checkBox = (CheckBox) root.findViewById(R.id.favorite_filled);
            }
        }


        /* CheckBox checkBox = (CheckBox) root.findViewById(R.id.favorite_border);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter db = new DBAdapter(getActivity());
                long id = db.insertData(getData("id"));
                if (id == 0) {
                    Log.e(DetailsFragment.class.toString(), "SQLite id insert failed");
                } else {
                    Toast.makeText(getActivity(), "Succesfully inserted movie is", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        // Attach movie title
        TextView textView = (TextView) root.findViewById(R.id.details_test_textview);
        textView.setText(getData("title"));

        // Attach movie poster
        ImageView imageView = (ImageView) root.findViewById(R.id.details_image_view);
        Picasso.with(getActivity()).load(BASEURL + getData("poster_path")).into(imageView);

        // Attach movie release date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy", Locale.US);
        try {
            String date = formatter.format(formatter.parse(getData("release_date")));
            TextView releaseDate = (TextView) root.findViewById(R.id.details_release_date);
            releaseDate.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Attach rating
        TextView rating = (TextView) root.findViewById(R.id.details_rating);
        rating.setText(getData("vote_average") + "/10");

        // Attach movie synopsis
        TextView synopsis = (TextView) root.findViewById(R.id.details_synopsis);
        synopsis.setText(getData("overview"));
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate options menu
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        //Setup action logic
        switch (itemId) {
            case R.id.action_settings:
                Toast.makeText(getActivity(), "Hello from toast", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getData(String param) {
        // Create JSON movie object from intent
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
