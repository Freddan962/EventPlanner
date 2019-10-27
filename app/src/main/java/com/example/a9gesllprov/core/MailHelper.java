package com.example.a9gesllprov.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Responsible for mail handling.
 */
public class MailHelper {

    /**
     * Starts a mail intent with recipient filled in.
     * @param from The context the mail is sent from.
     * @param recipient The recipient of the mail.
     */
    public static void mailRecipient(Context from, String recipient) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { recipient });
        from.startActivity(intent);
    }
}
