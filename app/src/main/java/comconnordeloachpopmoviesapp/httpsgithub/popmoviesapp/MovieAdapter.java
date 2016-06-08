package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Cursor Adapter used in MainFragment to attach to GridView
 */
public class MovieAdapter extends CursorAdapter {

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.single_gridview, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Moviesdb poster path
        final String BASE_URL = "http://image.tmdb.org/t/p/w500/";
        // Movie posterpath from cursor returned from CursorLoader
        String posterPath = cursor.getString(1);

        // Bind view returned from newView and use picasso to inject image
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        Picasso.with(context).load(BASE_URL + posterPath).placeholder(android.R.mipmap.sym_def_app_icon).error(android.R.mipmap.sym_def_app_icon).into(imageView);

    }
}
