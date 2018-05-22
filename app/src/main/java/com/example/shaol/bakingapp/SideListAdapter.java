package com.example.shaol.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shaol.bakingapp.Models.Ingredients;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shaol on 5/18/2018.
 */

public class SideListAdapter extends RecyclerView.Adapter<SideListAdapter.SideListViewHolder> {

    Ingredients[] aIngredientsInfo;

    public SideListAdapter(Ingredients[] ingredients) {
        aIngredientsInfo = ingredients;
    }

    class SideListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient) TextView ingredientView;
        @BindView(R.id.ingredient_amount) TextView ingredientAmountView;
        @BindView(R.id.ingredient_measure) TextView ingredientMeasureView;

        public SideListViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public SideListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.side_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new SideListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SideListViewHolder viewHolder, int position) {
        Ingredients ingredients = aIngredientsInfo[position];

        String ingredient = ingredients.getIngredients();
        viewHolder.ingredientView.setText(ingredient);

        String ingredientAmount = ingredients.getQuantity();
        viewHolder.ingredientAmountView.setText(String.valueOf(ingredientAmount));

        String ingredientMeasure = ingredients.getMeasure();
        viewHolder.ingredientMeasureView.setText(ingredientMeasure);
    }

    @Override
    public int getItemCount() {
        if (aIngredientsInfo == null) return 0;
        return aIngredientsInfo.length;
    }
}
