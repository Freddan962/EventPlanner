package com.example.a9gesllprov.core;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Responsible for call handling.
 */
public class CallHelper {

    /**
     * Starts a new activity that dials the provided number.
     * @param from The activity to be created from.
     * @param number The number to be calling.
     */
    public static void callNumber(AppCompatActivity from, String number) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));
        from.startActivity(callIntent);
    }
}
