package com.example.shaol.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.shaol.bakingapp.Models.Ingredients;
import com.example.shaol.bakingapp.Models.Steps;

/**
 * Created by shaol on 5/18/2018.
 */

public class MasterFragmentActivity extends AppCompatActivity implements MasterListFragment.OnRecipeClickListener{

    Boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);

        if (findViewById(R.id.side_fragment) != null) {
            mTwoPane = true;

            StepsFragment sideFragment = new StepsFragment();

            String description = "";
            String videoURL = "";
            String thumbnailURL = "";

            sideFragment.setSteps(description, videoURL, thumbnailURL);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.side_fragment, sideFragment)
                    .commit();
        } else {
            mTwoPane = false;
        }

    }

    public void onRecipeSelected(@Nullable Ingredients[] ingredients, @Nullable Steps stepsInfo, int position) {

        if (mTwoPane) {
            if (position == 500) {
                IngredientsFragment newFragment = new IngredientsFragment();
                newFragment.setIngredients(ingredients);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.side_fragment, newFragment)
                        .commit();
            } else {
                StepsFragment newFragment = new StepsFragment();
                newFragment.setSteps(stepsInfo.getDescription(), stepsInfo.getVideoURL(), stepsInfo.getThumbnailURL());
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.side_fragment, newFragment)
                        .commit();
            }
        } else {
            Intent newIntent = new Intent(this, SideFragmentActivity.class);

            if (position == 500) {
                newIntent.putExtra("ingredients", ingredients);
                startActivity(newIntent);
            } else {
                newIntent.putExtra("description", stepsInfo.getDescription());
                newIntent.putExtra("videoURL", stepsInfo.getVideoURL());
                newIntent.putExtra("thumbnail", stepsInfo.getThumbnailURL());
                startActivity(newIntent);
            }
        }
    }
}
