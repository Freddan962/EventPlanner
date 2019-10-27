package com.example.a9gesllprov;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.a9gesllprov.core.ContactMaintainer;
import com.example.a9gesllprov.core.EventSharer;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Activity responsible for SMS contact selection.
 */
public class SMSContactPicker extends AppCompatActivity {

    private final int REQUEST_PERMISSION_READ_CONTACTS = 1;

    private TextInputLayout textFilter = null;
    private ContactMaintainer contactMaintainer;

    /**
     * Called when the SMSContactPicker activity is created.
     * @param savedInstanceState The saved state bundle for this activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);

        textFilter = findViewById(R.id.textFilter);
        int event_uid = getIntent().getIntExtra(EventSharer.EXTRA_EVENT_ID, -1);

        LinearLayout layoutContacts = findViewById(R.id.viewContactsLayout);
        contactMaintainer = new ContactMaintainer(this, layoutContacts, event_uid);

        prepareToolbar();
        prepareTextListener();
        fillContacts();
    }

    /**
     * Prepares the activity's toolbar.
     */
    private void prepareToolbar() {
        CollapsingToolbarLayout collapsing = findViewById(R.id.collapsing);
        collapsing.setTitle(getString(R.string.contact_picker_navbar_title));
        collapsing.setExpandedTitleColor(Color.WHITE);
        collapsing.setCollapsedTitleTextColor(Color.WHITE);
    }

    /**
     * Fills the contact list with selectable contacts.
     */
    private void fillContacts() {
        if (!requireContactPermissions())
            return;

        if (contactMaintainer.isWorking())
            contactMaintainer.restart(getFilter());
        else
            contactMaintainer.start(getFilter());
    }

    /**
     * Checks if activity has read contacts permissions.
     * Requests permissions if not.
     * @return True if application has permissions false if not.
     */
    private boolean requireContactPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSION_READ_CONTACTS);
            return false;
        }

        return true;
    }

    /**
     * Fetches the current filter.
     * @return The current active filter.
     */
    private String getFilter() {
        return textFilter.getEditText().getText().toString();
    }

    /**
     * Adds a text watcher to the filter field.
     * Allows for "automatic" updating of contact when filter has been changed.
     */
    private void prepareTextListener() {
        textFilter.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fillContacts();
            }

            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void afterTextChanged(Editable editable) { }
        });
    }

    /**
     * Called when a permission request has a result.
     * @param requestCode The request code.
     * @param permissions The permissions requested.
     * @param grantResults The grant results of all permissions requested.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                fillContacts();
            else
                fillContacts();
        }
    }

    /**
     * Callback for when the activity has paused.
     */
    @Override
    public void onPause() {
        super.onPause();
        contactMaintainer.stop();
    }
}
