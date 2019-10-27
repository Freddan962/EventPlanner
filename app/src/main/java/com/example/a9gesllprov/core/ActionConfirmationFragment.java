package com.example.a9gesllprov.core;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.a9gesllprov.R;

/**
 * Creates a confirmation dialog for a specific action.
 */
public class ActionConfirmationFragment extends DialogFragment {

    private Activity activity;
    private ActionConfirmationCallback callback;

    private String title = "";
    private String message = "";
    private String positiveButtonText;
    private String negativeButtonText;

    private int requestID = -1;

    /**
     * Constructs a new ActionConfirmationFragment
     * @param activity The activity for which the fragment should be constructed.
     * @param callback The callback entity for confirmation actions.
     */
    public ActionConfirmationFragment(Activity activity, ActionConfirmationCallback callback) {
        this.activity = activity;
        this.callback = callback;

        positiveButtonText = activity.getString(R.string.action_confirmation_positive);
        negativeButtonText = activity.getString(R.string.action_confirmation_negative);
    }

    /**
     * Sets the dialog title.
     * @param title The title to be set.
     * @return The action confirmation fragment.
     */
    public ActionConfirmationFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the dialog message.
     * @param message The message to be set.
     * @return The action confirmation fragment.
     */
    public ActionConfirmationFragment setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Sets the dialog positive button text.
     * @param text The text to be set.
     * @return The action confirmation fragment.
     */
    public ActionConfirmationFragment setPositiveButtonText(String text) {
        this.positiveButtonText = text;
        return this;
    }

    /**
     * Sets the dialog negative button text.
     * @param text The text to be set.
     * @return The action confirmation fragment.
     */
    public ActionConfirmationFragment setNegativeButtonText(String text) {
        this.negativeButtonText = text;
        return this;
    }

    /**
     * Sets the dialog request id.
     * @param id The request id.
     * @return The action confirmation fragment.
     */
    public ActionConfirmationFragment setRequestID(int id) {
        this.requestID = id;
        return this;
    }

    /**
     * Constructs a new dialog from this action confirmation fragment.
     * @param savedInstanceState The saved instance state.
     * @return The constructed dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callback.onDialogPositiveConfirmation(requestID);
                }
            })
            .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callback.onDialogNegativeConfirmation(requestID);
                }
            });

        return builder.create();
    }
}
