package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Contains the view for each movie
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

        // Retrieve movie object
        final String movieId = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        final MovieObject movieObject = new MovieObject(getActivity(), movieId);

        // Attach title
        TextView textView = (TextView) root.findViewById(R.id.details_test_textview);
        textView.setText(movieObject.getMovieTitle());

        // Attach movie poster
        ImageView imageView = (ImageView) root.findViewById(R.id.details_image_view);
        Picasso.with(getActivity()).load(movieObject.getMoviePoster()).into(imageView);

        // Attach release date
        TextView releaseDate = (TextView) root.findViewById(R.id.details_release_date);
        releaseDate.setText(movieObject.getReleaseDate());

        // Attach rating
        TextView rating = (TextView) root.findViewById(R.id.details_rating);
        rating.setText(movieObject.getRating());

        // Attach movie synopsis
        TextView synopsis = (TextView) root.findViewById(R.id.details_synopsis);
        synopsis.setText(movieObject.getSynopsis());

        // Attach trailer(s)
        final String[] trailers = movieObject.getTrailer().split(",");
        if (trailers.length != 0) {
            if (!trailers[0].isEmpty()) {
                Button trailer = (Button) root.findViewById(R.id.trailer_button);
                trailer.setVisibility(View.VISIBLE);
                trailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailers[0])));
                    }
                });
            }
            if (trailers.length == 2) {
                Button trailer2 = (Button) root.findViewById(R.id.trailer_button2);
                trailer2.setVisibility(View.VISIBLE);
                trailer2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailers[1])));
                    }
                });
            }
        }

        // Create toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.details_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        // Enable home navigation button
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup favorites logic
        final CheckBox checkBox = (CheckBox) root.findViewById(R.id.favorite);
        // Check to see if movie is favorite or not
        if (movieObject.getMovieIsFavorite() == 1) {
            checkBox.setChecked(true);
        }

        // update favorite change in database
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MoviesContract.FAVORITES, 1);
                    getContext().getContentResolver().update(MovieProvider.CONTENT_URI, contentValues, MoviesContract.UID + "=?", new String[]{movieId});
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MoviesContract.FAVORITES, 0);
                    getContext().getContentResolver().update(MovieProvider.CONTENT_URI, contentValues, MoviesContract.UID + "=?", new String[]{movieId});
                }
            }
        });

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
}
