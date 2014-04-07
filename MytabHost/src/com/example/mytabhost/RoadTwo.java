package com.example.mytabhost;


import java.lang.reflect.Field;
import java.security.PublicKey;
import java.util.Timer;
import java.util.TimerTask;

import android.R.bool;
import android.R.layout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mytabhost.MyTurnplateView2.Point;
import com.example.mytabhost.MyTurnplateView2.OnTurnplateListener2;
import com.example.mytabhost.RoadOne.BtnClickListener;
import com.example.mytabhost.RoadOne.MyView;
import com.example.mytabhost.TotalRoad.MyTouchListener;
import com.example.mytabhost.TotalRoad.POINT;
import com.example.mytabhost.TotalRoadBus.MyTouchListener2;
import com.example.mytabhost.db.DateBaseHelper;




@SuppressLint("NewApi")
public class RoadTwo extends Activity implements OnTurnplateListener2,MyTouchListener2
{
	
	private float mPointX=0, mPointY=0;
	private float mRadius = 0;
	private int t=0;
	private float x=0,y=0;
	private double w=1;
	//private MyView myView1;
	private Button btnreturn;//���⻷�л���ť
    private	ImageView imageView;
    private  Handler handler=null;
    private PopupWindow menuWindow;
	private LayoutInflater inflater;
	private LinearLayout mClose;
    private LinearLayout mCloseBtn;
    private LinearLayout ad1;
    private LinearLayout ad2;
    private WebView wv=null;
    
    private Button myCloseBtn;
    private TextView windowTextView;
    private View layout;	
    private boolean dismiss=false;
    private MyView myView1;
    private FrameLayout frame;
    private int height,width;
    private Button total1btn=null;
    private Button total2btn=null;
    private static int FinalFlag=0;//��־λ��0ʱΪ��Ȧ��1ʱΪ��Ȧ
    
    private Button btnsearch=null;
    
    private ImageView ad1View=null;
    private ImageView ad2View=null;
    
    private AutoCompleteTextView autoCompleteTextView=null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
		//int height = getWindowManager().getDefaultDisplay().getHeight();
		
		height=metric.heightPixels;
		width=metric.widthPixels;
		mPointX=(float)(width+height*0.86);
		mPointY=height/2;
		mRadius=(float)height;
		
		final MyTurnplateView2 myView = new MyTurnplateView2(this,mPointX, mPointY,mRadius,28,0);
		
		myView.setOnTurnplateListener2(this);	
		
		LayoutInflater inflater=LayoutInflater.from(this);
		
		
		View itemView = inflater.inflate(R.layout.layout2,null);//���������ļ���   
		
//		FrameLayout
		
