package com.example.shaol.bakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shaol.bakingapp.Models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shaol on 5/17/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final ListItemClickListener mOnClickListener;
    Recipe[] aRecipeInfo;

    public interface ListItemClickListener {
        void onListItemClick(Recipe recipeInfo);
    }

    public RecipeAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_text_li) TextView recipeNameText;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipeInfo = aRecipeInfo[adapterPosition];
            mOnClickListener.onListItemClick(recipeInfo);
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder viewHolder, int position) {
        Recipe recipe = aRecipeInfo[position];
        String recipeName = recipe.getName();
        viewHolder.recipeNameText.setText(recipeName);
    }

    @Override
    public int getItemCount() {
        if (aRecipeInfo == null) return 0;
        return aRecipeInfo.length;
    }

    public void setData(Recipe[] recipeInfo){
        aRecipeInfo = recipeInfo;
        notifyDataSetChanged();
    }
}
