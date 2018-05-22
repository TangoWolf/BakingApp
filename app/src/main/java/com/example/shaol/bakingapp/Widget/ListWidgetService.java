package com.example.shaol.bakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.shaol.bakingapp.Models.Ingredients;
import com.example.shaol.bakingapp.R;

/**
 * Created by shaol on 5/20/2018.
 */

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        Ingredients[] aIngredients;

        public ListRemoteViewsFactory(Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            aIngredients = MyWidgetProvider.aIngredients;
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
