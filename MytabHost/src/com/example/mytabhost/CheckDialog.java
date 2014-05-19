package com.example.mytabhost;


import com.example.mytabhost.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CheckDialog extends Activity{
	private Button check=null;
	private Button cancer=null;
	private TextView textView=null;
	private int num;
	private ProgressDialog myDialog;// 声明ProgressDialog类型变量
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_xml);
		check=(Button)findViewById(R.id.btnupdate);
		cancer=(Button)findViewById(R.id.btncancer2);
		textView=(TextView)findViewById(R.id.checktext);
		num=checkEdition();
		if (num==0) {
			textView.setText("当前版本为最新版本");
			check.setVisibility(View.GONE);
		}
		else {
			textView.setText("当前版本不是最新版本，请点击升级");
			check.setOnClickListener(new myDialogListener());
		}
		check.setVisibility(View.VISIBLE);
		
		cancer.setOnClickListener(new myDialogListener());
	}
	private int checkEdition()//判断是否需要更新版本，1代表需要，0代表不需要
	{
		return 1;
	}
	
	class myDialogListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Intent intent1=new Intent();
			if(v==check)
			{
				myDialog = ProgressDialog.show(CheckDialog.this, "请稍等一会哦...", "正在验证版本号...",
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
			textView.setText("当前版本为最新版本");
			check.setVisibility(View.GONE);
			if(myDialog.isShowing()){
				myDialog.dismiss();
			}
		}};


}
