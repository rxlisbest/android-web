package net.ruixinglong.www.chnt2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class index extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("http://www.baidu.com");
    }
}
