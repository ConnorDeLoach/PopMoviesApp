package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.async;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.R;

/**
 * Cursor Adapter used in MainFragment to attach to GridView
 */
public class MovieAdapter extends CursorAdapter {

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_gridview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Moviesdb poster path
        final String BASE_URL = "http://image.tmdb.org/t/p/w500/";
        // Movie posterpath from cursor returned from CursorLoader
        String posterPath = cursor.getString(1);

        // Bind view returned from newView and use picasso to inject image
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Picasso.with(context).load(BASE_URL + posterPath).placeholder(android.R.mipmap.sym_def_app_icon).error(android.R.mipmap.sym_def_app_icon).into(viewHolder.imageView);

    }

    // ViewHolderder pattern for the imageview picasso injects into each view in GridView
    static class ViewHolder {
        final ImageView imageView;

        ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.image_view);
        }
    }
}
