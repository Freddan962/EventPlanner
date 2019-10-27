package com.example.a9gesllprov.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.a9gesllprov.core.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;

/**
 * The event entity for the Room database.
 */
@Entity(tableName = "Event")
public class Event {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    public int uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "startDate")
    public Date startDate;

    @ColumnInfo(name = "endDate")
    public Date endDate;

    @ColumnInfo(name = "contactNumber")
    public String contactNumber;

    @ColumnInfo(name = "mail")
    public String mail;

    @ColumnInfo(name = "picturePath")
    public String picturePath;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "youtubeUrl")
    public String youtubeUrl;

    @ColumnInfo(name = "homePage")
    public String homePage;

    @ColumnInfo(name = "calendarEntryID")
    public Long calendarEntryID;

    /**
     * Returns formatted start date for the event.
     * @return The formatted start time as a string.
     */
    public String getStartDateFormatted() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.startDate);

        return DateTimeFormatter.formatTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                                            c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }

    /**
     * Returns formatted end date for the event.
     * @return The formatted end time as a string.
     */
    public String getEndDateFormatted() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.endDate);

        return DateTimeFormatter.formatTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                                            c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }

    /**
     * Checks whether or not the event has a calendar entry.
     * @return True or false.
     */
    public boolean hasCalendarEntry() {
        return calendarEntryID != null;
    }
}
