package com.example.mytabhost;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytabhost.R;
import com.example.mytabhost.db.DateBaseHelper;
import com.example.mytabhost.entity.CrossingEntity;
import com.example.mytabhost.MyTurnplateView.OnTurnplateListener;
import com.example.mytabhost.MyTurnplateView.Point;
import com.example.mytabhost.TotalRoad.MyTouchListener;
import com.example.mytabhost.TotalRoad.POINT;

@SuppressLint("NewApi")
public class RoadOne extends Activity implements OnTurnplateListener,MyTouchListener {

	private float mPointX = 0, mPointY = 0;
	private float mRadius = 0;
	private int t = 0;
	private float x = 0, y = 0;
	private double w = 1;
	// private MyView myView1;
	private Button btnreturn;// 内外环切换按钮
	private ImageView imageView;
	private ImageView imageView2;
	private Handler handler = null;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	private LinearLayout mClose;
	private LinearLayout mCloseBtn;
	private LinearLayout mCloseBtn2;
	private Button myCloseBtn;
	private TextView windowTextView;
	private View layout;
	private boolean dismiss = false;
	private MyView myView1;
	private FrameLayout frame = null;
	private int height, width;
	private Button total1btn = null;
	private Button total2btn = null;
	private Button icon_represent1 = null;
	private Button icon_represent2 = null;	
	private Button btnsearch = null;
	private AutoCompleteTextView editTextlukou = null;
	private static int FinalFlag = 1;// 标志位，0时为内圈，1时为外圈
	//路口信息列表
	private  List<CrossingEntity> CrossingListInner = null;
	private  List<CrossingEntity> CrossingListOut = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		// int height = getWindowManager().getDefaultDisplay().getHeight();

