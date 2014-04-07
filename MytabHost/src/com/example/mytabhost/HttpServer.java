package com.example.mytabhost;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HttpServer {
	public static final String BASE_PATH="http://192.168.1.102:8000/";

	public void HttpServer(){}

	/**
	 * 获取交通公告信息
	 * @param type
	 * @return
	 */
	public static String getNotification(Integer type)
	{
		String urlgetcategory = BASE_PATH+"api/notificationlist/?type="+type; 
		String result=null;
		HttpGet request = new HttpGet(urlgetcategory);
		try 
		{
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
			}else{
				return null;
			}
		} 
		catch (Exception e)
		{
			result=null;
		}finally{
			return result;	
		}
	}
	
	public static String getRoadConditions(){
		String urlgetcategory = BASE_PATH+"api/roadConditionlist/";
		String result=null;
		HttpGet request = new HttpGet(urlgetcategory);
		try 
		{
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
			}else{
				return null;
			}
		} 
		catch (Exception e)
		{
			result=null;
		}finally{
			return result;	
		}
	}
	
	/**
	 * 获取随手拍路况信息
	 * @return
	 */
	public static String getTrafficPics(){
		String url = BASE_PATH+"api/piclist/";
		String result=null;
		HttpGet request = new HttpGet(url);
		try 
		{
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
			}else{
				return null;
			}
		} 
		catch (Exception e)
		{
			result=null;
		}finally{
			return result;	
		}
		
	}
	
	
	/**
	 * 获取随手拍分享图片
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static Bitmap getBitmap(String url) throws IOException{
		URL realUrl = new URL(BASE_PATH+"media/"+url);
		Bitmap image = BitmapFactory.decodeStream(realUrl.openConnection().getInputStream());
		return image;
	}
	
	
	/**
	 * 获取天气信息
	 * @return
	 */
	public static String getWeather(){
		String url = "http://api.map.baidu.com/telematics/v3/weather?location=成都&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ";
		String result=null;
		HttpGet request = new HttpGet(url);
		try 
		{
			HttpResponse response = new DefaultHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
				Log.d("waether results", result);  
				
			}else{
				return null;
			}
		} 
		catch (Exception e)
		{
			result=null;
		}finally{
			return result;	
		}
	}
	

}
