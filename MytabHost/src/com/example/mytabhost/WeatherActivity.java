package com.example.mytabhost;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.example.mytabhost.entity.WeatherEntity;
import com.example.mytabhost.map.LocationOverlay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class WeatherActivity extends Activity {
	
	private TextView todayDate,todayTemperature,todayWeather,todayWind;
	private TextView oneDate,oneTemperature,oneWeather,oneWind;
	private TextView twoDate,twoTemperature,twoWeather,twoWind;
	private TextView threeDate,threeTemperature,threeWeather,threeWind;
	private ProgressDialog myDialog;// 声明ProgressDialog类型变量
	private List<WeatherEntity> lists = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//full screen and no title 
		final Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather);
		
		todayDate = (TextView) findViewById(R.id.weather_today_date);
		todayTemperature = (TextView) findViewById(R.id.weather_today_temperature);
		todayWeather = (TextView)findViewById(R.id.weather_today_weather);
		todayWind = (TextView) findViewById(R.id.weather_today_wind);
		
		oneDate = (TextView)findViewById(R.id.weather_one_date);
		oneTemperature = (TextView)findViewById(R.id.weather_one_temperature);
		oneWeather = (TextView)findViewById(R.id.weather_one_weather);
		oneWind = (TextView)findViewById(R.id.weather_one_wind);
		
		twoDate = (TextView)findViewById(R.id.weather_two_date);
		twoTemperature = (TextView)findViewById(R.id.weather_two_temperature);
		twoWeather = (TextView)findViewById(R.id.weather_two_weather);
		twoWind = (TextView)findViewById(R.id.weather_two_wind);
		
		threeDate = (TextView)findViewById(R.id.weather_three_date);
		threeTemperature = (TextView)findViewById(R.id.weather_three_temperature);
		threeWeather = (TextView)findViewById(R.id.weather_three_weather);
		threeWind = (TextView)findViewById(R.id.weather_three_wind);
		
		initData();

	}
	
	public void initData(){
		
		myDialog = ProgressDialog.show(WeatherActivity.this, "请稍等一会哦...", "正在努力加载地图数据...",
				true);
		myDialog.getWindow().setGravity(Gravity.CENTER); //居中显示加载数据对话框
		myDialog.setCancelable(true);
		new Thread() {
			public void run() {
				try {
					TransferData transferData = new TransferData();
					try {
						lists = transferData.getWeather();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						WeatherActivity.this.finish();
					}
					Thread.sleep(1500);//模拟加载，停留1.5秒
					Message m = new Message();
					listHandler.sendMessage(m);
				} catch (Exception e) {
					e.printStackTrace();
					WeatherActivity.this.finish();
				}
			}
		}.start();
	}
	
	
	
	final Handler listHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(lists != null){
				WeatherEntity entity1 = lists.get(0);
				todayDate.setText(entity1.getDate());
				todayTemperature.setText(entity1.getTemperature());
				todayWeather.setText(entity1.getWeather());
				todayWind.setText(entity1.getWind());
				
				WeatherEntity entity2 = lists.get(1);
				oneDate.setText(entity2.getDate());
				oneTemperature.setText(entity2.getTemperature());
				oneWeather.setText(entity2.getWeather());
				oneWind.setText(entity2.getWind());
				
				WeatherEntity entity3 = lists.get(2);
				twoDate.setText(entity3.getDate());
				twoTemperature.setText(entity3.getTemperature());
				twoWeather.setText(entity3.getWeather());
				twoWind.setText(entity3.getWind());
				
				WeatherEntity entity4 = lists.get(3);
				threeDate.setText(entity4.getDate());
				threeTemperature.setText(entity4.getTemperature());
				threeWeather.setText(entity4.getWeather());
				threeWind.setText(entity4.getWind());
			}
			
			if(myDialog.isShowing()){
				myDialog.dismiss();
			}
		}
		
	};
	
}
