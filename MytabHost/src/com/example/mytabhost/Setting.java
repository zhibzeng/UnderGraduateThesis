package com.example.mytabhost;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class Setting extends Activity {
	
	private PopupWindow popupWindow;
	private Button aboutBt  = null;
	private Button helpBt   = null;
	private Button chearBt  = null;
	private Button exit     = null;
	private Button update   = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		initclick();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void initclick(){
		//update Button
				update = (Button)findViewById(R.id.update);
				update.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent=new Intent();
						intent.setClass(Setting.this, CheckDialog.class);
						Setting.this.startActivity(intent);
						}  
				});
		//chear Button
		chearBt = (Button)findViewById(R.id.chear);
		chearBt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(Setting.this, ClearDialog.class);
				Setting.this.startActivity(intent);
				}  
		});
		//about Button
		aboutBt = (Button)findViewById(R.id.about);
		aboutBt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(popupWindow!=null&&popupWindow.isShowing())   
                    popupWindow.dismiss();  
                else   
                    initPopuWindow(1);    
				}  
		});
		
	  //help Button
		helpBt = (Button)findViewById(R.id.help);
		helpBt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(popupWindow!=null&&popupWindow.isShowing())   
                    popupWindow.dismiss();  
                else   
                    initPopuWindow(2);    
				}  
		});
		
	// exit Button
		exit = (Button)findViewById(R.id.Settingexit);
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ExitApplication.getInstance().exit();			
			}
		});
		
	}
	
	//setting --popupWindow
	public void initPopuWindow(int which){
		
		 int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth(); 
	     int screenHeight = this.getWindowManager().getDefaultDisplay().getHeight(); 
	     
		 View view = LayoutInflater.from(Setting.this).inflate(R.layout.popu_item, null);
		 TextView Tv = (TextView)view.findViewById(R.id.popuText);
		 
		 switch(which){
			 case 1:
				 Tv.setText("�������ǣ�"+"\n"+"��Ʒ�����Ͻ�ͨ��ѧ���ù���ѧԺ���ܽ�ͨ��������Ϣϵͳʵ����"+"\n������Դ���ɶ��й�������");
				 break;
			 
			 case 2:
				 Tv.setText("������");
				 break;
			
		 }
		 
		 
		 popupWindow = new PopupWindow(view, screenWidth-80, screenHeight/2-20);
		 view.setBackgroundResource(R.drawable.popupwindow);
		 /***  ��2�����Ҫ   ***/
		 ColorDrawable cd = new ColorDrawable(-0000);  
		 popupWindow.setBackgroundDrawable(cd);  
		 
		 popupWindow.showAsDropDown(chearBt);
		 popupWindow.setFocusable(true);   
		 popupWindow.setOutsideTouchable(true);//�����ⲿ�ܵ��
		 popupWindow.showAtLocation(findViewById(R.id.settintId), Gravity.CENTER, 0, 0); 
		 popupWindow.update();
		 
		//setting popupWindow d�����ʧ
		 popupWindow.setTouchInterceptor(new View.OnTouchListener() {  
	         public boolean onTouch(View v, MotionEvent event) {  
	             /****   ��������popupwindow���ⲿ��popupwindowҲ����ʧ ****/  
	             if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {  
	            	 popupWindow.dismiss();  
	                 return true;   
	             }  
	             return false;  
	         }  
		 });
	}

}
