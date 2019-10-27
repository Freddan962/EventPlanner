package com.example.a9gesllprov.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

/**
 * The EventDao for the room database.
 *
 * Provides the required query functionality for events.
 */
@Dao
public interface EventDao {
    @Query("SELECT * FROM event")
    List<Event> getAll();

    @Insert
    void insert(Event... prime);

    @Query("SELECT * FROM event ORDER BY startDate")
    List<Event> getAllOrderClosestStart();

    @Query("DELETE FROM event")
    void deleteAll();

    @Query("SELECT * FROM event WHERE uid=:uid")
    Event findById(int uid);

    @Query("DELETE FROM event where uid=:uid")
    void delete(int uid);

    @Update
    void update(Event... eventEntities);

    @Query("SELECT * FROM event WHERE :date BETWEEN startDate AND endDate")
    List<Event> getEventsFor(Date date);
}
