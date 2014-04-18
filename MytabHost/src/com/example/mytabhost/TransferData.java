package com.example.mytabhost;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.mytabhost.entity.CrossingEntity;
import com.example.mytabhost.entity.NotificationEntity;
import com.example.mytabhost.entity.RoadConditionEntity;
import com.example.mytabhost.entity.SelfGeneratedTrafficPicEntity;
import com.example.mytabhost.entity.WeatherEntity;
import com.example.mytabhost.util.ReadFile;
public class TransferData {
	
	
	/**
	 * 获取交通公告信息列表
	 * @param type
	 * @return
	 * @throws JSONException 
	 */
	public List<NotificationEntity> getNotificationList(Integer type) throws JSONException{
		List<NotificationEntity> list = new ArrayList<NotificationEntity>();
		HttpServer httpServer = new HttpServer();
		String result = HttpServer.getNotification(type);
		if(result!=null){
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("rows");
			for(int i=0;i<jsonArray.length();i++)
			{	NotificationEntity notificationEntity = new NotificationEntity();
				JSONObject temp=jsonArray.getJSONObject(i);
				notificationEntity.setId(1);
				notificationEntity.setType(temp.getInt("type"));
				notificationEntity.setDate(temp.getString("date"));
				notificationEntity.setTitle(temp.getString("title"));
				notificationEntity.setContent(temp.getString("content"));
				list.add(notificationEntity);
			}
			return list;
			
		}else{
			return null;
		}
		
	}
	
	public List<RoadConditionEntity> getRoadConditionList()throws JSONException{
		List<RoadConditionEntity> list = new ArrayList<RoadConditionEntity>();
		HttpServer httpServer = new HttpServer();
		String result = HttpServer.getRoadConditions();
		if(result!=null){
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("info");
			for(int i=0;i<jsonArray.length();i++)
			{	RoadConditionEntity entity = new RoadConditionEntity();
				JSONObject temp=jsonArray.getJSONObject(i);
				entity.setContent(temp.getString("content"));
				entity.setDatetime(temp.getString("datetime").replace("+00:00",""));
				list.add(entity);
			}
			return list;
			
		}else{
			return null;
		}		
	}
	
	/**
	 * 获取随手拍信息列表
	 * @return
	 * @throws JSONException
	 */
	public List<SelfGeneratedTrafficPicEntity> getTrafficPics() throws JSONException{
		List<SelfGeneratedTrafficPicEntity> list = new ArrayList<SelfGeneratedTrafficPicEntity>();
		HttpServer httpServer = new HttpServer();
		String result = HttpServer.getTrafficPics();
		if(result!=null){
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("info");
			for(int i=0;i<jsonArray.length();i++)
			{	SelfGeneratedTrafficPicEntity entity = new SelfGeneratedTrafficPicEntity();
				JSONObject temp=jsonArray.getJSONObject(i);
				entity.setLatitude(temp.getString("latitude"));
				entity.setNotes(temp.getString("notes"));
				entity.setPicPath(temp.getString("pic"));
				entity.setLongitude(temp.getString("longitude"));
				entity.setDatetime(temp.getString("datetime"));
				list.add(entity);
			}
			return list;
			
		}else{
			return null;
		}
		
	}
	
	/**
	 * 获取天气信息
	 * @return
	 * @throws JSONException
	 */
	public List<WeatherEntity> getWeather()throws JSONException{
		List<WeatherEntity> list = new ArrayList<WeatherEntity>();
		HttpServer httpServer = new HttpServer();
		String result = HttpServer.getWeather();
		if(result!=null){
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("results");
			for(int i=0;i<jsonArray.length();i++){
				JSONObject temp=jsonArray.getJSONObject(i);
				JSONArray jarray = temp.getJSONArray("weather_data");
				for(int j=0;j<jarray.length();j++){
					WeatherEntity entity = new WeatherEntity();
					JSONObject t=jarray.getJSONObject(j);
					entity.setDate(t.getString("date"));
					entity.setDayPictureUrl(t.getString("dayPictureUrl"));
					entity.setNightPictureUrl(t.getString("nightPictureUrl"));
					entity.setWeather(t.getString("weather"));
					entity.setWind(t.getString("wind"));
					entity.setTemperature(t.getString("temperature"));
					list.add(entity);
				}
				
			}
			return list;
			
		}else{
			return null;
		}
		
	}
	
	public List<CrossingEntity> getCrossingLists(Context context,int type) throws JSONException, IOException{
		List<CrossingEntity> list = new ArrayList<CrossingEntity>();
		String result = null;
		InputStream is = null;
		if(type==0){
			is = context.getResources().openRawResource(R.raw.innerround);
		}else{
			is = context.getResources().openRawResource(R.raw.outround);
		}
		result = ReadFile.convertStreamToString(is);
		if(result!=null){
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("rows");
			for(int i=0;i<jsonArray.length();i++)
			{	CrossingEntity entity = new CrossingEntity();
				JSONObject temp=jsonArray.getJSONObject(i);
				entity.setName(temp.getString("name"));
				entity.setLongitude(temp.getString("longitude"));
				entity.setLatitude(temp.getString("latitude"));
				entity.setId(temp.getInt("id"));
				entity.setType(temp.getInt("type"));
				entity.setLabelid(temp.getInt("labelid"));
				list.add(entity);
			}
			return list;
			
		}else{
			return null;
		}
	}
	
	
	/**
	 * 获取路口照片
	 */
	public Bitmap getCrossingBitmap(Context context,int type,int crossingid){
		//这里可以从网上获取图片
		//不同类型路口配置不同图片
		int CROSSING_PIC[] = {
			R.drawable.k2_1,R.drawable.k2_2,R.drawable.k2_3,
			R.drawable.k2_4,R.drawable.k2_5,R.drawable.k2_6,
			R.drawable.k2_7,R.drawable.k2_8,R.drawable.k2_9,
			R.drawable.k2_10,R.drawable.k2_11,R.drawable.k2_12,
			R.drawable.k2_13,R.drawable.k2_14,R.drawable.k2_15,
			R.drawable.k2_16,R.drawable.k2_17,R.drawable.k2_18,
			R.drawable.k2_19,R.drawable.k2_20,R.drawable.k2_21,
			R.drawable.k2_22,R.drawable.k2_23,R.drawable.k2_24,
			R.drawable.k2_25,R.drawable.k2_26,R.drawable.k2_27,
			R.drawable.k2_28,R.drawable.k2_29,R.drawable.k2_30,
			};
		Resources r = context.getResources();
		Bitmap bitmap = null;
		if(type==1){
			bitmap = BitmapFactory.decodeResource(context.getResources(),CROSSING_PIC[crossingid-1]);
		}else{
			bitmap = BitmapFactory.decodeResource(context.getResources(),CROSSING_PIC[crossingid-31]);
		}
		return bitmap;
	}

}
