package net.ruixinglong.www.chnt2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class index extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("http://www.baidu.com");
        webView = (WebView) findViewById(R.id.webview);

        WebSettings wSet = webView.getSettings();
        //从ListView中获得URL
        String url = getIntent().getStringExtra("content_url");
        //加载URL
        webView.loadUrl(url);
        //开启JavaScript的功能
        wSet.setJavaScriptEnabled(true);
        //开启缓存，无网络时加载本地内容
        wSet.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //关闭打开第三方浏览器
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });


    }
}
