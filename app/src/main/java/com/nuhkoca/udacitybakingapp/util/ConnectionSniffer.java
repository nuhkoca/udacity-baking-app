package com.nuhkoca.udacitybakingapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.nuhkoca.udacitybakingapp.App;
import com.nuhkoca.udacitybakingapp.BuildConfig;
import com.nuhkoca.udacitybakingapp.helper.Constants;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by nuhkoca on 3/13/18.
 */

public class ConnectionSniffer {
    private static boolean isReachable;

    public static boolean sniff() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        new MyConnectionSniffer().execute(BuildConfig.BASE_URL);

        return networkInfo != null &&
                networkInfo.isConnected() &&
                networkInfo.isConnectedOrConnecting() &&
                networkInfo.isAvailable() &&
                !isReachable;
    }


    private static class MyConnectionSniffer extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            String hostName = strings[0];

            try {
                return InetAddress.getByName(hostName).isReachable(Constants.NETWORK_TIMEOUT_DURATION);
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isReachable = aBoolean;
        }
    }
}