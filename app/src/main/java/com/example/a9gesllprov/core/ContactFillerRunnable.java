package com.example.a9gesllprov.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.a9gesllprov.R;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;

/**
 * Fills a ViewGroup with contacts over time.
 */
public class ContactFillerRunnable implements Runnable {
    private Activity activity;
    private ViewGroup toBeFilled;

    private int sleepTime;
    private boolean isAlive = true;

    private String filter;
    private int event_uid;

    private HashMap<Integer, View> visibleContacts;

    /**
     * Constructs a new ContactFillerRunnable.
     * @param activity The activity owning this runnable.
     * @param toBeFilled The view group to be filled with contact views.
     * @param event_uid The UID of the event to be shared.
     * @param visibleContacts A HashMap containg contact id to view mapping.
     */
    public ContactFillerRunnable(Activity activity, ViewGroup toBeFilled, int event_uid, HashMap<Integer, View> visibleContacts) {
        this.activity = activity;
        this.toBeFilled = toBeFilled;
        this.event_uid = event_uid;
        this.visibleContacts = visibleContacts;
    }

    /**
     * Adds one view per contact found over a set time duration.
     * This avoids the device to freeze when there are many contacts to generate buttons for.
     */
    @Override
    public void run() {
        Cursor contactsCursor = ContactUtil.getContactsCursor(activity);
        int cursorCount = contactsCursor.getCount();

        if (cursorCount > 0)
            sleepTime = activity.getResources().getInteger(R.integer.sms_contact_picker_fill_contacts_time_ms) / cursorCount;

        while (contactsCursor.moveToNext() && isAlive) {
            final int contactId = contactsCursor.getInt(contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
            final String contactName = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            if (visibleContacts.containsKey(contactId) || ContactFilter.shouldBeFiltered(contactName, filter))
                continue;

            Cursor contactCursor = ContactUtil.getContactCursor(activity, Integer.toString(contactId));
            while (contactCursor.moveToNext()) {
                processEntry(contactName, contactCursor);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * Stops the contact filler.
     */
    public void stop() {
        isAlive = false;
    }

    /**
     * Starts the contact filler.
     */
    public void start() {
        isAlive = true;
    }

    /**
     * Sets the filter of the contact runner.
     * @param filter The filter to be set.
     */
    public void setFilter(String filter)  {
        this.filter = filter;
    }

    /**
     * Processes one entry in the contact list.
     * @param name The name of the contact.
     * @param contactCursor The contacts cursor.
     */
    private void processEntry(final String name, Cursor contactCursor) {
        final String phoneNumber = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        final int id = contactCursor.getInt(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

        activity.runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                MaterialButton contactButton = createButton(name, phoneNumber);

                if (isAlive && !visibleContacts.containsKey(id)) {
                    visibleContacts.put(id, contactButton);
                    toBeFilled.addView(contactButton);
                }
            }
        });
    }

    /**
     * Creates the contact selection button.
     * @param name The name of the contact.
     * @param phoneNumber The phone number of the contact.
     * @return The constructed button.
     */
    private MaterialButton createButton(String name, final String phoneNumber) {
        MaterialButton contactButton = new MaterialButton(activity);
        contactButton.setTag(R.integer.tag_contact_name, name);
        contactButton.setText(name + System.lineSeparator() + phoneNumber);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int margin = (int) ResolutionUtil.convertDpToPixels(activity, 16);
        params.setMargins(margin, 0, margin, 0);
        contactButton.setLayoutParams(params);

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventSharer.shareSMS(activity, phoneNumber, event_uid);
                activity.finish();
            }
        });

        return contactButton;
    }
}
