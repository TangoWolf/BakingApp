package com.example.shaol.bakingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaol.bakingapp.Models.Recipe;
import com.example.shaol.bakingapp.Utils.JsonUtils;
import com.example.shaol.bakingapp.Utils.NetworkUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    @BindView(R.id.rv_recipes) RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.emptyView) TextView mEmptyView;
    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.main_activity_title);

        ButterKnife.bind(this);

        int columns;

        if(findViewById(R.id.tablet_layout) != null){
            columns = 3;
        } else {
            columns = 1;
        }

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, columns, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecipeAdapter = new RecipeAdapter(this);

        mRecyclerView.setAdapter(mRecipeAdapter);

        loadRecipeInfo();
    }

    private void loadRecipeInfo() {
        new FetchRecipeInfoTask().execute();
    }

    @Override
    public void onListItemClick(Recipe recipeInfo) {
        int recipe = recipeInfo.getId();
        Intent intent = new Intent(this, MasterFragmentActivity.class);
        intent.putExtra("id", recipe);
        startActivity(intent);
    }

    public class FetchRecipeInfoTask extends AsyncTask<Void, Void, Recipe[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Recipe[] doInBackground(Void... voids) {

            URL recipeRequestUrl = NetworkUtils.buildUrl();

            try {
                String JsonResponse = NetworkUtils.getResponseFromHttpUrl(recipeRequestUrl);

                Recipe[] recipeInfo = JsonUtils.getBasicInfoFromJson(JsonResponse);

                return recipeInfo;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Recipe[] recipeInfo) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.INVISIBLE);
            mRecipeAdapter.setData(recipeInfo);
        }
    }
}
