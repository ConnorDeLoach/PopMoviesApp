package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.R;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.fragment.FavoritesFragment;

/**
 * Activity to view movie favorites
 */
public class FavoritesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Create Fragment manager begin a transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Instantiate MainFragment and attach it to activity_main layout
        FavoritesFragment favoritesFragment = new FavoritesFragment();
        fragmentTransaction.add(R.id.favorite_fragment_container, favoritesFragment);
        fragmentTransaction.commit();
    }
}
