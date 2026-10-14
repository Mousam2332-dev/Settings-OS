package com.example.androidtemplate

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This line hides the purple top bar!
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webview)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        
        // This bridges your HTML UI to native Android code
        webView.addJavascriptInterface(WebAppInterface(this), "Android")
        webView.loadUrl("file:///android_asset/index.html")
    }

    inner class WebAppInterface(private val context: AppCompatActivity) {
        @JavascriptInterface
        fun sendTrigger(trigger: String) {
            context.runOnUiThread {
                try {
                    // Check which button was tapped in the HTML
                    if (trigger.startsWith("OpenDevMode") || trigger.startsWith("DevMode")) {
                        context.startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
                    } else if (trigger.startsWith("SystemConnect")) {
                        context.startActivity(Intent(Settings.ACTION_SETTINGS))
                    } else {
                        // Shows a popup for buttons we haven't linked up yet
                        Toast.makeText(context, "Clicked: $trigger", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
