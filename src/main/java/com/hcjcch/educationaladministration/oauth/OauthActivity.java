package com.hcjcch.educationaladministration.oauth;

import android.app.Activity;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hcjcch.educationaladministration.educational.R;
import com.hcjcch.educationaladministration.oauthsdk.OAuth;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by hcjcch on 2014/9/15.
 */
@EActivity(R.layout.activity_oauth)
public class OauthActivity extends Activity {
    @ViewById(R.id.webView_oauth)
    WebView webView;

    @AfterInject
    void afterConstruct() {
        //ActionBar bar = getActionBar();
        //bar.setDisplayHomeAsUpEnabled(true);
    }

    @AfterViews
    void contentView() {
        try{
            String url = OAuth.authorizeURL("token");
            openBrowser(url);
        }catch (Exception e){
            e.printStackTrace();
        }
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println(url);
                if(url.contains("#")){
                    finish();
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
    }

    // 利用webView的loadUrl方法
    public void openBrowser(String url) {
        webView.loadUrl(url);
    }
}