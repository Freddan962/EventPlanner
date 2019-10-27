package com.example.a9gesllprov.core;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * Provides utility functions for contacts.
 */
public class ContactUtil {
    /**
     * Fetches a cursor pointing to the device's contacts.
     * @return The contact cursor.
     */
    public static Cursor getContactsCursor(Context context) {
        return context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    }

    /**
     * Fetches a cursor pointing to the contact entry.
     * @param contactId The ID of the contract.
     * @return The cursor pointing to the contract or null if not.
     */
    public static Cursor getContactCursor(Context context, String contactId) {
        return context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);
    }
}
