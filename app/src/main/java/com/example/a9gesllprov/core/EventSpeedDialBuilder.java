package com.example.a9gesllprov.core;

import android.app.Activity;

import com.example.a9gesllprov.R;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

/**
 * Responsible for event speed dial construction.
 */
public class EventSpeedDialBuilder {

    private Activity activity;

    /**
     * Creates a new speed dial builder.
     * @param activity The activity in which the dial will be built.
     */
    public EventSpeedDialBuilder(Activity activity) {
        this.activity = activity;
    }

    /**
     * Builds a new speed dial view instance.
     * @return The built speed dial view instance.
     */
    public SpeedDialView build() {
        final SpeedDialView speedDial = activity.findViewById(R.id.speedDial);
        if (speedDial == null)
            return null;

        speedDial.addActionItem(
                new SpeedDialActionItem.Builder(R.id.event_fab_action_delete_event, R.drawable.ic_remove_circle_outline_white_24px)
                        .setLabel(R.string.event_fab_delete_event_label)
                        .create()
        );

        speedDial.addActionItem(
                new SpeedDialActionItem.Builder(R.id.event_fab_action_edit_event, R.drawable.ic_edit_24px)
                        .setLabel(R.string.event_fab_edit_event_label)
                        .create()
        );

        speedDial.addActionItem(
                new SpeedDialActionItem.Builder(R.id.event_fab_action_calendary_entry, R.drawable.ic_calendar_today_24px)
                        .setLabel(R.string.event_fab_create_calendar_entry_label)
                        .create()
        );

        speedDial.addActionItem(
                new SpeedDialActionItem.Builder(R.id.event_fab_action_share_sms, R.drawable.ic_sms_24px)
                        .setLabel(R.string.event_fab_share_event_sms_label)
                        .create()
        );

        return speedDial;
    }
}
