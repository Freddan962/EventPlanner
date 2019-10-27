package com.example.a9gesllprov;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.a9gesllprov.core.DateTimeFormatter;
import com.example.a9gesllprov.core.YoutubeUtil;
import com.example.a9gesllprov.database.DatabaseManager;
import com.example.a9gesllprov.database.Event;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * The activity responsible for event creation.
 */
public class CreateEventActivity extends AppCompatActivity {

    public static final int REQUEST_SPEAK_DESCRIPTION = 1;
    public static final String EXTRA_EDIT_EVENT_UID = "EXTRA_EDIT_EVENT_UID";

    private int startYear = 0;
    private int startMonth = 0;
    private int startDay = 0;
    private int startHour = 0;
    private int startMinute = 0;

    private int endYear = 0;
    private int endMonth = 0;
    private int endDay = 0;
    private int endHour = 0;
    private int endMinute = 0;

    private EditText fieldName;
    private EditText fieldPhoneNumber;
    private EditText fieldDescription;
    private EditText fieldHomePage;
    private EditText fieldStartTime;
    private EditText fieldEndTime;
    private EditText fieldMail;
    private EditText fieldYoutube;

    private ClipboardManager clipManager;

    private Event event;

    /**
     * Called when CreateEventActivity is created.
     * @param savedInstanceState The saved instance bundle or null if none exists.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        clipManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        int event_uid = getIntent().getIntExtra(EXTRA_EDIT_EVENT_UID, -1);
        if (event_uid != -1)
            event = DatabaseManager.getInstance().getDatabase().eventDao().findById(event_uid);

        prepareToolbar();
        findFields();
        prepareUI();
    }

    /**
     * Prepares the toolbar of the activity.
     */
    private void prepareToolbar() {
        CollapsingToolbarLayout collapsing = findViewById(R.id.collapsing);

        if (isInEditingMode()) {
            collapsing.setTitle(event.name);
            collapsing.setExpandedTitleTextAppearance(R.style.EventNavbarText);
            collapsing.setCollapsedTitleTextAppearance(R.style.EventNavbarText);
        } else {
            collapsing.setTitle(getString(R.string.create_event_navbar_title));
        }

        collapsing.setExpandedTitleColor(Color.WHITE);
        collapsing.setCollapsedTitleTextColor(Color.WHITE);
    }

    /**
     * Finds and set field references.
     */
    private void findFields() {
        fieldName = findViewById(R.id.fieldName);
        fieldPhoneNumber = findViewById(R.id.fieldPhoneNumber);
        fieldMail = findViewById(R.id.fieldMail);
        fieldDescription = findViewById(R.id.fieldDescription);
        fieldHomePage = findViewById(R.id.fieldHomepage);
        fieldStartTime = findViewById(R.id.fieldStartTime);
        fieldEndTime = findViewById(R.id.fieldEndTime);
        fieldYoutube = findViewById(R.id.fieldYoutube);
    }

    /**
     * Prepares the UI of the activity.
     */
    private void prepareUI() {
        fieldStartTime.setFocusable(false);
        fieldEndTime.setFocusable(false);
        resetTimeSelection();

        if (isInEditingMode()) {
            prepareUIForEditing();
        }
    }

