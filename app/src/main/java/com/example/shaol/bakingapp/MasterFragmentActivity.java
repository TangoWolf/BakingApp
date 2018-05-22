package com.example.shaol.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.shaol.bakingapp.Models.Ingredients;
import com.example.shaol.bakingapp.Models.Steps;

/**
 * Created by shaol on 5/18/2018.
 */

public class MasterFragmentActivity extends AppCompatActivity implements MasterListFragment.OnRecipeClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);

    }

    public void onRecipeSelected(@Nullable Ingredients[] ingredients, @Nullable Steps stepsInfo, int position) {

        Intent newIntent = new Intent(this, SideFragmentActivity.class);

        if (position == 500){
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
