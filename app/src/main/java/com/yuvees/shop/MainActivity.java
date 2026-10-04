package com.yuvees.shop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    private final Handler splashHandler =
            new Handler(Looper.getMainLooper());

    private final Runnable websiteRunnable = new Runnable() {
        @Override
        public void run() {
            showWebsite();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // White splash screen
        setContentView(R.layout.activity_splash);

        ImageView splashLogo = findViewById(R.id.splashLogo);

        // Initial position of logo
        splashLogo.setAlpha(0f);
        splashLogo.setScaleX(0.92f);
        splashLogo.setScaleY(0.92f);

        // Smooth luxury logo animation
        splashLogo.animate()
                .alpha(1.0f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(1100)
                .setInterpolator(
                        new AccelerateDecelerateInterpolator()
                )
                .start();

        // Open website after splash
        splashHandler.postDelayed(
                websiteRunnable,
                2100
        );
    }

    private void showWebsite() {

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(false);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request) {

                Uri uri = request.getUrl();

                // Keep YUVEES website inside the app
                if ("yuvees.com".equals(uri.getHost())
                        || "www.yuvees.com".equals(uri.getHost())) {

                    return false;
                }

                // Open external links outside the app
                Intent intent =
                        new Intent(Intent.ACTION_VIEW, uri);

                startActivity(intent);

                return true;
            }
        });

        // Load YUVEES website
        webView.loadUrl("https://yuvees.com/");
    }

    @Override
    public void onBackPressed() {

        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {

        // Cancel splash timer
        splashHandler.removeCallbacks(websiteRunnable);

        // Clean WebView
        if (webView != null) {
            webView.destroy();
            webView = null;
        }

        super.onDestroy();
    }
}
