package com.hcjcch.educationaladministration.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hcjcch.educationaladministration.educational.R;
import com.hcjcch.educationaladministration.oauthsdk.OAuth;
import com.hcjcch.educationaladministration.oauthsdk.OauthUtil;

/**
 * Created by hcjcch on 2014/10/26.
 */

public class OauthActivity extends Activity {

    private WebView webView;
    private String url;  //请求的地址
    private String result; //得到的返回结果
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);
        intent = getIntent();
        webView = (WebView) findViewById(R.id.webView_oauth);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                try {   //当得到返回结果后，对结果进行数据的处理
                    if (url.contains("#")) {
                        result = url.substring(url.indexOf("#") + 1,
                                url.length());
                        String token  = result.substring(result.indexOf("access_token="), result.length());
                        token =token.substring(0, token.indexOf("&"));
                        token = token.substring(token.indexOf("=")+1,token.length());
                        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(OauthActivity.this);
                        OauthUtil.saveOauth(prefs, token);
                        Toast.makeText(OauthActivity.this,OauthUtil.getAccessToken(prefs),Toast.LENGTH_SHORT).show();
                        OauthActivity.this.finish();
                    }
                } catch (Exception e) {
                    result = "";
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });

        try {
            url = OAuth.authorizeURL("token");
            openBrowser(url);
        } catch (Exception e) {
            url = "";
        }
    }

    // 利用webView的loadUrl方法
    public void openBrowser(String url) {
        webView.loadUrl(url);
    }
}