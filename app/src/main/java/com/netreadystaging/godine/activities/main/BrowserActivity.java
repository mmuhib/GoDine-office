package com.netreadystaging.godine.activities.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.netreadystaging.godine.R;

public class BrowserActivity extends AppCompatActivity {

    ProgressBar progressBar;
    WebView browser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        Intent  intent= getIntent();
        String url   = intent.getStringExtra("url");

        final String title   = intent.getStringExtra("title");
        setupToolbar(title);
        progressBar = (ProgressBar) findViewById(R.id.progressBar) ;
        if(url!=null && !url.isEmpty())
        {
            if(!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("file://"))
            {
                url= "http://"+url ;
            }
            browser = (WebView) findViewById(R.id.browser);
            WebSettings webSettings = browser.getSettings();
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setSupportZoom(true);
            webSettings.setJavaScriptEnabled(true);
            browser.setWebViewClient(webViewClient);
            browser.loadUrl(url);
        }

    }

    private void setupToolbar(final String title)
    {
        ActionBar actionBar =  getSupportActionBar() ;
        if(actionBar!=null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.filter_toolbar);
            Toolbar toolbar = (Toolbar) actionBar.getCustomView() ;
            final ImageView ivToolBarBack = (ImageView) toolbar.findViewById(R.id.ivToolBarBack) ;
            ivToolBarBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            final ImageView ivToolBarDone = (ImageView) toolbar.findViewById(R.id.ivToolBarDone) ;
            ivToolBarDone.setVisibility(View.GONE);
            final TextView tvTitle = (TextView) toolbar.findViewById(R.id.tvTitle) ;
            tvTitle.setText(title);
            tvTitle.setTextSize(14f);
        }
    }

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(request.getUrl().getEncodedPath());
            }
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing,R.anim.slide_out_bottom);
    }
}


