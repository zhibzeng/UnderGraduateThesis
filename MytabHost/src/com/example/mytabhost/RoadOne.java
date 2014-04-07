package com.example.mytabhost;

import java.lang.reflect.Field;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
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

import com.example.mytabhost.MyTurnplateView.OnTurnplateListener;
import com.example.mytabhost.MyTurnplateView.Point;
import com.example.mytabhost.TotalRoad.MyTouchListener;
import com.example.mytabhost.TotalRoad.POINT;
import com.example.mytabhost.db.DateBaseHelper;

@SuppressLint("NewApi")
public class RoadOne extends Activity implements OnTurnplateListener,
		MyTouchListener {

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
	private FrameLayout frame;
	private int height, width;
	private Button total1btn = null;
	private Button total2btn = null;
	private Button btnsearch = null;
	private AutoCompleteTextView editTextlukou = null;
	private static int FinalFlag = 0;// 标志位，0时为内圈，1时为外圈

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

		final MyTurnplateView myView = new MyTurnplateView(this,
				(float) (-height * 0.86), mPointY, mRadius, 30, 0);

		myView.setOnTurnplateListener(this);

		LayoutInflater inflater = LayoutInflater.from(this);

		View itemView = inflater.inflate(R.layout.layout1, null);// 创建布局文件。

		// FrameLayout

		// final TotalRoad yourview=new TotalRoad(this, width, height);
		frame = new FrameLayout(this);
		setContentView(frame);
		myView1 = new MyView(this);
		frame.addView(myView1);
		frame.addView(myView);
		// frame.addView(yourview);
		frame.addView(itemView);

		editTextlukou = (AutoCompleteTextView) findViewById(R.id.acTextView);
		AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(this,
				R.layout.autoconplete_dropdown_item, null,
				DateBaseHelper.NAME, android.R.id.text1, 1, 1);
		((AutoCompleteTextView) this.findViewById(R.id.acTextView))
				.setThreshold(1);
		((AutoCompleteTextView) this.findViewById(R.id.acTextView))
				.setAdapter(cursorAdapter);

		btnreturn = (Button) findViewById(R.id.BtnReturn);
		btnreturn.setText("外环");
		btnreturn.setOnClickListener(new BtnClickListener());

		total1btn = (Button) findViewById(R.id.TotalRoad1);
		total1btn.setOnClickListener(new BtnClickListener());

		total2btn = (Button) findViewById(R.id.TotalRoad2);
		total2btn.setOnClickListener(new BtnClickListener());

		btnsearch = (Button) findViewById(R.id.BtnSearch);
		btnsearch.setOnClickListener(new BtnClickListener());

		if (FinalFlag == 0) {
			total2btn.setVisibility(View.VISIBLE);
			total1btn.setVisibility(View.GONE);
		} else if (FinalFlag == 1) {
			total1btn.setVisibility(View.VISIBLE);
			total2btn.setVisibility(View.GONE);
		}
	}

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
				final TotalRoad yourview = new TotalRoad(RoadOne.this, width,
						height);
				yourview.setMyTouchListener(RoadOne.this);
				frame = new FrameLayout(RoadOne.this);
				setContentView(frame);
				frame.addView(yourview);
			}
			if (v == total2btn) {// 外圈实现方法
				// total1btn.setVisibility(View.VISIBLE);
				// total2btn.setVisibility(View.GONE);
				frame.removeAllViews();
				final TotalRoad yourview = new TotalRoad(RoadOne.this, width,
						height);
				yourview.setMyTouchListener(RoadOne.this);
				frame = new FrameLayout(RoadOne.this);
				setContentView(frame);
				frame.addView(yourview);
			}
			if (v == btnsearch) {// 搜索按钮
				String messlukou = editTextlukou.getText().toString();
				if (messlukou.compareTo("") == 0) {
					Toast.makeText(RoadOne.this, "请输入需要搜索的信息", 1000).show();
				} else {
					// Toast.makeText(RoadOne.this, "nima", 1000).show();
					if (btnreturn.getText().toString() == "外环") {// 如果当前按钮的字符是"内环"
						int tangleTorun;
						if (messlukou.equals("龙腾东路出口")) {
							tangleTorun = 0;
						} else if (messlukou.equals("丽都路口入口")) {
							tangleTorun = 1;
						} else if (messlukou.equals("创业路入口")) {
							tangleTorun = 2;
						} else if (messlukou.equals("创业路出口")) {
							tangleTorun = 3;
						} else if (messlukou.equals("人南立交出口")) {
							tangleTorun = 4;
						} else if (messlukou.equals("人南立交入口")) {
							tangleTorun = 5;
						} else if (messlukou.equals("科华中路出口")) {
							tangleTorun = 6;
						} else if (messlukou.equals("科华中路入口")) {
							tangleTorun = 7;
						} else if (messlukou.equals("琉璃路出口")) {
							tangleTorun = 8;
						} else if (messlukou.equals("锦华路出口")) {
							tangleTorun = 9;
						} else if (messlukou.equals("龙舟路入口")) {
							tangleTorun = 10;
						} else if (messlukou.equals("东大路出口")) {
							tangleTorun = 11;
						} else if (messlukou.equals("双庆桥出口")) {
							tangleTorun = 12;
						} else if (messlukou.equals("万年桥出口")) {
							tangleTorun = 13;
						} else if (messlukou.equals("东篱路入口")) {
							tangleTorun = 14;
						} else if (messlukou.equals("衫板桥立交出口")) {
							tangleTorun = 15;
						} else if (messlukou.equals("衫板桥立交入口")) {
							tangleTorun = 16;
						} else if (messlukou.equals("建设路出口")) {
							tangleTorun = 17;
						} else if (messlukou.equals("春木林入口")) {
							tangleTorun = 18;
						} else if (messlukou.equals("府青路出口")) {
							tangleTorun = 19;
						} else if (messlukou.equals("刃具立交入口")) {
							tangleTorun = 20;
						} else if (messlukou.equals("解放路出口")) {
							tangleTorun = 21;
						} else if (messlukou.equals("高笋塘入口")) {
							tangleTorun = 22;
						} else if (messlukou.equals("北新干道出口")) {
							tangleTorun = 23;
						} else if (messlukou.equals("北新干道入口")) {
							tangleTorun = 24;
						} else if (messlukou.equals("火车北站入口")) {
							tangleTorun = 25;
						} else if (messlukou.equals("九里堤出口")) {
							tangleTorun = 26;
						} else if (messlukou.equals("银沙路入口")) {
							tangleTorun = 27;
						} else if (messlukou.equals("茶店子出口")) {
							tangleTorun = 28;
						} else if (messlukou.equals("清溪西路入口")) {
							tangleTorun = 29;
						} else {
							Toast.makeText(RoadOne.this, "请输入需要搜索的信息", 1000)
									.show();
							tangleTorun = 0;
						}
						frame.removeAllViews();
						tangleTorun = 12 * tangleTorun + 12;
						final MyTurnplateView myView = new MyTurnplateView(
								RoadOne.this, (float) (-height * 0.86),
								mPointY, mRadius, 30, tangleTorun);
						myView.setOnTurnplateListener(RoadOne.this);
						LayoutInflater inflater = LayoutInflater
								.from(RoadOne.this);
						View itemView = inflater
								.inflate(R.layout.layout1, null);// 创建布局文件。
						frame = new FrameLayout(RoadOne.this);
						setContentView(frame);
						myView1 = new MyView(RoadOne.this);
						frame.addView(myView1);
						frame.addView(myView);
						frame.addView(itemView);
						btnreturn = (Button) findViewById(R.id.BtnReturn);
						btnreturn.setText("外环");
						btnreturn.setOnClickListener(new BtnClickListener());

						total1btn = (Button) findViewById(R.id.TotalRoad1);
						total1btn.setVisibility(View.GONE);

						total2btn = (Button) findViewById(R.id.TotalRoad2);
						total2btn.setVisibility(View.VISIBLE);

						total2btn.setOnClickListener(new BtnClickListener());

						FinalFlag = 0;

						editTextlukou = (AutoCompleteTextView) findViewById(R.id.acTextView);
						editTextlukou.setText("");
						AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(
								RoadOne.this,R.layout.autoconplete_dropdown_item,
								null, DateBaseHelper.NAME, android.R.id.text1,
								1, 1);
						((AutoCompleteTextView) RoadOne.this
								.findViewById(R.id.acTextView)).setThreshold(1);
						((AutoCompleteTextView) RoadOne.this
								.findViewById(R.id.acTextView))
								.setAdapter(cursorAdapter);

						btnsearch = (Button) findViewById(R.id.BtnSearch);
						btnsearch.setOnClickListener(new BtnClickListener());
					} else {// 当前按钮字符是“外环”时
						int tangleTorun;
						if (messlukou.equals("大石西路出口")) {
							tangleTorun = 0;
						} else if (messlukou.equals("家园南路入口")) {
							tangleTorun = 1;
						} else if (messlukou.equals("抚琴西路出口")) {
							tangleTorun = 2;
						} else if (messlukou.equals("营兴街入口")) {
							tangleTorun = 3;
						} else if (messlukou.equals("茶店子出口")) {
							tangleTorun = 4;
						} else if (messlukou.equals("沙湾路出口")) {
							tangleTorun = 5;
						} else if (messlukou.equals("府河市场入口")) {
							tangleTorun = 6;
						} else if (messlukou.equals("北站东二路出口")) {
							tangleTorun = 7;
						} else if (messlukou.equals("北星大道出口")) {
							tangleTorun = 8;
						} else if (messlukou.equals("北新干道入口")) {
							tangleTorun = 9;
						} else if (messlukou.equals("红花北路入口")) {
							tangleTorun = 10;
						} else if (messlukou.equals("府青路出口")) {
							tangleTorun = 11;
						} else if (messlukou.equals("府青路入口")) {
							tangleTorun = 12;
						} else if (messlukou.equals("建设路出口")) {
							tangleTorun = 13;
						} else if (messlukou.equals("杉板桥路出口")) {
							tangleTorun = 14;
						} else if (messlukou.equals("杉板桥路入口")) {
							tangleTorun = 15;
						} else if (messlukou.equals("双林北支路入口")) {
							tangleTorun = 16;
						} else if (messlukou.equals("双庆路 双桥路出口")) {
							tangleTorun = 17;
						} else if (messlukou.equals("牛市口入口")) {
							tangleTorun = 18;
						} else if (messlukou.equals("龙舟路出口")) {
							tangleTorun = 19;
						} else if (messlukou.equals("莲桂东路入口")) {
							tangleTorun = 20;
						} else if (messlukou.equals("净居寺路出口")) {
							tangleTorun = 21;
						} else if (messlukou.equals("科华中路出口")) {
							tangleTorun = 22;
						} else if (messlukou.equals("人民南路出口")) {
							tangleTorun = 23;
						} else if (messlukou.equals("玉林南街入口")) {
							tangleTorun = 24;
						} else if (messlukou.equals("创业路出口")) {
							tangleTorun = 25;
						} else if (messlukou.equals("创业路入口")) {
							tangleTorun = 26;
						} else if (messlukou.equals("高升桥路出口")) {
							tangleTorun = 27;
						} else if (messlukou.equals("红牌楼路入口")) {
							tangleTorun = 28;
						} else if (messlukou.equals("武侯大道出口")) {
							tangleTorun = 29;
						} else if (messlukou.equals("成温立交入口")) {
							tangleTorun = 30;
						} else {
							Toast.makeText(RoadOne.this, "请输入需要搜索的信息", 1000)
									.show();
							tangleTorun = 0;
						}
						frame.removeAllViews();
						float tangleTorunf;
						tangleTorunf = (16 - tangleTorun) * 360 / 31;
						final MyTurnplateView myView = new MyTurnplateView(
								RoadOne.this, mPointX, mPointY, mRadius, 31,
								tangleTorunf);
						myView.setOnTurnplateListener(RoadOne.this);
						LayoutInflater inflater = LayoutInflater
								.from(RoadOne.this);
						View itemView = inflater
								.inflate(R.layout.layout1, null);// 创建布局文件。
						frame = new FrameLayout(RoadOne.this);
						setContentView(frame);
						myView1 = new MyView(RoadOne.this);
						frame.addView(myView1);
						frame.addView(myView);
						frame.addView(itemView);
						btnreturn = (Button) findViewById(R.id.BtnReturn);
						btnreturn.setText("内环");
						btnreturn.setOnClickListener(new BtnClickListener());

						total1btn = (Button) findViewById(R.id.TotalRoad1);
						total1btn.setVisibility(View.VISIBLE);
						total1btn.setOnClickListener(new BtnClickListener());

						total2btn = (Button) findViewById(R.id.TotalRoad2);
						total2btn.setVisibility(View.GONE);

						editTextlukou = (AutoCompleteTextView) findViewById(R.id.acTextView);
						editTextlukou.setText("");

						AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(
								RoadOne.this,R.layout.autoconplete_dropdown_item,
								null, DateBaseHelper.NAME, android.R.id.text1,
								1, 0);
						((AutoCompleteTextView) RoadOne.this
								.findViewById(R.id.acTextView)).setThreshold(1);
						((AutoCompleteTextView) RoadOne.this
								.findViewById(R.id.acTextView))
								.setAdapter(cursorAdapter);

						btnsearch = (Button) findViewById(R.id.BtnSearch);
						btnsearch.setOnClickListener(new BtnClickListener());

						FinalFlag = 1;
					}
				}
				editTextlukou.setText("");
			}
			if (v == btnreturn) {
				// finish();
				if (btnreturn.getText().toString() == "外环") {
					frame.removeAllViews();
					final MyTurnplateView myView = new MyTurnplateView(
							RoadOne.this, mPointX, mPointY, mRadius, 31, 0);
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
					btnreturn.setText("内环");
					btnreturn.setOnClickListener(new BtnClickListener());

					total1btn = (Button) findViewById(R.id.TotalRoad1);
					total1btn.setVisibility(View.VISIBLE);
					total1btn.setOnClickListener(new BtnClickListener());

					total2btn = (Button) findViewById(R.id.TotalRoad2);
					total2btn.setVisibility(View.GONE);

					editTextlukou = (AutoCompleteTextView) findViewById(R.id.acTextView);
					editTextlukou.setText("");

					AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(
							RoadOne.this,R.layout.autoconplete_dropdown_item, null,
							DateBaseHelper.NAME, android.R.id.text1, 1, 0);
					((AutoCompleteTextView) RoadOne.this
							.findViewById(R.id.acTextView)).setThreshold(1);
					((AutoCompleteTextView) RoadOne.this
							.findViewById(R.id.acTextView))
							.setAdapter(cursorAdapter);

					btnsearch = (Button) findViewById(R.id.BtnSearch);
					btnsearch.setOnClickListener(new BtnClickListener());

					FinalFlag = 1;
				} else {

					frame.removeAllViews();
					final MyTurnplateView myView = new MyTurnplateView(
							RoadOne.this, (float) (-height * 0.86), mPointY,
							mRadius, 30, 0);
					myView.setOnTurnplateListener(RoadOne.this);
					LayoutInflater inflater = LayoutInflater.from(RoadOne.this);
					View itemView = inflater.inflate(R.layout.layout1, null);// 创建布局文件。
					frame = new FrameLayout(RoadOne.this);
					setContentView(frame);
					myView1 = new MyView(RoadOne.this);
					frame.addView(myView1);
					frame.addView(myView);
					frame.addView(itemView);

					editTextlukou = (AutoCompleteTextView) findViewById(R.id.acTextView);
					editTextlukou.setText("");
					AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(
							RoadOne.this,R.layout.autoconplete_dropdown_item, null,
							DateBaseHelper.NAME, android.R.id.text1, 1, 1);
					((AutoCompleteTextView) RoadOne.this
							.findViewById(R.id.acTextView)).setThreshold(1);
					((AutoCompleteTextView) RoadOne.this
							.findViewById(R.id.acTextView))
							.setAdapter(cursorAdapter);

					btnreturn = (Button) findViewById(R.id.BtnReturn);
					btnreturn.setText("外环");
					btnreturn.setOnClickListener(new BtnClickListener());

					total1btn = (Button) findViewById(R.id.TotalRoad1);
					total1btn.setVisibility(View.GONE);
					total1btn.setOnClickListener(new BtnClickListener());

					total2btn = (Button) findViewById(R.id.TotalRoad2);
					total2btn.setVisibility(View.VISIBLE);
					total2btn.setOnClickListener(new BtnClickListener());

					btnsearch = (Button) findViewById(R.id.BtnSearch);
					btnsearch.setOnClickListener(new BtnClickListener());

					FinalFlag = 0;
				}
			}
		}

	}

	// 创建一个myview类，来显示背景
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
				// Paint paint = new Paint();
				// paint.setColor(Color.BLACK);
				// paint.setStrokeWidth(5);
				// canvas.drawCircle(50, 50, 20, paint);
				// Bitmap backgroundBitmap =
				// ((BitmapDrawable)(getResources().getDrawable(R.drawable.hot))).getBitmap();
				// canvas.drawBitmap(backgroundBitmap,0, 0, null);
			} catch (Exception e) {
				// TODO: handle exception
			}
			super.onDraw(canvas);
		}

	}

	@Override
	public void onPointTouch(Point point) {

		final int flag = point.flag;
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

			if (FinalFlag == 0) {
				switch (flag) {
				case 29:
					imageView.setImageResource(R.drawable.k2_30);
					windowTextView.setText("龙腾东路出口");
					break;
				case 28:
					imageView.setImageResource(R.drawable.k2_29);
					windowTextView.setText("丽都路口入口");
					break;
				case 27:
					imageView.setImageResource(R.drawable.k2_28);
					windowTextView.setText("创业路入口");
					break;
				case 26:
					imageView.setImageResource(R.drawable.k2_27);
					windowTextView.setText("创业路出口");
					break;
				case 25:
					imageView.setImageResource(R.drawable.k2_26);
					windowTextView.setText("人南立交出口");
					break;
				case 24:
					imageView.setImageResource(R.drawable.k2_25);
					windowTextView.setText("人南立交入口");
					break;
				case 23:
					imageView.setImageResource(R.drawable.k2_24);
					windowTextView.setText("科华中路出口");
					break;
				case 22:
					imageView.setImageResource(R.drawable.k2_23);
					windowTextView.setText("科华中路入口");
					break;
				case 21:
					imageView.setImageResource(R.drawable.k2_22);
					windowTextView.setText("琉璃路出口");
					break;
				case 20:
					imageView.setImageResource(R.drawable.k2_21);
					windowTextView.setText("锦华路出口");
					break;
				case 19:
					imageView.setImageResource(R.drawable.k2_20);
					windowTextView.setText("龙舟路入口");
					break;
				case 18:
					imageView.setImageResource(R.drawable.k2_19);
					windowTextView.setText("东大路出口");
					break;
				case 17:
					imageView.setImageResource(R.drawable.k2_18);
					windowTextView.setText("双庆桥出口");
					break;
				case 16:
					imageView.setImageResource(R.drawable.k2_17);
					windowTextView.setText("万年桥出口");
					break;
				case 15:
					imageView.setImageResource(R.drawable.k2_16);
					windowTextView.setText("东篱路入口");
					break;
				case 14:
					imageView.setImageResource(R.drawable.k2_15);
					windowTextView.setText("衫板桥立交出口");
					break;
				case 13:
					imageView.setImageResource(R.drawable.k2_14);
					windowTextView.setText("衫板桥立交入口");
					break;
				case 12:
					imageView.setImageResource(R.drawable.k2_13);
					windowTextView.setText("建设路出口");
					break;
				case 11:
					imageView.setImageResource(R.drawable.k2_12);
					windowTextView.setText("春木林入口");
					break;
				case 10:
					imageView.setImageResource(R.drawable.k2_11);
					windowTextView.setText("府青路出口");
					break;
				case 9:
					imageView.setImageResource(R.drawable.k2_10);
					windowTextView.setText("刃具立交入口");
					break;
				case 8:
					imageView.setImageResource(R.drawable.k2_9);
					windowTextView.setText("解放路出口");
					break;
				case 7:
					imageView.setImageResource(R.drawable.k2_8);
					windowTextView.setText("高笋塘入口");
					break;
				case 6:
					imageView.setImageResource(R.drawable.k2_7);
					windowTextView.setText("北新干道出口");
					break;
				case 5:
					imageView.setImageResource(R.drawable.k2_6);
					windowTextView.setText("北新干道入口");
					break;
				case 4:
					imageView.setImageResource(R.drawable.k2_5);
					windowTextView.setText("火车北站入口");
					break;
				case 3:
					imageView.setImageResource(R.drawable.k2_4);
					windowTextView.setText("九里堤出口");
					break;
				case 2:
					imageView.setImageResource(R.drawable.k2_3);
					windowTextView.setText("银沙路入口");
					break;
				case 1:
					imageView.setImageResource(R.drawable.k2_2);
					windowTextView.setText("茶店子出口");
					break;
				case 0:
					imageView.setImageResource(R.drawable.k2_1);
					windowTextView.setText("清溪西路入口");
					break;
				default:
					break;
				}
				imageView2.setImageResource(R.drawable.chukou);
			} else if (FinalFlag == 1) {
				switch (flag) {
				case 0:
					imageView.setImageResource(R.drawable.k1_1);
					windowTextView.setText("大石西路出口");
					break;
				case 1:
					imageView.setImageResource(R.drawable.k1_2);
					windowTextView.setText("家园南路入口");
					break;
				case 2:
					imageView.setImageResource(R.drawable.k1_3);
					windowTextView.setText("抚琴西路出口");
					break;
				case 3:
					imageView.setImageResource(R.drawable.k1_4);
					windowTextView.setText("营兴街入口");
					break;
				case 4:
					imageView.setImageResource(R.drawable.k1_5);
					windowTextView.setText("茶店子出口");
					break;
				case 5:
					imageView.setImageResource(R.drawable.k1_6);
					windowTextView.setText("沙湾路出口");
					break;
				case 6:
					imageView.setImageResource(R.drawable.k1_7);
					windowTextView.setText("府河市场入口");
					break;
				case 7:
					imageView.setImageResource(R.drawable.k1_8);
					windowTextView.setText("北站东二路出口");
					break;
				case 8:
					imageView.setImageResource(R.drawable.k1_9);
					windowTextView.setText("北星大道出口");
					break;
				case 9:
					imageView.setImageResource(R.drawable.k1_10);
					windowTextView.setText("北新干道入口");
					break;
				case 10:
					imageView.setImageResource(R.drawable.k1_11);
					windowTextView.setText("红花北路入口");
					break;
				case 11:
					imageView.setImageResource(R.drawable.k1_12);
					windowTextView.setText("府青路出口");
					break;
				case 12:
					imageView.setImageResource(R.drawable.k1_13);
					windowTextView.setText("府青路入口");
					break;
				case 13:
					imageView.setImageResource(R.drawable.k1_14);
					windowTextView.setText("建设路出口");
					break;
				case 14:
					imageView.setImageResource(R.drawable.k1_15);
					windowTextView.setText("杉板桥路出口");
					break;
				case 15:
					imageView.setImageResource(R.drawable.k1_16);
					windowTextView.setText("杉板桥路入口");
					break;
				case 16:
					imageView.setImageResource(R.drawable.k1_17);
					windowTextView.setText("双林北支路入口");
					break;
				case 17:
					imageView.setImageResource(R.drawable.k1_18);
					windowTextView.setText("双庆路 双桥路出口");
					break;
				case 18:
					imageView.setImageResource(R.drawable.k1_19);
					windowTextView.setText("牛市口入口");
					break;
				case 19:
					imageView.setImageResource(R.drawable.k1_20);
					windowTextView.setText("龙舟路出口");
					break;
				case 20:
					imageView.setImageResource(R.drawable.k1_21);
					windowTextView.setText("莲桂东路入口");
					break;
				case 21:
					imageView.setImageResource(R.drawable.k1_22);
					windowTextView.setText("净居寺路出口");
					break;
				case 22:
					imageView.setImageResource(R.drawable.k1_23);
					windowTextView.setText("科华中路出口");
					break;
				case 23:
					imageView.setImageResource(R.drawable.k1_24);
					windowTextView.setText("人民南路出口");
					break;
				case 24:
					imageView.setImageResource(R.drawable.k1_25);
					windowTextView.setText("玉林南街入口");
					break;
				case 25:
					imageView.setImageResource(R.drawable.k1_26);
					windowTextView.setText("创业路出口");
					break;
				case 26:
					imageView.setImageResource(R.drawable.k1_27);
					windowTextView.setText("创业路入口");
					break;
				case 27:
					imageView.setImageResource(R.drawable.k1_28);
					windowTextView.setText("高升桥路 出口");
					break;
				case 28:
					imageView.setImageResource(R.drawable.k1_29);
					windowTextView.setText("红牌楼路入口");
					break;
				case 29:
					imageView.setImageResource(R.drawable.k1_30);
					windowTextView.setText("武侯大道出口");
					break;
				case 30:
					imageView.setImageResource(R.drawable.k1_31);
					windowTextView.setText("成温立交入口");
					break;
				default:
					break;
				}
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
					bundle.putInt("index", flag);
					if (FinalFlag == 0) {
						bundle.putInt("round", 1);// 表示内圈
					} else if (FinalFlag == 1) {
						bundle.putInt("round", 2);// 表示外圈
					}
					intent1.putExtras(bundle);
					RoadOne.this.startActivity(intent1);

				}
			});

			mCloseBtn2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent1 = new Intent();
					intent1.setClass(RoadOne.this, PhotoShow.class);

					Bundle bundle = new Bundle();
					bundle.putInt("index", flag);
					if (FinalFlag == 0) {
						bundle.putInt("round", 1);// 表示内圈
					} else if (FinalFlag == 1) {
						bundle.putInt("round", 2);// 表示外圈
					}
					intent1.putExtras(bundle);
					RoadOne.this.startActivity(intent1);

				}
			});
		}
	}

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
		if (FinalFlag == 0) {
			frame.removeAllViews();
			final MyTurnplateView myView = new MyTurnplateView(RoadOne.this,
					(float) (-height * 0.86), mPointY, mRadius, 30,
					(float) (Angle + 3) * 5 * 12);
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
			btnreturn.setText("外环");
			btnreturn.setOnClickListener(new BtnClickListener());

			total1btn = (Button) findViewById(R.id.TotalRoad1);
			total1btn.setVisibility(View.GONE);
			total1btn.setOnClickListener(new BtnClickListener());

			total2btn = (Button) findViewById(R.id.TotalRoad2);
			total2btn.setVisibility(View.VISIBLE);
			total2btn.setOnClickListener(new BtnClickListener());

			editTextlukou = (AutoCompleteTextView) findViewById(R.id.acTextView);
			editTextlukou.setText("");
			AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(
					RoadOne.this,R.layout.autoconplete_dropdown_item,
					null, DateBaseHelper.NAME, android.R.id.text1, 1, 1);
			((AutoCompleteTextView) RoadOne.this.findViewById(R.id.acTextView))
					.setThreshold(1);
			((AutoCompleteTextView) RoadOne.this.findViewById(R.id.acTextView))
					.setAdapter(cursorAdapter);

			btnsearch = (Button) findViewById(R.id.BtnSearch);
			btnsearch.setOnClickListener(new BtnClickListener());

			FinalFlag = 0;
		} else if (FinalFlag == 1) {
			frame.removeAllViews();
			final MyTurnplateView myView = new MyTurnplateView(RoadOne.this,
					mPointX, mPointY, mRadius, 31,
					((6 - Angle) * 5 + 17) * 360 / 31);
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
			btnreturn.setText("内环");
			btnreturn.setOnClickListener(new BtnClickListener());

			total1btn = (Button) findViewById(R.id.TotalRoad1);
			total1btn.setVisibility(View.VISIBLE);
			total1btn.setOnClickListener(new BtnClickListener());

			total2btn = (Button) findViewById(R.id.TotalRoad2);
			total2btn.setVisibility(View.GONE);

			editTextlukou = (AutoCompleteTextView) findViewById(R.id.acTextView);
			editTextlukou.setText("");
			AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(
					RoadOne.this, R.layout.autoconplete_dropdown_item,
					null, DateBaseHelper.NAME, android.R.id.text1, 1, 0);
			((AutoCompleteTextView) RoadOne.this.findViewById(R.id.acTextView))
					.setThreshold(1);
			((AutoCompleteTextView) RoadOne.this.findViewById(R.id.acTextView))
					.setAdapter(cursorAdapter);

			btnsearch = (Button) findViewById(R.id.BtnSearch);
			btnsearch.setOnClickListener(new BtnClickListener());

			FinalFlag = 1;
		}
		return;
	}

	@Override
	public void onPointTouchRing(int i, int speed) {
		// TODO Auto-generated method stub
		// Toast.makeText(RoadOne.this, i+"", 1000).show();
		String Sspeed = speed + "";
		if (FinalFlag == 0)// 内圈
		{
			switch (i) {
			case 29:
				Toast.makeText(RoadOne.this,
						"清溪西路入口->龙腾东路出口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 28:
				Toast.makeText(RoadOne.this,
						"龙腾东路入口->丽都路口入口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 27:
				Toast.makeText(RoadOne.this,
						"丽都路口入口->创业路入口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 26:
				Toast.makeText(RoadOne.this,
						"创业路入口->创业路出口-，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 25:
				Toast.makeText(RoadOne.this,
						"创业路出口->人南立交出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 24:
				Toast.makeText(RoadOne.this,
						"人南立交出口->人南立交入口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 23:
				Toast.makeText(RoadOne.this,
						"人南立交入口->科华中路出口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 22:
				Toast.makeText(RoadOne.this,
						"科华中路出口->科华中路入口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 21:
				Toast.makeText(RoadOne.this,
						"科华中路入口->琉璃路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 20:
				Toast.makeText(RoadOne.this,
						"琉璃路出口->锦华路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 19:
				Toast.makeText(RoadOne.this,
						"锦华路出口->龙舟路入口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 18:
				Toast.makeText(RoadOne.this,
						"龙舟路入口->东大路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 17:
				Toast.makeText(RoadOne.this,
						"东大桥出口->双庆桥出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 16:
				Toast.makeText(RoadOne.this,
						"双庆桥出口->万年桥出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 15:
				Toast.makeText(RoadOne.this,
						"万年桥出口->东篱路入口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 14:
				Toast.makeText(RoadOne.this,
						"东篱路入口->衫板桥立交出口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 13:
				Toast.makeText(RoadOne.this,
						"杉板桥立交出口->衫板桥立交入口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 12:
				Toast.makeText(RoadOne.this,
						"杉板桥立交入口->建设路出口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 11:
				Toast.makeText(RoadOne.this,
						"建设路出口->春木林入口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 10:
				Toast.makeText(RoadOne.this,
						"春木林入口->府青路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 9:
				Toast.makeText(RoadOne.this,
						"府青路出口->刃具立交入口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 8:
				Toast.makeText(RoadOne.this,
						"刀刃立交入口->解放路出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 7:
				Toast.makeText(RoadOne.this,
						"解放路出口->高笋塘入口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 6:
				Toast.makeText(RoadOne.this,
						"高笋塘入口->北新干道出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
				break;
			case 5:
				Toast.makeText(RoadOne.this,
						"北新干道出口->北新干道入口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 4:
				Toast.makeText(RoadOne.this,
						"北新干道入口->火车北站入口，当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 3:
				Toast.makeText(RoadOne.this,
						"火车北站入口->九里堤出口，当前平均时速为" + Sspeed + "km/h", 1000).show();
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
		} else if (FinalFlag == 1) {
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
						"北站东二路出口->北星大道出口,当前平均时速为" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 8:
				Toast.makeText(RoadOne.this,
						"北星大道出口->北新干道入口,当前平均时速为" + Sspeed + "km/h", 1000)
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
