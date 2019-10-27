package com.example.a9gesllprov.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides Youtube related utility functionality.
 */
public class YoutubeUtil {

    /**
     * Extracts the Youtube video ID from a Youtube video URL.
     * @param url The URL of the youtube video.
     * @return The extracted video ID or null.
     */
    public static String getYoutubeVideoIdFromUrl(String url) {
        url = url.replace("&feature=youtu.be", "");
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        return matcher.find() ? matcher.group() : null;
    }
}
