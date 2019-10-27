package com.example.a9gesllprov.core;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.a9gesllprov.EventActivity;
import com.example.a9gesllprov.R;
import com.example.a9gesllprov.database.Event;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

/**
 * Responsible of construction for cards representing an event.
 */
public class EventCardBuilder {

    private Activity activity;
    private Event event;

    public static final String EXTRA_EVENT_UID = "EVENT_UID";

    /**
     * Constructs a new EventCardBuilder instance.
     * @param activity The activity for the card to be constructed in.
     * @param event The event the card is created for.
     */
    public EventCardBuilder(Activity activity, Event event) {
        this.activity = activity;
        this.event = event;
    }

    /**
     * Constructs the card for the event.
     * @return The constructed card.
     */
    public MaterialCardView build() {
        MaterialCardView card = createCardBody();
        return fillCard(card);
    }

    /**
     * Creates the title text.
     * @return The created title text.
     */
    private TextView createTitle() {
        TextView title = new TextView(activity);
        title.setTextSize(32);
        title.setText(event.name);
        title.setId(View.generateViewId());
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        return title;
    }

    /**
     * Creates the occurs at text.
     * @return The created occurs at TextView.
     */
    private TextView createOccursAt() {
        TextView occurs = new TextView(activity);
        occurs.setText(event.getStartDateFormatted() + " - " + event.getEndDateFormatted());
        occurs.setId(View.generateViewId());
        return occurs;
    }

    /**
     * Creates the internal card layout.
     * @return The created ConstraintLayout.
     */
    private ConstraintLayout createCardLayout() {
        ConstraintLayout cardLayout = new ConstraintLayout(activity);
        ConstraintLayout.LayoutParams innerCardLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );

        int cardSpacing = (int) activity.getResources().getDimension(R.dimen.mtrl_card_spacing);
        innerCardLayoutParams.setMargins(cardSpacing, cardSpacing, cardSpacing, 0);
        cardLayout.setLayoutParams(innerCardLayoutParams);

        cardLayout.setTag(R.integer.tag_event_uid, event.uid);
        cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), EventActivity.class);
                intent.putExtra(EXTRA_EVENT_UID, (int) view.getTag(R.integer.tag_event_uid));
                activity.startActivity(intent);
            }
        });

        return cardLayout;
    }

    /**
     * Creates the view button.
     * @return The created button.
     */
    private MaterialButton createViewButton() {
        MaterialButton viewButton = (MaterialButton) activity.getLayoutInflater().inflate(R.layout.component_view_events_open_button, null);
        viewButton.setText(activity.getResources().getString(R.string.view_events_button_open));
        viewButton.setId(View.generateViewId());

        // Prepare open click listener
        viewButton.setTag(R.integer.tag_event_uid, event.uid);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), EventActivity.class);
                intent.putExtra(EXTRA_EVENT_UID, (int) view.getTag(R.integer.tag_event_uid));
                activity.startActivity(intent);
            }
        });

        return viewButton;
    }

    /**
     * Fills a MaterialCardView with content based on the builders event.
     * @param card The card to be filled with content.
     * @return The filled card.
     */
    private MaterialCardView fillCard(MaterialCardView card) {
        ConstraintLayout cardLayout = createCardLayout();
        MaterialButton viewButton = createViewButton();
        TextView title = createTitle();
        TextView occurs = createOccursAt();

        cardLayout.addView(title);
        cardLayout.addView(occurs);
        cardLayout.addView(viewButton);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cardLayout);

        constraintSet.connect(occurs.getId(), ConstraintSet.TOP,
                title.getId(), ConstraintSet.BOTTOM, 0);

        constraintSet.connect(viewButton.getId(), ConstraintSet.TOP,
                occurs.getId(), ConstraintSet.BOTTOM, activity.getResources().getInteger(R.integer.view_events_view_button_margin_top));

        constraintSet.applyTo(cardLayout);
        card.addView(cardLayout);
        return card;
    }

    /**
     * Creates a MaterialCardView to be used as the body for event representation.
     * @return The created MaterialCardView.
     */
    private MaterialCardView createCardBody() {
        MaterialCardView card = new MaterialCardView(activity);

        LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        int cardSpacing = (int) activity.getResources().getDimension(R.dimen.mtrl_card_spacing);
        cardLayoutParams.setMargins(cardSpacing, cardSpacing, cardSpacing, 0);
        card.setLayoutParams(cardLayoutParams);
        card.setMinimumHeight(400);

        return card;
    }
}
