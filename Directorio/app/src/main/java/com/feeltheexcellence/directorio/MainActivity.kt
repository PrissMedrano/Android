package com.feeltheexcellence.directorio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val indexWebView : WebView
        indexWebView = findViewById(R.id.indexWebView)
        indexWebView.settings.javaScriptEnabled = true;
        indexWebView.loadUrl("file:///android_asset/index.html")
    }
}