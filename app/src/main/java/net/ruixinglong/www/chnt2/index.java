package net.ruixinglong.www.chnt2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yanzhenjie.andserver.website.AssetsWebsite;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.filter.HttpCacheFilter;

import net.ruixinglong.www.chnt2.util.NetUtils;
import net.ruixinglong.www.chnt2.handler.RegisterHandler;

import java.util.concurrent.TimeUnit;

public class index extends AppCompatActivity {
    private WebView webView;

    private Server mServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mServer = AndServer.serverBuilder()
                .inetAddress(NetUtils.getLocalIPAddress()) // Bind IP address.
                .port(8080)
                .timeout(10, TimeUnit.SECONDS)
                .website(new AssetsWebsite(getAssets(), "web"))
                .registerHandler("/register", new RegisterHandler())
                .filter(new HttpCacheFilter())
                .listener(mListener)
                .build();
        if (mServer.isRunning()) {
            String hostAddress = mServer.getInetAddress().getHostAddress();
//            ServerManager.serverStart(CoreService.this, hostAddress);
        } else {
            mServer.startup();
        }
        Log.d("error","" +  NetUtils.getLocalIPAddress());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("http://" + NetUtils.getLocalIPAddress() +":8080/register.html");
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

    /**
     * Server listener.
     */
    private Server.ServerListener mListener = new Server.ServerListener() {
        @Override
        public void onStarted() {
            String hostAddress = mServer.getInetAddress().getHostAddress();
        }

        @Override
        public void onStopped() {
        }

        @Override
        public void onError(Exception e) {
        }
    };
}
