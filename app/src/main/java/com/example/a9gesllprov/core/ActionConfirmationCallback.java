package com.example.a9gesllprov.core;

/**
 * Interface providing callbacks for the action confirmation fragment.
 */
public interface ActionConfirmationCallback {
    void onDialogPositiveConfirmation(int requestID);
    void onDialogNegativeConfirmation(int requestID);
}
