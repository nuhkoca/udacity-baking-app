package com.nuhkoca.udacitybakingapp.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.nuhkoca.udacitybakingapp.R;
import com.nuhkoca.udacitybakingapp.helper.Constants;

/**
 * Created by nuhkoca on 3/15/18.
 */

public class ColumnCalculator {
    public static int getOptimalNumberOfColumn(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (context != null) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }

        int widthDivider = Constants.TABLET_DIVIDER;

        if (context != null) {
            if (context.getResources().getBoolean(R.bool.isTablet)) {
                int width = displayMetrics.widthPixels;
                int nColumns = width / widthDivider;
                if (nColumns < 2) return 2;
                return nColumns;
            }
        }

        return 2;
    }
}