package com.example.a9gesllprov.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.a9gesllprov.R;
import com.example.a9gesllprov.SMSContactPicker;
import com.example.a9gesllprov.database.DatabaseManager;
import com.example.a9gesllprov.database.Event;

/**
 * Responsible for providing event sharing functionality.
 */
public class EventSharer {

    public static String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";

    /**
     * Shares an event by SMS.
     * Requests a contact to be chosen.
     * @param context The context to be shared from.
     * @param event_uid The UID of the event.
     */
    public static void shareSMS(Context context, int event_uid) {
        Intent intent = new Intent(context, SMSContactPicker.class);
        intent.putExtra(EXTRA_EVENT_ID, event_uid);
        context.startActivity(intent);
    }

    /**
     * Shares an event by SMS.
     * @param activity The activity to be shared from.
     * @param phoneNumber The phone number of the recipient.
     * @param event_uid The UID of the event.
     */
    public static void shareSMS(Activity activity, String phoneNumber, int event_uid) {
        SmsManager manager = SmsManager.getDefault();
        Event event = DatabaseManager.getInstance().getDatabase().eventDao().findById(event_uid);

        String message = new StringBuilder()
                .append(event.name)
                .append(System.lineSeparator())
                .append(activity.getString(R.string.event_sharer_sms_from))
                .append(" ")
                .append(event.getStartDateFormatted())
                .append(" ")
                .append(activity.getString(R.string.event_sharer_sms_to))
                .append(" ")
                .append(event.getEndDateFormatted())
                .append(System.lineSeparator())
                .append(event.description)
                .toString();

        manager.sendMultipartTextMessage(phoneNumber, null, manager.divideMessage(message), null, null);
        Toast.makeText(activity, activity.getString(R.string.event_sharer_sms_successful), Toast.LENGTH_LONG).show();
    }
}
