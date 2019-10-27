package com.example.a9gesllprov;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a9gesllprov.component.GetStartedView;
import com.example.a9gesllprov.core.CardFillerRunnable;
import com.example.a9gesllprov.core.DailyEventsNotification;
import com.example.a9gesllprov.core.OpenCounter;
import com.example.a9gesllprov.core.ResolutionUtil;
import com.example.a9gesllprov.database.DatabaseManager;
import com.example.a9gesllprov.database.Event;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * The activity responsible for the event page.
 */
public class ViewEventsActivity extends AppCompatActivity {

    private OpenCounter openCounter;

    /**
     * Called when the ViewEventsActivity is created.
     * @param savedInstanceState The saved instance bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        DatabaseManager.initialize(getApplicationContext());
        openCounter = new OpenCounter(this);

        prepareToolbar();
        prepareFAB();
        showDailyEventsNotification();
    }

    /**
     * Shows the daily events notification.
     */
    private void showDailyEventsNotification() {
        if (DailyEventsNotification.hasShown())
            return;

        DailyEventsNotification notification = new DailyEventsNotification(this);
        notification.show();
    }

    /**
     * Prepares the toolbar of the activity.
     */
    private void prepareToolbar() {
        CollapsingToolbarLayout collapsing = findViewById(R.id.collapsing);
        collapsing.setTitle(getString(R.string.view_events_title) + "#" + openCounter.getOpenCount());
        collapsing.setExpandedTitleColor(Color.WHITE);
        collapsing.setCollapsedTitleTextColor(Color.WHITE);
    }

    /**
     * Prepares the activity's floating action button.
     */
    private void prepareFAB() {
        FloatingActionButton buttonNewEvent = findViewById(R.id.viewEventsButtonFloatingNewEvent);
        final Context context = this;
        buttonNewEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CreateEventActivity.class));
            }
        });
    }

    /**
     * Prepares the event list by creating clickable cards for each event.
     * Showing important information for the user.
     */
    private void prepareEventList() {
        LinearLayout nestedEventLayout = findViewById(R.id.viewContactsLayout);
        List<Event> events = DatabaseManager.getInstance().getDatabase().eventDao().getAllOrderClosestStart();

        CardFillerRunnable cardFiller = new CardFillerRunnable(
                this,
                events,
                nestedEventLayout
        );

        new Thread(cardFiller).start();
    }

    /**
     * Cleans the full event list.
     */
    private void cleanEventList() {
        LinearLayout nestedEventLayout = findViewById(R.id.viewContactsLayout);
        nestedEventLayout.removeAllViews();
    }

    /**
     * Called after all preparations has been made for the view to resume.
     */
    private void onFinishedPreparing() {
        LinearLayout nestedEventLayout = findViewById(R.id.viewContactsLayout);
        int eventCount = DatabaseManager.getInstance().getDatabase().eventDao().getAll().size();
        if (nestedEventLayout.getChildCount() > 0 || eventCount > 0)
            return;

        GetStartedView getStartedView = new GetStartedView(this);
        getStartedView.setLayoutParams(new LinearLayout.LayoutParams(
           LinearLayout.LayoutParams.MATCH_PARENT,
           LinearLayout.LayoutParams.MATCH_PARENT
        ));

        getStartedView.getLayoutParams().height = (int) ResolutionUtil.convertDpToPixels(this, 400);

        nestedEventLayout.addView(getStartedView);
    }

    /**
     * Called when the application resumes prior to being placed in the foreground.
     */
    @Override
    public void onResume() {
        cleanEventList();
        prepareEventList();
        onFinishedPreparing();
        super.onResume();
    }
}
