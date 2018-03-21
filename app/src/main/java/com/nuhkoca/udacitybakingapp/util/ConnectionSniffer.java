package com.nuhkoca.udacitybakingapp.util;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by nuhkoca on 3/13/18.
 */

public class ConnectionSniffer {
    private static boolean isReachable;

    /**
     *
     * @param myUrl to check if server is alive
     * @return true if there is an active connection. This sends pings to the target URL and if server responds, it returns true. This is different than check connectivity because if there is no connection even though you are on a Wifi, it nevertheless returns true.
     */
    public static boolean sniff(String myUrl) {
        try {
            isReachable = new Pinger(myUrl).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return isReachable;
    }

    private static class Pinger extends AsyncTask<Void, Void, Boolean> {

        private String myUrl;

        Pinger(String myUrl) {
            this.myUrl = myUrl;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            URL url;
            try {
                url = new URL(myUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setConnectTimeout(30000);
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return true;
                }
            } catch (IOException e) {
                return false;
            }

            return true;
        }
    }
}