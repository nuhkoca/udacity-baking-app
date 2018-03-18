package com.nuhkoca.udacitybakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.nuhkoca.udacitybakingapp.R;
import com.nuhkoca.udacitybakingapp.provider.BakingContract;
import com.nuhkoca.udacitybakingapp.provider.BakingProvider;

/**
 * Created by nuhkoca on 3/17/18.
 */

public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }

    public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private Cursor mCursor;

        GridRemoteViewsFactory(Context context) {
            this.mContext = context;
        }

        @Override
        public void onCreate() {}

        @Override
        public void onDataSetChanged() {
            if (mCursor != null) mCursor.close();

            mCursor = mContext.getContentResolver().query(
                    BakingProvider.BakingIngredients.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onDestroy() {
            mCursor.close();
        }

        @Override
        public int getCount() {
            if (mCursor == null) {
                return 0;
            }

            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            if (mCursor == null || mCursor.getCount() == 0) return null;

            mCursor.moveToPosition(i);

            int foodNameIndex = mCursor.getColumnIndex(BakingContract.COLUMN_FOOD_NAME);
            int quantityIndex = mCursor.getColumnIndex(BakingContract.COLUMN_QUANTITY_MEASURE_INGREDIENTS);

            String foodName = mCursor.getString(foodNameIndex);
            String quantity = mCursor.getString(quantityIndex);

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_widget_item);

            remoteViews.setTextViewText(R.id.tvWidgetRecipeName, foodName);
            remoteViews.setViewVisibility(R.id.tvWidgetRecipeName, View.VISIBLE);

            remoteViews.setTextViewText(R.id.tvWidgetRecipeDetails, quantity);
            remoteViews.setViewVisibility(R.id.tvWidgetRecipeDetails, View.VISIBLE);

            Intent fillIntent = new Intent();
            remoteViews.setOnClickFillInIntent(R.id.tvWidgetRecipeName, fillIntent);
            remoteViews.setOnClickFillInIntent(R.id.tvWidgetRecipeDetails, fillIntent);

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
    }
}