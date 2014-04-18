package com.example.mytabhost.map;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;
import com.example.mytabhost.R;
import com.example.mytabhost.HttpServer;
import com.example.mytabhost.NotificationActivity;
import com.example.mytabhost.TransferData;
import com.example.mytabhost.entity.SelfGeneratedTrafficPicEntity;

public class LocationOverlay extends MapActivity {
	
	private LocationListener mLocationListener = null;//onResume时注册此listener，onPause时需要Remove
	private MyLocationOverlay mLocationOverlay = null;	//定位图层
	static View mPopView = null;	// 点击mark时弹出的气泡View
	static MapView mMapView = null;
	private  GeoPoint locationPoint= null;
	//存储随手拍图片内容
	private List<SelfGeneratedTrafficPicEntity> trafficPicLists = 
			new ArrayList<SelfGeneratedTrafficPicEntity>();
	private ProgressDialog myDialog;// 声明ProgressDialog类型变量
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.mapviewdemo);
        
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
		}
		app.mBMapMan.start();
        // 如果使用地图SDK，请初始化地图Activity
        super.initMapActivity(app.mBMapMan);
        
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.setBuiltInZoomControls(true);
        //设置在缩放动画过程中也显示overlay,默认为不绘制
        mMapView.setDrawOverlayWhenZooming(true);
        
        // 添加定位图层
        mLocationOverlay = new MyLocationOverlay(this, mMapView);
		mMapView.getOverlays().add(mLocationOverlay);
		
        // 注册定位事件
        mLocationListener = new LocationListener(){
			@Override
			public void onLocationChanged(Location location) {
				if (location != null){
					GeoPoint pt = new GeoPoint((int)(location.getLatitude()*1e6),
							(int)(location.getLongitude()*1e6));
					mMapView.getController().animateTo(pt);
					mMapView.getController().setCenter(pt);//设置地图中心点
					mMapView.getController().setZoom(10);//设置地图zoom级别
					mMapView.setTraffic(false);//显示交通情况
				}
			}
        };
        
        initPopView();
        initPoints();
}
	
	
	/**
	 * 初始化随后拍地图数据
	 */
	private void initPoints(){
		myDialog = ProgressDialog.show(LocationOverlay.this, "请稍等一会哦...", "正在努力加载地图数据...",
				true);
		myDialog.getWindow().setGravity(Gravity.CENTER); //居中显示加载数据对话框
		myDialog.setCancelable(true);
		new Thread() {
			public void run() {
				try {
					TransferData transferData = new TransferData();
					try {
						trafficPicLists = transferData.getTrafficPics();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						LocationOverlay.this.finish();
					}
					Thread.sleep(1500);//模拟加载，停留1.5秒
					Message m = new Message();
					listHandler.sendMessage(m);
				} catch (Exception e) {
					e.printStackTrace();
					LocationOverlay.this.finish();
				} finally {
					myDialog.dismiss();
				}
			}
		}.start();
		
	}
	
	
	/**
	 * 刷新数据Hander
	 */
	final Handler listHandler = new Handler() {
		public void handleMessage(Message msg) {
	        if(trafficPicLists!=null){
	        	if(trafficPicLists.size()>0){
	        		myDialog = ProgressDialog.show(LocationOverlay.this, "请稍等一会哦...", "正在努力加载图片数据...",
	        				true);
	        		myDialog.getWindow().setGravity(Gravity.CENTER); //居中显示加载数据对话框
	        		myDialog.setCancelable(true);
	        		new Thread() {
	        			public void run() {
				            // 添加ItemizedOverlay
				            // Read your drawable from somewhere
				            Drawable dr = getResources().getDrawable(R.drawable.map_marker_1);
				            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
				            // Scale it to 50 x 50
				            Drawable marker = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));
				            // Set your new, scaled drawable "marker"
				            //设置事故点列表
				            List<OverlayItem>  overItemList = new ArrayList<OverlayItem>();
				            // 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
				            for(int j=0;j<trafficPicLists.size();j++){
				            	SelfGeneratedTrafficPicEntity temp = trafficPicLists.get(j);
				            	GeoPoint tpoint =  new GeoPoint((int) ( Double.parseDouble(temp.getLatitude()) * 1E6), 
				            				(int) ( Double.parseDouble(temp.getLongitude()) * 1E6));
				            	overItemList.add(new OverlayItem(tpoint,temp.getPicPath(),temp.getDatetime()+"\n"+temp.getNotes()));		            	
				            }
				            mMapView.getOverlays().add(new OverItemT(marker, LocationOverlay.this,overItemList)); //添加ItemizedOverlay实例到mMapView
				            Message m = new Message();
				            mapHandler.sendMessage(m);
	        			}	
	        		}.start();
	        	}
	        	
	        }
	        else {
				Log.i("TAG", "trafficPicLists has no entities");
			}   
		}
	};
	
	final Handler mapHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(myDialog.isShowing()){
				myDialog.dismiss();
			}
		}
		
	};
	
	
	
	/**
	 * 点击mark弹出布局
	 */
	public void initPopView(){
		   // 创建点击mark时的弹出泡泡
	  	mPopView=super.getLayoutInflater().inflate(R.layout.maker_popview, null);
	  	mMapView.addView( mPopView,new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
	                 null, MapView.LayoutParams.BOTTOM_CENTER));
	  	mPopView.setVisibility(View.GONE);
	}
	
	@Override
	protected void onPause() {
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		mLocationOverlay.disableMyLocation();
        mLocationOverlay.disableCompass(); // 关闭指南针
		app.mBMapMan.stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		// 注册定位事件，定位后将地图移动到定位点
        app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableCompass(); // 打开指南针
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}










