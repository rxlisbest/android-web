package net.ruixinglong.www.chnt2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.filter.HttpCacheFilter;
import com.yanzhenjie.andserver.website.AssetsWebsite;

import net.ruixinglong.www.chnt2.handler.LoginHandler;
import net.ruixinglong.www.chnt2.handler.RegisterHandler;
import net.ruixinglong.www.chnt2.handler.SurveyLogHandler;
import net.ruixinglong.www.chnt2.handler.SurveyQuestionHandler;
import net.ruixinglong.www.chnt2.helper.DBHelper;
import net.ruixinglong.www.chnt2.util.NetUtils;

import java.util.concurrent.TimeUnit;

public class index extends AppCompatActivity {

    private WebView webView;
    private Server mServer;

    @SuppressLint("JavascriptInterface")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        mServer = AndServer.serverBuilder()
                .inetAddress(NetUtils.getLocalIPAddress()) // Bind IP address.
                .port(8080)
                .timeout(10, TimeUnit.SECONDS)
                .website(new AssetsWebsite(getAssets(), "web"))
                .registerHandler("/register", new RegisterHandler(this))
                .registerHandler("/login", new LoginHandler(this))
                .registerHandler("/survey_question", new SurveyQuestionHandler(this))
                .registerHandler("/survey_log", new SurveyLogHandler(this))
                .filter(new HttpCacheFilter())
                .listener(mListener)
                .build();
        if (mServer.isRunning()) {
            String hostAddress = mServer.getInetAddress().getHostAddress();
//            ServerManager.serverStart(CoreService.this, hostAddress);
        } else {
            mServer.startup();
        }
//        getWindow().setFlags( WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        webView = (WebView) findViewById(R.id.webview);
//        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.addJavascriptInterface(this, "nativeMethod");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.loadUrl("http://" + NetUtils.getLocalIPAddress() + ":8080/list.html");
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

    @JavascriptInterface
    public void toActivity(String activityName) {
        //此处应该定义常量对应，同时提供给web页面编写者
        if (TextUtils.equals(activityName, "survey")) {
            startActivity(new Intent(this, survey.class));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
