package com.example.a9gesllprov.core;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Responsible for providing connection related functionality.
 */
public class Connectivity
{
    /**
     * Checks whether the application is connected to the internet or not.
     * @param from The context to check from.
     * @return True or false.
     */
    public static boolean isConnectedToInternet(Context from) {
        ConnectivityManager connectivityManager = (ConnectivityManager)from.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
