package com.example.shaol.bakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.RemoteViews;

import com.example.shaol.bakingapp.Models.Ingredients;
import com.example.shaol.bakingapp.R;

/**
 * Created by shaol on 5/20/2018.
 */

public class MyWidgetProvider extends AppWidgetProvider {

    static Ingredients[] aIngredients;

    public static final String UPDATE_ACTION = "com.example.shaol.bakingapp.Widget.action.UPDATE";
    public static final String CLEAR_ACTION = "com.example.shaol.bakingapp.Widget.action.CLEAR";

    public void onReceive(Context context, Intent intent) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(UPDATE_ACTION)){
            int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, MyWidgetProvider.class));

            Parcelable[] parcelables = intent.getExtras().getParcelableArray("aIngredients");
            Ingredients[] recipeIngredients = new Ingredients[parcelables.length];
            for (int i = 0; i < parcelables.length; i++) {
                Ingredients aIngredient = (Ingredients) parcelables[i];
                recipeIngredients[i] = aIngredient;
            }

            aIngredients = recipeIngredients;

            manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);

            onUpdate(context, manager, appWidgetIds);
        }
        super.onReceive(context, intent);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            Intent intent = new Intent(context, ListWidgetService.class);

            intent.putExtra("AppWidgetId", appWidgetIds[i]);
            Bundle b = new Bundle();
            b.putParcelableArray("Ingredient", aIngredients);
            intent.putExtra("bundle", b);

            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_view);

            remoteViews.setRemoteAdapter(appWidgetIds[i], R.id.widget_list_view, intent);

            remoteViews.setEmptyView(R.id.widget_list_view, R.id.emptyWidgetView);

            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /*public void onClear(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            Intent intent = new Intent(context, ListWidgetService.class);

            Ingredients[] hope = null;

            intent.putExtra("AppWidgetId", appWidgetIds[i]);
            Bundle b = new Bundle();
            b.putParcelableArray("Ingredient", hope);
            intent.putExtra("bundle", b);

            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_view);

            remoteViews.setRemoteAdapter(appWidgetIds[i], R.id.widget_list_view, intent);

            remoteViews.setEmptyView(R.id.widget_list_view, R.id.emptyWidgetView);

            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        }
    }*/
}
