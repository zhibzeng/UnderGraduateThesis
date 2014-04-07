package com.example.mytabhost;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

}
