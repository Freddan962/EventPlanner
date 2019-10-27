package com.example.a9gesllprov.core;

/**
 * Responsible for providing formatting functionality for date and times.
 */
public class DateTimeFormatter {

    /**
     * Formats a time string.
     * @param year The year.
     * @param month The month.
     * @param day The day,
     * @param hour The hour.
     * @param minute The minute.
     * @return The formatted time string.
     */
    public static String formatTime(int year, int month, int day, int hour, int minute) {
        StringBuilder builder = new StringBuilder();

        builder.append(year + "-");
        builder.append(String.format("%02d-", month));
        builder.append(String.format("%02d", day));
        builder.append(String.format(" %02d:", hour));
        builder.append(String.format("%02d", minute));

        return builder.toString();
    }
}
