package com.nuhkoca.udacitybakingapp.helper;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.nuhkoca.udacitybakingapp.App;
import com.nuhkoca.udacitybakingapp.R;
import com.nuhkoca.udacitybakingapp.provider.BakingProvider;

/**
 * Created by nuhkoca on 3/18/18.
 */

public class DatabaseHandler {

    public static class Splicer extends AsyncTask<Void, Void, Uri> {

        private ContentValues contentValues;
        private String recipeName;

        public Splicer(ContentValues contentValues, String recipeName) {
            this.contentValues = contentValues;
            this.recipeName = recipeName;
        }


        @Override
        protected Uri doInBackground(Void... voids) {
            try {
                return App.getInstance().getContentResolver().insert(BakingProvider.BakingIngredients.CONTENT_URI, contentValues);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Uri uri) {
            if (uri != null) {
                Toast.makeText(App.getInstance().getApplicationContext(),
                        String.format(App.getInstance().getString(R.string.added_to_widget), recipeName), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(App.getInstance().getApplicationContext(),
                        App.getInstance().getString(R.string.error_while_adding_to_widget), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class Remover extends AsyncTask<Void, Void, Integer> {

        private String recipeName;

        public Remover(String recipeName) {
            this.recipeName = recipeName;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                Uri uri = BakingProvider.BakingIngredients.CONTENT_URI;

                return App.getInstance().getContentResolver().delete(uri, "foodName = ?", new String[]{"" + recipeName});
            } catch (Exception e) {
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer > 0) {
                Toast.makeText(App.getInstance().getApplicationContext(),
                        String.format(App.getInstance().getString(R.string.remove_from_widget), recipeName), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(App.getInstance().getApplicationContext(),
                        App.getInstance().getString(R.string.error_while_removing_from_widget), Toast.LENGTH_SHORT).show();
            }
        }
    }
}