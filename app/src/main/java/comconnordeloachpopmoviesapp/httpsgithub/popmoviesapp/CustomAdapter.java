package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by connor on 4/12/16.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    final String BASE_URL = "http://image.tmdb.org/t/p/w500/";
    Context context;

    public CustomAdapter(Context context, int resource, int imageViewResourceId) {
        super(context, resource, imageViewResourceId);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the imageView that will hold each movie poster
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View grid = inflater.inflate(R.layout.single_gridview, parent, false);
        ImageView imageView = (ImageView) grid.findViewById(R.id.image_view);

        // Picasso fetch movie posters and place them into imageview
        Picasso.with(context).load(BASE_URL + getItem(position)).into(imageView);
        return imageView;
    }
}