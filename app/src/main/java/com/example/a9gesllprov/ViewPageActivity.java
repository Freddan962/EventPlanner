package com.example.a9gesllprov;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Provides functionality to view a webpage within the application.
 */
public class ViewPageActivity extends AppCompatActivity {

    /**
     * Called when ViewPageActivity is constructed.
     * @param savedInstanceState The saved instance bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        String url = getIntent().getStringExtra("EXTRA_WEBPAGE");
        if (url == null || url.isEmpty())
            finish();

        viewWebPage(url);
    }

    /**
     * Views the web page in the activity.
     * @param url The URL to be viewed.
     */
    private void viewWebPage(String url) {
        WebView view = findViewById(R.id.primaryWebView);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setAppCacheEnabled(true);
        view.setWebViewClient(new WebViewClient());
        view.loadUrl(url);
    }
}
