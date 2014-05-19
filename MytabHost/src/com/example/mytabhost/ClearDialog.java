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
	private ProgressDialog myDialog;// ����ProgressDialog���ͱ���
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clear_xml);
		clear=(Button)findViewById(R.id.btnclear);
		cancer=(Button)findViewById(R.id.btncancer);
		textView=(TextView)findViewById(R.id.cleartext);
		textView.setText("��ǰ����Ϊ1.2M");
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
				myDialog = ProgressDialog.show(ClearDialog.this, "���Ե�һ��Ŷ...", "����Ŭ���������ing...",
						true);
				myDialog.getWindow().setGravity(Gravity.CENTER); //������ʾ�������ݶԻ���
				myDialog.setCancelable(true);
				new Thread() {
					public void run() {
						try {
							Thread.sleep(3000);//ģ����أ�ͣ��1.5��
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
			textView.setText("����������");
			if(myDialog.isShowing()){
				myDialog.dismiss();
			}
		}};


}