		height = metric.heightPixels;
		width = metric.widthPixels;
		mPointX = (float) (width + height * 0.86);
		mPointY = height / 2;
		mRadius = (float) height;
		//初始化
		renderView(FinalFlag,(float)(-height*0.86),mPointY,mRadius,0);
	}
	
	/**
	 * 生成布局画面
	 * @param flag
	 * @param px
	 * @param py
	 * @param radius
	 * @param tangleTorun
	 */
	private void renderView(int flag,float px,float py,float radius,float tangleTorun){
		//flag 0 内环 flag 1 外环
		if(frame!=null){
			frame.removeAllViews();	
		}
		MyTurnplateView myView = null;
		try {
			myView = new MyTurnplateView(
					RoadOne.this, px, py, radius,flag, tangleTorun);
			if(flag==0){
				CrossingListInner = myView.CrossingListInner;
			}else{
				CrossingListOut = myView.CrossingListOut;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myView.setOnTurnplateListener(RoadOne.this);
		LayoutInflater inflater = LayoutInflater.from(RoadOne.this);
		View itemView = inflater.inflate(R.layout.layout1, null);// 创建布局文件。
		frame = new FrameLayout(RoadOne.this);
		setContentView(frame);
		myView1 = new MyView(RoadOne.this);
		frame.addView(myView1);
		frame.addView(myView);
		frame.addView(itemView);
		btnreturn = (Button) findViewById(R.id.BtnReturn);
		if(flag==0){
			btnreturn.setText("内环");
		}else{
			btnreturn.setText("外环");
		}
		
		btnreturn.setOnClickListener(new BtnClickListener());
		//内环全景
		total1btn = (Button) findViewById(R.id.TotalRoad1);
		//外环全景
		total2btn = (Button) findViewById(R.id.TotalRoad2);
		//内环图例
		icon_represent1 = (Button) findViewById(R.id.icon_represent1);
		//外环图例
		icon_represent2 = (Button) findViewById(R.id.icon_represent2);
		
		total1btn.setOnClickListener(new BtnClickListener());
		icon_represent1.setOnClickListener(new BtnClickListener());
		icon_represent2.setOnClickListener(new BtnClickListener());
		total2btn.setOnClickListener(new BtnClickListener());
		if(flag==0){
			total1btn.setVisibility(View.VISIBLE);
			icon_represent1.setVisibility(View.VISIBLE);
			total2btn.setVisibility(View.GONE);
			icon_represent2.setVisibility(View.GONE);
		
		}else{
			total1btn.setVisibility(View.GONE);
			icon_represent1.setVisibility(View.GONE);
			total2btn.setVisibility(View.VISIBLE);
			icon_represent2.setVisibility(View.VISIBLE);
		}
		
		int ACTFlag = 1;
		if(flag==0){
			ACTFlag = 1;
		}else{
			ACTFlag = 0;
		}
		editTextlukou = (AutoCompleteTextView) findViewById(R.id.acTextView);
		editTextlukou.setText("");
		AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(
				RoadOne.this,R.layout.autoconplete_dropdown_item, null,
				DateBaseHelper.NAME, android.R.id.text1, 1, ACTFlag);
		((AutoCompleteTextView) RoadOne.this
				.findViewById(R.id.acTextView)).setThreshold(1);
		((AutoCompleteTextView) RoadOne.this
				.findViewById(R.id.acTextView))
				.setAdapter(cursorAdapter);
		btnsearch = (Button) findViewById(R.id.BtnSearch);
		btnsearch.setOnClickListener(new BtnClickListener());
		FinalFlag = flag;
	}
	
	
	
/**
 * 单击触发事件
 *	@author JeffreyTseng
 *
 */
	class BtnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (dismiss == true)// 若在点击切换时未关闭pop弹窗
			{
				menuWindow.dismiss();
				dismiss = false;
			}
			if (v == total1btn) {
				frame.removeAllViews();
				final TotalRoad yourview = new TotalRoad(RoadOne.this, width,height);
				yourview.setMyTouchListener(RoadOne.this);
				frame = new FrameLayout(RoadOne.this);
				setContentView(frame);
				frame.addView(yourview);
			}
			if (v == total2btn) {// 外圈实现方法
				// total1btn.setVisibility(View.VISIBLE);
				// total2btn.setVisibility(View.GONE);
				frame.removeAllViews();
				final TotalRoad yourview = new TotalRoad(RoadOne.this,width,height);
				frame = new FrameLayout(RoadOne.this);
				yourview.setMyTouchListener(RoadOne.this);
				setContentView(frame);
				frame.addView(yourview);
			}
			if (v == icon_represent1) {
				inflater = (LayoutInflater) RoadOne.this.getSystemService(LAYOUT_INFLATER_SERVICE);
				// 这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
				// 该方法返回的是一个View的对象，是布局中的根
				layout = inflater.inflate(R.layout.illustrate_pics, null);
				menuWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT); // 后两个参数是width和height
				layout.setBackgroundResource(R.drawable.popupwindow);
				ColorDrawable cd = new ColorDrawable(-0000);
				menuWindow.setBackgroundDrawable(cd);
				menuWindow.setFocusable(true);
				menuWindow.setOutsideTouchable(true);
				menuWindow.showAsDropDown(icon_represent1);
				menuWindow.showAtLocation(RoadOne.this.findViewById(R.id.window),
						Gravity.TOP | Gravity.LEFT, 0, (int) mPointY / 2); // 设置layout在PopupWindow中显示的位置
				menuWindow.update();
				menuWindow.setTouchInterceptor(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						/**** 如果点击了popupwindow的外部，popupwindow也会消失 ****/
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
							menuWindow.dismiss();
						}
						return false;
					}
				});
				
			}
			if (v == icon_represent2) {
				inflater = (LayoutInflater) RoadOne.this.getSystemService(LAYOUT_INFLATER_SERVICE);
				// 这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
				// 该方法返回的是一个View的对象，是布局中的根
				layout = inflater.inflate(R.layout.illustrate_pics, null);
				menuWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT); // 后两个参数是width和height
				layout.setBackgroundResource(R.drawable.popupwindow);
				ColorDrawable cd = new ColorDrawable(-0000);
				menuWindow.setBackgroundDrawable(cd);
				menuWindow.setFocusable(true);
				menuWindow.setOutsideTouchable(true);
				menuWindow.showAsDropDown(icon_represent2);
				menuWindow.showAtLocation(RoadOne.this.findViewById(R.id.window),
						Gravity.TOP | Gravity.LEFT, 0, (int) mPointY / 2); // 设置layout在PopupWindow中显示的位置
				menuWindow.update();
				menuWindow.setTouchInterceptor(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						/**** 如果点击了popupwindow的外部，popupwindow也会消失 ****/
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
							menuWindow.dismiss();
						}
						return false;
					}
				});
			}
			if (v == btnsearch) {// 搜索按钮
				String messlukou = editTextlukou.getText().toString();
				if (messlukou.compareTo("") == 0) {
					Toast.makeText(RoadOne.this, "请输入需要搜索的信息", 1000).show();
				} else {
					if (btnreturn.getText().toString() == "外环") {
						// 如果当前按钮的字符是"外环"
						int tangleTorun = -1;
						for(int i=0;i<CrossingListOut.size();i++){
							CrossingEntity entity = CrossingListOut.get(i);
							if(entity.getName().trim().equals(messlukou.trim())){
								tangleTorun = i;
								Log.d("RoadOne.class tangleTorun", i+"");
								break;
							}
						}
						if(tangleTorun==-1){
							Toast.makeText(RoadOne.this, "请输入需要搜索的信息", 1000)
									.show();
							tangleTorun = 0;
						}
						float tangle = (float) (-(360.0/CrossingListOut.size())*tangleTorun);
						renderView(1,(float)(-height*0.86),mPointY,mRadius,tangle);

					} else {
						// 当前按钮字符是“外环”时
						int tangleTorun = -1;
						for(int i=0;i<CrossingListInner.size();i++){
							CrossingEntity entity = CrossingListInner.get(i);
							if(entity.getName().trim().equals(messlukou.trim())){
								tangleTorun = i;
								Log.d("RoadOne.class tangleTorun", i+"");
								break;
							}
						}
						if(tangleTorun==-1){
							Toast.makeText(RoadOne.this, "请输入需要搜索的信息", 1000)
									.show();
							tangleTorun = 0;
						}
						float tangle= (float) (-(360.0/CrossingListInner.size())*tangleTorun);
						renderView(0,mPointX,mPointY,mRadius,tangle);

					}
				}
				editTextlukou.setText("");
			}
			if (v == btnreturn) {
				// 切换内环/外环 按钮
				if (btnreturn.getText().toString() == "外环") {
					renderView(0,mPointX,mPointY,mRadius,0);
				} else {
					//准备切换成外环
					renderView(1,(float)(-height*0.86),mPointY,mRadius,0);
				}
			}
		}

	}

	
	/**
	 * 点击路口弹出图片
	 */
	@Override
	public void onPointTouch(Point point) {

		final int flag = point.flag;
		final CrossingEntity entity = point.entity;
		Intent intent = new Intent();
		if (!dismiss) {
			// 获取LayoutInflater实例
			dismiss = true;
			inflater = (LayoutInflater) this
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			// 这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
			// 该方法返回的是一个View的对象，是布局中的根
			layout = inflater.inflate(R.layout.window, null);
			imageView = (ImageView) layout.findViewById(R.id.picture);
			imageView2 = (ImageView) layout.findViewById(R.id.picture2);
			windowTextView = (TextView) layout.findViewById(R.id.windowtext);
			TransferData t = new TransferData();
			if (FinalFlag == 0) {
					//内环
					imageView.setImageBitmap(t.getCrossingBitmap(RoadOne.this,FinalFlag, entity.getId()));
					windowTextView.setText(entity.getName());
					imageView2.setImageResource(R.drawable.chukou);
			} else if (FinalFlag == 1) {
					//外环
					imageView.setImageBitmap(t.getCrossingBitmap(RoadOne.this,FinalFlag, entity.getId()));
					windowTextView.setText(entity.getName());
					imageView2.setImageResource(R.drawable.rukou);
			}
			
			// 下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
			menuWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT); // 后两个参数是width和height
			layout.setBackgroundResource(R.drawable.popupwindow);

			ColorDrawable cd = new ColorDrawable(-0000);
			menuWindow.setBackgroundDrawable(cd);
			menuWindow.setFocusable(true);
			menuWindow.setOutsideTouchable(true);

			// menuWindow.showAsDropDown(layout); //设置弹出效果
			// menuWindow.showAsDropDown(null, 0, layout.getHeight());
			// menuWindow.showAsDropDown(null, 0, (int)mPointY/2);
			// 如何获取我们main中的控件呢？也很简单
			if (FinalFlag == 0) {
				menuWindow.showAsDropDown(total2btn);
				menuWindow.showAtLocation(this.findViewById(R.id.window),
						Gravity.TOP | Gravity.RIGHT, 0, (int) mPointY / 2); // 设置layout在PopupWindow中显示的位置
			} else if (FinalFlag == 1) {// 调整位置
				menuWindow.showAsDropDown(total1btn);
				menuWindow.showAtLocation(this.findViewById(R.id.window),
						Gravity.TOP | Gravity.LEFT, 0, (int) mPointY / 2); // 设置layout在PopupWindow中显示的位置
			}

			menuWindow.update();

			menuWindow.setTouchInterceptor(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					/**** 如果点击了popupwindow的外部，popupwindow也会消失 ****/
					// TODO Auto-generated method stub
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						menuWindow.dismiss();
					}
					return false;
				}
			});

			dismiss = false;

			mClose = (LinearLayout) layout.findViewById(R.id.menu_close);
			mCloseBtn = (LinearLayout) layout.findViewById(R.id.menu_close_btn);
			mCloseBtn2 = (LinearLayout) layout
					.findViewById(R.id.menu_close_btn2);
			myCloseBtn = (Button) layout.findViewById(R.id.windowreturn);

			myCloseBtn.setOnClickListener(new View.OnClickListener() { // "点击返回"按钮的相应事件
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							menuWindow.dismiss();
							dismiss = false;
						}
					});

			mCloseBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent1 = new Intent();
					intent1.setClass(RoadOne.this, PhotoShow.class);

					Bundle bundle = new Bundle();
					bundle.putInt("crossingid", entity.getId());
					if (FinalFlag == 0) {
						bundle.putInt("round", 0);// 表示内圈
					} else if (FinalFlag == 1) {
						bundle.putInt("round", 1);// 表示外圈
					}
					intent1.putExtras(bundle);
					RoadOne.this.startActivity(intent1);

				}
			});

			/**
			 * 这是路口照片
			 */
			mCloseBtn2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent1 = new Intent();
					intent1.setClass(RoadOne.this, PhotoShow.class);

					Bundle bundle = new Bundle();
					bundle.putInt("index", flag);
					if (FinalFlag == 0) {
						bundle.putInt("round", 3); //round 3 表示显示入口照片
					} else if (FinalFlag == 1) {
						bundle.putInt("round", 3);
					}
					intent1.putExtras(bundle);
					RoadOne.this.startActivity(intent1);

				}
			});
		}
	}

	
	/**
	 *  创建一个myview类，来显示背景
	 * @author JeffreyTseng
	 */
	class MyView extends ImageView {

		public MyView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public void setlocation(int top, int left) {
			this.setFrame(left, top, left + 40, top + 40);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			try {
				Field field = AnimationDrawable.class
						.getDeclaredField("mCurFrame");
				field.setAccessible(true);
			} catch (Exception e) {
				// TODO: handle exception
			}
			super.onDraw(canvas);
		}

	}
	
	/**
	 * 点击全景时重新生成当前画面
	 * @author JeffreyTseng
	 */
	@Override
	public void onMyPointTouch(POINT point) {// 内圈
		// TODO Auto-generated method stub
		float px = point.x;
		float py = point.y;
		float MPointX = width / 2;
		float MPointY = height / 2;
		float MRadius = (float) (width / 2.2);
		float Angle = 0;
		double distance = Math.sqrt((py - MPointY) * (py - MPointY)
				+ (px - MPointX) * (px - MPointX));
		if (distance > MRadius) {
			return;
		}
		if (px != MPointX) {
			double d = (MPointY - py) / (px - MPointX);
			double tanx = Math.atan(d);
			double angle = 180 * tanx / Math.PI;
			if (angle < 60 && angle >= 0 && py < MPointY)// 表示点击到第一块区域
			{
				Angle = 1;
			} else if ((angle >= 60 && angle < 90 && py <= MPointY)
					|| (angle < -60 && angle > -90 && py <= MPointY)) {
				Angle = 2;
			} else if (angle >= -60 && angle < 0 && py <= MPointY) {
				Angle = 3;
			} else if (angle < 60 && angle >= 0 && py >= MPointY) {
				Angle = 4;
			} else if ((angle >= 60 && angle < 90 && py >= MPointY)
					|| (angle < -60 && angle > -90 && py >= MPointY)) {
				Angle = 5;
			} else if (angle >= -60 && angle < 0 && py >= MPointY) {
				Angle = 6;
			}
		} else {
			if (py < mPointY) {
				Angle = 2;
			} else if (py > mPointY) {
				Angle = 5;
			}
		}
		if (FinalFlag == 1) {
			renderView(FinalFlag, (float) (-height * 0.86), mPointY, mRadius, (float) (Angle + 3) * 5 * (360/CrossingListOut.size()));
			FinalFlag = 1;
		} else if (FinalFlag == 0) {
			renderView(FinalFlag, mPointX, mPointY, mRadius,((6 - Angle) * 5 + 17) *(360/CrossingListInner.size()));
			FinalFlag = 0;
		}
		return;
	}

	/**
	 * 点击路段，显示速度
	 * @author JeffreyTseng
	 */
	@Override
	public void onPointTouchRing(int i, int speed) {
		// TODO Auto-generated method stub
		// Toast.makeText(RoadOne.this, i+"", 1000).show();
		String Sspeed = speed + "";
		if (FinalFlag == 1)// 外环
		{
			switch (i) {
			case 27:
				Toast.makeText(RoadOne.this,
						"清溪西路入口->龙腾东路出口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 26:
				Toast.makeText(RoadOne.this,
						"龙腾东路入口->丽都路口入口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 25:
				Toast.makeText(RoadOne.this,
						"丽都路口入口->创业路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 24:
				Toast.makeText(RoadOne.this,
						"创业路出口->创业路入口-，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 23:
				Toast.makeText(RoadOne.this,
						"创业路入口->人民南路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 22:
				Toast.makeText(RoadOne.this,
						"人民南路出口->人民南路入口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 21:
				Toast.makeText(RoadOne.this,
						"人民南路入口->科华中路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 20:
				Toast.makeText(RoadOne.this,
						"科华中路出口->科华中路入口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 19:
				Toast.makeText(RoadOne.this,
						"科华中路入口->琉璃路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 18:
				Toast.makeText(RoadOne.this,
						"琉璃路出口->锦华路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 17:
				Toast.makeText(RoadOne.this,
						"锦华路出口->东大路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 16:
				Toast.makeText(RoadOne.this,
						"东大路出口->双桂路入口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 15:
				Toast.makeText(RoadOne.this,
						"双桂路入口->双桂路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 14:
				Toast.makeText(RoadOne.this,
						"双桂路出口->双庆路出口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 13:
				Toast.makeText(RoadOne.this,
						"双庆路出口->万年路出口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 12:
				Toast.makeText(RoadOne.this,
						"万年路出口->杉板桥路出口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 11:
				Toast.makeText(RoadOne.this,
						"杉板桥路出口->杉板桥路入口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 10:
				Toast.makeText(RoadOne.this,
						"杉板桥路入口->建设北路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 9:
				Toast.makeText(RoadOne.this,
						"建设北路出口->建设北路入口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 8:
				Toast.makeText(RoadOne.this,
						"建设北路入口->府青路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 7:
				Toast.makeText(RoadOne.this,
						"府青路出口->驷马桥路入口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 6:
				Toast.makeText(RoadOne.this,
						"驷马桥路入口->驷马桥街出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 5:
				Toast.makeText(RoadOne.this,
						"驷马桥街出口->北新干道出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 4:
				Toast.makeText(RoadOne.this,
						"北新干道出口->北新干道入口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 3:
				Toast.makeText(RoadOne.this,
						"北新干道入口->九里堤出口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 2:
				Toast.makeText(RoadOne.this,
						"九里堤出口->银沙路入口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 1:
				Toast.makeText(RoadOne.this,
						"银沙路入口->茶店子出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 0:
				Toast.makeText(RoadOne.this,
						"茶店子出口->清溪西路入口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			default:
				break;
			}
		} else if (FinalFlag == 0) {
			//内环
			switch (i) {
			case 30:
				Toast.makeText(RoadOne.this,
						"成温立交入口->大石西路出口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 0:
				Toast.makeText(RoadOne.this,
						"大石西路出口->家园南路入口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 1:
				Toast.makeText(RoadOne.this,
						"家园南路入口->抚琴西路出口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 2:
				Toast.makeText(RoadOne.this,
						"抚琴西路出口->营兴街入口,当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 3:
				Toast.makeText(RoadOne.this,
						"营兴街入口->茶店子出口,当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 4:
				Toast.makeText(RoadOne.this,
						"茶店子出口->沙湾路出口,当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 5:
				Toast.makeText(RoadOne.this,
						"沙湾路出口->府河市场入口,当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 6:
				Toast.makeText(RoadOne.this,
						"府河市场入口->北站东二路出口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 7:
				Toast.makeText(RoadOne.this,
						"北站东二路出口->北新干道出口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 8:
				Toast.makeText(RoadOne.this,
						"北新干道出口->北新干道入口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 9:
				Toast.makeText(RoadOne.this,
						"北新干道入口->红花北路入口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 10:
				Toast.makeText(RoadOne.this,
						"红花北路入口->府青路出口,当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 11:
				Toast.makeText(RoadOne.this,
						"府青路出口->府青路入口,当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 12:
				Toast.makeText(RoadOne.this,
						"府青路入口->建设路出口,当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 13:
				Toast.makeText(RoadOne.this,
						"建设路出口->杉板桥路出口,当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 14:
				Toast.makeText(RoadOne.this,
						"杉板桥路出口->杉板桥路入口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 15:
				Toast.makeText(RoadOne.this,
						"杉板桥路入口->双林北支路入口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 16:
				Toast.makeText(RoadOne.this,
						"双林北支路入口->双庆路 双桥路出口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 17:
				Toast.makeText(RoadOne.this,
						"双庆路双庆桥出口->牛市口入口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 18:
				Toast.makeText(RoadOne.this,
						"牛市口入口->龙舟路出口,当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 19:
				Toast.makeText(RoadOne.this,
						"龙舟路出口->莲桂东路入口,当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 20:
				Toast.makeText(RoadOne.this,
						"莲桂东路入口->净居寺路出口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 21:
				Toast.makeText(RoadOne.this,
						"净居寺路出口->科华中路出口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 22:
				Toast.makeText(RoadOne.this,
						"科华中路出口->人民南路出口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 23:
				Toast.makeText(RoadOne.this,
						"人民南路出口->玉林南街入口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 24:
				Toast.makeText(RoadOne.this,
						"玉林南街入口->创业路出口,当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 25:
				Toast.makeText(RoadOne.this,
						"创业路出口->创业路入口,当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 26:
				Toast.makeText(RoadOne.this,
						"创业路入口->高升桥路 出口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 27:
				Toast.makeText(RoadOne.this,
						"高升桥路出口->红牌楼路入口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 28:
				Toast.makeText(RoadOne.this,
						"红牌楼路入口->武侯大道出口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 29:
				Toast.makeText(RoadOne.this,
						"武侯大道出口->成温立交入口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			default:
				break;
			}
		}

	}

}
