package com.example.mytabhost;


import com.example.mytabhost.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class OilPrice extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.oilprice);
		

	}

}