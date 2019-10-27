package com.example.a9gesllprov.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.a9gesllprov.R;
import com.example.a9gesllprov.core.ResolutionUtil;

/**
 * A view that displays information for how to get started
 * with the application.
 */
public class GetStartedView extends View {

    private Rect background;
    private Paint backgroundPaint;

    private Paint textPaint;
    private String textOne;
    private String textTwo;

    private Bitmap icon;

    /**
     * Constructs a new GetStartedView instance.
     * @param context The context for which the view should be created in.
     */
    public GetStartedView(Context context) {
        super(context);
        prepareListener();
    }

    /**
     * Constructs a new GetStartedView.
     * @param context The context for which the view should be created in.
     * @param attrs The view's attribute set.
     */
    public GetStartedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        prepareListener();
    }

    /**
     * Prepares a listener that will be responsible for creating drawables as soon as the view's size is set.
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
     * Prepares the drawable elements.
     */
    private void prepareDrawables() {
        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize((float) (getWidth() * 0.07));

        textOne = getContext().getString(R.string.view_events_get_started_part_one);
        textTwo = getContext().getString(R.string.view_events_get_started_part_two);

        icon = BitmapFactory.decodeResource(getResources(), R.drawable.pick_world_location);
    }

    /**
     * Called when the view is drawn.
     * Drawns the get started view on the canvas.
     * @param canvas The canvas for the get started view to be drawn on.
     */
    @Override
    public void onDraw(Canvas canvas) {
        int marginTop = (int) ResolutionUtil.convertDpToPixels(getContext(), 16);

        int bitmapX = getWidth()/2 - icon.getWidth()/2;
        int bitmapY = (int) (getHeight() * 0.1) + marginTop;
        canvas.drawBitmap(icon, bitmapX, bitmapY, null);

        int textX = getWidth() / 2;
        int textY = bitmapY + icon.getHeight() + (int) (getHeight() * 0.15) + marginTop;
        canvas.drawText(textOne, textX, textY, textPaint);

        textY += getHeight() * 0.08;
        canvas.drawText(textTwo, textX, textY, textPaint);
    }
}
