package com.example.mytabhost;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SinaWeiboWebView extends Activity{
	private static int num=0;//�ж���ʾ�Ի�����������
	 /** Called when the activity is first created. */
	private WebView wv;
	private ProgressDialog pd;
	private Handler handler;
	private long exitTime = 0;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sina_weibo);
        init();//ִ�г�ʼ������
        String url = "http://m.weibo.cn/p/1005051878396095_-_WEIBO_SECOND_PROFILE_WEIBO/detailTpl?module=mblog&action=mblogs&itemid=&title=%E5%85%A8%E9%83%A8%E5%BE%AE%E5%8D%9A&rl=2&luicode=10000011&lfid=1005051878396095";
        handler=new Handler(){
        	public void handleMessage(Message msg)
    	    {//����һ��Handler�����ڴ��������߳���UI��ͨѶ
    	      if (!Thread.currentThread().isInterrupted())
    	      {
    	        switch (msg.what)
    	        {
    	        case 0:
    	        	if(num==0){
    	        		pd.show();//��ʾ���ȶԻ��� 
        	        	pd.getWindow().setGravity(Gravity.CENTER);
        	        	num++;
    	        	}
    	        	break;
    	        case 1:
    	        	pd.hide();//���ؽ��ȶԻ��򣬲���ʹ��dismiss()��cancel(),�����ٴε���show()ʱ����ʾ�ĶԻ���СԲȦ���ᶯ��
    	        	break;
    	        }
    	      }
    	      super.handleMessage(msg);
    	    }
        };
        loadurl(wv,url);
      
    }
	
	
	/*
	 * ��ʼ��
	 */
    public void init(){
    	wv=(WebView)findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);//����JS
        wv.setScrollBarStyle(0);//���������Ϊ0���ǲ������������ռ䣬��������������ҳ��
        wv.setWebViewClient(new WebViewClient(){   
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            	loadurl(view,url);//������ҳ
            	
                return true;   
            }//��д�������,��webview����
 
        });
        wv.setWebChromeClient(new WebChromeClient(){
        	public void onProgressChanged(WebView view,int progress){//������ȸı������ 
             	if(progress==100){
            		handler.sendEmptyMessage(1);//���ȫ������,���ؽ��ȶԻ���
            	}else{
            		pd.setMessage("ҳ������У����Ժ�..."+progress + "%");
            	}   
                super.onProgressChanged(view, progress);   
            }   
        });
    	pd=new ProgressDialog(SinaWeiboWebView.this);
    	pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       
        
    }
    
    
    
    
    
    public void loadurl(final WebView view,final String url){
    	new Thread(){
        	public void run(){
        		handler.sendEmptyMessage(0);
        		view.loadUrl(url);//������ҳ
        	}
        }.start();
    }
    
    
    
   
    
    
    
    
//		/**
//	    * �˳�ȷ��
//	    */
//	public void ConfirmExit(){
//	    	AlertDialog.Builder ad=new AlertDialog.Builder(SinaWeiboWebView.this);
//	    	ad.setTitle("�˳�");
//	    	ad.setMessage("���Ҫ�뿪?��������...");
//	    	ad.setPositiveButton("ȷ���뿪", new DialogInterface.OnClickListener() {//�˳���ť
//				public void onClick(DialogInterface dialog, int i) {
//					// TODO Auto-generated method stub
//					SinaWeiboWebView.this.finish();//�ر�activity
//	 
//				}
//			});
//	    	ad.setNegativeButton("����һ���",new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int i) {
//					//���˳�����ִ���κβ���
//				}
//			});
//	    	ad.show();//��ʾ�Ի���
//	    }
	
    /*
     * ��׽���ؼ�(non-Javadoc)
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {   
            wv.goBack();   
            return true;   
        }else if(keyCode == KeyEvent.KEYCODE_BACK){
        	SinaWeiboWebView.this.finish();//���˷��ؼ������Ѿ����ܷ��أ���ִ���˳�ȷ��
        	return true; 
        }   
        return super.onKeyDown(keyCode, event);   
    }

}
