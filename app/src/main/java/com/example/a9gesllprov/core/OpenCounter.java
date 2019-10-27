package com.example.a9gesllprov.core;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * OpenCounter is responsible for providing the functionality required to
 * count and read the amount of times the application has been opened.
 */
public class OpenCounter {

    private final String KEY_OPEN_COUNT = "OPEN_COUNT";
    private int openCount = 0;

    /**
     * Constructs a new OpenCounter instance.
     * @param activity The activity context for which this instance is created in.
     */
    public OpenCounter(Activity activity) {
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        openCount = preferences.getInt(KEY_OPEN_COUNT, 0);
        openCount++;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_OPEN_COUNT, openCount);
        editor.commit();
    }

    /**
     * Fetches the open count.
     * @return The open count of this application.
     */
    public int getOpenCount() {
        return openCount;
    }
}
