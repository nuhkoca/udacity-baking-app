package com.nuhkoca.udacitybakingapp.util;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.nuhkoca.udacitybakingapp.R;

/**
 * Created by nuhkoca on 3/13/18.
 */

public class SnackbarPopper {
    private enum Length {
        SHORT,
        INDEFINITE
    }

    public static void pop(View view, String message) {
        Snackbar snackbar = getSnackbar(view, message, Length.SHORT);

        snackbar.show();
    }

    public static void popIndefinite(View view, String message) {
        Snackbar snackbar = getSnackbar(view, message, Length.INDEFINITE);

        snackbar.show();
    }

    private static Snackbar getSnackbar(View view, String message, Length duration) {
        Snackbar snackbar = Snackbar.make(view, message, 0);

        switch (duration) {
            case SHORT:
                snackbar = snackbar.setDuration(Snackbar.LENGTH_SHORT);
                break;
            case INDEFINITE:
                snackbar = snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                break;
        }

        View currentView = snackbar.getView();
        currentView.setBackgroundColor(ContextCompat.getColor(currentView.getContext(), R.color.colorPrimary));

        TextView currentText = currentView.findViewById(android.support.design.R.id.snackbar_text);
        currentText.setTextSize(16);
        currentText.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorWhite));

        return snackbar;
    }
}