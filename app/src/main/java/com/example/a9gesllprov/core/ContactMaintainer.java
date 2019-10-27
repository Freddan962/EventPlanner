package com.example.a9gesllprov.core;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Maintains contacts within a ViewGroup dynamically over time.
 */
public class ContactMaintainer {

    private Activity activity;
    private boolean isWorking = false;
    private volatile HashMap<Integer, View> visibleContacts = new HashMap<>();

    private Thread contactFillerThread;
    private ContactFillerRunnable contactFiller;

    private Thread contactCleanerThread;
    private ContactCleanerRunnable contactCleaner;

    /**
     * Constructs a new ContactMaintainer
     * @param activity The activity for which the ContactMaintainer is created for.
     * @param toBeFilled The view group to be filled with contact views.
     * @param event_uid The id of the event.
     */
    public ContactMaintainer(Activity activity, ViewGroup toBeFilled, int event_uid) {
        this.activity = activity;

        contactFiller = new ContactFillerRunnable(activity, toBeFilled, event_uid, visibleContacts);
        contactFillerThread = new Thread(contactFiller);

        contactCleaner = new ContactCleanerRunnable(activity, toBeFilled, visibleContacts);
        contactCleaner.setChainedThread(contactFillerThread);
        contactCleanerThread = new Thread(contactCleaner);
    }

    /**
     * Restarts the contact maintainer.
     * @param filter The filter to be used for filtering contacts.
     */
    public void restart(String filter) {
        stop();

        contactFillerThread = new Thread(contactFiller);
        contactCleanerThread = new Thread(contactCleaner);
        contactCleaner.setChainedThread(contactFillerThread);

        start(filter);
    }

    /**
     * Starts the contact maintainer.
     * @param filter The filter to be used for filtering contacts.
     */
    public void start(String filter) {
        contactFiller.setFilter(filter);
        contactCleaner.setFilter(filter);

        contactFiller.start();
        contactCleaner.start();
        contactCleanerThread.start();

        isWorking = true;
    }

    /**
     * Stops the contact maintainer.
     */
    public void stop() {
        if (!isWorking)
            return;

        contactCleaner.stop();
        contactFiller.stop();

        isWorking = false;
    }

    /**
     * Returns whether or not the contact maintainer is working.
     * @return True or false.
     */
    public boolean isWorking() {
        return isWorking;
    }
}
