package com.example.a9gesllprov.core;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;

import com.example.a9gesllprov.database.Event;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Responsible for providing calendar utility functionality.
 */
public class CalendarUtil {
    /**
     * Creates a new entry for a event in the primary calendar.
     * @param context The context for which to create the entry in.
     * @param event The event to be filled into the calendar.
     * @return True if successful false if not.
     */
    public static boolean createEntry(Context context, Event event) {
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(event.startDate);

        Calendar endTime = Calendar.getInstance();
        endTime.setTime(event.endDate);

        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startTime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, event.name);
        values.put(CalendarContract.Events.DESCRIPTION, event.description);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        values.put(CalendarContract.Events.CALENDAR_ID, 1);

        if (context.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            return false;

        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
        event.calendarEntryID = Long.parseLong(uri.getLastPathSegment());
        return true;
    }

    /**
     * Deletes a event entry from the calendar.
     * @param context The context for which the deletion takes place.
     * @param event The event to be deleted from the device.
     */
    public static void deleteEntry(Context context, Event event) {
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, event.calendarEntryID);
        context.getContentResolver().delete(deleteUri, null, null);
        event.calendarEntryID = null;
    }
}
