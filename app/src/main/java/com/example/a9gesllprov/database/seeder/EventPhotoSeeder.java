package com.example.a9gesllprov.database.seeder;

import android.net.Uri;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a9gesllprov.R;
import com.example.a9gesllprov.database.AppDatabase;
import com.example.a9gesllprov.database.DatabaseManager;
import com.example.a9gesllprov.database.Event;
import com.example.a9gesllprov.database.EventPhoto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Responsible for seeding photos for the applications events.
 * Mainly used during development.
 */
public class EventPhotoSeeder implements EntitySeeder {

    private AppCompatActivity activity;
    private AppDatabase database;
    private int photosPerEvent;

    /**
     * Constructs a new EventPhotoSeeder instance.
     * @param activity The activity that this instance is created within.
     */
    public EventPhotoSeeder(AppCompatActivity activity) {
        this.activity = activity;
        photosPerEvent = activity.getResources().getInteger(R.integer.seeder_event_photo_photos_per_event);
        database = DatabaseManager.getInstance().getDatabase();
    }

    /**
     * Seeds the database's events with photos.
     */
    @Override
    public void seed() {
        File file = fetchPlaceholderFile();
        List<Event> events = database.eventDao().getAll();

        for (Event event : events) {
            for (int i = 0; i < photosPerEvent; i++) {
                EventPhoto photo = new EventPhoto();
                photo.eventId = event.uid;
                photo.uri = Uri.parse(file.toString());
                database.eventPhotoDao().insert(photo);
            }
        }
    }

    /**
     * Fetches a generic photo file, if not found on storage it copies a drawable.
     * @return The generic photo File instance.
     */
    private File fetchPlaceholderFile() {
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + "dashboard_photo.png");

            if (!file.exists())
            {
                InputStream inputStream = activity.getResources().openRawResource(R.drawable.dashboard_photo);
                OutputStream outStream = new FileOutputStream(file);

                byte buffer[] = new byte[1024];
                int length;

                while ((length = inputStream.read(buffer)) > 0)
                    outStream.write(buffer, 0, length);

                outStream.close();
                inputStream.close();
            }
        } catch (IOException e) { e.printStackTrace(); }

        return file;
    }
}
