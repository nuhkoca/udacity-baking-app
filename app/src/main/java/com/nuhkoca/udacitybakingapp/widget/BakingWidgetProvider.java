package com.nuhkoca.udacitybakingapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.nuhkoca.udacitybakingapp.R;
import com.nuhkoca.udacitybakingapp.view.recipe.activity.RecipeActivity;

import java.util.Objects;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    private static final String INTENT_UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        RemoteViews views = getRecipesFromGridView(context);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public RemoteViews getRecipesFromGridView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipes_widget);

        Intent gridIntent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.gvRecipeWidget, gridIntent);

        Intent appIntent = new Intent(context, RecipeActivity.class);
        PendingIntent intent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.gvRecipeWidget, intent);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent != null) {
            if (Objects.equals(INTENT_UPDATE_ACTION, intent.getAction())) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisWidget = new ComponentName(context.getApplicationContext(), BakingWidgetProvider.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

                RemoteViews remoteViews = getRecipesFromGridView(context);

                appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.gvRecipeWidget);
                onUpdate(context, appWidgetManager, appWidgetIds);
            }
        }
    }
}