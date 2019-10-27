package com.example.a9gesllprov;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.a9gesllprov.component.EventBarView;
import com.example.a9gesllprov.core.ActionConfirmationCallback;
import com.example.a9gesllprov.core.ActionConfirmationFragment;
import com.example.a9gesllprov.core.CalendarUtil;
import com.example.a9gesllprov.core.CallHelper;
import com.example.a9gesllprov.core.EventCardBuilder;
import com.example.a9gesllprov.core.EventSharer;
import com.example.a9gesllprov.core.EventSpeedDialBuilder;
import com.example.a9gesllprov.core.FileUtil;
import com.example.a9gesllprov.core.MailHelper;
import com.example.a9gesllprov.core.ResolutionUtil;
import com.example.a9gesllprov.core.TextToSpeechHelper;
import com.example.a9gesllprov.core.ThumbnailGenerator;
import com.example.a9gesllprov.core.WebsiteHelper;
import com.example.a9gesllprov.core.YoutubeUtil;
import com.example.a9gesllprov.database.AppDatabase;
import com.example.a9gesllprov.database.DatabaseManager;
import com.example.a9gesllprov.database.Event;
import com.example.a9gesllprov.database.EventPhoto;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.File;

/**
 * The activity responsible for the event page.
 */
public class EventActivity extends AppCompatActivity implements ActionConfirmationCallback {

    private Event event;
    private AppDatabase database;

    private boolean isEditingPhotos = false;
    private Uri lastPhotoUri;
    private Toast toast = null;
    private SpeedDialView speedDial;
    private int thumbnailSize;
    private boolean isInitialized = false;
    private View lastPressedPhoto;

    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQUEST_PERMISSION_SHARE_SMS = 2;
    private final int REQUEST_PERMISSION_CALENDAR = 3;

    private final int REQUEST_CONFIRMATION_DELETE_EVENT = 1;
    private final int REQUEST_CONFIRMATION_DELETE_PHOTO = 2;
    private final int REQUEST_CONFIRMATION_DELETE_EVENT_CALENDAR_ENTRY = 3;
    private final int REQUEST_CONFIRMATION_ADD_EVENT_CALENDAR_ENTRY = 4;

    /**
     * Called when EventActivity is created.
     * @param savedInstanceState The saved instance bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        database = DatabaseManager.getInstance().getDatabase();
        thumbnailSize = getResources().getInteger(R.integer.event_photo_thumbnail_size);
    }

    /**
     * Prepares the activity's toolbar.
     */
    private void prepareToolbar() {
       EventBarView eventBar = findViewById(R.id.eventBar);
       eventBar.setEventTitle(event.name);
    }

    /**
     * Prepares the information for the event.
     */
    private void prepareInformation() {
        TextView textDate = findViewById(R.id.textDate);
        String date = event.getStartDateFormatted() + " - " + event.getEndDateFormatted();
        textDate.setText(date);

        TextView textNumber = findViewById(R.id.textNumber);
        textNumber.setText(event.contactNumber);

        TextView textWebsite = findViewById(R.id.textWebsite);
        textWebsite.setText(event.homePage);

        TextView textMail = findViewById(R.id.textMail);
        textMail.setText(event.mail);

        TextView textDescription = findViewById(R.id.textDescription);
        textDescription.setText(event.description);
    }

    /**
     * Prepares event's photos.
     */
    private void preparePhotos() {
        for (EventPhoto photo : database.eventPhotoDao().getAll(event.uid))
            addEventPhoto(photo);
    }

    /**
     * Prepares the Youtube video if it exists.
     */
    private void prepareYoutube() {
        if (event.youtubeUrl != null && !event.youtubeUrl.isEmpty())
            prepareYoutubeVideo();
    }

