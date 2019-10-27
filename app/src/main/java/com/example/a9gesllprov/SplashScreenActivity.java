package com.example.a9gesllprov;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Represents a splash screen with fade in and fade out logo animation.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private int splashDuration;
    private int logoDuration;
    private Handler handler = new Handler();

    private ConstraintLayout splashLayout;

    /**
     * Constructs a new SplashScreenActivity instance.
     * @param savedInstanceState The saved instance bundle or null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Prepare timer values
        splashDuration = getResources().getInteger(R.integer.splash_screen_logo_duration);
        logoDuration = splashDuration - 500;

        // Set logo text
        TextView titleText = findViewById(R.id.splashScreenTextTitle);
        titleText.setText(getString(R.string.app_name));

        // Initiate fade in animation
        splashLayout = findViewById(R.id.splashLayout);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        splashLayout.startAnimation(fadeIn);

        // Initiate timed operations
        handler.postDelayed(new Runnable() {
            @Override
            public void run() { fadeOutLogo(); }
        }, logoDuration);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() { changeActivity(); }
        }, splashDuration);
    }

    /**
     * Changes activity to the developer activity.
     */
    private void changeActivity() {
        Intent intent = new Intent(this, DeveloperActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Fades out the application logo.
     */
    private void fadeOutLogo() {
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        splashLayout.startAnimation(fadeOut);
    }
}
