package com.example.mytabhost;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class welcome extends Activity{
	public static int count=0;
	public void onCreate(Bundle savedInstanceState) {
		ExitApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		//full screen and no title 
		final Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		
		 SharedPreferences preferences = getSharedPreferences("data", 0);

         count = preferences.getInt("count", 0);
        
		new Handler().postDelayed(new Runnable() {			
			@Override
			
			public void run() {
				Intent intent = new Intent();
				// TODO Auto-generated method stub\
				if (count!=0) {             
	                 intent.setClass(welcome.this, MainActivity.class);
	         } else {
	                 Editor sharedata = getSharedPreferences("data", 0).edit();
	                 sharedata.putInt("count", 1);
	                 sharedata.commit();
	                 intent.setClass(welcome.this, SwitchViewDemoActivity.class);
	         }
				welcome.this.startActivity(intent);
				welcome.this.finish();		
			}
		}, 2000);	
	}
}