    /**
     * Prepares the layout for a event that has a Youtube video.
     */
    private void prepareYoutubeVideo() {
        LinearLayout main = findViewById(R.id.eventMainLayout);

        YouTubePlayerView playerView = findViewById(R.id.youtube_player_view);
        if (playerView != null) {
            main.removeView(findViewById(R.id.textVideoTitle));
            main.removeView(playerView);
        }

        TextView videoTitle = (TextView) getLayoutInflater().inflate(R.layout.component_videotitle, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int marginTitle = (int) ResolutionUtil.convertDpToPixels(this, 16);
        params.setMargins(marginTitle, 0,0, marginTitle);
        videoTitle.setLayoutParams(params);

        YouTubePlayerView view = (YouTubePlayerView) getLayoutInflater().inflate(R.layout.component_youtubeview, null);

        view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                String videoId = YoutubeUtil.getYoutubeVideoIdFromUrl(event.youtubeUrl);
                youTubePlayer.loadVideo(videoId, 0);
                youTubePlayer.pause();
            }
        });

        main.addView(videoTitle);
        main.addView(view);
    }

    /**
     * Prepares the floating action buttons in this activity.
     */
    private void prepareFABs() {
        EventSpeedDialBuilder builder = new EventSpeedDialBuilder(this);
        speedDial = builder.build();

        speedDial.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                speedDial.close();
                switch (actionItem.getId()) {
                    case R.id.event_fab_action_share_sms:
                        onPressShareWithSMS();
                        return true;

                    case R.id.event_fab_action_delete_event:
                        onPressDelete();
                        return true;

                    case R.id.event_fab_action_edit_event:
                        onPressEdit();
                        return true;

                    case R.id.event_fab_action_calendary_entry:
                        onPressCalendarEntry();
                        return true;
                }

                return false;
            }
        });
    }

    /**
     * Adds a event photo to the layout.
     * @param photo The event photo to be added.
     */
    private void addEventPhoto(EventPhoto photo) {
        LinearLayout layoutPhotos = findViewById(R.id.layoutPhotos);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 0, 15, 0);

        Bitmap thumbnailPhoto = ThumbnailGenerator.generate(this, photo.uri, thumbnailSize, thumbnailSize);

        ImageView image = new ImageView(this);
        image.setImageBitmap(thumbnailPhoto);
        image.setLayoutParams(params);
        image.setTag(R.integer.tag_event_photo, photo.uid);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPhotoThumbnail(view);
            }
        });

        layoutPhotos.addView(image);
    }

    /**
     * Callback for when a photo thumbnail has been pressed.
     * Shows the pressed thumbnails original photo for the user.
     * @param view The view responsible for triggering the event.
     */
    private void onClickPhotoThumbnail(View view) {
        int photoId = (int) view.getTag(R.integer.tag_event_photo);
        EventPhoto photo = DatabaseManager.getInstance().getDatabase().eventPhotoDao().findById(photoId);

        if (!isEditingPhotos) {
            Intent intent = new Intent(this, ViewPhotoActivity.class);
            intent.putExtra(ViewPhotoActivity.EXTRA_PHOTO, photo.uri);
            startActivity(intent);
            return;
        }

        new ActionConfirmationFragment(this, this)
            .setTitle(getString(R.string.event_confirmation_delete_photo))
            .setMessage(getString(R.string.action_confirmation_cannot_be_undone))
            .setRequestID(REQUEST_CONFIRMATION_DELETE_PHOTO)
            .show(getSupportFragmentManager(), null);

        lastPressedPhoto = view;
    }

    /**
     * Deletes the last pressed photo marked for deletion.
     */
    private void deletePressedPhoto() {
        if (lastPressedPhoto == null)
            return;

        int photoId = (int) lastPressedPhoto.getTag(R.integer.tag_event_photo);

        LinearLayout layoutPhotos = findViewById(R.id.layoutPhotos);
        layoutPhotos.removeView(lastPressedPhoto);

        EventPhoto photo = DatabaseManager.getInstance().getDatabase().eventPhotoDao().findById(photoId);
        DatabaseManager.getInstance().getDatabase().eventPhotoDao().deleteById(photo.uid);
        showToast(getString(R.string.event_photo_removed), Toast.LENGTH_SHORT);
        disablePhotoEditing();
    }

    /**
     * Enables photo editing.
     * This makes it possible to remove photos by pressing them.
     */
    private void enablePhotoEditing() {
        isEditingPhotos = true;
        showToast(getString(R.string.event_photo_press_to_remove), Toast.LENGTH_LONG);
    }

    /**
     * Disables photo editing.
     * This makes it no longer possible to remove photos by pressing them.
     */
    private void disablePhotoEditing() {
        isEditingPhotos = false;
    }

    /**
     * Shows a toast that overwrites any existing local activity toasts.
     * @param text The text to be shown.
     * @param length The length of the toast.
     */
    private void showToast(String text, int length) {
        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(this, text, length);
        toast.show();
    }

    /**
     * Callback for when the delete FAB has been pressed.
     */
    public void onPressDelete() {
        new ActionConfirmationFragment(this, this)
            .setTitle(getString(R.string.event_confirmation_delete_event))
            .setMessage(getString(R.string.action_confirmation_cannot_be_undone))
            .setRequestID(REQUEST_CONFIRMATION_DELETE_EVENT)
            .show(getSupportFragmentManager(), null);
    }

    /**
     * Deletes the current event.
     */
    public void deleteEvent() {
        database.eventDao().delete(event.uid);
        finish();
    }

    /**
     * Callback for when the edit FAB has been pressed.
     */
    public void onPressEdit() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        intent.putExtra(CreateEventActivity.EXTRA_EDIT_EVENT_UID, event.uid);
        startActivity(intent);
    }

    /**
     * Callback for when the website has been pressed.
     * Opens a website if the device is connected to the internet.
     * @param view The view responsible for triggering the event.
     */
    public void onPressWebsite(View view) {
        if (!WebsiteHelper.openLocalWithConnection(this, event.homePage))
            showToast(getString(R.string.event_website_failed_to_open_internet), Toast.LENGTH_LONG);
    }

    /**
     * Callback for when the press number button is pressed.
     * Calls the events contact number.
     * @param view The view responsible for triggering the event.
     */
    public void onPressNumber(View view) {
        CallHelper.callNumber(this, event.contactNumber);
    }

    /**
     * Callback for when the edit photos button has been pressed.
     * Enables or disables photo editing.
     * @param view The view responsible for triggering the event.
     */
    public void onPressEditPhotos(View view) {
        if (isEditingPhotos)
            disablePhotoEditing();
        else
            enablePhotoEditing();
    }

    /**
     * Callback for when the add photo button is pressed.
     * Starts image capturing for the event.
     * @param view The view responsible for triggering the event.
     */
    public void onPressAddPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = FileUtil.createUniqueJPGFile(this);

            lastPhotoUri = FileProvider.getUriForFile(
                    this,
                    "com.example.a31taochvisakort.fileprovider",
                    imageFile
            );

            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, lastPhotoUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Callback for when the mail button is pressed.
     * Starts a mail activity for the event's contact email.
     * @param view The view responsible for triggering the event.
     */
    public void onPressMail(View view) {
        MailHelper.mailRecipient(this, event.mail);
    }

    /**
     * Callback for when the read description button is pressed.
     * Reads the description of the event using TTS.
     * @param view The view responsible for triggering the event.
     */
    public void onPressReadDescription(View view) {
        TextToSpeechHelper.speak(this, event.description);
    }

    /**
     * Callback for when the share with SMS fab is pressed.
     * Shares the event information through a SMS.
     */
    public void onPressShareWithSMS() {
        if (!requireSMSPermissions()) {
            return;
        }

        shareSMS();
    }

    /**
     * Callback for when the calendar FAB item is presssed.
     */
    private void onPressCalendarEntry() {
        if (event.hasCalendarEntry())
            onPressRemoveFromCalendar();
        else
            onPressAddToCalendar();
    }

    /**
     * Callback for when the add to calendar FAB is pressed.
     * Create a new calendar entry for the activity.
     */
    public void onPressAddToCalendar() {
        new ActionConfirmationFragment(this, this)
            .setTitle(getString(R.string.event_confirmation_add_event_calendar_entry))
            .setMessage(getString(R.string.action_confirmation_can_be_undone))
            .setRequestID(REQUEST_CONFIRMATION_ADD_EVENT_CALENDAR_ENTRY)
            .show(getSupportFragmentManager(), null);
    }

    /**
     * Called when remove from calendar FAB is pressed.
     */
    public void onPressRemoveFromCalendar() {
        new ActionConfirmationFragment(this, this)
            .setTitle(getString(R.string.event_confirmation_delete_event_calendar_entry))
            .setMessage(getString(R.string.action_confirmation_cannot_be_undone))
            .setRequestID(REQUEST_CONFIRMATION_DELETE_EVENT_CALENDAR_ENTRY)
            .show(getSupportFragmentManager(), null);
    }

    /**
     * Removes the calendar entry for the event.
     */
    private void removeFromCalendar() {
        CalendarUtil.deleteEntry(this, event);
        database.eventDao().update(event);
        updateSpeedDialActionItems();
        showToast(getString(R.string.event_calendar_remove_event_success), Toast.LENGTH_LONG);
    }

    /**
     * Requires permissions needed to complete add to calendar operation.
     * @return True if permissions are granted false if not.
     */
    private boolean requireCalendarPermissions() {
        boolean hasWriteCalendarPerms = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;
        if (hasWriteCalendarPerms)
            return true;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, REQUEST_PERMISSION_CALENDAR);
        return false;
    }

    /**
     * Callback for when calendar permission request has been denied.
     */
    private void onRequireCalendarPermissionsDenied() {
        showToast(getString(R.string.event_require_calendar_permission), Toast.LENGTH_LONG);
    }

    /**
     * Adds the event to the local device's calendar.
     */
    private void addToCalendar() {
        if (!requireCalendarPermissions())
            return;

        if (CalendarUtil.createEntry(this, event))
            onAddToCalendarSuccess();
        else
            onRequireCalendarPermissionsDenied();
    }

    /**
     * Called when event was successfully added to the calendar.
     */
    private void onAddToCalendarSuccess() {
        database.eventDao().update(event);
        updateSpeedDialActionItems();
        showToast(getString(R.string.event_calendar_add_event_sucess), Toast.LENGTH_LONG);
    }

    /**
     * Updates the speed dial to represent event state.
     */
    private void updateSpeedDialActionItems() {
        SpeedDialActionItem oldItem = findActionItem(R.id.event_fab_action_calendary_entry);
        int labelId = event.hasCalendarEntry() ? R.string.event_fab_remove_calendar_entry_label : R.string.event_fab_create_calendar_entry_label;
        SpeedDialActionItem.Builder builder = new SpeedDialActionItem.Builder(R.id.event_fab_action_calendary_entry, R.drawable.ic_calendar_today_24px)
                         .setLabel(getString(labelId));

        speedDial.replaceActionItem(oldItem, builder.create());
    }

    /**
     * Finds a speed dial action item.
     * @param id The unique ID for the item to be found.
     * @return The found item or null.
     */
    private SpeedDialActionItem findActionItem(int id) {
        for (SpeedDialActionItem item : speedDial.getActionItems())
            if (item.getId() == id)
                return item;

        return null;
    }

    /**
     * Requires permissions needed to complete share with SMS operation.
     * @return True if permissions are granted false if not.
     */
    private boolean requireSMSPermissions() {
        boolean hasSendSMSPerms = ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        boolean hasReadPhoneStatePerms = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;

        if (hasSendSMSPerms && hasReadPhoneStatePerms)
            return true;

        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE
        }, REQUEST_PERMISSION_SHARE_SMS);

        return false;
    }

    /**
     * Shares the event as an SMS.
     */
    private void shareSMS() {
        EventSharer.shareSMS(this, event.uid);
    }

    /**
     * Called when SMS permission request has been denied.
     * Displays a toast for the user.
     */
    private void onRequireSMSPermissionsDenied() {
        showToast(getString(R.string.contact_picker_requires_sms_permission), Toast.LENGTH_LONG);
    }

    /**
     * Callback for when the copy description button is pressed.
     * @param view The view responsible for triggering the event.
     */
    public void onPressCopyDescription(View view) {
        TextView textView = findViewById(R.id.textDescription);
        String text = textView.getText().toString();

        if (text.equals(""))
            return;

        ClipboardManager clipManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("text", text);
        clipManager.setPrimaryClip(data);
    }

    @Override
    public void onPause() {
        super.onPause();
        TextToSpeechHelper.stopSpeech();
        speedDial.close();
    }

    /**
     * Called when the activity is resumed.
     * Initialises activity or updates values for the event.
     */
    @Override
    public void onResume() {
        super.onResume();

        event = database.eventDao().findById(
                getIntent().getIntExtra(EventCardBuilder.EXTRA_EVENT_UID, -1)
        );

        if (!isInitialized) {
            preparePhotos();
            prepareFABs();
        }

        updateSpeedDialActionItems();
        prepareInformation();
        prepareToolbar();
        prepareYoutube();
        isInitialized = true;
    }

    /**
     * Called once the started activity obtains a result.
     * @param requestCode The request code of the started activity.
     * @param resultCode The result code of the started activity operation.
     * @param data The data or null if none exists.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            EventPhoto photo = new EventPhoto();
            photo.eventId = event.uid;
            photo.uri = lastPhotoUri;
            database.eventPhotoDao().insert(photo);
            addEventPhoto(database.eventPhotoDao().findByURI(lastPhotoUri)); // Required to load EventPhoto with UID
        }
    }

    /**
     * Called when a permission request has a result.
     * @param requestCode The request code.
     * @param permissions The permissions requested.
     * @param grantResults The grant results of all permissions requested.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_SHARE_SMS) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                shareSMS();
            else
                onRequireSMSPermissionsDenied();
        } else if (requestCode == REQUEST_PERMISSION_CALENDAR) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                onPressAddToCalendar();
            else
                onRequireCalendarPermissionsDenied();
        }
    }

    /**
     * Callback for when a confirmation action has been positively confirmed.
     * @param requestID The request identifier.
     */
    @Override
    public void onDialogPositiveConfirmation(int requestID) {
        switch (requestID) {
            case REQUEST_CONFIRMATION_DELETE_EVENT:
                deleteEvent();
                break;
            case REQUEST_CONFIRMATION_DELETE_PHOTO:
                deletePressedPhoto();
                break;
            case REQUEST_CONFIRMATION_DELETE_EVENT_CALENDAR_ENTRY:
                removeFromCalendar();
                break;
            case REQUEST_CONFIRMATION_ADD_EVENT_CALENDAR_ENTRY:
                addToCalendar();
                break;
        }
    }

    /**
     * Callback for when a confirmation action has been negatively confirmed.
     * @param requestID The request identifier.
     */
    @Override
    public void onDialogNegativeConfirmation(int requestID) {
        switch (requestID) {
            case REQUEST_CONFIRMATION_DELETE_PHOTO:
                disablePhotoEditing();
                break;
        }
    }
}
