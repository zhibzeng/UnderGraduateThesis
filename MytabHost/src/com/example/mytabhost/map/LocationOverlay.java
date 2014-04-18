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
	
	private LocationListener mLocationListener = null;//onResumeʱע���listener��onPauseʱ��ҪRemove
	private MyLocationOverlay mLocationOverlay = null;	//��λͼ��
	static View mPopView = null;	// ���markʱ����������View
	static MapView mMapView = null;
	private  GeoPoint locationPoint= null;
	//�洢������ͼƬ����
	private List<SelfGeneratedTrafficPicEntity> trafficPicLists = 
			new ArrayList<SelfGeneratedTrafficPicEntity>();
	private ProgressDialog myDialog;// ����ProgressDialog���ͱ���
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.mapviewdemo);
        
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
		}
		app.mBMapMan.start();
        // ���ʹ�õ�ͼSDK�����ʼ����ͼActivity
        super.initMapActivity(app.mBMapMan);
        
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.setBuiltInZoomControls(true);
        //���������Ŷ���������Ҳ��ʾoverlay,Ĭ��Ϊ������
        mMapView.setDrawOverlayWhenZooming(true);
        
        // ��Ӷ�λͼ��
        mLocationOverlay = new MyLocationOverlay(this, mMapView);
		mMapView.getOverlays().add(mLocationOverlay);
		
        // ע�ᶨλ�¼�
        mLocationListener = new LocationListener(){
			@Override
			public void onLocationChanged(Location location) {
				if (location != null){
					GeoPoint pt = new GeoPoint((int)(location.getLatitude()*1e6),
							(int)(location.getLongitude()*1e6));
					mMapView.getController().animateTo(pt);
					mMapView.getController().setCenter(pt);//���õ�ͼ���ĵ�
					mMapView.getController().setZoom(10);//���õ�ͼzoom����
					mMapView.setTraffic(false);//��ʾ��ͨ���
				}
			}
        };
        
        initPopView();
        initPoints();
}
	
	
	/**
	 * ��ʼ������ĵ�ͼ����
	 */
	private void initPoints(){
		myDialog = ProgressDialog.show(LocationOverlay.this, "���Ե�һ��Ŷ...", "����Ŭ�����ص�ͼ����...",
				true);
		myDialog.getWindow().setGravity(Gravity.CENTER); //������ʾ�������ݶԻ���
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
					Thread.sleep(1500);//ģ����أ�ͣ��1.5��
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
	 * ˢ������Hander
	 */
	final Handler listHandler = new Handler() {
		public void handleMessage(Message msg) {
	        if(trafficPicLists!=null){
	        	if(trafficPicLists.size()>0){
	        		myDialog = ProgressDialog.show(LocationOverlay.this, "���Ե�һ��Ŷ...", "����Ŭ������ͼƬ����...",
	        				true);
	        		myDialog.getWindow().setGravity(Gravity.CENTER); //������ʾ�������ݶԻ���
	        		myDialog.setCancelable(true);
	        		new Thread() {
	        			public void run() {
				            // ���ItemizedOverlay
				            // Read your drawable from somewhere
				            Drawable dr = getResources().getDrawable(R.drawable.map_marker_1);
				            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
				            // Scale it to 50 x 50
				            Drawable marker = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));
				            // Set your new, scaled drawable "marker"
				            //�����¹ʵ��б�
				            List<OverlayItem>  overItemList = new ArrayList<OverlayItem>();
				            // �ø����ľ�γ�ȹ���GeoPoint����λ��΢�� (�� * 1E6)
				            for(int j=0;j<trafficPicLists.size();j++){
				            	SelfGeneratedTrafficPicEntity temp = trafficPicLists.get(j);
				            	GeoPoint tpoint =  new GeoPoint((int) ( Double.parseDouble(temp.getLatitude()) * 1E6), 
				            				(int) ( Double.parseDouble(temp.getLongitude()) * 1E6));
				            	overItemList.add(new OverlayItem(tpoint,temp.getPicPath(),temp.getDatetime()+"\n"+temp.getNotes()));		            	
				            }
				            mMapView.getOverlays().add(new OverItemT(marker, LocationOverlay.this,overItemList)); //���ItemizedOverlayʵ����mMapView
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
	 * ���mark��������
	 */
	public void initPopView(){
		   // �������markʱ�ĵ�������
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
        mLocationOverlay.disableCompass(); // �ر�ָ����
		app.mBMapMan.stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		// ע�ᶨλ�¼�����λ�󽫵�ͼ�ƶ�����λ��
        app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableCompass(); // ��ָ����
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
	 * @param pt �¹�λ��
	 * @param title �¹ʼ�Ҫ����
	 * @param text �¹�����
	 */
	public OverItemT(Drawable marker, Context context,List<OverlayItem> plists) {
		super(boundCenterBottom(marker));
		this.marker = marker;
		this.mContext = context;
		this.mGeoList = plists;
		// ����OverlayItem��������������Ϊ��item��λ�ã������ı�������Ƭ��
		// �����ı�������Ƭ�ηֱ�ͨ�� overLayItem.getTitle() overLayItem.getSnippet()����
		//mGeoList.add(new OverlayItem(mGeoPoint,mTitle,mText));	
		populate();  //createItem(int)��������item��һ���������ݣ��ڵ�����������ǰ�����ȵ����������
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		// Projection�ӿ�������Ļ��������;�γ������֮��ı任
		Projection projection = mapView.getProjection(); 
		for (int index = size() - 1; index >= 0; index--) { // ����mGeoList
			OverlayItem overLayItem = getItem(index); // �õ�����������item
			//String title = overLayItem.getTitle();
			String title = "";
			// �Ѿ�γ�ȱ任�������MapView���Ͻǵ���Ļ��������
			Point point = projection.toPixels(overLayItem.getPoint(), null); 
			// ���ڴ˴�������Ļ��ƴ���
			Paint paintText = new Paint();
			paintText.setColor(Color.BLUE);
			paintText.setTextSize(15);
			canvas.drawText(title, point.x-30, point.y, paintText); // �����ı�
		}

		super.draw(canvas, mapView, shadow);
		//����һ��drawable�߽磬ʹ�ã�0��0�������drawable�ײ����һ�����ĵ�һ������
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
	// ��������¼�
	protected boolean onTap(int i) {
		setFocus(mGeoList.get(i));
		// ��������λ��,��ʹ֮��ʾ
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
		// ��ȥ����������
		LocationOverlay.mPopView.setVisibility(View.GONE);
		return super.onTap(arg0, arg1);
	}
}
