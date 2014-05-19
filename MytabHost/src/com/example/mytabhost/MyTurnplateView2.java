package com.example.mytabhost;

import java.util.Timer;

import com.example.mytabhost.R;

import android.R.integer;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


/**
 * 
 *    
 * �����ƣ�TurnplateView   
 * ��������   
 * �����ˣ� zhoujun   
 * ����ʱ�䣺2011-11-29 ����10:49:27   
 * �޸��ˣ�Administrator   
 * �޸�ʱ�䣺2011-11-29 ����10:49:27   
 * �޸ı�ע��   
 * @version    
 *
 */
public class MyTurnplateView2 extends View implements  OnTouchListener{
	
	

	private OnTurnplateListener2 onTurnplateListener2;
	public void setOnTurnplateListener2(OnTurnplateListener2 onTurnplateListener2) {
		this.onTurnplateListener2 = onTurnplateListener2;
	}
	/**
	 * ���ʣ��㡢��
	 */
	private Paint mPaint = new Paint();
	/**
	 * ���ʣ�Բ
	 */
	private Paint paintCircle =  new Paint();
	/**
	 * ͼ���б�
	 */
	private static int  PONIT_NUM = 28;
	private static int  PONIT_NUM1;
	private Bitmap[] icons = new Bitmap[31];
	/**
	 * point�б�
	 */
	private Point[] points;
	/**
	 * ��Ŀ
	 */
	
	/**
	 * Բ������
	 */
	private float mPointX=0, mPointY=0;
	/**
	 * �뾶
	 */
	private float mRadius = 0;
	/**
	 * ÿ���������ĽǶ�
	 */
	//private int mDegreeDelta;
	private double mDegreeDelta;
	/**
	 * ÿ��ת���ĽǶȲ�
	 */
	//private int tempDegree = 0;
	private float tempDegree=0;
	/**
	 * ѡ�е�ͼ���ʶ 999��δѡ���κ�ͼ��
	 */
	private int chooseBtn=999;
	private Matrix mMatrix = new Matrix();  
	//initangelΪת���ĽǶ�
	
	private float Iangle=0;
	public MyTurnplateView2(Context context, float px, float py, float radius,int num,float initangle) {
		super(context);	
		setLayout(context);
		PONIT_NUM1=num;
		mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth(2);
		paintCircle.setAntiAlias(true);
		paintCircle.setColor(Color.WHITE);
		loadIcons();
		mPointX = px;
		mPointY = py;
		mRadius = radius;
		
		Iangle=initangle;	
		
		initPoints();
		computeCoordinates();
		
		
	}
	public void setLayout(Context context)
	{
		
	}
	
