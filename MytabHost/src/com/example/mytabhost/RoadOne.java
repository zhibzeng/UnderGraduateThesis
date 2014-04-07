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
	private Button btnreturn;// ���⻷�л���ť
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
	private static int FinalFlag = 0;// ��־λ��0ʱΪ��Ȧ��1ʱΪ��Ȧ

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

		View itemView = inflater.inflate(R.layout.layout1, null);// ���������ļ���

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
		btnreturn.setText("�⻷");
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
			if (dismiss == true)// ���ڵ���л�ʱδ�ر�pop����
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
			if (v == total2btn) {// ��Ȧʵ�ַ���
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
			if (v == btnsearch) {// ������ť
				String messlukou = editTextlukou.getText().toString();
				if (messlukou.compareTo("") == 0) {
					Toast.makeText(RoadOne.this, "��������Ҫ��������Ϣ", 1000).show();
				} else {
					// Toast.makeText(RoadOne.this, "nima", 1000).show();
					if (btnreturn.getText().toString() == "�⻷") {// �����ǰ��ť���ַ���"�ڻ�"
						int tangleTorun;
						if (messlukou.equals("���ڶ�·����")) {
							tangleTorun = 0;
						} else if (messlukou.equals("����·�����")) {
							tangleTorun = 1;
						} else if (messlukou.equals("��ҵ·���")) {
							tangleTorun = 2;
						} else if (messlukou.equals("��ҵ·����")) {
							tangleTorun = 3;
						} else if (messlukou.equals("������������")) {
							tangleTorun = 4;
						} else if (messlukou.equals("�����������")) {
							tangleTorun = 5;
						} else if (messlukou.equals("�ƻ���·����")) {
							tangleTorun = 6;
						} else if (messlukou.equals("�ƻ���·���")) {
							tangleTorun = 7;
						} else if (messlukou.equals("����·����")) {
							tangleTorun = 8;
						} else if (messlukou.equals("����·����")) {
							tangleTorun = 9;
						} else if (messlukou.equals("����·���")) {
							tangleTorun = 10;
						} else if (messlukou.equals("����·����")) {
							tangleTorun = 11;
						} else if (messlukou.equals("˫���ų���")) {
							tangleTorun = 12;
						} else if (messlukou.equals("�����ų���")) {
							tangleTorun = 13;
						} else if (messlukou.equals("����·���")) {
							tangleTorun = 14;
						} else if (messlukou.equals("��������������")) {
							tangleTorun = 15;
						} else if (messlukou.equals("�������������")) {
							tangleTorun = 16;
						} else if (messlukou.equals("����·����")) {
							tangleTorun = 17;
						} else if (messlukou.equals("��ľ�����")) {
							tangleTorun = 18;
						} else if (messlukou.equals("����·����")) {
							tangleTorun = 19;
						} else if (messlukou.equals("�о��������")) {
							tangleTorun = 20;
						} else if (messlukou.equals("���·����")) {
							tangleTorun = 21;
						} else if (messlukou.equals("���������")) {
							tangleTorun = 22;
						} else if (messlukou.equals("���¸ɵ�����")) {
							tangleTorun = 23;
						} else if (messlukou.equals("���¸ɵ����")) {
							tangleTorun = 24;
						} else if (messlukou.equals("�𳵱�վ���")) {
							tangleTorun = 25;
						} else if (messlukou.equals("����̳���")) {
							tangleTorun = 26;
						} else if (messlukou.equals("��ɳ·���")) {
							tangleTorun = 27;
						} else if (messlukou.equals("����ӳ���")) {
							tangleTorun = 28;
						} else if (messlukou.equals("��Ϫ��·���")) {
							tangleTorun = 29;
						} else {
							Toast.makeText(RoadOne.this, "��������Ҫ��������Ϣ", 1000)
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
								.inflate(R.layout.layout1, null);// ���������ļ���
						frame = new FrameLayout(RoadOne.this);
						setContentView(frame);
						myView1 = new MyView(RoadOne.this);
						frame.addView(myView1);
						frame.addView(myView);
						frame.addView(itemView);
						btnreturn = (Button) findViewById(R.id.BtnReturn);
						btnreturn.setText("�⻷");
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
					} else {// ��ǰ��ť�ַ��ǡ��⻷��ʱ
						int tangleTorun;
						if (messlukou.equals("��ʯ��·����")) {
							tangleTorun = 0;
						} else if (messlukou.equals("��԰��·���")) {
							tangleTorun = 1;
						} else if (messlukou.equals("������·����")) {
							tangleTorun = 2;
						} else if (messlukou.equals("Ӫ�˽����")) {
							tangleTorun = 3;
						} else if (messlukou.equals("����ӳ���")) {
							tangleTorun = 4;
						} else if (messlukou.equals("ɳ��·����")) {
							tangleTorun = 5;
						} else if (messlukou.equals("�����г����")) {
							tangleTorun = 6;
						} else if (messlukou.equals("��վ����·����")) {
							tangleTorun = 7;
						} else if (messlukou.equals("���Ǵ������")) {
							tangleTorun = 8;
						} else if (messlukou.equals("���¸ɵ����")) {
							tangleTorun = 9;
						} else if (messlukou.equals("�컨��·���")) {
							tangleTorun = 10;
						} else if (messlukou.equals("����·����")) {
							tangleTorun = 11;
						} else if (messlukou.equals("����·���")) {
							tangleTorun = 12;
						} else if (messlukou.equals("����·����")) {
							tangleTorun = 13;
						} else if (messlukou.equals("ɼ����·����")) {
							tangleTorun = 14;
						} else if (messlukou.equals("ɼ����·���")) {
							tangleTorun = 15;
						} else if (messlukou.equals("˫�ֱ�֧·���")) {
							tangleTorun = 16;
						} else if (messlukou.equals("˫��· ˫��·����")) {
							tangleTorun = 17;
						} else if (messlukou.equals("ţ�п����")) {
							tangleTorun = 18;
						} else if (messlukou.equals("����·����")) {
							tangleTorun = 19;
						} else if (messlukou.equals("����·���")) {
							tangleTorun = 20;
						} else if (messlukou.equals("������·����")) {
							tangleTorun = 21;
						} else if (messlukou.equals("�ƻ���·����")) {
							tangleTorun = 22;
						} else if (messlukou.equals("������·����")) {
							tangleTorun = 23;
						} else if (messlukou.equals("�����Ͻ����")) {
							tangleTorun = 24;
						} else if (messlukou.equals("��ҵ·����")) {
							tangleTorun = 25;
						} else if (messlukou.equals("��ҵ·���")) {
							tangleTorun = 26;
						} else if (messlukou.equals("������·����")) {
							tangleTorun = 27;
						} else if (messlukou.equals("����¥·���")) {
							tangleTorun = 28;
						} else if (messlukou.equals("���������")) {
							tangleTorun = 29;
						} else if (messlukou.equals("�����������")) {
							tangleTorun = 30;
						} else {
							Toast.makeText(RoadOne.this, "��������Ҫ��������Ϣ", 1000)
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
								.inflate(R.layout.layout1, null);// ���������ļ���
						frame = new FrameLayout(RoadOne.this);
						setContentView(frame);
						myView1 = new MyView(RoadOne.this);
						frame.addView(myView1);
						frame.addView(myView);
						frame.addView(itemView);
						btnreturn = (Button) findViewById(R.id.BtnReturn);
						btnreturn.setText("�ڻ�");
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
				if (btnreturn.getText().toString() == "�⻷") {
					frame.removeAllViews();
					final MyTurnplateView myView = new MyTurnplateView(
							RoadOne.this, mPointX, mPointY, mRadius, 31, 0);
					myView.setOnTurnplateListener(RoadOne.this);
					LayoutInflater inflater = LayoutInflater.from(RoadOne.this);
					View itemView = inflater.inflate(R.layout.layout1, null);// ���������ļ���
					frame = new FrameLayout(RoadOne.this);
					setContentView(frame);
					myView1 = new MyView(RoadOne.this);
					frame.addView(myView1);
					frame.addView(myView);
					frame.addView(itemView);
					btnreturn = (Button) findViewById(R.id.BtnReturn);
					btnreturn.setText("�ڻ�");
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
					View itemView = inflater.inflate(R.layout.layout1, null);// ���������ļ���
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
					btnreturn.setText("�⻷");
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

	// ����һ��myview�࣬����ʾ����
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
			// ��ȡLayoutInflaterʵ��
			dismiss = true;
			inflater = (LayoutInflater) this
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			// �����main��������inflate�м����Ŷ����ǰ����ֱ��this.setContentView()�İɣ��Ǻ�
			// �÷������ص���һ��View�Ķ����ǲ����еĸ�
			layout = inflater.inflate(R.layout.window, null);
			imageView = (ImageView) layout.findViewById(R.id.picture);
			imageView2 = (ImageView) layout.findViewById(R.id.picture2);
			windowTextView = (TextView) layout.findViewById(R.id.windowtext);

			if (FinalFlag == 0) {
				switch (flag) {
				case 29:
					imageView.setImageResource(R.drawable.k2_30);
					windowTextView.setText("���ڶ�·����");
					break;
				case 28:
					imageView.setImageResource(R.drawable.k2_29);
					windowTextView.setText("����·�����");
					break;
				case 27:
					imageView.setImageResource(R.drawable.k2_28);
					windowTextView.setText("��ҵ·���");
					break;
				case 26:
					imageView.setImageResource(R.drawable.k2_27);
					windowTextView.setText("��ҵ·����");
					break;
				case 25:
					imageView.setImageResource(R.drawable.k2_26);
					windowTextView.setText("������������");
					break;
				case 24:
					imageView.setImageResource(R.drawable.k2_25);
					windowTextView.setText("�����������");
					break;
				case 23:
					imageView.setImageResource(R.drawable.k2_24);
					windowTextView.setText("�ƻ���·����");
					break;
				case 22:
					imageView.setImageResource(R.drawable.k2_23);
					windowTextView.setText("�ƻ���·���");
					break;
				case 21:
					imageView.setImageResource(R.drawable.k2_22);
					windowTextView.setText("����·����");
					break;
				case 20:
					imageView.setImageResource(R.drawable.k2_21);
					windowTextView.setText("����·����");
					break;
				case 19:
					imageView.setImageResource(R.drawable.k2_20);
					windowTextView.setText("����·���");
					break;
				case 18:
					imageView.setImageResource(R.drawable.k2_19);
					windowTextView.setText("����·����");
					break;
				case 17:
					imageView.setImageResource(R.drawable.k2_18);
					windowTextView.setText("˫���ų���");
					break;
				case 16:
					imageView.setImageResource(R.drawable.k2_17);
					windowTextView.setText("�����ų���");
					break;
				case 15:
					imageView.setImageResource(R.drawable.k2_16);
					windowTextView.setText("����·���");
					break;
				case 14:
					imageView.setImageResource(R.drawable.k2_15);
					windowTextView.setText("��������������");
					break;
				case 13:
					imageView.setImageResource(R.drawable.k2_14);
					windowTextView.setText("�������������");
					break;
				case 12:
					imageView.setImageResource(R.drawable.k2_13);
					windowTextView.setText("����·����");
					break;
				case 11:
					imageView.setImageResource(R.drawable.k2_12);
					windowTextView.setText("��ľ�����");
					break;
				case 10:
					imageView.setImageResource(R.drawable.k2_11);
					windowTextView.setText("����·����");
					break;
				case 9:
					imageView.setImageResource(R.drawable.k2_10);
					windowTextView.setText("�о��������");
					break;
				case 8:
					imageView.setImageResource(R.drawable.k2_9);
					windowTextView.setText("���·����");
					break;
				case 7:
					imageView.setImageResource(R.drawable.k2_8);
					windowTextView.setText("���������");
					break;
				case 6:
					imageView.setImageResource(R.drawable.k2_7);
					windowTextView.setText("���¸ɵ�����");
					break;
				case 5:
					imageView.setImageResource(R.drawable.k2_6);
					windowTextView.setText("���¸ɵ����");
					break;
				case 4:
					imageView.setImageResource(R.drawable.k2_5);
					windowTextView.setText("�𳵱�վ���");
					break;
				case 3:
					imageView.setImageResource(R.drawable.k2_4);
					windowTextView.setText("����̳���");
					break;
				case 2:
					imageView.setImageResource(R.drawable.k2_3);
					windowTextView.setText("��ɳ·���");
					break;
				case 1:
					imageView.setImageResource(R.drawable.k2_2);
					windowTextView.setText("����ӳ���");
					break;
				case 0:
					imageView.setImageResource(R.drawable.k2_1);
					windowTextView.setText("��Ϫ��·���");
					break;
				default:
					break;
				}
				imageView2.setImageResource(R.drawable.chukou);
			} else if (FinalFlag == 1) {
				switch (flag) {
				case 0:
					imageView.setImageResource(R.drawable.k1_1);
					windowTextView.setText("��ʯ��·����");
					break;
				case 1:
					imageView.setImageResource(R.drawable.k1_2);
					windowTextView.setText("��԰��·���");
					break;
				case 2:
					imageView.setImageResource(R.drawable.k1_3);
					windowTextView.setText("������·����");
					break;
				case 3:
					imageView.setImageResource(R.drawable.k1_4);
					windowTextView.setText("Ӫ�˽����");
					break;
				case 4:
					imageView.setImageResource(R.drawable.k1_5);
					windowTextView.setText("����ӳ���");
					break;
				case 5:
					imageView.setImageResource(R.drawable.k1_6);
					windowTextView.setText("ɳ��·����");
					break;
				case 6:
					imageView.setImageResource(R.drawable.k1_7);
					windowTextView.setText("�����г����");
					break;
				case 7:
					imageView.setImageResource(R.drawable.k1_8);
					windowTextView.setText("��վ����·����");
					break;
				case 8:
					imageView.setImageResource(R.drawable.k1_9);
					windowTextView.setText("���Ǵ������");
					break;
				case 9:
					imageView.setImageResource(R.drawable.k1_10);
					windowTextView.setText("���¸ɵ����");
					break;
				case 10:
					imageView.setImageResource(R.drawable.k1_11);
					windowTextView.setText("�컨��·���");
					break;
				case 11:
					imageView.setImageResource(R.drawable.k1_12);
					windowTextView.setText("����·����");
					break;
				case 12:
					imageView.setImageResource(R.drawable.k1_13);
					windowTextView.setText("����·���");
					break;
				case 13:
					imageView.setImageResource(R.drawable.k1_14);
					windowTextView.setText("����·����");
					break;
				case 14:
					imageView.setImageResource(R.drawable.k1_15);
					windowTextView.setText("ɼ����·����");
					break;
				case 15:
					imageView.setImageResource(R.drawable.k1_16);
					windowTextView.setText("ɼ����·���");
					break;
				case 16:
					imageView.setImageResource(R.drawable.k1_17);
					windowTextView.setText("˫�ֱ�֧·���");
					break;
				case 17:
					imageView.setImageResource(R.drawable.k1_18);
					windowTextView.setText("˫��· ˫��·����");
					break;
				case 18:
					imageView.setImageResource(R.drawable.k1_19);
					windowTextView.setText("ţ�п����");
					break;
				case 19:
					imageView.setImageResource(R.drawable.k1_20);
					windowTextView.setText("����·����");
					break;
				case 20:
					imageView.setImageResource(R.drawable.k1_21);
					windowTextView.setText("����·���");
					break;
				case 21:
					imageView.setImageResource(R.drawable.k1_22);
					windowTextView.setText("������·����");
					break;
				case 22:
					imageView.setImageResource(R.drawable.k1_23);
					windowTextView.setText("�ƻ���·����");
					break;
				case 23:
					imageView.setImageResource(R.drawable.k1_24);
					windowTextView.setText("������·����");
					break;
				case 24:
					imageView.setImageResource(R.drawable.k1_25);
					windowTextView.setText("�����Ͻ����");
					break;
				case 25:
					imageView.setImageResource(R.drawable.k1_26);
					windowTextView.setText("��ҵ·����");
					break;
				case 26:
					imageView.setImageResource(R.drawable.k1_27);
					windowTextView.setText("��ҵ·���");
					break;
				case 27:
					imageView.setImageResource(R.drawable.k1_28);
					windowTextView.setText("������· ����");
					break;
				case 28:
					imageView.setImageResource(R.drawable.k1_29);
					windowTextView.setText("����¥·���");
					break;
				case 29:
					imageView.setImageResource(R.drawable.k1_30);
					windowTextView.setText("���������");
					break;
				case 30:
					imageView.setImageResource(R.drawable.k1_31);
					windowTextView.setText("�����������");
					break;
				default:
					break;
				}
				imageView2.setImageResource(R.drawable.rukou);
			}
			// ��������Ҫ�����ˣ����������ҵ�layout���뵽PopupWindow���أ������ܼ�
			menuWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT); // ������������width��height
			layout.setBackgroundResource(R.drawable.popupwindow);

			ColorDrawable cd = new ColorDrawable(-0000);
			menuWindow.setBackgroundDrawable(cd);
			menuWindow.setFocusable(true);
			menuWindow.setOutsideTouchable(true);

			// menuWindow.showAsDropDown(layout); //���õ���Ч��
			// menuWindow.showAsDropDown(null, 0, layout.getHeight());
			// menuWindow.showAsDropDown(null, 0, (int)mPointY/2);
			// ��λ�ȡ����main�еĿؼ��أ�Ҳ�ܼ�
			if (FinalFlag == 0) {
				menuWindow.showAsDropDown(total2btn);
				menuWindow.showAtLocation(this.findViewById(R.id.window),
						Gravity.TOP | Gravity.RIGHT, 0, (int) mPointY / 2); // ����layout��PopupWindow����ʾ��λ��
			} else if (FinalFlag == 1) {// ����λ��
				menuWindow.showAsDropDown(total1btn);
				menuWindow.showAtLocation(this.findViewById(R.id.window),
						Gravity.TOP | Gravity.LEFT, 0, (int) mPointY / 2); // ����layout��PopupWindow����ʾ��λ��
			}

			menuWindow.update();

			menuWindow.setTouchInterceptor(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					/**** ��������popupwindow���ⲿ��popupwindowҲ����ʧ ****/
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

			myCloseBtn.setOnClickListener(new View.OnClickListener() { // "�������"��ť����Ӧ�¼�
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
						bundle.putInt("round", 1);// ��ʾ��Ȧ
					} else if (FinalFlag == 1) {
						bundle.putInt("round", 2);// ��ʾ��Ȧ
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
						bundle.putInt("round", 1);// ��ʾ��Ȧ
					} else if (FinalFlag == 1) {
						bundle.putInt("round", 2);// ��ʾ��Ȧ
					}
					intent1.putExtras(bundle);
					RoadOne.this.startActivity(intent1);

				}
			});
		}
	}

	@Override
	public void onMyPointTouch(POINT point) {// ��Ȧ
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
			if (angle < 60 && angle >= 0 && py < MPointY)// ��ʾ�������һ������
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
			View itemView = inflater.inflate(R.layout.layout1, null);// ���������ļ���
			frame = new FrameLayout(RoadOne.this);
			setContentView(frame);
			myView1 = new MyView(RoadOne.this);
			frame.addView(myView1);
			frame.addView(myView);
			frame.addView(itemView);
			btnreturn = (Button) findViewById(R.id.BtnReturn);
			btnreturn.setText("�⻷");
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
			View itemView = inflater.inflate(R.layout.layout1, null);// ���������ļ���
			frame = new FrameLayout(RoadOne.this);
			setContentView(frame);
			myView1 = new MyView(RoadOne.this);
			frame.addView(myView1);
			frame.addView(myView);
			frame.addView(itemView);
			btnreturn = (Button) findViewById(R.id.BtnReturn);
			btnreturn.setText("�ڻ�");
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
		if (FinalFlag == 0)// ��Ȧ
		{
			switch (i) {
			case 29:
				Toast.makeText(RoadOne.this,
						"��Ϫ��·���->���ڶ�·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 28:
				Toast.makeText(RoadOne.this,
						"���ڶ�·���->����·����ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 27:
				Toast.makeText(RoadOne.this,
						"����·�����->��ҵ·��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 26:
				Toast.makeText(RoadOne.this,
						"��ҵ·���->��ҵ·����-����ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 25:
				Toast.makeText(RoadOne.this,
						"��ҵ·����->�����������ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 24:
				Toast.makeText(RoadOne.this,
						"������������->����������ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 23:
				Toast.makeText(RoadOne.this,
						"�����������->�ƻ���·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 22:
				Toast.makeText(RoadOne.this,
						"�ƻ���·����->�ƻ���·��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 21:
				Toast.makeText(RoadOne.this,
						"�ƻ���·���->����·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 20:
				Toast.makeText(RoadOne.this,
						"����·����->����·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 19:
				Toast.makeText(RoadOne.this,
						"����·����->����·��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 18:
				Toast.makeText(RoadOne.this,
						"����·���->����·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 17:
				Toast.makeText(RoadOne.this,
						"�����ų���->˫���ų��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 16:
				Toast.makeText(RoadOne.this,
						"˫���ų���->�����ų��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 15:
				Toast.makeText(RoadOne.this,
						"�����ų���->����·��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 14:
				Toast.makeText(RoadOne.this,
						"����·���->�������������ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 13:
				Toast.makeText(RoadOne.this,
						"ɼ������������->������������ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 12:
				Toast.makeText(RoadOne.this,
						"ɼ�����������->����·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 11:
				Toast.makeText(RoadOne.this,
						"����·����->��ľ����ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 10:
				Toast.makeText(RoadOne.this,
						"��ľ�����->����·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 9:
				Toast.makeText(RoadOne.this,
						"����·����->�о�������ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 8:
				Toast.makeText(RoadOne.this,
						"�����������->���·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 7:
				Toast.makeText(RoadOne.this,
						"���·����->��������ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 6:
				Toast.makeText(RoadOne.this,
						"���������->���¸ɵ����ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 5:
				Toast.makeText(RoadOne.this,
						"���¸ɵ�����->���¸ɵ���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 4:
				Toast.makeText(RoadOne.this,
						"���¸ɵ����->�𳵱�վ��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 3:
				Toast.makeText(RoadOne.this,
						"�𳵱�վ���->����̳��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 2:
				Toast.makeText(RoadOne.this,
						"����̳���->��ɳ·��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 1:
				Toast.makeText(RoadOne.this,
						"��ɳ·���->����ӳ��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 0:
				Toast.makeText(RoadOne.this,
						"����ӳ���->��Ϫ��·��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			default:
				break;
			}
		} else if (FinalFlag == 1) {
			switch (i) {
			case 30:
				Toast.makeText(RoadOne.this,
						"�����������->��ʯ��·����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 0:
				Toast.makeText(RoadOne.this,
						"��ʯ��·����->��԰��·���,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 1:
				Toast.makeText(RoadOne.this,
						"��԰��·���->������·����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 2:
				Toast.makeText(RoadOne.this,
						"������·����->Ӫ�˽����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 3:
				Toast.makeText(RoadOne.this,
						"Ӫ�˽����->����ӳ���,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 4:
				Toast.makeText(RoadOne.this,
						"����ӳ���->ɳ��·����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 5:
				Toast.makeText(RoadOne.this,
						"ɳ��·����->�����г����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 6:
				Toast.makeText(RoadOne.this,
						"�����г����->��վ����·����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 7:
				Toast.makeText(RoadOne.this,
						"��վ����·����->���Ǵ������,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 8:
				Toast.makeText(RoadOne.this,
						"���Ǵ������->���¸ɵ����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 9:
				Toast.makeText(RoadOne.this,
						"���¸ɵ����->�컨��·���,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 10:
				Toast.makeText(RoadOne.this,
						"�컨��·���->����·����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 11:
				Toast.makeText(RoadOne.this,
						"����·����->����·���,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 12:
				Toast.makeText(RoadOne.this,
						"����·���->����·����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 13:
				Toast.makeText(RoadOne.this,
						"����·����->ɼ����·����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 14:
				Toast.makeText(RoadOne.this,
						"ɼ����·����->ɼ����·���,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 15:
				Toast.makeText(RoadOne.this,
						"ɼ����·���->˫�ֱ�֧·���,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 16:
				Toast.makeText(RoadOne.this,
						"˫�ֱ�֧·���->˫��· ˫��·����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 17:
				Toast.makeText(RoadOne.this,
						"˫��·˫���ų���->ţ�п����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 18:
				Toast.makeText(RoadOne.this,
						"ţ�п����->����·����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 19:
				Toast.makeText(RoadOne.this,
						"����·����->����·���,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 20:
				Toast.makeText(RoadOne.this,
						"����·���->������·����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 21:
				Toast.makeText(RoadOne.this,
						"������·����->�ƻ���·����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 22:
				Toast.makeText(RoadOne.this,
						"�ƻ���·����->������·����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 23:
				Toast.makeText(RoadOne.this,
						"������·����->�����Ͻ����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 24:
				Toast.makeText(RoadOne.this,
						"�����Ͻ����->��ҵ·����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 25:
				Toast.makeText(RoadOne.this,
						"��ҵ·����->��ҵ·���,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 26:
				Toast.makeText(RoadOne.this,
						"��ҵ·���->������· ����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 27:
				Toast.makeText(RoadOne.this,
						"������·����->����¥·���,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 28:
				Toast.makeText(RoadOne.this,
						"����¥·���->���������,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 29:
				Toast.makeText(RoadOne.this,
						"���������->�����������,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			default:
				break;
			}
		}

	}

}
