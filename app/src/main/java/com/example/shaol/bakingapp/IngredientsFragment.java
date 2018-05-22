package com.example.shaol.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shaol.bakingapp.Models.Ingredients;
import com.example.shaol.bakingapp.Widget.ListWidgetService;
import com.example.shaol.bakingapp.Widget.MyWidgetProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shaol on 5/19/2018.
 */

public class IngredientsFragment extends Fragment {
    @BindView(R.id.ingredient_rv)
    RecyclerView recyclerView;
    @BindView(R.id.ingredient_button_create)
    Button ingredientButton;
    @BindView(R.id.ingredient_button_destroy)
    Button clearButton;

    SideListAdapter mSideListAdapter;

    Ingredients[] dummyIngredientsArray;

    public IngredientsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredient_view, container, false);

        ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        mSideListAdapter = new SideListAdapter(dummyIngredientsArray);

        recyclerView.setAdapter(mSideListAdapter);

        ingredientButton.setOnClickListener(new MyClickListener(dummyIngredientsArray));

        clearButton.setOnClickListener(new OtherClickListener());

        return rootView;
    }

    public void setIngredients(Ingredients[] recipeIngredients) {
        dummyIngredientsArray = recipeIngredients;
    }

    public class MyClickListener implements View.OnClickListener {

        Ingredients[] mIngredients;

        public MyClickListener(Ingredients[] ingredients){
            mIngredients = ingredients;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), MyWidgetProvider.class);
            intent.setAction(MyWidgetProvider.UPDATE_ACTION);
            intent.putExtra("aIngredients", mIngredients);
            getContext().sendBroadcast(intent);
        }
    }

    public class OtherClickListener implements View.OnClickListener {

        public OtherClickListener() {
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), MyWidgetProvider.class);
            intent.setAction(MyWidgetProvider.CLEAR_ACTION);
            getContext().sendBroadcast(intent);
        }
    }
}