	/**
	 * 
	 * ��������loadBitmaps 
	 * ���ܣ�װ��ͼƬ
	 * ������
	 * @param key
	 * @param d
	 * �����ˣ�zhoujun  
	 * ����ʱ�䣺2011-11-28
	 */
	public void loadBitmaps(int key,Drawable d){
		Bitmap bitmap = Bitmap.createBitmap(60,60,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint p=new Paint();
		p.setColor(Color.RED);
		p.setStrokeWidth(3);
		
		Paint p1=new Paint();
		p1.setColor(Color.BLACK);
		p1.setStrokeWidth(7);
		
		
		d.setBounds(0, 0, 60, 60);
		d.draw(canvas);
		icons[key]=bitmap;
	}
	/**
	 * 
	 * ��������loadIcons 
	 * ���ܣ���ȡ����ͼƬ
	 * ������
	 * �����ˣ�zhoujun  
	 * ����ʱ�䣺2011-11-28
	 */
	public void loadIcons(){
		Resources r = getResources();
		for(int i=0;i<PONIT_NUM;i++)
	       loadBitmaps(i, r.getDrawable(R.drawable.bus));
	}
	
	/**
	 * 
	 * ��������initPoints 
	 * ���ܣ���ʼ��ÿ����
	 * ������
	 * �����ˣ�zhoujun  
	 * ����ʱ�䣺2011-11-28
	 */
	 
	private void initPoints() {
		points = new Point[PONIT_NUM];
		Point point;
		
		float angle=Iangle;
		mDegreeDelta =360.0/PONIT_NUM;
		
		for(int index=0; index<PONIT_NUM; index++) {
			point = new Point();
			point.angle = angle;
			angle += mDegreeDelta;
			point.bitmap = icons[index];
			point.flag=index;
			points[index] = point;		
		}
	}
	
	/**
	 * 
	 * ��������resetPointAngle 
	 * ���ܣ����¼���ÿ����ĽǶ�
	 * ������
	 * @param x
	 * @param y
	 * �����ˣ�zhoujun  
	 * ����ʱ�䣺2011-11-28
	 */	
	public void resetPointAngle(float x, float y) {
		float degree = computeMigrationAngle(x, y);
		for(int index=0; index<PONIT_NUM; index++) {			
			points[index].angle += degree;		
			if(points[index].angle>360){
				points[index].angle -=360;
			}else if(points[index].angle<0){
				points[index].angle +=360;  
			}			
		}
	}
	
	/**
	 * 
	 * ��������computeCoordinates 
	 * ���ܣ�����ÿ���������
	 * ������
	 * �����ˣ�zhoujun  
	 * ����ʱ�䣺2011-11-28
	 */
	public void computeCoordinates() {
		Point point;
		for(int index=0; index<PONIT_NUM; index++) {
			point = points[index];
			point.x = mPointX+ (float)(mRadius * Math.cos(point.angle*Math.PI/180));
			point.y = mPointY+ (float)(mRadius * Math.sin(point.angle*Math.PI/180));	
			point.x_c = mPointX+(point.x-mPointX)/2;
			point.y_c = mPointY+(point.y-mPointY)/2;
		}
	}
	
	/**
	 * 
	 * ��������computeMigrationAngle 
	 * ���ܣ�����ƫ�ƽǶ�
	 * ������
	 * @param x
	 * @param y
	 * �����ˣ�zhoujun  
	 * ����ʱ�䣺2011-11-28
	 *///
	private float computeMigrationAngle(float x, float y) {
		float a=0;
		float distance = (float)Math.sqrt(((x-mPointX)*(x-mPointX) + (y-mPointY)*(y-mPointY)));
		//int degree = (int)(Math.acos((x-mPointX)/distance)*180/Math.PI);
		float degree=(float)(Math.acos((x-mPointX)/distance)*180/Math.PI);
		if(y < mPointY) {
			degree = -degree;
		}	
		if(tempDegree!=0){
			a = degree - tempDegree;
		}
		tempDegree=degree;		
		return a;
	}
	
	
	
	/**
	 * 
	 * ��������computeCurrentDistance 
	 * ���ܣ����㴥����λ�������Ԫ��ľ���
	 * ������
	 * @param x
	 * @param y
	 * @return
	 * �����ˣ�zhoujun  
	 * ����ʱ�䣺2011-11-29
	 */
	private void computeCurrentDistance(float x, float y) {
		for(Point point:points){
			float distance = (float)Math.sqrt(((x-point.x)*(x-point.x) + (y-point.y)*(y-point.y)));			
			//if(distance<31){
			if(distance<20){
				//chooseBtn = 999;
				chooseBtn=point.flag;
				point.isCheck = true;
				break;
			}else{
				point.isCheck = false;
				//chooseBtn =  point.flag;
				chooseBtn=999;
			}
		}	
	}
	
	private void switchScreen(MotionEvent event){
		computeCurrentDistance(event.getX(), event.getY());
		for(Point point:points){
			if(point.isCheck)
			{
				onTurnplateListener2.onPointTouch2(point);
				//break;
				return;
			}
		}
		
		
		float offset=mRadius/100;//����̫ϸ��������չԲ������Ӧ�Ŀ��
		float eventx=mPointX-event.getX();
		float eventy=mPointY-event.getY();
		
		if ((eventx*eventx+eventy*eventy)>=(mRadius-offset)*(mRadius-offset)
				&&(eventx*eventx+eventy*eventy)<=(mRadius+offset)*(mRadius+offset)) {//�ڵ����Բ����
			if(PONIT_NUM1==28){//��ʾk1
			for (int i = 0; i <PONIT_NUM ; i++) {
				if (i==PONIT_NUM-1) {
					if (points[i].y>event.getY()&&points[0].y<event.getY()&&points[i].x<mPointX) {
						onTurnplateListener2.onPointTouchRing2(i);
						break;
					}
				}
				if (points[i].y>event.getY()&&points[i+1].y<event.getY()&&points[i].x<mPointX&&i!=PONIT_NUM-1) {
					onTurnplateListener2.onPointTouchRing2(i);
					break;
				}
			}
			}
			else if(PONIT_NUM1==29)//��ʾ��Ȧk2
			{
				for (int i = 0; i <PONIT_NUM ; i++) {
					if (i==PONIT_NUM-1) {
						if (points[i].y<event.getY()&&points[0].y>event.getY()&&points[i].x>mPointX) {
							onTurnplateListener2.onPointTouchRing2(i);
							break;
						}
					}
					if (points[i].y<event.getY()&&points[i+1].y>event.getY()&&points[i].x>mPointX&&i!=PONIT_NUM-1) {
						onTurnplateListener2.onPointTouchRing2(i);
						break;
					}
				}
			}
		}
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {		
		 int action = event.getAction();
	        switch (action) {
	        case MotionEvent.ACTION_DOWN:
	            break;
	        case MotionEvent.ACTION_MOVE:
	        	resetPointAngle(event.getX(), event.getY());//ת��
	    		computeCoordinates();
	    		invalidate();
	            break;
	        case MotionEvent.ACTION_UP:
	        	switchScreen(event);
	        	tempDegree = 0;
	        	invalidate();
	            break;
	        case MotionEvent.ACTION_CANCEL:
	        	//ϵͳ�����е�һ���̶����޷�������Ӧ��ĺ�������ʱ��������¼���
	        	//һ����ڴ����н�����Ϊ�쳣��֧�������
	            break;
	        }
		return true;
	}
	
  	@Override
	public void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.rgb(48, 189, 243));
		paint.setAntiAlias(true);//�������
 		paint.setStyle(Paint.Style.STROKE);// ���ΪԲ��
		paint.setStrokeWidth(10); // Բ�����
		
		Paint paint1 = new Paint();
		paint1.setColor(Color.RED);
		paint1.setAntiAlias(true);//�������
 		paint1.setStyle(Paint.Style.STROKE);// ���ΪԲ��
		paint1.setStrokeWidth(10); // Բ�����
		
		Paint paint2 = new Paint();
		paint2.setColor(Color.rgb(40, 112, 33));//ī��ɫ
		paint2.setAntiAlias(true);//�������
 		paint2.setStyle(Paint.Style.STROKE);// ���ΪԲ��
		paint2.setStrokeWidth(10); // Բ�����
		paint2.setStyle(Paint.Style.FILL);
		paint2.setTextSize(20);
		
		Paint p=new Paint();
		p.setColor(Color.BLACK);//ī��ɫ
		p.setTextSize(30);
		p.setAntiAlias(true);//�������  

		//����ͷ�����г�����
				float widthofscreen=0;
				if (mPointX>0) {
					widthofscreen=mPointX-mRadius-(float)(mRadius/2.6);
				}
				else {
					widthofscreen=mPointX+mRadius+(float)(mRadius/2.6);
				}
				canvas.drawLine(widthofscreen, mPointY-50, widthofscreen, mPointY+50, paint2);
				//������
				Path path=new Path();
				path.moveTo(widthofscreen-15, mPointY-50);
				path.lineTo(widthofscreen+15, mPointY-50);
				path.lineTo(widthofscreen,mPointY-90);
				path.close();
				canvas.drawPath(path,paint2);
	
		
		if (mPointX>0) {
			p.setTextAlign(Align.RIGHT);
			canvas.drawText("��", widthofscreen-15, mPointY-60, p);
			canvas.drawText("��", widthofscreen-15, mPointY-25, p);
			canvas.drawText("��", widthofscreen-15, mPointY+10, p);
			canvas.drawText("��", widthofscreen-15, mPointY+45, p);
		}
		else {
			p.setTextAlign(Align.LEFT);
			canvas.drawText("��", widthofscreen+15, mPointY-60, p);
			canvas.drawText("��", widthofscreen+15, mPointY-25, p);
			canvas.drawText("��", widthofscreen+15, mPointY+10, p);
			canvas.drawText("��", widthofscreen+15, mPointY+45, p);
		}
		
		
		
		
		canvas.drawCircle( mPointX, mPointY, mRadius, paint);
//		Bitmap bitmap = ((BitmapDrawable)(getResources().getDrawable(R.drawable.circle_bg))).getBitmap();
	//	Bitmap backgroundBitmap = ((BitmapDrawable)(getResources().getDrawable(R.drawable.hot))).getBitmap();
		//���м����ͼƬ
		RectF oval=new RectF(mPointX-mRadius,mPointY-mRadius,mPointX+mRadius,mPointY+mRadius);
			
		
		if(PONIT_NUM1==28){
		for(int index=0; index<PONIT_NUM; index++) {
			//canvas.drawPoint(points[index].x_c, points[index].y_c, mPaint);
			drawInCenter(canvas, points[index].bitmap, points[index].x, points[index].y,points[index].flag);
			drwaText(canvas,index,points[index].x-50,points[index].y);			
		}
		}
		else if(PONIT_NUM1==29){
			for(int index=0; index<PONIT_NUM; index++) {
				drawInCenter(canvas, points[index].bitmap, points[index].x, points[index].y,points[index].flag);
				drwaText(canvas,index,points[index].x+50,points[index].y);
				
			}
		}
		
		//canvas.drawCircle( mPointX, mPointY, mRadius+30, paint);
	}
	
