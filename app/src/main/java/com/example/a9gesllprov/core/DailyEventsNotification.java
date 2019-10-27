package com.example.a9gesllprov.core;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.a9gesllprov.R;
import com.example.a9gesllprov.database.DatabaseManager;
import com.example.a9gesllprov.database.Event;

import java.util.Date;
import java.util.List;

//Author Fredrik Sander (frsa5550)

/**
 * Shows a notification with the number of events scheduled for today.
 */
public class DailyEventsNotification {

    private Activity activity;
    private final String CHANNEL_ID = "1";
    private int notificationID = 0;
    private int eventCount = -1;

    private static boolean hasShown = false;

    /**
     * Constructs a new DailyEventsNotification instance.
     * @param activity The activity from which the activity is created.
     */
    public DailyEventsNotification(Activity activity) {
        this.activity = activity;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "COMM", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        List<Event> todaysEvents = DatabaseManager.getInstance().getDatabase().eventDao().getEventsFor(new Date());
        eventCount = todaysEvents.size();
    }

    /**
     * Shows the notification on the device.
     */
    public void show() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(activity.getString(R.string.view_events_daily_events_notification_title))
                .setContentText(eventCount + activity.getString(R.string.view_events_daily_events_notification_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
        notificationManager.notify(notificationID++, builder.build());
        DailyEventsNotification.hasShown = true;
    }

    /**
     * Tests whether or not the notification has already been shown.
     * @return
     */
    public static boolean hasShown() {
        return hasShown;
    }
}