		//final TotalRoad yourview=new TotalRoad(this, width, height);
		frame=new FrameLayout(this); 
		setContentView(frame);
		myView1=new MyView(this);
		frame.addView(myView1);
		frame.addView(myView);
		//frame.addView(yourview);
		frame.addView(itemView);
		
		
		autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.acEditText2);
		autoCompleteTextView.setText("");
		AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(RoadTwo.this, android.R.layout.simple_dropdown_item_1line,null, DateBaseHelper.NAME, android.R.id.text1,0,1);
		((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setThreshold(1);
	    ((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setAdapter(cursorAdapter);
		
		btnreturn=(Button)findViewById(R.id.BtnReturn2);
		btnreturn.setText("K1");
		btnreturn.setOnClickListener(new BtnClickListener());
		
		
		total1btn=(Button)findViewById(R.id.TotalRoad12);
		total1btn.setOnClickListener(new BtnClickListener());
		
	    
		total2btn=(Button)findViewById(R.id.TotalRoad22);
		total2btn.setOnClickListener(new BtnClickListener());
		
		btnsearch=(Button)findViewById(R.id.BtnSearch2);
		btnsearch.setOnClickListener(new BtnClickListener());
		
		if (FinalFlag==0) {
			total1btn.setVisibility(View.VISIBLE);
			total2btn.setVisibility(View.GONE);
		}
		else if (FinalFlag==1) {
			total2btn.setVisibility(View.VISIBLE);
			total1btn.setVisibility(View.GONE);
		}
	}
	
	class BtnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(dismiss==true)//���ڵ���л�ʱδ�ر�pop����
			{
				menuWindow.dismiss();
				dismiss=false;
			}
			if (v==total1btn) {
				frame.removeAllViews();
				final TotalRoadBus yourview=new TotalRoadBus(RoadTwo.this, width, height);	
				yourview.setMyTouchListener2(RoadTwo.this);
				frame=new FrameLayout(RoadTwo.this); 
				setContentView(frame);
				frame.addView(yourview);
			}
			if (v==total2btn) {//��Ȧʵ�ַ���
				//total1btn.setVisibility(View.VISIBLE);
				//total2btn.setVisibility(View.GONE);
				frame.removeAllViews();
				final TotalRoadBus yourview=new TotalRoadBus(RoadTwo.this, width, height);	
				yourview.setMyTouchListener2(RoadTwo.this);
				frame=new FrameLayout(RoadTwo.this); 
				setContentView(frame);
				frame.addView(yourview);
			}
			if (v==btnsearch) {
				String messlukou=autoCompleteTextView.getText().toString();
				if (messlukou.compareTo("")==0) {
					Toast.makeText(RoadTwo.this, "��������Ҫ��������Ϣ", 1000).show();
				}
			  else {
					//Toast.makeText(RoadOne.this, "nima", 1000).show();
				if(btnreturn.getText().toString()=="K1") {//�����ǰ��ť���ַ���"K2"
					int tangleTorun;
					if (messlukou.equals("˫���������ű�վ")) {
						tangleTorun=0;
					}else if(messlukou.equals("˫������������վ")){
						tangleTorun=1;
					}else if(messlukou.equals("ţ�п�վ")){
						tangleTorun=2;
					}else if(messlukou.equals("������·��վ")){
						tangleTorun=3;
					}else if(messlukou.equals("����·��վ")){
						tangleTorun=4;
					}else if(messlukou.equals("���ʹ���վ")){
						tangleTorun=5;
					}else if(messlukou.equals("������԰վ")){
						tangleTorun=6;
					}else if(messlukou.equals("�ƻ�·��վ")){
						tangleTorun=7;
					}else if(messlukou.equals("����������վ")){
						tangleTorun=8;
					}else if(messlukou.equals("�Ͼ���·��վ")){
						tangleTorun=9;
					}else if(messlukou.equals("����¥��վ")){
						tangleTorun=10;
					}else if(messlukou.equals("����¥��վ")){
						tangleTorun=11;
					}else if(messlukou.equals("����·��վ")){
						tangleTorun=12;
					}else if(messlukou.equals("��ˮ��վ")){
						tangleTorun=13;
					}else if(messlukou.equals("�⻪������վ")){
						tangleTorun=14;
					}else if(messlukou.equals("��������վ")){
						tangleTorun=15;
					}else if(messlukou.equals("��·�ڱ�վ")){
						tangleTorun=16;
					}else if(messlukou.equals("Ӫ�ſڱ�վ")){
						tangleTorun=17;
					}else if(messlukou.equals("���Ͻ���վ")){
						tangleTorun=18;
					}else if(messlukou.equals("��ó�����վ")){
						tangleTorun=19;
					}else if(messlukou.equals("�𳵱�վ��վ")){
						tangleTorun=20;
					}else if(messlukou.equals("����·��վ")){
						tangleTorun=21;
					}else if(messlukou.equals("����·��վ")){
						tangleTorun=22;
					}else if(messlukou.equals("����·��վ")){
						tangleTorun=23;
					}else if(messlukou.equals("�����Ͽ�·վ")){
						tangleTorun=24;
					}else if(messlukou.equals("ɼ����վ")){
						tangleTorun=25;
					}else if(messlukou.equals("˫�ֱ�֧··��վ")){
						tangleTorun=26;
					}else if(messlukou.equals("���곡վ")){
						tangleTorun=27;
					}else {
						Toast.makeText(RoadTwo.this, "��������Ҫ��������Ϣ", 1000).show();
						tangleTorun=0;
					}
					frame.removeAllViews();
					tangleTorun=(tangleTorun-13)*360/28;
					final MyTurnplateView2 myView = new MyTurnplateView2(RoadTwo.this,mPointX, mPointY,mRadius,28,tangleTorun);				
					myView.setOnTurnplateListener2(RoadTwo.this);	
					LayoutInflater inflater=LayoutInflater.from(RoadTwo.this);
					View itemView = inflater.inflate(R.layout.layout2,null);//���������ļ���   
					frame=new FrameLayout(RoadTwo.this); 
					setContentView(frame);
					myView1=new MyView(RoadTwo.this);
					frame.addView(myView1);
					frame.addView(myView);
					frame.addView(itemView);
					
					autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.acEditText2);
					autoCompleteTextView.setText("");
					AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(RoadTwo.this, android.R.layout.simple_dropdown_item_1line,null, DateBaseHelper.NAME, android.R.id.text1,0,1);
					((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setThreshold(1);
				    ((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setAdapter(cursorAdapter);
				    
				    
					btnreturn=(Button)findViewById(R.id.BtnReturn2);
					btnreturn.setText("K1");	
					btnreturn.setOnClickListener(new BtnClickListener());
					
					total1btn=(Button)findViewById(R.id.TotalRoad12);
					total1btn.setVisibility(View.VISIBLE);
					total1btn.setOnClickListener(new BtnClickListener());
					
					total2btn=(Button)findViewById(R.id.TotalRoad22);
					total2btn.setVisibility(View.GONE);
					total2btn.setOnClickListener(new BtnClickListener());
					
					
					btnsearch=(Button)findViewById(R.id.BtnSearch2);
					btnsearch.setOnClickListener(new BtnClickListener());
							
					FinalFlag=0;			
				}
				else {//��ǰ��ť�ַ��ǡ�k2��ʱ
					int tangleTorun;
					if(messlukou.equals("������·��վ")){
						tangleTorun=0;
					}else if(messlukou.equals("����·��վ")){
						tangleTorun=1;
					}else if(messlukou.equals("����·��վ")){
						tangleTorun=2;
					}else if(messlukou.equals("����·��վ")){
						tangleTorun=3;
					}else if(messlukou.equals("������վ")){
						tangleTorun=4;
					}else if(messlukou.equals("�𳵱�վ��վ")){
						tangleTorun=5;
					}else if(messlukou.equals("��ó�����վ")){
						tangleTorun=6;
					}else if(messlukou.equals("���Ͻ���վ")){
						tangleTorun=7;
					}else if(messlukou.equals("Ӫ�ſڱ�")){
						tangleTorun=8;
					}else if(messlukou.equals("��·�ڱ�վ")){
						tangleTorun=9;
					}else if(messlukou.equals("��������վ")){
						tangleTorun=10;
					}else if(messlukou.equals("�⻪��ֿ�վ")){
						tangleTorun=11;
					}else if(messlukou.equals("��ˮ��վ")){
						tangleTorun=12;
					}else if(messlukou.equals("����·��վ")){
						tangleTorun=13;
					}else if(messlukou.equals("����¥��վ")){
						tangleTorun=14;
					}else if(messlukou.equals("����¥��վ")){
						tangleTorun=15;
					}else if(messlukou.equals("�Ͼ���·��վ")){
						tangleTorun=16;
					}else if(messlukou.equals("����������վ")){
						tangleTorun=17;
					}else if(messlukou.equals("�ƻ�·��վ")){
						tangleTorun=18;
					}else if(messlukou.equals("������԰վ")){
						tangleTorun=19;
					}else if(messlukou.equals("���ʹ���վ")){
						tangleTorun=20;
					}else if(messlukou.equals("����·��վ")){
						tangleTorun=21;
					}else if(messlukou.equals("����·��վ")){
						tangleTorun=22;
					}else if(messlukou.equals("ţ�п�վ")){
						tangleTorun=23;
					}else if(messlukou.equals("˫������վ")){
						tangleTorun=24;
					}else if(messlukou.equals("˫���ӱ�վ")){
						tangleTorun=25;
					}else if(messlukou.equals("���곡վ")){
						tangleTorun=26;
					}else if(messlukou.equals("˫�ֱ�֧·��վ")){
						tangleTorun=27;
					}else {
						Toast.makeText(RoadTwo.this, "��������Ҫ��������Ϣ", 1000).show();
						tangleTorun=0;
					}
					frame.removeAllViews();
					tangleTorun=-tangleTorun*360/28;
					final MyTurnplateView2 myView = new MyTurnplateView2(RoadTwo.this,(float)(-height*0.86), mPointY,mRadius,29,tangleTorun);				
					myView.setOnTurnplateListener2(RoadTwo.this);	
					LayoutInflater inflater=LayoutInflater.from(RoadTwo.this);
					View itemView = inflater.inflate(R.layout.layout2,null);//���������ļ���   
					frame=new FrameLayout(RoadTwo.this); 
					setContentView(frame);
					myView1=new MyView(RoadTwo.this);
					frame.addView(myView1);
					frame.addView(myView);
					frame.addView(itemView);
					
					
					autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.acEditText2);
					autoCompleteTextView.setText("");
					AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(RoadTwo.this, android.R.layout.simple_dropdown_item_1line,null, DateBaseHelper.NAME, android.R.id.text1,0,0);
					((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setThreshold(1);
				    ((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setAdapter(cursorAdapter);
				    
				    
					btnreturn=(Button)findViewById(R.id.BtnReturn2);
					btnreturn.setText("K2");	
					btnreturn.setOnClickListener(new BtnClickListener());
					
					total1btn=(Button)findViewById(R.id.TotalRoad12);
					total1btn.setVisibility(0x00000004);			
					
					total2btn=(Button)findViewById(R.id.TotalRoad22);
					total2btn.setVisibility(View.VISIBLE);
					total2btn.setOnClickListener(new BtnClickListener());
					
					btnsearch=(Button)findViewById(R.id.BtnSearch2);
					btnsearch.setOnClickListener(new BtnClickListener());
					
					FinalFlag=1;
				}
			  }
				autoCompleteTextView.setText("");		
				
			}
			if(v==btnreturn)
			{
			    //finish();
				if(btnreturn.getText().toString()=="K1"){
				frame.removeAllViews();
				final MyTurnplateView2 myView = new MyTurnplateView2(RoadTwo.this,(float)(-height*0.86), mPointY,mRadius,29,0);				
				myView.setOnTurnplateListener2(RoadTwo.this);	
				LayoutInflater inflater=LayoutInflater.from(RoadTwo.this);
				View itemView = inflater.inflate(R.layout.layout2,null);//���������ļ���   
				frame=new FrameLayout(RoadTwo.this); 
				setContentView(frame);
				myView1=new MyView(RoadTwo.this);
				frame.addView(myView1);
				frame.addView(myView);
				frame.addView(itemView);
				
				
				autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.acEditText2);
				autoCompleteTextView.setText("");
				AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(RoadTwo.this, android.R.layout.simple_dropdown_item_1line,null, DateBaseHelper.NAME, android.R.id.text1,0,0);
				((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setThreshold(1);
			    ((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setAdapter(cursorAdapter);
			    
			    
				btnreturn=(Button)findViewById(R.id.BtnReturn2);
				btnreturn.setText("K2");	
				btnreturn.setOnClickListener(new BtnClickListener());
				
				total1btn=(Button)findViewById(R.id.TotalRoad12);
				total1btn.setVisibility(0x00000004);			
				
				total2btn=(Button)findViewById(R.id.TotalRoad22);
				total2btn.setVisibility(View.VISIBLE);
				total2btn.setOnClickListener(new BtnClickListener());
				
				btnsearch=(Button)findViewById(R.id.BtnSearch2);
				btnsearch.setOnClickListener(new BtnClickListener());
				
				FinalFlag=1;
				}
				else {
					
					frame.removeAllViews();
					final MyTurnplateView2 myView = new MyTurnplateView2(RoadTwo.this,mPointX, mPointY,mRadius,28,0);				
					myView.setOnTurnplateListener2(RoadTwo.this);	
					LayoutInflater inflater=LayoutInflater.from(RoadTwo.this);
					View itemView = inflater.inflate(R.layout.layout2,null);//���������ļ���   
					frame=new FrameLayout(RoadTwo.this); 
					setContentView(frame);
					myView1=new MyView(RoadTwo.this);
					frame.addView(myView1);
					frame.addView(myView);
					frame.addView(itemView);
					
					autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.acEditText2);
					autoCompleteTextView.setText("");
					AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(RoadTwo.this, android.R.layout.simple_dropdown_item_1line,null, DateBaseHelper.NAME, android.R.id.text1,0,1);
					((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setThreshold(1);
				    ((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setAdapter(cursorAdapter);
				    
				    
					btnreturn=(Button)findViewById(R.id.BtnReturn2);
					btnreturn.setText("K1");	
					btnreturn.setOnClickListener(new BtnClickListener());
					
					total1btn=(Button)findViewById(R.id.TotalRoad12);
					total1btn.setVisibility(View.VISIBLE);
					total1btn.setOnClickListener(new BtnClickListener());
					
					total2btn=(Button)findViewById(R.id.TotalRoad22);
					total2btn.setVisibility(View.GONE);
					total2btn.setOnClickListener(new BtnClickListener());
					
					
					btnsearch=(Button)findViewById(R.id.BtnSearch2);
					btnsearch.setOnClickListener(new BtnClickListener());
					
					
					
					FinalFlag=0;			
				}
			}	
		}
		
	}
	
	//����һ��myview�࣬����ʾ����
	class MyView extends ImageView
	{  

		public MyView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
		
		public  void setlocation(int top,int left) {
			this.setFrame(left, top, left+40, top+40);
		}
		
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			try {
				Field field=AnimationDrawable.class.getDeclaredField("mCurFrame");
				field.setAccessible(true);
//				Paint paint = new Paint();
//				paint.setColor(Color.BLACK);
//				paint.setStrokeWidth(5);
//			    canvas.drawCircle(50, 50, 20, paint);
			   // Bitmap backgroundBitmap = ((BitmapDrawable)(getResources().getDrawable(R.drawable.hot))).getBitmap();
			   //canvas.drawBitmap(backgroundBitmap,0, 0, null);
			} catch (Exception e) {
				// TODO: handle exception
			}
			super.onDraw(canvas);
		}
		
		
	}
	
	@Override
	public void onPointTouch2(Point point)
	{
		
		final int flag = point.flag;
		Intent intent = new Intent();
		
		
		if(!dismiss){
			//��ȡLayoutInflaterʵ��
			dismiss=true;
			inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
			//�����main��������inflate�м����Ŷ����ǰ����ֱ��this.setContentView()�İɣ��Ǻ�
			//�÷������ص���һ��View�Ķ����ǲ����еĸ�
			layout = inflater.inflate(R.layout.window2, null);	
			imageView=(ImageView)layout.findViewById(R.id.pictureb);
			windowTextView=(TextView)layout.findViewById(R.id.windowtextb);
			
			ad1View=(ImageView)findViewById(R.id.picture_ad1);
			ad2View=(ImageView)findViewById(R.id.picture_ad2);
			
			if (FinalFlag==0) {
			switch (flag) {
			case 27:imageView.setImageResource(R.drawable.b1_1);windowTextView.setText("˫���������ű�վ");break;
			case 26:imageView.setImageResource(R.drawable.b1_2);windowTextView.setText("˫������������վ");break;
			case 25:imageView.setImageResource(R.drawable.b1_3);windowTextView.setText("ţ�п�վ");break;
			case 24:imageView.setImageResource(R.drawable.b1_4);windowTextView.setText("������·��վ");break;
			case 23:imageView.setImageResource(R.drawable.b1_5);windowTextView.setText("����·��վ");break;
			case 22:imageView.setImageResource(R.drawable.b1_6);windowTextView.setText("���ʹ���վ");break;
			case 21:imageView.setImageResource(R.drawable.b1_7);windowTextView.setText("������԰վ");break;
			case 20:imageView.setImageResource(R.drawable.b1_8);windowTextView.setText("�ƻ�·��վ");break;
			case 19:imageView.setImageResource(R.drawable.b1_9);windowTextView.setText("����������վ");break;
			case 18:imageView.setImageResource(R.drawable.b1_10);windowTextView.setText("�Ͼ���·��վ");break;
			case 17:imageView.setImageResource(R.drawable.b1_11);windowTextView.setText("����¥��վ");break;
			case 16:imageView.setImageResource(R.drawable.b1_12);windowTextView.setText("����¥��վ");break;
			case 15:imageView.setImageResource(R.drawable.b1_13);windowTextView.setText("����·��վ");break;
			case 14:imageView.setImageResource(R.drawable.b1_14);windowTextView.setText("��ˮ��վ");break;
			case 13:imageView.setImageResource(R.drawable.b1_15);windowTextView.setText("�⻪������վ");break;
			case 12:imageView.setImageResource(R.drawable.b1_16);windowTextView.setText("��������վ");break;
			case 11:imageView.setImageResource(R.drawable.b1_17);windowTextView.setText("��·�ڱ�վ");break;
			case 10:imageView.setImageResource(R.drawable.b1_18);windowTextView.setText("Ӫ�ſڱ�վ");break;
			case 9:imageView.setImageResource(R.drawable.b1_19);windowTextView.setText("���Ͻ���վ");break;
			case 8:imageView.setImageResource(R.drawable.b1_20);windowTextView.setText("��ó�����վ");break;
			case 7:imageView.setImageResource(R.drawable.b1_21);windowTextView.setText("�𳵱�վ��վ");break;
			case 6:imageView.setImageResource(R.drawable.b1_22);windowTextView.setText("����·��վ");break;
			case 5:imageView.setImageResource(R.drawable.b1_23);windowTextView.setText("����·��վ");break;
			case 4:imageView.setImageResource(R.drawable.b1_24);windowTextView.setText("����·��վ");break;
			case 3:imageView.setImageResource(R.drawable.b1_25);windowTextView.setText("�����Ͽ�·վ");break;
			case 2:imageView.setImageResource(R.drawable.b1_26);windowTextView.setText("ɼ����վ");break;
			case 1:imageView.setImageResource(R.drawable.b1_27);windowTextView.setText("˫�ֱ�֧··��վ");break;
			case 0:imageView.setImageResource(R.drawable.b1_28);windowTextView.setText("���곡վ");break;
			default:
				break;
			}
			}
			else if(FinalFlag==1){
				switch (flag) {
				case 0:imageView.setImageResource(R.drawable.b2_4);windowTextView.setText("������·��վ");break;
				case 1:imageView.setImageResource(R.drawable.b2_5);windowTextView.setText("����·��վ");break;
				case 2:imageView.setImageResource(R.drawable.b2_6);windowTextView.setText("����·��վ");break;
				case 3:imageView.setImageResource(R.drawable.b2_7);windowTextView.setText("����·��վ");break;
				case 4:imageView.setImageResource(R.drawable.b2_8);windowTextView.setText("������վ");break;
				case 5:imageView.setImageResource(R.drawable.b2_9);windowTextView.setText("�𳵱�վ��վ");break;
				case 6:imageView.setImageResource(R.drawable.b2_10);windowTextView.setText("��ó�����վ");break;
				case 7:imageView.setImageResource(R.drawable.b2_11);windowTextView.setText("���Ͻ���վ");break;
				case 8:imageView.setImageResource(R.drawable.b2_12);windowTextView.setText("Ӫ�ſڱ�");break;
				case 9:imageView.setImageResource(R.drawable.b2_13);windowTextView.setText("��·�ڱ�վ");break;
				case 10:imageView.setImageResource(R.drawable.b2_14);windowTextView.setText("��������վ");break;
				case 11:imageView.setImageResource(R.drawable.b2_15);windowTextView.setText("�⻪��ֿ�վ");break;
				case 12:imageView.setImageResource(R.drawable.b2_16);windowTextView.setText("��ˮ��վ");break;
				case 13:imageView.setImageResource(R.drawable.b2_17);windowTextView.setText("����·��վ");break;
				case 14:imageView.setImageResource(R.drawable.b2_18);windowTextView.setText("����¥��վ");break;
				case 15:imageView.setImageResource(R.drawable.b2_19);windowTextView.setText("����¥��վ");break;
				case 16:imageView.setImageResource(R.drawable.b2_20);windowTextView.setText("�Ͼ���·��վ");break;
				case 17:imageView.setImageResource(R.drawable.b2_21);windowTextView.setText("����������վ");break;
				case 18:imageView.setImageResource(R.drawable.b2_22);windowTextView.setText("�ƻ�·��վ");break;
				case 19:imageView.setImageResource(R.drawable.b2_23);windowTextView.setText("������԰վ");break;
				case 20:imageView.setImageResource(R.drawable.b2_24);windowTextView.setText("���ʹ���վ");break;
				case 21:imageView.setImageResource(R.drawable.b2_25);windowTextView.setText("����·��վ");break;
				case 22:imageView.setImageResource(R.drawable.b2_26);windowTextView.setText("����·��վ");break;
				case 23:imageView.setImageResource(R.drawable.b2_27);windowTextView.setText("ţ�п�վ");break;
				case 24:imageView.setImageResource(R.drawable.b2_28);windowTextView.setText("˫������վ");break;
				case 25:imageView.setImageResource(R.drawable.b2_1);windowTextView.setText("˫���ӱ�վ");break;
				case 26:imageView.setImageResource(R.drawable.b2_2);windowTextView.setText("���곡վ");break;
				case 27:imageView.setImageResource(R.drawable.b2_3);windowTextView.setText("˫�ֱ�֧·��վ");break;	
				default:
					break;
				}
			}
			
			//��������Ҫ�����ˣ����������ҵ�layout���뵽PopupWindow���أ������ܼ�
			menuWindow = new PopupWindow(layout,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); //������������width��height
			layout.setBackgroundResource(R.drawable.popupwindow);
			
			ColorDrawable cd = new ColorDrawable(-0000);
			menuWindow.setBackgroundDrawable(cd);
			menuWindow.setFocusable(true);
			menuWindow.setOutsideTouchable(true);
			//menuWindow.showAsDropDown(layout); //���õ���Ч��
			//menuWindow.showAsDropDown(null, 0, layout.getHeight());
			//menuWindow.showAsDropDown(null, 0, (int)mPointY/2);
			//��λ�ȡ����main�еĿؼ��أ�Ҳ�ܼ�
			if (FinalFlag==0) {
				menuWindow.showAsDropDown(total1btn);
				menuWindow.showAtLocation(this.findViewById(R.id.window2), Gravity.TOP|Gravity.RIGHT,0, (int)mPointY/2); //����layout��PopupWindow����ʾ��λ��
			}
			else if(FinalFlag==1){//����λ��
				menuWindow.showAsDropDown(total2btn);
				menuWindow.showAtLocation(this.findViewById(R.id.window2), Gravity.TOP|Gravity.LEFT, 0, (int)mPointY/2); //����layout��PopupWindow����ʾ��λ��
			}
			
            menuWindow.update();		
			menuWindow.setTouchInterceptor(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					/****   ��������popupwindow���ⲿ��popupwindowҲ����ʧ ****/  
					// TODO Auto-generated method stub
					if (event.getAction()==MotionEvent.ACTION_OUTSIDE) {
						menuWindow.dismiss();
					}
					return false;
				}
			});
			
			dismiss=false;
			
			
			mClose = (LinearLayout)layout.findViewById(R.id.menu_closeb);
			mCloseBtn = (LinearLayout)layout.findViewById(R.id.menu_close_btnb);
			ad1=(LinearLayout)layout.findViewById(R.id.ad1);//���
			ad2=(LinearLayout)layout.findViewById(R.id.ad2);		
			myCloseBtn=(Button)layout.findViewById(R.id.windowreturnb);//��ť
			
			myCloseBtn.setOnClickListener(new View.OnClickListener() {	//"�������"��ť����Ӧ�¼�
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					menuWindow.dismiss();
					dismiss=false;
				}
			});
			mCloseBtn.setOnClickListener (new View.OnClickListener() {		//ͼƬ��ʾ			
				@Override
				public void onClick(View arg0) {	
					Intent intent1=new Intent();
					intent1.setClass(RoadTwo.this, PhotoShowBus.class);
					
					Bundle bundle=new Bundle();
			    	bundle.putInt("index", flag);
			    	if (FinalFlag==0) {
						bundle.putInt("round", 1);//��ʾ��Ȧ
					}
			    	else if (FinalFlag==1){
			    		bundle.putInt("round", 2);//��ʾ��Ȧ
					}
    				intent1.putExtras(bundle);
    				RoadTwo.this.startActivity(intent1);
					
				}
			});	
			ad1.setOnClickListener (new View.OnClickListener() {	//���1�����¼�			
				@Override
				public void onClick(View arg0) {	
					Uri uri = Uri.parse("http://www.kfc.com.cn/kfccda/index.aspx");
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
					
				}
			});	
			
			ad2.setOnClickListener (new View.OnClickListener() {	//���2�����¼�			
				@Override
				public void onClick(View arg0) {	
					Uri uri = Uri.parse("http://www.mcdonalds.com.cn/");
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}
			});	
			}
}
	@Override
	public void onPointTouchRing2(int i) {
		// TODO Auto-generated method stub
		//Toast.makeText(RoadOne.this, i+"", 1000).show();
//		if(FinalFlag==0)//��Ȧ
//		{
//		switch (i) {
//		case 0:Toast.makeText(RoadTwo.this, "���ڶ�·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 1:Toast.makeText(RoadTwo.this, "����·�ڣ���ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 2:Toast.makeText(RoadTwo.this, "��ҵ·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 3:Toast.makeText(RoadTwo.this, "��ҵ·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 4:Toast.makeText(RoadTwo.this, "������������ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 5:Toast.makeText(RoadTwo.this, "������������ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 6:Toast.makeText(RoadTwo.this, "�ƻ���·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 7:Toast.makeText(RoadTwo.this, "�ƻ���·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 8:Toast.makeText(RoadTwo.this, "����·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 9:Toast.makeText(RoadTwo.this, "����·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 10:Toast.makeText(RoadTwo.this, "����·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 11:Toast.makeText(RoadTwo.this, "����·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 12:Toast.makeText(RoadTwo.this, "˫���ţ���ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 13:Toast.makeText(RoadTwo.this, "�����ţ���ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 14:Toast.makeText(RoadTwo.this, "����·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 15:Toast.makeText(RoadTwo.this, "��������������ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 16:Toast.makeText(RoadTwo.this, "��������������ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 17:Toast.makeText(RoadTwo.this, "����·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 18:Toast.makeText(RoadTwo.this, "��ľ�֣���ǰƽ��ʱ��Ϊ40",1000).show();break;
//		case 19:Toast.makeText(RoadTwo.this, "����·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 20:Toast.makeText(RoadTwo.this, "�о���������ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 21:Toast.makeText(RoadTwo.this, "���·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 22:Toast.makeText(RoadTwo.this, "����������ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 23:Toast.makeText(RoadTwo.this, "���¸ɵ�����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 24:Toast.makeText(RoadTwo.this, "���¸ɵ�����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 25:Toast.makeText(RoadTwo.this, "�𳵱�վ����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 26:Toast.makeText(RoadTwo.this, "����̣���ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 27:Toast.makeText(RoadTwo.this, "��ɳ·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 28:Toast.makeText(RoadTwo.this, "����ӣ���ǰƽ��ʱ��Ϊ40km/h",1000).show();break;
//		case 29:Toast.makeText(RoadTwo.this, "��Ϫ��·����ǰƽ��ʱ��Ϊ40km/h",1000).show();break;		
//		default:
//			break;
//		}
//		}
//		else if(FinalFlag==1){
//			switch (i) {
//			case 0:break;
//			case 1:break;
//			case 2:break;
//			case 3:break;
//			case 4:break;
//			case 5:break;
//			case 6:break;
//			case 7:break;
//			case 8:break;
//			case 9:break;
//			case 10:break;
//			case 11:break;
//			case 12:break;
//			case 13:break;
//			case 14:break;
//			case 15:break;
//			case 16:break;
//			case 17:break;
//			case 18:break;
//			case 19:break;
//			case 20:break;
//			case 21:break;
//			case 22:break;
//			case 23:break;
//			case 24:break;
//			case 25:break;
//			case 26:break;
//			case 27:break;
//			case 28:break;
//			case 29:break;
//			case 30:break;
//			default:
//				break;
//			}
//		}
		
	}

	@Override
	public void onMyPointTouch2(com.example.mytabhost.TotalRoadBus.POINT point) {
		// TODO Auto-generated method stub
		 float px=point.x;
		 float py=point.y;
		 float MPointX=width/2;
		 float MPointY=height/2;
		 float MRadius=(float)(width/2.2);
		 float Angle=0;
		double distance=Math.sqrt((py-MPointY)*(py-MPointY)+(px-MPointX)*(px-MPointX));
		if (distance>MRadius) {
			return;
		}
		if (px!=MPointX) {
			double d=(MPointY-py)/(px-MPointX);
			double tanx=Math.atan(d);
			double angle=180*tanx/Math.PI;
			if(angle<60&&angle>=0&&py<MPointY)//��ʾ�������һ������
			{							
				Angle=1;
			}
			else if((angle>=60&&angle<90&&py<=MPointY)||angle<-60&&angle>-90&&py<=MPointY){
				Angle=2;
			}
			else if(angle>=-60&&angle<0&&py<=MPointY){
				Angle=3;
			}
			else if(angle<60&&angle>=0&&py>=MPointY){
				Angle=4;
			}
			else if((angle>=60&&angle<90&&py>=MPointY)||(angle<-60&&angle>-90&&py>=MPointY)){
				Angle=5;
			}
			else if(angle>=-60&&angle<0&&py>=MPointY){
				Angle=6;
			}
		}
		else {
			if (py<mPointY) {
				Angle=2;
			}
			else if (py>mPointY) {
				Angle=5;
			}
		}	
		if(FinalFlag==0)
		{
		frame.removeAllViews();
		final MyTurnplateView2 myView = new MyTurnplateView2(RoadTwo.this,mPointX, mPointY,mRadius,28,(float)((6-Angle)*(4.5)+1)*360/28);				
		myView.setOnTurnplateListener2(RoadTwo.this);	
		LayoutInflater inflater=LayoutInflater.from(RoadTwo.this);
		View itemView = inflater.inflate(R.layout.layout2,null);//���������ļ���   
		frame=new FrameLayout(RoadTwo.this); 
		setContentView(frame);
		myView1=new MyView(RoadTwo.this);
		frame.addView(myView1);
		frame.addView(myView);
		frame.addView(itemView);
		
		autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.acEditText2);
		autoCompleteTextView.setText("");
		AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(RoadTwo.this, android.R.layout.simple_dropdown_item_1line,null, DateBaseHelper.NAME, android.R.id.text1,0,1);
		((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setThreshold(1);
	    ((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setAdapter(cursorAdapter);
	    
		btnreturn=(Button)findViewById(R.id.BtnReturn2);
		btnreturn.setText("K1");	
		btnreturn.setOnClickListener(new BtnClickListener());
		
		total1btn=(Button)findViewById(R.id.TotalRoad12);
		total1btn.setVisibility(View.VISIBLE);
		total1btn.setOnClickListener(new BtnClickListener());
		
		total2btn=(Button)findViewById(R.id.TotalRoad22);
		total2btn.setVisibility(View.GONE);
							
		btnsearch=(Button)findViewById(R.id.BtnSearch2);
		btnsearch.setOnClickListener(new BtnClickListener());
		
		FinalFlag=0;
		}
		else if(FinalFlag==1){
			frame.removeAllViews();
			final MyTurnplateView2 myView = new MyTurnplateView2(RoadTwo.this,(float)(-height*0.86), mPointY,mRadius,29,((Angle-1)*5+2)*360/29);	
			myView.setOnTurnplateListener2(RoadTwo.this);	
			LayoutInflater inflater=LayoutInflater.from(RoadTwo.this);
			View itemView = inflater.inflate(R.layout.layout2,null);//���������ļ���   
			frame=new FrameLayout(RoadTwo.this); 
			setContentView(frame);
			myView1=new MyView(RoadTwo.this);
			frame.addView(myView1);
			frame.addView(myView);
			frame.addView(itemView);
			
			autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.acEditText2);
			autoCompleteTextView.setText("");
			AutoCompleteAdater cursorAdapter = new AutoCompleteAdater(RoadTwo.this, android.R.layout.simple_dropdown_item_1line,null, DateBaseHelper.NAME, android.R.id.text1,0,0);
			((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setThreshold(1);
		    ((AutoCompleteTextView) RoadTwo.this.findViewById(R.id.acEditText2)).setAdapter(cursorAdapter);
		    
			btnreturn=(Button)findViewById(R.id.BtnReturn2);
			btnreturn.setText("K2");	
			btnreturn.setOnClickListener(new BtnClickListener());
			
			total1btn=(Button)findViewById(R.id.TotalRoad12);
			total1btn.setVisibility(View.GONE);
			
			total2btn=(Button)findViewById(R.id.TotalRoad22);
			total2btn.setVisibility(View.VISIBLE);
			total2btn.setOnClickListener(new BtnClickListener());
			
			btnsearch=(Button)findViewById(R.id.BtnSearch2);
			btnsearch.setOnClickListener(new BtnClickListener());
			
			FinalFlag=1;
		}
		return;	
		
	}
	
}

