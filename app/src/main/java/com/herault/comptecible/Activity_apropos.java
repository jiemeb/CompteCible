package com.herault.comptecible;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_apropos extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String version = "CompteCible version : ";
        setContentView(R.layout.activity_apropos);


        try {
            PackageManager manager = getApplicationContext().getPackageManager();
            PackageInfo info = null;
            info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
            version += info.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        TextView Version = findViewById(R.id.app_version);
        Version.setText(version);

        WebView mywebView = (WebView) findViewById(R.id.apropos_webview);
        mywebView.loadUrl("file:///android_asset/guide.html");

        mywebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String urlx) {
                view.loadUrl(urlx);
                return false;
            }

        });


    }

    /*********************************************************************************/
    /** Managing LifeCycle and database open/close operations ************************/
    /*********************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}
