package com.example.a9gesllprov;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.a9gesllprov.database.DatabaseManager;
import com.example.a9gesllprov.database.seeder.EntitySeeder;
import com.example.a9gesllprov.database.seeder.EventPhotoSeeder;
import com.example.a9gesllprov.database.seeder.EventSeeder;

import java.util.ArrayList;
import java.util.List;

/**
 * The developer activity.
 * Provides functionality such as seeding, database wipes etc for the developer.
 */
public class DeveloperActivity extends AppCompatActivity {

    private List<EntitySeeder> seeders = new ArrayList<>();
    private static int PERMISSION_EXTERNAL_STORAGE = 1;

    /**
     * Called when DeveloperActivity is created.
     * @param savedInstanceState The saved instance bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        DatabaseManager.initialize(getApplicationContext());
        prepareSeeders();
    }

    /**
     * Prepare database seeders.
     */
    private void prepareSeeders() {
        seeders.add(new EventSeeder());
        seeders.add(new EventPhotoSeeder(this));
    }

    /**
     * Changes to the application activity.
     * @param view
     */
    public void onPressMoveToApplication(View view) {
        Intent intent = new Intent(this, ViewEventsActivity.class);
        startActivity(intent);
    }

    /**
     * Callback for when the seed database button is called.
     * Attempts to seed the database if permissions are provided.
     * @param view The view responsible for triggering the event.
     */
    public void onPressSeedDatabase(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE }, PERMISSION_EXTERNAL_STORAGE);
            return;
        }

        for (EntitySeeder seeder : seeders)
            seeder.seed();
    }

    /**
     * Callback for when the clean database button is pressed.
     * Cleans the database of all entries.
     * @param view The view responsible for triggering the event.
     */
    public void onPressCleanDatabase(View view) {
        DatabaseManager.getInstance().getDatabase().eventDao().deleteAll();
        DatabaseManager.getInstance().getDatabase().eventPhotoDao().deleteAll();
    }
}
