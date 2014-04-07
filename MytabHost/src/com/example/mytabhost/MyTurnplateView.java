package com.example.mytabhost;

import java.util.Timer;

import android.R.color;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
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
public class MyTurnplateView extends View implements  OnTouchListener{
	
	

	private OnTurnplateListener onTurnplateListener;
	public void setOnTurnplateListener(OnTurnplateListener onTurnplateListener) {
		this.onTurnplateListener = onTurnplateListener;
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
	private static int  PONIT_NUM = 30;
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
	
	private int speed1[]={15,64,78,23,44,45,76,87,37,12,98,34,13,53,12,46,68,24,89,34,56,79,21,35,69,20,36,94,11,30};
	private int speed2[]={53,12,46,68,24,89,34,56,79,15,64,78,23,44,45,76,87,37,12,98,79,21,35,69,20,36,94,11,30,98,34};
	public MyTurnplateView(Context context, float px, float py, float radius,int num,float initangle) {
		super(context);	
		setLayout(context);
		PONIT_NUM=num;
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
		Bitmap bitmap = Bitmap.createBitmap(80,80,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint p=new Paint();
		p.setColor(Color.RED);
		p.setStrokeWidth(3);
		
		Paint p1=new Paint();
		p1.setColor(Color.BLACK);
		p1.setStrokeWidth(7);
		
		
		d.setBounds(0, 0, 80, 80);
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
		if(PONIT_NUM==30){
			for(int i=0;i<PONIT_NUM;i++)
			{
				switch(i)
				{
				//���
				case 0:
				case 2:loadBitmaps(i, r.getDrawable(R.drawable.enter1));break;
				case 4:
				case 5:
				case 7:
				case 9:loadBitmaps(i, r.getDrawable(R.drawable.enter3));break;
				case 11:
				case 13:
				case 15:loadBitmaps(i, r.getDrawable(R.drawable.enter2));break;
				case 19:
				case 22:
				case 24:				
				case 26:	
				case 28:loadBitmaps(i, r.getDrawable(R.drawable.enter1));break;
				
				//����
				
				case 8:
				case 10:loadBitmaps(i, r.getDrawable(R.drawable.exitr2));break;//������ת
				case 14:
				case 16:
				case 23:
				case 1:loadBitmaps(i, r.getDrawable(R.drawable.exitr3));break;//������ת	
				case 6:
				case 12:
				case 17:
				case 27:loadBitmaps(i, r.getDrawable(R.drawable.exitr1));break;//������ת
					
				case 20:
				case 21:loadBitmaps(i, r.getDrawable(R.drawable.exitl2));break;//��������
				
				case 29:loadBitmaps(i, r.getDrawable(R.drawable.exits3));break;//����ֱ��
				case 18:loadBitmaps(i, r.getDrawable(R.drawable.exits2));break;//����ֱ��
				case 3:loadBitmaps(i, r.getDrawable(R.drawable.exits1));break;//����ֱ��
				
				
				case 25:loadBitmaps(i, r.getDrawable(R.drawable.exitfor1));break;//������������
				}
			
			}
		}
		else if(PONIT_NUM==31){
			for(int i=0;i<PONIT_NUM;i++)
			{
				switch(i)
				{
				case 4:loadBitmaps(i, r.getDrawable(R.drawable.exitl2));break;//�������
				case 22:loadBitmaps(i, r.getDrawable(R.drawable.exitl3));break;//�������
				case 25:loadBitmaps(i, r.getDrawable(R.drawable.exitl1));break;//�������
					
				
				case 7:loadBitmaps(i, r.getDrawable(R.drawable.exitr2));break;//�����ҹ�
				case 8:
				case 14:loadBitmaps(i, r.getDrawable(R.drawable.exitr3));break;//�����ҹ�
				case 29:loadBitmaps(i, r.getDrawable(R.drawable.exitr1));break;//�����ҹ�
					
				case 0:
				case 2:
				case 5:
				case 11:loadBitmaps(i, r.getDrawable(R.drawable.exits1));break;//����ֱ��
				case 13:
				case 17:loadBitmaps(i, r.getDrawable(R.drawable.exits2));break;//����ֱ��
				case 19:
				case 21:loadBitmaps(i, r.getDrawable(R.drawable.exits3));break;//����ֱ��
				case 23:
				case 27:loadBitmaps(i, r.getDrawable(R.drawable.exits1));break;//����ֱ��
				
				
				case 1:
				case 3:
				case 6:
				case 9:loadBitmaps(i, r.getDrawable(R.drawable.enter1));break;
				case 10:	
				case 12:loadBitmaps(i, r.getDrawable(R.drawable.enter2));break;
				case 15:
				case 16:
				case 18:
				case 20:loadBitmaps(i, r.getDrawable(R.drawable.enter1));break;
				case 24:
				case 26:loadBitmaps(i, r.getDrawable(R.drawable.enter3));break;
				case 28:
				case 30:loadBitmaps(i, r.getDrawable(R.drawable.enter1));break;
				}
			}
		}
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
		
		
		float angle=Iangle;//��ʼ���Ƕ�
		
		
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
				onTurnplateListener.onPointTouch(point);
				//break;
				return;
			}
		}
		
		
		float offset=mRadius/100;//����̫ϸ��������չԲ������Ӧ�Ŀ��
		float eventx=mPointX-event.getX();
		float eventy=mPointY-event.getY();
		
		if ((eventx*eventx+eventy*eventy)>=(mRadius-offset)*(mRadius-offset)
				&&(eventx*eventx+eventy*eventy)<=(mRadius+offset)*(mRadius+offset)) {//�ڵ����Բ����
			if(PONIT_NUM==30){//��ʾ��Ȧ
			for (int i = 0; i <PONIT_NUM ; i++) {
				if (i==PONIT_NUM-1) {
					if (points[i].y<event.getY()&&points[0].y>event.getY()&&points[i].x>mPointX) {
						onTurnplateListener.onPointTouchRing(i,speed1[i]);
						break;
					}
				}
				if (points[i].y<event.getY()&&points[i+1].y>event.getY()&&points[i].x>mPointX&&i!=PONIT_NUM-1) {
					onTurnplateListener.onPointTouchRing(i,speed1[i]);
					break;
				}
			}
			}
			else if(PONIT_NUM==31)//��ʾ��Ȧ
			{
				for (int i = 0; i <PONIT_NUM ; i++) {
					if (i==PONIT_NUM-1) {
						if (points[i].y>event.getY()&&points[0].y>event.getY()&&points[i].x<mPointX) {
							onTurnplateListener.onPointTouchRing(i,speed2[i]);
							break;
						}
					}
					if (points[i].y>event.getY()&&points[i+1].y<event.getY()&&points[i].x<mPointX&&i!=PONIT_NUM-1) {
						onTurnplateListener.onPointTouchRing(i,speed2[i]);      
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
  		//��ɫ
		Paint paintBlue = new Paint();
		paintBlue.setColor(Color.rgb(48, 189, 243));
		paintBlue.setAntiAlias(true);//�������
		paintBlue.setStyle(Paint.Style.STROKE);// ���ΪԲ��
		paintBlue.setStrokeWidth(20); // Բ�����
		
		
		//��ɫ
		Paint paintRed = new Paint();
		paintRed.setColor(Color.RED);
		paintRed.setAntiAlias(true);//�������
		paintRed.setStyle(Paint.Style.STROKE);// ���ΪԲ��
		paintRed.setStrokeWidth(20); // Բ�����
		
		//��ɫ
		Paint paintOrange = new Paint();
		paintOrange.setColor(Color.rgb(254,147,1));
		paintOrange.setAntiAlias(true);//�������
		paintOrange.setStyle(Paint.Style.STROKE);// ���ΪԲ��
		paintOrange.setStrokeWidth(20); // Բ�����
		
		
		Paint paintGreen = new Paint();
		paintGreen.setColor(Color.GREEN);
		paintGreen.setAntiAlias(true);//�������
		paintGreen.setStyle(Paint.Style.STROKE);// ���ΪԲ��
		paintGreen.setStrokeWidth(20); // Բ�����
		
		
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
		
		//canvas.drawTextOnPath("�н�����", path2, 0, 0, paint2);
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
		
	
		
		
		
		//canvas.drawCircle( mPointX, mPointY, mRadius, paint);
		RectF oval=new RectF(mPointX-mRadius,mPointY-mRadius,mPointX+mRadius,mPointY+mRadius);
		
		
		if(PONIT_NUM==30){
		for(int index=0; index<PONIT_NUM; index++) {
			if(speed1[index]<=15)//����
			{
				canvas.drawArc(oval, points[index].angle, 360/PONIT_NUM, false, paintRed);	
			}
			else if(speed1[index]>15&&speed1[index]<=40)
			{
				canvas.drawArc(oval, points[index].angle, 360/PONIT_NUM, false, paintOrange);	
			}
			else if (speed1[index]>40&&speed1[index]<=60) {
				canvas.drawArc(oval, points[index].angle, 360/PONIT_NUM, false, paintBlue);	
			}
			else 
			{
				canvas.drawArc(oval, points[index].angle, 360/PONIT_NUM, false, paintGreen);	
			}		
		}
		for (int index = 0; index < PONIT_NUM; index++) {
			drawInCenter(canvas, points[index].bitmap, points[index].x, points[index].y,points[index].flag);
			drwaText(canvas,index,points[index].x+50,points[index].y);	
		}
		}
		else if(PONIT_NUM==31){
			for(int index=0; index<PONIT_NUM; index++) {
				if(speed2[index]<=15)//����
				{
					canvas.drawArc(oval, points[index].angle, 360/PONIT_NUM, false, paintRed);	
				}
				else if(speed2[index]>15&&speed2[index]<=40)
				{
					canvas.drawArc(oval, points[index].angle, 360/PONIT_NUM, false, paintOrange);	
				}
				else if (speed2[index]>40&&speed2[index]<=60) {
					canvas.drawArc(oval, points[index].angle, 360/PONIT_NUM, false, paintBlue);	
				}
				else 
				{
					canvas.drawArc(oval, points[index].angle, 360/PONIT_NUM, false, paintGreen);	
				}
			}
			for(int index=0;index<PONIT_NUM;index++)
			{
				drawInCenter(canvas, points[index].bitmap, points[index].x, points[index].y,points[index].flag);
				drwaText(canvas,index,points[index].x-50,points[index].y);			
			}
		}
	}
	
	private void drwaText(Canvas canvas,int index,float x,float y) {
		// TODO Auto-generated method stub
		Paint p1=new Paint();
		p1.setColor(Color.BLACK);
		p1.setTextSize(30);
		p1.setAntiAlias(true);//�������  
		if(PONIT_NUM==30){
			p1.setTextAlign(Align.LEFT);//���Ҷ��� 
		switch(index)
		{
		case 29:canvas.drawText("���ڶ�·����", x, y, p1);break;
		case 28:canvas.drawText("����·�����", x, y, p1);break;
		case 27:canvas.drawText("��ҵ·����", x, y, p1);break;
		case 26:canvas.drawText("��ҵ·���", x, y, p1);break;
		case 25:canvas.drawText("������������", x, y, p1);break;
		case 24:canvas.drawText("�����������", x, y, p1);break;
		case 23:canvas.drawText("�ƻ���·����", x, y, p1);break;
		case 22:canvas.drawText("�ƻ���·���", x, y, p1);break;
		case 21:canvas.drawText("����·����", x, y, p1);break;
		case 20:canvas.drawText("����·����", x, y, p1);break;
		case 19:canvas.drawText("����·���", x, y, p1);break;		
		case 18:canvas.drawText("����·����",x, y, p1);break;
		case 17:canvas.drawText("˫���ų���", x, y, p1);break;
		case 16:canvas.drawText("�����ų���", x, y, p1);break;
		case 15:canvas.drawText("����·���", x, y, p1);break;
		case 14:canvas.drawText("��������������", x, y, p1);break;
		case 13:canvas.drawText("ɼ�����������", x, y, p1);break;
		case 12:canvas.drawText("����·����", x, y, p1); break;
		case 11:canvas.drawText("��ľ�����", x, y, p1);break;
		case 10:canvas.drawText("����·����", x, y, p1);break;
		case 9:canvas.drawText("�о��������", x, y, p1);break;
		case 8:canvas.drawText("���·����", x, y, p1);break;
		case 7:canvas.drawText("���������", x, y, p1);break;
		case 6:canvas.drawText("���¸ɵ�����", x, y, p1);break;
		case 5:canvas.drawText("���¸ɵ����", x, y, p1);break;
		case 4:canvas.drawText("�𳵱�վ���", x, y, p1);break;
		case 3:canvas.drawText("����̳���", x, y, p1);break;
		case 2:canvas.drawText("��ɳ·���", x, y, p1);break;
		case 1:canvas.drawText("����ӳ���", x, y, p1);break;
		case 0:canvas.drawText("��Ϫ��·���",x, y, p1);break;
		}
		}
		else if(PONIT_NUM==31){
			p1.setTextAlign(Align.RIGHT);//������� 
			switch(index)
			{
			case 0:canvas.drawText("��ʯ��·����", x, y, p1);break;
			case 1:canvas.drawText("��԰��·���", x, y, p1);break;
			case 2:canvas.drawText("������·����", x, y, p1);break;
			case 3:canvas.drawText("Ӫ�˽����", x, y, p1);break;
			case 4:canvas.drawText("����ӳ���", x, y, p1);break;
			case 5:canvas.drawText("ɳ��·����", x, y, p1);break;
			case 6:canvas.drawText("�����г����", x, y, p1);break;
			case 7:canvas.drawText("��վ����·����", x, y, p1);break;
			case 8:canvas.drawText("���Ǵ������", x, y, p1);break;
			case 9:canvas.drawText("���¸ɵ����", x, y, p1);break;
			case 10:canvas.drawText("�컨��·���", x, y, p1);break;		
			case 11:canvas.drawText("����·����",x, y, p1);break;
			case 12:canvas.drawText("����·���", x, y, p1);break;
			case 13:canvas.drawText("����·����", x, y, p1);break;
			case 14:canvas.drawText("ɼ����·����", x, y, p1);break;
			case 15:canvas.drawText("ɼ����·���", x, y, p1);break;
			case 16:canvas.drawText("˫�ֱ�֧·���", x, y, p1);break;
			case 17:canvas.drawText("˫��· ˫��·����", x, y, p1); break;
			case 18:canvas.drawText("ţ�п����", x, y, p1);break;
			case 19:canvas.drawText("����·����", x, y, p1);break;
			case 20:canvas.drawText("����·���", x, y, p1);break;
			case 21:canvas.drawText("������·����", x, y, p1);break;
			case 22:canvas.drawText("�ƻ���·����", x, y, p1);break;
			case 23:canvas.drawText("������·����", x, y, p1);break;
			case 24:canvas.drawText("�����Ͻ����", x, y, p1);break;
			case 25:canvas.drawText("��ҵ·����", x, y, p1);break;
			case 26:canvas.drawText("��ҵ·���", x, y, p1);break;
			case 27:canvas.drawText("������· ����", x, y, p1);break;
			case 28:canvas.drawText("����¥·���", x, y, p1);break;
			case 29:canvas.drawText("���������",x, y, p1);break;
			case 30:canvas.drawText("�����������",x, y, p1);break;
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

	 public static interface OnTurnplateListener {
	       
	     public void onPointTouch(Point point);//·����Ӧ����
	     public void onPointTouchRing(int i,int speed);//�ߵ���Ӧ����
	       	             	        
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
