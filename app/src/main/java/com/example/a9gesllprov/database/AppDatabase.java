package com.example.a9gesllprov.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * The class representing this application's Room Database
 */
@TypeConverters({DateConverter.class, UriConverter.class})
@Database(entities = {Event.class, EventPhoto.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
    public abstract EventPhotoDao eventPhotoDao();
}