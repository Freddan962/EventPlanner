package com.example.a9gesllprov.core;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.example.a9gesllprov.R;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Cleans contact views from a ViewGroup over time.
 */
public class ContactCleanerRunnable implements Runnable {

    private Activity activity;
    private String filter;
    private HashMap<Integer, View> visibleContacts;
    private ViewGroup toBeCleaned;
    private Thread chainedThread;
    private boolean isAlive;
    private AtomicInteger inRemovalQueue = new AtomicInteger(0);

    /**
     * Constructs a new ContactCleaner instance.
     * @param activity The activity for which the contact cleaner is created for.
     * @param toBeCleaned The ViewGroup to be cleaned.
     * @param visibleContacts A HashMap containg contact id to view mapping.
     */
    public ContactCleanerRunnable(Activity activity, ViewGroup toBeCleaned, HashMap<Integer, View> visibleContacts) {
        this.activity = activity;
        this.visibleContacts = visibleContacts;
        this.toBeCleaned = toBeCleaned;
    }

    /**
     * Removes all contact views not matching the current active filter over time.
     */
    @Override
    public void run() {
        clean();

        while (inRemovalQueue.get() > 0) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }

        if (isAlive)
            onCompletion();
    }

    /**
     * Stops the contact cleaner.
     */
    public void stop() {
        isAlive = false;
    }

    /**
     * Starts the contact cleaner.
     */
    public void start() {
        isAlive = true;
    }

    /**
     * Checks whether or not the contact cleaner is running.
     * @return True or false.
     */
    public boolean isRunning() {
        return isAlive;
    }

    /**
     * Callback for when the runnable has completed.
     * Starts a chained thread if such one exists.
     */
    public void onCompletion() {
        if (chainedThread != null && !chainedThread.isAlive())
            chainedThread.start();
    }

    /**
     * Sets the chained thread to be called on completion.
     * @param chainedThread The chained thread to be completed.
     */
    public void setChainedThread(Thread chainedThread) {
        this.chainedThread = chainedThread;
    }

    /**
     * Sets the filter of the contact cleaner.
     * @param filter The filter to be set.
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * Cleans the ViewGroup from views which should be filtered.
     */
    private void clean() {
        HashMap<Integer, View> visibleContactsClone = (HashMap<Integer, View>) visibleContacts.clone();

        for (int aux_id : visibleContactsClone.keySet()) {
            if (!isRunning())
                break;

            final int id = aux_id;
            String name = (String) visibleContacts.get(aux_id).getTag(R.integer.tag_contact_name);
            name = name.toLowerCase();

            if (ContactFilter.shouldBeFiltered(name, filter)) {
                final View view = visibleContacts.get(id);
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        toBeCleaned.removeView(view);
                        inRemovalQueue.getAndDecrement();
                        visibleContacts.remove(id);
                    }
                });

                inRemovalQueue.getAndIncrement();
            }
        }
    }
}
