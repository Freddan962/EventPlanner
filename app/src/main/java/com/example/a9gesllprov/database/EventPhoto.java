package com.example.a9gesllprov.database;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The event photo entity for the Room database.
 */
@Entity(tableName = "EventPhoto")
public class EventPhoto {
    @PrimaryKey( autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "uri")
    public Uri uri;

    @ColumnInfo(name = "event_id")
    public int eventId;
}
