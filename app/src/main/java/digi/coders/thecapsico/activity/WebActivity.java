package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityWebBinding;
import digi.coders.thecapsico.helper.InternetCheck;

public class WebActivity extends AppCompatActivity {


    ActivityWebBinding binding;
    private int key;

    public static Activity WebActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WebActivity=this;
        key=getIntent().getIntExtra("key",0);
        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(key==1)
        {
            binding.toolbarText.setText("Chat Here");
            loadLink("https://yourcapsico.com/Home/CustomerChat");
        }
        else if(key==2)
        {
            binding.toolbarText.setText("About us");
            loadLink("https://yourcapsico.com/Home#about");
        }

    }

    private void loadLink(String link)
    {
        if(new InternetCheck().isNetworkAvailable(getApplicationContext())) {
            binding.webview.getSettings().setLoadsImagesAutomatically(true);
            binding.webview.getSettings().setJavaScriptEnabled(true);
            binding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            binding.webview.getSettings().setAllowContentAccess(true);
            binding.webview.getSettings().setAllowFileAccess(true);
            binding.webview.getSettings().setAllowFileAccessFromFileURLs(true);
            binding.webview.getSettings().setAllowUniversalAccessFromFileURLs(true);

            binding.webview.getSettings().setAppCachePath(getApplicationContext().getApplicationContext().getCacheDir().getAbsolutePath());
            binding.webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            binding.webview.getSettings().setDatabaseEnabled(true);
            binding.webview.getSettings().setDomStorageEnabled(true);
            binding.webview.getSettings().setUseWideViewPort(true);
            binding.webview.getSettings().setLoadWithOverviewMode(true);
            binding.webview.getSettings().setPluginState(WebSettings.PluginState.ON);

            binding.webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    binding.progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    binding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String request) {
                    view.loadUrl(request);
                    binding.progressBar.setVisibility(View.GONE);
                    return super.shouldOverrideUrlLoading(view, request);
                }
            });
            binding.webview.loadUrl(link);
        }else{
            Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBackPressed() {

        if(binding.webview.canGoBack()){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.webview.goBack();
        }
        else
        {
            finish();
        }
    }
}