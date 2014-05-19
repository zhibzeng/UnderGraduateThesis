package com.example.mytabhost;


import com.example.mytabhost.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ClearDialog extends Activity{
	private Button clear=null;
	private Button cancer=null;
	private TextView textView=null;
	private ProgressDialog myDialog;// 声明ProgressDialog类型变量
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clear_xml);
		clear=(Button)findViewById(R.id.btnclear);
		cancer=(Button)findViewById(R.id.btncancer);
		textView=(TextView)findViewById(R.id.cleartext);
		textView.setText("当前缓存为1.2M");
		clear.setOnClickListener(new myDialogListener());
		cancer.setOnClickListener(new myDialogListener());
	}
	class myDialogListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Intent intent1=new Intent();
			if(v==clear)
			{
				myDialog = ProgressDialog.show(ClearDialog.this, "请稍等一会哦...", "正在努力清除缓存ing...",
						true);
				myDialog.getWindow().setGravity(Gravity.CENTER); //居中显示加载数据对话框
				myDialog.setCancelable(true);
				new Thread() {
					public void run() {
						try {
							Thread.sleep(3000);//模拟加载，停留1.5秒
							Message m = new Message();
							listHandler.sendMessage(m);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}.start();
				
			}
			else {
				finish();
			}
		}
		
	}
	
	final Handler listHandler = new Handler() {
		public void handleMessage(Message msg) {
			textView.setText("缓存已清理");
			if(myDialog.isShowing()){
				myDialog.dismiss();
			}
		}};


}
