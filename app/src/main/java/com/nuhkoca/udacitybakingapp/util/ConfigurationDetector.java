package com.nuhkoca.udacitybakingapp.util;

import android.content.Context;

/**
 * Created by nuhkoca on 3/19/18.
 */

public class ConfigurationDetector {

    public static int isTabletInLandscapeMode(Context context) {
        return context.getResources().getConfiguration().orientation;
    }
}