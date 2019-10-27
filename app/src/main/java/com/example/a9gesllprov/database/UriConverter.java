package com.example.a9gesllprov.database;

import android.net.Uri;

import androidx.room.TypeConverter;

/**
 * Responsible for converting back and forth between Uri objects for the Room database.
 */
class UriConverter {

    /**
     * Converts a string to a Uri object.
     * @param value The string value.
     * @return The converted Uri object.
     */
    @TypeConverter
    public Uri fromString(String value) {
        return value == null ? null : Uri.parse(value);
    }

    /**
     * Converts a Uri object to a string.
     * @param uri The uri object to be converted.
     * @return The converted String.
     */
    @TypeConverter
    public String toString(Uri uri) {
        return uri.toString();
    }
}