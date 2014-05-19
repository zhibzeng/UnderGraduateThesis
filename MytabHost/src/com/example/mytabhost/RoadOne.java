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
	private FrameLayout frame = null;
	private int height, width;
	private Button total1btn = null;
	private Button total2btn = null;
	private Button icon_represent1 = null;
	private Button icon_represent2 = null;	
	private Button btnsearch = null;
	private AutoCompleteTextView editTextlukou = null;
	private static int FinalFlag = 1;// ��־λ��0ʱΪ��Ȧ��1ʱΪ��Ȧ
	//·����Ϣ�б�
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
		//��ʼ��
		renderView(FinalFlag,(float)(-height*0.86),mPointY,mRadius,0);
	}
	
	/**
	 * ���ɲ��ֻ���
	 * @param flag
	 * @param px
	 * @param py
	 * @param radius
	 * @param tangleTorun
	 */
	private void renderView(int flag,float px,float py,float radius,float tangleTorun){
		//flag 0 �ڻ� flag 1 �⻷
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
		View itemView = inflater.inflate(R.layout.layout1, null);// ���������ļ���
		frame = new FrameLayout(RoadOne.this);
		setContentView(frame);
		myView1 = new MyView(RoadOne.this);
		frame.addView(myView1);
		frame.addView(myView);
		frame.addView(itemView);
		btnreturn = (Button) findViewById(R.id.BtnReturn);
		if(flag==0){
			btnreturn.setText("�ڻ�");
		}else{
			btnreturn.setText("�⻷");
		}
		
		btnreturn.setOnClickListener(new BtnClickListener());
		//�ڻ�ȫ��
		total1btn = (Button) findViewById(R.id.TotalRoad1);
		//�⻷ȫ��
		total2btn = (Button) findViewById(R.id.TotalRoad2);
		//�ڻ�ͼ��
		icon_represent1 = (Button) findViewById(R.id.icon_represent1);
		//�⻷ͼ��
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
 * ���������¼�
 *	@author JeffreyTseng
 *
 */
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
				final TotalRoad yourview = new TotalRoad(RoadOne.this, width,height);
				yourview.setMyTouchListener(RoadOne.this);
				frame = new FrameLayout(RoadOne.this);
				setContentView(frame);
				frame.addView(yourview);
			}
			if (v == total2btn) {// ��Ȧʵ�ַ���
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
				// �����main��������inflate�м����Ŷ����ǰ����ֱ��this.setContentView()�İɣ��Ǻ�
				// �÷������ص���һ��View�Ķ����ǲ����еĸ�
				layout = inflater.inflate(R.layout.illustrate_pics, null);
				menuWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT); // ������������width��height
				layout.setBackgroundResource(R.drawable.popupwindow);
				ColorDrawable cd = new ColorDrawable(-0000);
				menuWindow.setBackgroundDrawable(cd);
				menuWindow.setFocusable(true);
				menuWindow.setOutsideTouchable(true);
				menuWindow.showAsDropDown(icon_represent1);
				menuWindow.showAtLocation(RoadOne.this.findViewById(R.id.window),
						Gravity.TOP | Gravity.LEFT, 0, (int) mPointY / 2); // ����layout��PopupWindow����ʾ��λ��
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
				
			}
			if (v == icon_represent2) {
				inflater = (LayoutInflater) RoadOne.this.getSystemService(LAYOUT_INFLATER_SERVICE);
				// �����main��������inflate�м����Ŷ����ǰ����ֱ��this.setContentView()�İɣ��Ǻ�
				// �÷������ص���һ��View�Ķ����ǲ����еĸ�
				layout = inflater.inflate(R.layout.illustrate_pics, null);
				menuWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT); // ������������width��height
				layout.setBackgroundResource(R.drawable.popupwindow);
				ColorDrawable cd = new ColorDrawable(-0000);
				menuWindow.setBackgroundDrawable(cd);
				menuWindow.setFocusable(true);
				menuWindow.setOutsideTouchable(true);
				menuWindow.showAsDropDown(icon_represent2);
				menuWindow.showAtLocation(RoadOne.this.findViewById(R.id.window),
						Gravity.TOP | Gravity.LEFT, 0, (int) mPointY / 2); // ����layout��PopupWindow����ʾ��λ��
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
			}
			if (v == btnsearch) {// ������ť
				String messlukou = editTextlukou.getText().toString();
				if (messlukou.compareTo("") == 0) {
					Toast.makeText(RoadOne.this, "��������Ҫ��������Ϣ", 1000).show();
				} else {
					if (btnreturn.getText().toString() == "�⻷") {
						// �����ǰ��ť���ַ���"�⻷"
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
							Toast.makeText(RoadOne.this, "��������Ҫ��������Ϣ", 1000)
									.show();
							tangleTorun = 0;
						}
						float tangle = (float) (-(360.0/CrossingListOut.size())*tangleTorun);
						renderView(1,(float)(-height*0.86),mPointY,mRadius,tangle);

					} else {
						// ��ǰ��ť�ַ��ǡ��⻷��ʱ
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
							Toast.makeText(RoadOne.this, "��������Ҫ��������Ϣ", 1000)
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
				// �л��ڻ�/�⻷ ��ť
				if (btnreturn.getText().toString() == "�⻷") {
					renderView(0,mPointX,mPointY,mRadius,0);
				} else {
					//׼���л����⻷
					renderView(1,(float)(-height*0.86),mPointY,mRadius,0);
				}
			}
		}

	}

	
	/**
	 * ���·�ڵ���ͼƬ
	 */
	@Override
	public void onPointTouch(Point point) {

		final int flag = point.flag;
		final CrossingEntity entity = point.entity;
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
			TransferData t = new TransferData();
			if (FinalFlag == 0) {
					//�ڻ�
					imageView.setImageBitmap(t.getCrossingBitmap(RoadOne.this,FinalFlag, entity.getId()));
					windowTextView.setText(entity.getName());
					imageView2.setImageResource(R.drawable.chukou);
			} else if (FinalFlag == 1) {
					//�⻷
					imageView.setImageBitmap(t.getCrossingBitmap(RoadOne.this,FinalFlag, entity.getId()));
					windowTextView.setText(entity.getName());
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
					bundle.putInt("crossingid", entity.getId());
					if (FinalFlag == 0) {
						bundle.putInt("round", 0);// ��ʾ��Ȧ
					} else if (FinalFlag == 1) {
						bundle.putInt("round", 1);// ��ʾ��Ȧ
					}
					intent1.putExtras(bundle);
					RoadOne.this.startActivity(intent1);

				}
			});

			/**
			 * ����·����Ƭ
			 */
			mCloseBtn2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent1 = new Intent();
					intent1.setClass(RoadOne.this, PhotoShow.class);

					Bundle bundle = new Bundle();
					bundle.putInt("index", flag);
					if (FinalFlag == 0) {
						bundle.putInt("round", 3); //round 3 ��ʾ��ʾ�����Ƭ
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
	 *  ����һ��myview�࣬����ʾ����
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
	 * ���ȫ��ʱ�������ɵ�ǰ����
	 * @author JeffreyTseng
	 */
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
	 * ���·�Σ���ʾ�ٶ�
	 * @author JeffreyTseng
	 */
	@Override
	public void onPointTouchRing(int i, int speed) {
		// TODO Auto-generated method stub
		// Toast.makeText(RoadOne.this, i+"", 1000).show();
		String Sspeed = speed + "";
		if (FinalFlag == 1)// �⻷
		{
			switch (i) {
			case 27:
				Toast.makeText(RoadOne.this,
						"��Ϫ��·���->���ڶ�·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 26:
				Toast.makeText(RoadOne.this,
						"���ڶ�·���->����·����ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 25:
				Toast.makeText(RoadOne.this,
						"����·�����->��ҵ·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 24:
				Toast.makeText(RoadOne.this,
						"��ҵ·����->��ҵ·���-����ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 23:
				Toast.makeText(RoadOne.this,
						"��ҵ·���->������·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 22:
				Toast.makeText(RoadOne.this,
						"������·����->������·��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 21:
				Toast.makeText(RoadOne.this,
						"������·���->�ƻ���·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 20:
				Toast.makeText(RoadOne.this,
						"�ƻ���·����->�ƻ���·��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 19:
				Toast.makeText(RoadOne.this,
						"�ƻ���·���->����·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 18:
				Toast.makeText(RoadOne.this,
						"����·����->����·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 17:
				Toast.makeText(RoadOne.this,
						"����·����->����·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 16:
				Toast.makeText(RoadOne.this,
						"����·����->˫��·��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 15:
				Toast.makeText(RoadOne.this,
						"˫��·���->˫��·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 14:
				Toast.makeText(RoadOne.this,
						"˫��·����->˫��·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 13:
				Toast.makeText(RoadOne.this,
						"˫��·����->����·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 12:
				Toast.makeText(RoadOne.this,
						"����·����->ɼ����·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 11:
				Toast.makeText(RoadOne.this,
						"ɼ����·����->ɼ����·��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 10:
				Toast.makeText(RoadOne.this,
						"ɼ����·���->���豱·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 9:
				Toast.makeText(RoadOne.this,
						"���豱·����->���豱·��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 8:
				Toast.makeText(RoadOne.this,
						"���豱·���->����·���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 7:
				Toast.makeText(RoadOne.this,
						"����·����->������·��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 6:
				Toast.makeText(RoadOne.this,
						"������·���->�����Žֳ��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 5:
				Toast.makeText(RoadOne.this,
						"�����Žֳ���->���¸ɵ����ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000).show();
				break;
			case 4:
				Toast.makeText(RoadOne.this,
						"���¸ɵ�����->���¸ɵ���ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 3:
				Toast.makeText(RoadOne.this,
						"���¸ɵ����->����̳��ڣ���ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
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
		} else if (FinalFlag == 0) {
			//�ڻ�
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
						"��վ����·����->���¸ɵ�����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
						.show();
				break;
			case 8:
				Toast.makeText(RoadOne.this,
						"���¸ɵ�����->���¸ɵ����,��ǰƽ��ʱ��Ϊ" + Sspeed + "km/h", 1000)
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
