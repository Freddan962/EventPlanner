package com.example.a9gesllprov.core;

import android.content.Context;
import android.content.Intent;

import com.example.a9gesllprov.ViewPageActivity;

/**
 * Provides website related functionality.
 */
public class WebsiteHelper {

    /**
     * Opens a website inside the application.
     * @param from The context to open the website within.
     * @param url The url of the website to open.
     */
    public static void openLocal(Context from, String url) {
        Intent intent = new Intent(from, ViewPageActivity.class);
        intent.putExtra("EXTRA_WEBPAGE", url);
        from.startActivity(intent);
    }

    /**
     * Opens a website inside the application if the device is connected to the internet.
     * @param from The context to open the application from.
     * @param url The URL of the application.
     * @return True if the website was opened false if not.
     */
    public static boolean openLocalWithConnection(Context from, String url) {
        if (!Connectivity.isConnectedToInternet(from))
            return false;

        openLocal(from, url);
        return true;
    }
}
