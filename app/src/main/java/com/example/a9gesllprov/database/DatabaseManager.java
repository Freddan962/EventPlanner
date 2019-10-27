package com.example.a9gesllprov.database;

import android.content.Context;

import androidx.room.Room;

/**
 * A class responsible for managing the AppDatabase instance.
 */
public class DatabaseManager {

    private static DatabaseManager Instance;
    private AppDatabase db;

    /**
     * Constructs a new database manager.
     * @param context The context to be created within.
     */
    private DatabaseManager(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "name").allowMainThreadQueries().build();
    }

    /**
     * Initialize the database manager by creating a new instance if needed.
     * @param context The context to be created within.
     */
    public static void initialize(Context context) {
        if (Instance == null)
            Instance = new DatabaseManager(context);
    }

    /**
     * Fetches the singleton DatabaseManager instance.
     * @return The DatabaseManager instance.
     */
    public static DatabaseManager getInstance() { return Instance; }

    /**
     * Gets the database instance.
     * @return The database instance.
     */
    public AppDatabase getDatabase() { return db; }
}
