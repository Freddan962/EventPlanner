package com.example.a9gesllprov;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.fresco.FrescoImageLoader;
import com.github.piasy.biv.view.BigImageView;

/**
 * ViewPhotoActivity provides functionality to view a photo.
 */
public class ViewPhotoActivity extends AppCompatActivity {

    public static String EXTRA_PHOTO = "EXTRA_PHOTO";
    BigImageView bigImageView;

    /**
     * Called when ViewPhotoActivity is created.
     * @param savedInstanceState The saved instance bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BigImageViewer.initialize(FrescoImageLoader.with(getApplicationContext()));
        setContentView(R.layout.activity_view_photo);

        showImage();
    }

    /**
     * Shows the image for the user.
     */
    private void showImage() {
        bigImageView = findViewById(R.id.mBigImage);
        bigImageView.setInitScaleType(BigImageView.INIT_SCALE_TYPE_CENTER);
        Uri image = getIntent().getParcelableExtra(EXTRA_PHOTO);
        bigImageView.showImage(image);
    }
}
