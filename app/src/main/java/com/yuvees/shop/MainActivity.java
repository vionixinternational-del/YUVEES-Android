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

        // Keep the existing white Splash Screen
        setContentView(R.layout.activity_splash);

        ImageView splashLogo = findViewById(R.id.splashLogo);

        // Start slightly smaller and invisible
        splashLogo.setAlpha(0f);
        splashLogo.setScaleX(0.88f);
        splashLogo.setScaleY(0.88f);

        // Elegant luxury logo reveal
        splashLogo.animate()
                .alpha(1.0f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(1200)
                .setInterpolator(
                        new AccelerateDecelerateInterpolator()
                )
                .start();

        // Keep the luxury splash visible before opening YUVEES
        splashHandler.postDelayed(websiteRunnable, 2300);
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

                if ("yuvees.com".equals(uri.getHost())
                        || "www.yuvees.com".equals(uri.getHost())) {
                    return false;
                }

                Intent intent =
                        new Intent(Intent.ACTION_VIEW, uri);

                startActivity(intent);
                return true;
            }
        });

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

        splashHandler.removeCallbacks(websiteRunnable);

        if (webView != null) {
            webView.destroy();
            webView = null;
        }

        super.onDestroy();
    }
}