//Itemized Class
class OverItemT extends ItemizedOverlay<OverlayItem> {

	private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	private Drawable marker;
	private Context mContext;
	/**
	 * @param marker 
	 * @param context 
	 * @param pt 事故位置
	 * @param title 事故简要描述
	 * @param text 事故详情
	 */
	public OverItemT(Drawable marker, Context context,List<OverlayItem> plists) {
		super(boundCenterBottom(marker));
		this.marker = marker;
		this.mContext = context;
		this.mGeoList = plists;
		// 构造OverlayItem的三个参数依次为：item的位置，标题文本，文字片段
		// 标题文本和文字片段分别通过 overLayItem.getTitle() overLayItem.getSnippet()调用
		//mGeoList.add(new OverlayItem(mGeoPoint,mTitle,mText));	
		populate();  //createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		// Projection接口用于屏幕像素坐标和经纬度坐标之间的变换
		Projection projection = mapView.getProjection(); 
		for (int index = size() - 1; index >= 0; index--) { // 遍历mGeoList
			OverlayItem overLayItem = getItem(index); // 得到给定索引的item
			//String title = overLayItem.getTitle();
			String title = "";
			// 把经纬度变换到相对于MapView左上角的屏幕像素坐标
			Point point = projection.toPixels(overLayItem.getPoint(), null); 
			// 可在此处添加您的绘制代码
			Paint paintText = new Paint();
			paintText.setColor(Color.BLUE);
			paintText.setTextSize(15);
			canvas.drawText(title, point.x-30, point.y, paintText); // 绘制文本
		}

		super.draw(canvas, mapView, shadow);
		//调整一个drawable边界，使得（0，0）是这个drawable底部最后一行中心的一个像素
		boundCenterBottom(marker);
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mGeoList.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mGeoList.size();
	}
	@Override
	// 处理当点击事件
	protected boolean onTap(int i) {
		setFocus(mGeoList.get(i));
		// 更新气泡位置,并使之显示
		GeoPoint pt = mGeoList.get(i).getPoint();
		
		TextView date = (TextView) LocationOverlay.mMapView.findViewById(R.id.marker_pop_date);
		date.setText(mGeoList.get(i).getSnippet());
		ImageView pop_image = (ImageView) LocationOverlay.mMapView.findViewById(R.id.marker_pop_image);
		try {
			Bitmap imageBitmap = HttpServer.getBitmap(mGeoList.get(i).getTitle());
			if(imageBitmap!=null){
				pop_image.setImageBitmap(imageBitmap);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LocationOverlay.mMapView.updateViewLayout( LocationOverlay.mPopView,
                new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                		pt, MapView.LayoutParams.BOTTOM_CENTER));
		LocationOverlay.mPopView.setVisibility(View.VISIBLE);
//		Toast.makeText(this.mContext, mGeoList.get(i).getSnippet(),
//				Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		// TODO Auto-generated method stub
		// 消去弹出的气泡
		LocationOverlay.mPopView.setVisibility(View.GONE);
		return super.onTap(arg0, arg1);
	}
}
