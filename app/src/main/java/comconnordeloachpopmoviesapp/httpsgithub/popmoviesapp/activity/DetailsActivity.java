package comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.R;
import comconnordeloachpopmoviesapp.httpsgithub.popmoviesapp.fragment.DetailsFragment;

public class DetailsActivity extends AppCompatActivity {

    final String DETAILSFRAGMENTTAG = DetailsFragment.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Create Fragment manager begin a transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Instantiate MainFragment and attach it to activity_main layout
        DetailsFragment detailsFragment = new DetailsFragment();
        fragmentTransaction.add(R.id.details_fragment_container, detailsFragment, DETAILSFRAGMENTTAG);
        fragmentTransaction.commit();
    }
}
