package com.example.a9gesllprov.core;

import android.app.Activity;
import android.view.ViewGroup;

import com.example.a9gesllprov.R;
import com.example.a9gesllprov.database.Event;

import java.util.List;

/**
 * Fills a ViewGroup with event cards.
 */
public class CardFillerRunnable implements Runnable  {

    private Activity activity;
    private List<Event> events;
    private ViewGroup toBeFilled;
    private int sleepTime;

    /**
     * Constructs a new CardFillerRunnable instance.
     * @param activity The activity owning this runnable.
     * @param events The events to be filled.
     * @param toBeFilled The ViewGroup to which the created cards will be added to.
     */
    public CardFillerRunnable(Activity activity, List<Event> events, ViewGroup toBeFilled) {
        this.activity = activity;
        this.events = events;
        this.toBeFilled = toBeFilled;

        if (events.size() > 0)
            sleepTime = activity.getResources().getInteger(R.integer.view_events_fill_events_time_ms) / events.size();
    }

    /**
     * Fills the view group with event cards over time.
     * This reduces the card creation load on the device when many events exists.
     */
    @Override
    public void run() {
        for (int i = 0; i < events.size(); i++) {
            final int finalI = i;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EventCardBuilder builder = new EventCardBuilder(activity, events.get(finalI));
                    toBeFilled.addView(builder.build());
                }
            });

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
