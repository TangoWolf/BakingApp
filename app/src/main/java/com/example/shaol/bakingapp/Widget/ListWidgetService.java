package com.example.shaol.bakingapp.Widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.example.shaol.bakingapp.IngredientsFragment;
import com.example.shaol.bakingapp.Models.Ingredients;
import com.example.shaol.bakingapp.R;

import java.io.Serializable;

/**
 * Created by shaol on 5/20/2018.
 */

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        Ingredients[] aIngredients;
        Intent thisIntent;

        public ListRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            thisIntent = intent;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            Bundle leBundle = thisIntent.getBundleExtra("bundle");
            Parcelable[] ughWhy = leBundle.getParcelableArray("Ingredient");
            Ingredients[] ugh = new Ingredients[ughWhy.length];
            for (int i = 0; i < ughWhy.length; i++) {
                Ingredients aIngredient = (Ingredients) ughWhy[i];
                ugh[i] = aIngredient;
            }
            aIngredients = ugh;
        }

        @Override
        public int getCount() {
            if (aIngredients == null) return 0;
            return aIngredients.length;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            if (aIngredients == null) return null;
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_view_item);

            Ingredients ingredient = aIngredients[i];

            remoteViews.setTextViewText(R.id.widget_ingredient, ingredient.getIngredients());
            remoteViews.setTextViewText(R.id.widget_ingredient_amount, ingredient.getQuantity());
            remoteViews.setTextViewText(R.id.widget_ingredient_measure, ingredient.getMeasure());

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDestroy() {
        }
    }
