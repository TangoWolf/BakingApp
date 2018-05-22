package com.example.shaol.bakingapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shaol.bakingapp.Models.Details;
import com.example.shaol.bakingapp.Models.Ingredients;
import com.example.shaol.bakingapp.Models.Steps;
import com.example.shaol.bakingapp.Utils.JsonUtils;
import com.example.shaol.bakingapp.Utils.NetworkUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shaol on 5/18/2018.
 */

public class MasterListFragment extends Fragment {
    OnRecipeClickListener mCallback;
    @BindView(R.id.rv_main_fragment) RecyclerView recyclerView;
    @BindView(R.id.detail_ingredient_li) TextView ingredientView;
    int recipeId;
    MasterListAdapter mMasterListAdapter;
    Ingredients[] fetchedIngredients;

    public interface OnRecipeClickListener {
        void onRecipeSelected(@Nullable Ingredients[] ingredients, @Nullable Steps stepsInfo, int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnRecipeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeClickListener");
        }
    }


    public MasterListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_view, container, false);

        ButterKnife.bind(this, rootView);

        ingredientView.setText(R.string.ingredients);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        mMasterListAdapter = new MasterListAdapter(new MasterListAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(Steps stepsInfo, int position) {
                mCallback.onRecipeSelected(null, stepsInfo, position);
            }
        });

        recyclerView.setAdapter(mMasterListAdapter);

        recipeId = getActivity().getIntent().getIntExtra("id", 0);

        loadStepsInfo();

        return rootView;
    }

    private void loadStepsInfo() {
        new FetchStepsInfoTask().execute(String.valueOf(recipeId));
    }

    public class FetchStepsInfoTask extends AsyncTask<String, Void, Details> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Details doInBackground(String... params) {
            int recipeId = Integer.parseInt(params[0]);

            URL recipeRequestUrl = NetworkUtils.buildUrl();

            try {
                String JsonResponse = NetworkUtils.getResponseFromHttpUrl(recipeRequestUrl);

                Details detailsInfo = JsonUtils.getStepsInfoFromJson(JsonResponse, recipeId);

                return detailsInfo;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Details detailsInfo) {
            fetchedIngredients = detailsInfo.getIngredients();
            ingredientView.setOnClickListener(new MyClickListener(fetchedIngredients));
            mMasterListAdapter.setData(detailsInfo);
        }
    }

    public class MyClickListener implements View.OnClickListener {

        Ingredients[] mIngredients;

        public MyClickListener(Ingredients[] ingredients){
            mIngredients = ingredients;
        }

        @Override
        public void onClick(View view) {
            mCallback.onRecipeSelected(mIngredients, null, 500);
        }
    }
}
