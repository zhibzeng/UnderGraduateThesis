package com.example.mytabhost.camera;

import java.util.HashMap;
import java.util.Map;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;
import com.example.mytabhost.HttpServer;
import com.example.mytabhost.R;
import com.example.mytabhost.util.CompressImage;
import com.example.mytabhost.util.UploadUtil;
import com.example.mytabhost.util.UploadUtil.OnUploadProcessListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mytabhost.map.BMapApiDemoApp;


//主要用于选择文件和上传文件操作

public class CramerProActivity extends Activity implements OnUploadProcessListener{
	private static final String TAG = "uploadImage";
	//去上传文件
	protected static final int TO_UPLOAD_FILE = 1;  
	//上传文件响应
	protected static final int UPLOAD_FILE_DONE = 2;  
	//选择文件
	public static final int TO_SELECT_PHOTO = 3;
	//上传初始化
	private static final int UPLOAD_INIT_PROCESS = 4;
	//上传中
	private static final int UPLOAD_IN_PROCESS = 5;
	//请求服务器uri 
	
	private static String requestURL = HttpServer.BASE_PATH+"upload/image/";
	
	//private static String requestURL = "http://10.0.0.147:8888/MyTest/p/file!upload";
	private Button selectButton,uploadButton,back;
	private ImageView imageView;
	private TextView uploadImageResult;
	static TextView txt;
	private ProgressBar progressBar;
	private ImageButton cramer;
	public static String picPath = null;
	private ProgressDialog progressDialog;
	private String longitude = "";
	private String latitude = "";
	//地图定位
	LocationListener mLocationListener = null;//create时注册此listener，Destroy时需要Remove
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_pro);
        
        uploadImageResult = (TextView) findViewById(R.id.uploadImageResult);
        txt=(TextView) findViewById(R.id.txt1);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        selectButton = (Button) findViewById(R.id.selectImage);
        uploadButton = (Button) findViewById(R.id.uploadImage);
        imageView = (ImageView) findViewById(R.id.imageView);
        cramer=(ImageButton) findViewById(R.id.camera);        
        back=(Button) findViewById(R.id.back);
        back.setOnClickListener(camClickListener);
        cramer.setOnClickListener(camClickListener);
        selectButton.setOnClickListener(camClickListener);
        uploadButton.setOnClickListener(camClickListener);                		
        progressDialog = new ProgressDialog(this);
        
        //地图定位
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
		}
		app.mBMapMan.start();

        // 注册定位事件
        mLocationListener = new LocationListener(){
			@Override
			public void onLocationChanged(Location location) {
				if(location != null){
					String strLog = String.format("您当前的位置:" +
							"纬度:%f\r\n" +
							"经度:%f",
							location.getLongitude(), location.getLatitude());
					longitude = Double.toString(location.getLongitude());
					latitude = Double.toString(location.getLatitude());
					TextView mainText = (TextView)findViewById(R.id.uploadImagelocation);
			        mainText.setText(strLog);
				}
			}
        };
        
        
        
    }
    
    
    public OnClickListener camClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.selectImage:
				Intent intent = new Intent(CramerProActivity.this,SystemCatalogActivity.class);
				startActivity(intent);
				break;
			case R.id.uploadImage:
				if(picPath!=null)
				{
					handler.sendEmptyMessage(TO_UPLOAD_FILE);
				}else{
					Toast.makeText(CramerProActivity.this, "上传的文件路径出错", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.camera:
				Intent intent1 = new Intent(CramerProActivity.this,SelectPicActivity.class);
				startActivityForResult(intent1, TO_SELECT_PHOTO);
				break;
			case R.id.back:
				AlertDialog.Builder builder = new Builder(CramerProActivity.this); 
				 builder.setIcon(android.R.drawable.ic_dialog_info);
			        builder.setMessage("确定要退出?"); 
			        builder.setTitle("提示"); 
			        builder.setPositiveButton("确认", 
			                new android.content.DialogInterface.OnClickListener() { 
			                    public void onClick(DialogInterface dialog, int which) { 
			                        dialog.dismiss(); 
			                        CramerProActivity.this.finish(); 
			                    } 
			                }); 
			        builder.setNegativeButton("取消", 
			                new android.content.DialogInterface.OnClickListener() { 
			                    public void onClick(DialogInterface dialog, int which) { 
			                        dialog.dismiss(); 
			                    } 
			                }); 
			        		builder.create().show();
				//MainActivity.this.finish();
				break;
			}

			
		}
	};
    

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode==Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO){
			//imageView不设null, 第一次上传成功后，第二次在选择上传的时候会报错。
			imageView.setImageBitmap(null);
			picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
			Log.i(TAG, "最终选择的图片="+picPath);
			txt.setText("文件路径"+picPath);
			//Bitmap bm = BitmapFactory.decodeFile(picPath);
			Bitmap bm = CompressImage.getSmallBitmap(picPath);
			imageView.setImageBitmap(bm);
	}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 上传服务器响应回调
	 */
	 
	public void onUploadDone(int responseCode, String message) {
		progressDialog.dismiss();
		Message msg = Message.obtain();
		msg.what = UPLOAD_FILE_DONE;
		msg.arg1 = responseCode;
		msg.obj = message;
		handler.sendMessage(msg);
	}
	
	private void toUploadFile()
	{
		uploadImageResult.setText("正在上传中...");
		progressDialog.setMessage("正在上传文件...");
		
		progressDialog.show();
		String fileKey = "pic";
		UploadUtil uploadUtil = UploadUtil.getInstance();;
		uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		params.put("notes", "this is remark");
		uploadUtil.uploadFile( picPath,fileKey, requestURL,params);
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TO_UPLOAD_FILE:
				toUploadFile();
				break;			
			case UPLOAD_INIT_PROCESS:
				progressBar.setMax(msg.arg1);
				break;
			case UPLOAD_IN_PROCESS:
				progressBar.setProgress(msg.arg1);
				break;
			case UPLOAD_FILE_DONE:
				String result = "响应码："+msg.arg1+"\n响应信息："+msg.obj+"\n耗时："+UploadUtil.getRequestTime()+"秒";
				uploadImageResult.setText(result);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};

	 
	public void onUploadProcess(int uploadSize) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_IN_PROCESS;
		msg.arg1 = uploadSize;
		handler.sendMessage(msg);
	}
	 
	public void initUpload(int fileSize) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_INIT_PROCESS;
		msg.arg1 = fileSize;
		handler.sendMessage(msg );
	}
	
	
	@Override
	protected void onPause() {
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		// 移除listener
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		app.mBMapMan.stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		// 注册Listener
        app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
		app.mBMapMan.start();
		super.onResume();
	}
	
}