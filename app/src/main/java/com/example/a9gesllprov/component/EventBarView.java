package com.example.a9gesllprov.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.a9gesllprov.R;
import com.example.a9gesllprov.core.ResolutionUtil;

/**
 * Represents a event bar view.
 */
public class EventBarView extends View {

    private Context context;

    private Paint titlePaint;
    private Paint backgroundPaint;
    private Rect background;

    private String eventTitle = "";

    /**
     * Constructs a new EventBarView instance.
     * @param context The context for which the view should be created in.
     */
    public EventBarView(Context context) {
        super(context);
        this.context = context;
        prepareListener();
    }

    /**
     * Constructs a new EventBarView.
     * @param context The context for which the view should be created in.
     * @param attrs The view's attribute set.
     */
    public EventBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        prepareListener();
    }

    /**
     * Prepares laoyout change listener.
     */
    private void prepareListener() {
        this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                prepareDrawables();
            }
        });
    }

    /**
     * Prepares drawables.
     */
    private void prepareDrawables() {
        titlePaint = new Paint();
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(ResolutionUtil.convertDpToPixels(context, 24));
        titlePaint.setColor(Color.WHITE);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(ContextCompat.getColor(context, R.color.theme_purple));

        background = new Rect();
        background.left = 0;
        background.top = 0;
        background.bottom = background.top + getHeight();
        background.right = background.left + getWidth();
    }

    /**
     * Called when the view is drawn.
     * Draws the event bar on the canvas.
     * @param canvas The canvas for the event bar to be drawn on.
     */
    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(background, backgroundPaint);

        int titleX = (int) ResolutionUtil.convertDpToPixels(context, 16);
        int titleY = (int) (getHeight()/2 - ((titlePaint.descent() + titlePaint.ascent()) / 2));
        canvas.drawText(eventTitle, titleX, titleY, titlePaint);
    }

    /**
     * Sets the bars event title.
     * @param title The title to be set.
     */
    public void setEventTitle(String title) {
        eventTitle = title;
    }
}
