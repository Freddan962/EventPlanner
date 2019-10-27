package com.example.a9gesllprov.database;

import android.net.Uri;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * The EventPhoto Dao for the room database.
 *
 * Provides the required query functionality for event photos.
 */
@Dao
public interface EventPhotoDao {
    @Query("SELECT * FROM EventPhoto WHERE event_id = :event_uid")
    List<EventPhoto> getAll(int event_uid);

    @Insert
    void insert(EventPhoto... eventPhoto);

    @Query("SELECT * FROM EventPhoto WHERE uid=:uid")
    EventPhoto findById(int uid);

    @Query("DELETE FROM EventPhoto where uid=:uid")
    void deleteById(int uid);

    @Query("SELECT * FROM EventPhoto where uri=:uri")
    EventPhoto findByURI(Uri uri);

    @Query("DELETE FROM EventPhoto")
    void deleteAll();
}