	private void drwaText(Canvas canvas,int index,float x,float y) {
		// TODO Auto-generated method stub
		Paint p1=new Paint();
		p1.setColor(Color.BLACK);
		p1.setTextSize(30);
		p1.setAntiAlias(true);//�������  
		if(PONIT_NUM1==28){
			p1.setTextAlign(Align.RIGHT);//���Ҷ��� 
		switch(index)
		{
		case 28:canvas.drawText("˫���ӱ�վ", x, y, p1);break;
		case 27:canvas.drawText("˫������վ", x, y, p1);break;
		case 26:canvas.drawText("ţ�п�վ", x, y, p1);break;
		case 25:canvas.drawText("����·��վ", x, y, p1);break;
		case 24:canvas.drawText("����·��վ", x, y, p1);break;
		case 23:canvas.drawText("���ʹ���վ", x, y, p1);break;
		case 22:canvas.drawText("������԰վ", x, y, p1);break;
		case 21:canvas.drawText("�ƻ�·��վ", x, y, p1);break;
		case 20:canvas.drawText("����������վ", x, y, p1);break;
		case 19:canvas.drawText("�Ͼ���·��վ", x, y, p1);break;		
		case 18:canvas.drawText("����¥��վ",x, y, p1);break;
		case 17:canvas.drawText("����¥��վ", x, y, p1);break;
		case 16:canvas.drawText("����·��վ", x, y, p1);break;
		case 15:canvas.drawText("��ˮ��վ", x, y, p1);break;
		case 14:canvas.drawText("�⻪��ֿ�վ", x, y, p1);break;
		case 13:canvas.drawText("��������վ", x, y, p1);break;
		case 12:canvas.drawText("��·�ڱ�վ", x, y, p1); break;
		case 11:canvas.drawText("Ӫ�ſڱ�վ", x, y, p1);break;
		case 10:canvas.drawText("���Ͻ���վ", x, y, p1);break;
		case 9:canvas.drawText("��ó�����վ", x, y, p1);break;
		case 8:canvas.drawText("�𳵱�վ��վ", x, y, p1);break;
		case 7:canvas.drawText("������վ", x, y, p1);break;
		case 6:canvas.drawText("����·��վ", x, y, p1);break;
		case 5:canvas.drawText("����·��վ", x, y, p1);break;
		case 4:canvas.drawText("����·��վ", x, y, p1);break;
		case 3:canvas.drawText("�����Ͽ�·վ", x, y, p1);break;
		case 2:canvas.drawText("ɼ����վ", x, y, p1);break;
		case 1:canvas.drawText("˫�ֱ�֧·��վ", x, y, p1);break;
		case 0:canvas.drawText("���곡վ", x, y, p1);break;
		}
		}
		else if(PONIT_NUM1==29){
			p1.setTextAlign(Align.LEFT);//������� 
			switch(index)
			{
			case 0:canvas.drawText("������·��վ", x, y, p1);break;
			case 1:canvas.drawText("����·��վ", x, y, p1);break;
			case 2:canvas.drawText("����·��վ", x, y, p1);break;
			case 3:canvas.drawText("����·��վ", x, y, p1);break;
			case 4:canvas.drawText("������վ", x, y, p1);break;
			case 5:canvas.drawText("�𳵱�վ��վ", x, y, p1);break;
			case 6:canvas.drawText("��ó�����վ", x, y, p1);break;
			case 7:canvas.drawText("���Ͻ���վ", x, y, p1);break;
			case 8:canvas.drawText("Ӫ�ſڱ�վ", x, y, p1);break;
			case 9:canvas.drawText("��·�ڱ�վ", x, y, p1);break;		
			case 10:canvas.drawText("��������վ",x, y, p1);break;
			case 11:canvas.drawText("�⻪��ֿ�վ", x, y, p1);break;
			case 12:canvas.drawText("��ˮ��վ", x, y, p1);break;
			case 13:canvas.drawText("����·��վ", x, y, p1);break;
			case 14:canvas.drawText("����¥��վ", x, y, p1);break;
			case 15:canvas.drawText("����¥��վ", x, y, p1);break;
			case 16:canvas.drawText("�Ͼ���·��վ", x, y, p1); break;
			case 17:canvas.drawText("����������վ", x, y, p1);break;
			case 18:canvas.drawText("�ƻ�·��վ", x, y, p1);break;
			case 19:canvas.drawText("������԰վ", x, y, p1);break;
			case 20:canvas.drawText("���ʹ���վ", x, y, p1);break;
			case 21:canvas.drawText("����·��վ", x, y, p1);break;
			case 22:canvas.drawText("����·��վ", x, y, p1);break;
			case 23:canvas.drawText("ţ�п�վ", x, y, p1);break;
			case 24:canvas.drawText("˫������վ", x, y, p1);break;
			case 25:canvas.drawText("˫���ӱ�վ", x, y, p1);break;
			case 26:canvas.drawText("���곡վ", x, y, p1);break;
			case 27:canvas.drawText("˫�ֱ�֧·��վ",x, y, p1);break;
			case 28:canvas.drawText("ɼ����վ",x, y, p1);break;
			}
		}
	}
	/**
	 * 
	 * ��������drawInCenter 
	 * ���ܣ��ѵ�ŵ�ͼƬ���Ĵ�
	 * ������
	 * @param canvas
	 * @param bitmap
	 * @param left
	 * @param top
	 * �����ˣ�zhoujun  
	 * ����ʱ�䣺2011-11-28
	 */
	void drawInCenter(Canvas canvas, Bitmap bitmap, float left, float top,int flag) {
		canvas.drawPoint(left, top, mPaint);
		if(chooseBtn==flag){
			mMatrix.setScale(70f/bitmap.getWidth(), 70f/bitmap.getHeight());   
			mMatrix.postTranslate(left-35, top-35);  
			canvas.drawBitmap(bitmap, mMatrix, null); 
		}else{
			canvas.drawBitmap(bitmap, left-bitmap.getWidth()/2, top-bitmap.getHeight()/2, null);
		}
		
		
	}	
	
	class Point {
		
		/**
		 * λ�ñ�ʶ
		 */
		int flag;
		/**
		 * ͼƬ
		 */
		Bitmap bitmap;
		
		/**
		 * �Ƕ�
		 */
		//int angle;
		float angle;
		
		/**
		 * x����
		 */
		float x;
		
		/**
		 * y����
		 */
		float y;
		
		/**
		 * ����Բ�ĵ�����x����
		 */
		float x_c;
		/**
		 * ����Բ�ĵ�����y����
		 */
		float y_c;
		
		boolean isCheck;
	}

	 public static interface OnTurnplateListener2 {
	       
	     public void onPointTouch2(Point point);//·����Ӧ����
	     public void onPointTouchRing2(int i);//�ߵ���Ӧ����
	       	             	        
	 }

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}
	
	
}
