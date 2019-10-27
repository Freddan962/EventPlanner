package com.example.a9gesllprov.core;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;

import java.io.FileNotFoundException;

/**
 * Responsible for thumbnail generation.
 */
public class ThumbnailGenerator
{

    /**
     * Generates a new thumbnail.
     * @param wrapper The wrapper of the context for the thumbnail to be created within.
     * @param uri The URI of the original asset.
     * @param width The width of the thumbnail.
     * @param height The height of the thumbnail.
     * @return The generated bitmap thumbnail or null.
     */
    public static Bitmap generate(ContextWrapper wrapper, Uri uri, final int width, final int height) {
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(wrapper.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException exception) { exception.printStackTrace(); }

        bitmap = bitmap == null ? BitmapFactory.decodeFile(uri.toString()) : bitmap;
        return ThumbnailUtils.extractThumbnail(bitmap, width, height);
    }
}
