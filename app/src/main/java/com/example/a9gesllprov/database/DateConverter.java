package com.example.a9gesllprov.database;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Responsible for converting back and forth between Date objects for the Room database.
 */
public class DateConverter {

    /**
     * Converts a Long object to a Date object.
     * @param dateLong The Long to convert.
     * @return The converted date object or null.
     */
    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    /**
     * Converts a Date object to a Long object.
     * @param date The Date to convert.
     * @return The converted Long or null.
     */
    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }
}