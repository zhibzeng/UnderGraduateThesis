package com.example.mytabhost;


import com.example.mytabhost.camera.CramerProActivity;
import com.example.mytabhost.map.LocationOverlay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AppCollecions extends Activity{
	private TextView app_one,app_two,app_three,app_four,app_five,app_six;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_collection);

        //define the textview
        app_one = (TextView)findViewById(R.id.app_collection_one);
        app_one.setOnClickListener(appClickListener);
        app_two = (TextView)findViewById(R.id.app_collection_two);
        app_two.setOnClickListener(appClickListener);
        app_three = (TextView)findViewById(R.id.app_collection_three);
        app_three.setOnClickListener(appClickListener);
        app_four = (TextView)findViewById(R.id.app_collection_four);
        app_four.setOnClickListener(appClickListener);
        app_five = (TextView)findViewById(R.id.app_collection_five);
        app_five.setOnClickListener(appClickListener);
        app_six = (TextView)findViewById(R.id.app_collection_six);
        app_six.setOnClickListener(appClickListener);
    }
    
    public OnClickListener appClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.app_collection_one:
				Intent intent1 = new Intent(AppCollecions.this, RoadConditions.class);
				startActivity(intent1);
				break;
			case R.id.app_collection_two:
				Intent intent2 = new Intent(AppCollecions.this, LocationOverlay.class);
				startActivity(intent2);
				break;
			case R.id.app_collection_three:
				Intent intent3 = new Intent(AppCollecions.this, TrafficControl.class);
				startActivity(intent3);
				break;
			case R.id.app_collection_four:
				Intent intent4 = new Intent(AppCollecions.this, CramerProActivity.class);
				startActivity(intent4);
				break;
			case R.id.app_collection_five:
				Intent intent5 = new Intent(AppCollecions.this, WeatherActivity.class);
				startActivity(intent5);
				break;
			case R.id.app_collection_six:
				Intent intent6 = new Intent(AppCollecions.this, OilPrice.class);
				startActivity(intent6);
				break;
			default:
				break;
			}
			
		}
	};

}
