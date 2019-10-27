package com.example.a9gesllprov.core;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Provides utility function(s) related to files.
 */
public class FileUtil {

    /**
     * Creates a unique image file.
     * @param context The context file is created within.
     * @return A new file instance or null.
     */
    public static File createUniqueJPGFile(Context context) {
        String stamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + stamp + "_";

        try {
            return File.createTempFile(
                    fileName,
                    ".jpg",
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );
        } catch (IOException e) { return null; }
    }
}