    /**
     * Prepares the UI for edit mode
     */
    private void prepareUIForEditing() {
        fillInEventInformation();

        MaterialButton resetButton = findViewById(R.id.createEventResetButton);
        resetButton.setText(getString(R.string.create_event_return_button));
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { finish(); }
        });

        MaterialButton createButton = findViewById(R.id.createEventCreateButton);
        createButton.setText(getString(R.string.create_event_update_button));
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { onPressUpdateEvent(); }
        });
    }

    /**
     * Update the event in the database.
     */
    private void onPressUpdateEvent() {
        updateEventValues(event);
        DatabaseManager.getInstance().getDatabase().eventDao().update(event);
        Toast.makeText(this, getString(R.string.create_event_updated_event), Toast.LENGTH_LONG).show();
    }

    /**
     * Callback for when the start time pick date button is pressed.
     * Shows a start date picker dialog.
     * @param view The view responsible for triggering the event.
     */
    public void onPressStartTimePickDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker picker, int year, int month, int day) {
                fieldStartTime.setText(DateTimeFormatter.formatTime(year, month, day, startHour, startMinute));
                startYear = year;
                startMonth = month;
                startDay = day;
            }
        }, startYear, startMonth, startDay);
        datePickerDialog.show();
    }

    /**
     * Callback for when the start time pick time button is pressed.
     * Shows a start time picker dialog.
     * @param view The view responsible for triggering the event.
     */
    public void onPressStartTimePickTime(View view) {
        LocalDateTime now = LocalDateTime.now();
        startHour = now.getHour();
        startMinute = now.getMinute();

       TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                fieldStartTime.setText(DateTimeFormatter.formatTime(startYear, startMonth, startDay, hour, minute));
                startHour = hour;
                startMinute = minute;
            }
        }, startHour, startMinute, true);
        timePickerDialog.show();
    }

    /**
     * Callback for when the end time pick date button is pressed.
     * Shows a end date picker dialog.
     * @param view The view responsible for triggering the event.
     */
    public void onPressEndTimePickDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker picker, int year, int month, int day) {
                fieldEndTime.setText(DateTimeFormatter.formatTime(year, month, day, endHour, endMinute));
                endYear = year;
                endMonth = month;
                endDay = day;
            }
        }, endYear, endMonth, endDay);
        datePickerDialog.show();
    }

    /**
     * Callback for when the end time pick time button is pressed.
     * Shows a end time picker dialog.
     * @param view The view responsible for triggering the event.
     */
    public void onPressEndTimePickTime(View view) {
        LocalDateTime now = LocalDateTime.now();
        endHour = now.getHour();
        endMinute = now.getMinute();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                EditText fieldEndTime = findViewById(R.id.fieldEndTime);
                fieldEndTime.setText(DateTimeFormatter.formatTime(endYear, endMonth, endDay, hour, minute));
                endHour = hour;
                endMinute = minute;
            }
        }, endHour, endMinute, true);
        timePickerDialog.show();
    }

    /**
     * Callback for when the description record with microphone button is pressed.
     * Starts a speech to text intent.
     * @param view The view responsible for triggering the event.
     */
    public void onPressDescriptionRecordWithMicrophone(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now");
        startActivityForResult(intent, REQUEST_SPEAK_DESCRIPTION);
    }

    /**
     * Callback for when the paste description button is pressed.
     * Fills in whatever is attached to the clipboard into the description field.
     * @param view The view responsible for triggering the event.
     */
    public void onPressPasteDescription(View view) {
        if (clipManager.hasPrimaryClip()) {
            ClipData data = clipManager.getPrimaryClip();
            ClipData.Item item = data.getItemAt(0);

            String text = item.getText().toString();
            if (!text.isEmpty())
                fieldDescription.setText(text);
        }
    }

    /**
     * Callback for when the create event button is pressed.
     * Attempts to create a event from the filled in information.
     * @param view The view responsible for triggering the event.
     */
    public void onPressCreateEvent(View view) {
        if (!validateFormInput()) {
            onCreateEventFailed();
            return;
        }

        onCreateEventSuccess();
    }

    /**
     * Called when event creation failed.
     */
    private void onCreateEventFailed() {
        Toast.makeText(getApplicationContext(), getString(R.string.create_event_failed), Toast.LENGTH_LONG).show();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    /**
     * Called when event creation was successful.
     */
    private void onCreateEventSuccess() {
        createEvent();
        Toast.makeText(getApplicationContext(), getString(R.string.create_event_sucess), Toast.LENGTH_LONG).show();
        NavUtils.navigateUpFromSameTask(this);
    }

    /**
     * Creates a new event from the information filled out in the activity.
     */
    private void createEvent() {
        Event event = new Event();
        updateEventValues(event);
        DatabaseManager.getInstance().getDatabase().eventDao().insert(event);
    }

    /**
     * Updates the event with form values
     * @param event The event to be updated.
     */
    private void updateEventValues(Event event) {
        Calendar calendar = Calendar.getInstance();
        event.name = fieldName.getText().toString();
        event.contactNumber = fieldPhoneNumber.getText().toString();
        event.mail = fieldMail.getText().toString();
        event.description = fieldDescription.getText().toString();
        event.homePage = fieldHomePage.getText().toString();
        event.youtubeUrl = fieldYoutube.getText().toString();

        calendar.set(startYear, startMonth, startDay, startHour, startMinute);
        event.startDate = calendar.getTime();
        calendar.set(endYear, endMonth, endDay, endHour, endMinute);
        event.endDate = calendar.getTime();
    }

    /**
     * Handles validation of form input.
     * Displays error messages for invalid fields.
     * @return true if valid false if not.
     */
    private boolean validateFormInput() {
        boolean isValid = true;

        if (fieldName.getText().toString().isEmpty())
        {
            fieldName.setError(getString(R.string.create_event_error_field_empty));
            isValid = false;
        }

        if (fieldStartTime.getText().toString().isEmpty()) {
            fieldStartTime.setError(getString(R.string.create_event_error_field_start_time));
            isValid = false;
        }

        if (fieldEndTime.getText().toString().isEmpty()) {
            fieldEndTime.setError(getString(R.string.create_event_error_field_end_time));
            isValid = false;
        }

        if (fieldPhoneNumber.getText().toString().isEmpty()) {
            fieldPhoneNumber.setError(getString(R.string.create_event_error_field_empty));
            isValid = false;
        }

        if (fieldMail.getText().toString().isEmpty()) {
            fieldMail.setError(getString(R.string.create_event_error_field_empty));
            isValid = false;
        }

        if (fieldDescription.getText().toString().isEmpty()) {
            fieldDescription.setError(getString(R.string.create_event_error_field_empty));
            isValid = false;
        }

        if (fieldHomePage.getText().toString().isEmpty()) {
            fieldHomePage.setError(getString(R.string.create_event_error_field_empty));
            isValid = false;
        }

        String videoInput = fieldYoutube.getText().toString();
        if (!videoInput.isEmpty()) {
            String videoID = YoutubeUtil.getYoutubeVideoIdFromUrl(videoInput);
            if (videoID == null) {
                fieldYoutube.setError(getString(R.string.create_event_invalid_youtube_url));
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Callback for when the reset button is pressed.
     * @param view The view responsible for triggering the event.
     */
    public void onPressReset(View view) {
        fieldName.setText("");
        fieldPhoneNumber.setText("");
        fieldMail.setText("");
        fieldDescription.setText("");
        fieldHomePage.setText("");
        resetTimeSelection();
        fieldStartTime.setText("");
        fieldEndTime.setText("");
        fieldYoutube.setText("");
    }

    /**
     * Returns whether or not the activity is in editing mode.
     * @return True or false
     */
    public boolean isInEditingMode() {
        return event != null;
    }

    /**
     * Callback for when a activity has finished with a result.
     * @param requestCode The request code of the activity.
     * @param resultCode The result code.
     * @param data The data of the result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SPEAK_DESCRIPTION && resultCode == RESULT_OK && data != null) {
            ArrayList result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            fieldDescription.setText(result.get(0).toString());
        }
    }

    /**
     * Fills the event information into the UI fields.
     */
    private void fillInEventInformation() {
        fieldHomePage.setText(event.homePage);
        fieldDescription.setText(event.description);
        fieldPhoneNumber.setText(event.contactNumber);
        fieldMail.setText(event.mail);
        fieldName.setText(event.name);
        fieldYoutube.setText(event.youtubeUrl);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.startDate);
        startYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        startHour = calendar.get(Calendar.HOUR_OF_DAY);
        startMinute = calendar.get(Calendar.MINUTE);

        fieldStartTime.setText(DateTimeFormatter.formatTime(startYear, startMonth, startDay, startHour, startMinute));

        calendar.setTime(event.endDate);
        endYear = calendar.get(Calendar.YEAR);
        endMonth = calendar.get(Calendar.MONTH);
        endDay = calendar.get(Calendar.DAY_OF_MONTH);
        endHour = calendar.get(Calendar.HOUR_OF_DAY);
        endMinute = calendar.get(Calendar.MINUTE);

        fieldEndTime.setText(DateTimeFormatter.formatTime(endYear, endMonth, endDay, endHour, endMinute));
    }

    /**
     * Resets time selection.
     */
    private void resetTimeSelection() {
        LocalDateTime now = LocalDateTime.now();
        startYear = now.getYear();
        startMonth = now.getMonthValue();
        startDay = now.getDayOfMonth();
        startHour = now.getHour();
        startMinute = now.getMinute();

        endYear = now.getYear();
        endMonth = now.getMonthValue();
        endDay = now.getDayOfMonth();
        endHour = now.getHour();
        endMinute = now.getMinute();
    }
}
