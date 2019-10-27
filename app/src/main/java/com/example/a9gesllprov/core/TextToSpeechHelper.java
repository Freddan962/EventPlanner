package com.example.a9gesllprov.core;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

/**
 * Provides text to speech related functionality.
 */
public class TextToSpeechHelper {

    private static TextToSpeech tts;

    /**
     * Translates a string to speech.
     * @param from The context which the text to speech is created in.
     * @param text The text to be spoken.
     */
    public static void speak(Context from, final String text) {
       stopSpeech();

        tts = new TextToSpeech(from, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage (Locale.UK);
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });
    }

    /**
     * Stops the current speaking TTS instance if it exists.
     */
    public static void stopSpeech() {
        if (tts == null)
            return;

        tts.stop();
    }
}
