package com.example.mytabhost;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class TrafficControl extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.traffic_control);
		ImageView traffic_pic = (ImageView) findViewById(R.id.traffic_control_pic);
		Calendar c = Calendar.getInstance();
		int day0fweek = c.get(Calendar.DAY_OF_WEEK);
		switch (day0fweek) {
		case 2:
			traffic_pic.setImageResource(R.drawable.traffic_c16);
			break;
		case 3:
			traffic_pic.setImageResource(R.drawable.traffic_c27);
			break;
		case 4:
			traffic_pic.setImageResource(R.drawable.traffic_c38);
			break;
		case 5:
			traffic_pic.setImageResource(R.drawable.traffic_c49);
			
			break;
		case 6:
			traffic_pic.setImageResource(R.drawable.traffic_c50);
			break;
		default:
			traffic_pic.setImageResource(R.drawable.traffic_c16);
			break;
		}
		

	}
}
