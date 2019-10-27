package com.example.a9gesllprov.core;

import android.content.Context;

/**
 * Provides resolution related utility functionality.
 */
public class ResolutionUtil
{
    /**
     * Converts density-independent pixels to pixels.
     * @param context The context in which the conversion takes place.
     * @param dp The amount of DP to convert.
     * @return The converted pixel value.
     */
    public static float convertDpToPixels(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
