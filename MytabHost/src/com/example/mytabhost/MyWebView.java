package com.example.mytabhost;

import com.example.mytabhost.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MyWebView extends Activity{
	private WebView wv=null;
	private Button btnReturn;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.webview);
	        wv = (WebView) findViewById(R.id.webview1);
	        btnReturn=(Button)findViewById(R.id.btn_wvReturn);
	        wv.getSettings().setJavaScriptEnabled(true);
	        wv.setScrollBarStyle(0);
	        WebSettings webSettings = wv.getSettings();
	        webSettings.setAllowFileAccess(true);
	        webSettings.setBuiltInZoomControls(true);
	        wv.loadUrl("http://www.qq.com");
	        btnReturn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();					
				}
			});
	        //加载数据
	        wv.setWebChromeClient(new WebChromeClient(){
	        	public void onProgressChanged(WebView view, int newProgress) {
	    	        if (newProgress == 100) {
	    	        MyWebView.this.setTitle("加载完成");
	    	        } else {
	    	        MyWebView.this.setTitle("加载中.......");
	    	        }
	        	  }
	        });
	        //这个是当网页上的连接被点击的时候
	        wv.setWebViewClient(new WebViewClient(){
	        	public boolean shouldOverrideUrlLoading(final WebView view,final String url) 
	        	{
	        	    view.loadUrl(url);
	        	        return true;
	        	        }
	        });   
	        }
	 // goBack()表示返回webView的上一页面
	 public boolean onKeyDown(int keyCoder, KeyEvent event) 
     {
     if (wv.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
     	wv.goBack();
     	return true;
     	}
     return false;
     }     
}
