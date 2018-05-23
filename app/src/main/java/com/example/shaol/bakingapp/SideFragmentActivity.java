package com.example.shaol.bakingapp;

import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.shaol.bakingapp.Models.Ingredients;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shaol on 5/18/2018.
 */

public class SideFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_fragment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle data = getIntent().getExtras();
        if (data.getParcelableArray("ingredients") != null) {
            Parcelable[] parcelable = data.getParcelableArray("ingredients");
            Ingredients[] recipeIngredients = new Ingredients[parcelable.length];
            for (int i = 0; i < parcelable.length; i++) {
                Ingredients aIngredient = (Ingredients) parcelable[i];
                recipeIngredients[i] = aIngredient;
            }

            IngredientsFragment iFragment = new IngredientsFragment();

            iFragment.setIngredients(recipeIngredients);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.side_fragment, iFragment)
                    .commit();
        } else {
            StepsFragment sFragment = new StepsFragment();

            String description = data.getString("description");
            String videoURL = data.getString("videoURL");
            String thumbnailURL = data.getString("thumbnail");

            sFragment.setSteps(description, videoURL, thumbnailURL);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.side_fragment, sFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
