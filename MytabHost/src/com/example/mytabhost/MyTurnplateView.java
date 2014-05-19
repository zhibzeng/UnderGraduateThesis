package com.example.mytabhost;

import java.io.IOException;
import java.util.List;
import java.util.Timer;

import org.json.JSONException;

import com.example.mytabhost.R;
import com.example.mytabhost.entity.CrossingEntity;

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
import android.util.Log;
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
 * ����· ���⻷��
 * @author Administrator
 *
 */
public class MyTurnplateView extends View implements  OnTouchListener{
	
	

	private OnTurnplateListener onTurnplateListener;
	public void setOnTurnplateListener(OnTurnplateListener onTurnplateListener) {
		this.onTurnplateListener = onTurnplateListener;
	}
	// ���ʣ��㡢��
	private Paint mPaint = new Paint();
	//���ʣ�Բ
	private Paint paintCircle =  new Paint();
	// ͼ���б�
	private static int  roadtype = 1;//0 ��ʾ��Ȧ 1��ʾ��Ȧ
	//point�б�
	private Point[] points;
	//Բ������
	private float mPointX=0, mPointY=0;
	 //�뾶
	private float mRadius = 0;
	//ÿ���������ĽǶ�
	//private int mDegreeDelta;
	private double mDegreeDelta;
	// ÿ��ת���ĽǶȲ�
	//private int tempDegree = 0;
	private float tempDegree=0;
	//ѡ�е�ͼ���ʶ 999��δѡ���κ�ͼ��
	private int chooseBtn=999;
	private Matrix mMatrix = new Matrix();  
	//initangelΪת���ĽǶ�
	private float Iangle=0;
	//�ڻ��ٶ�
	private int speed0[]={15,64,78,23,44,45,76,87,37,12,98,34,13,53,12,46,68,24,89,34,56,79,21,35,69,20,36,94,30,98};
	//�⻷�ٶ�
	private int speed1[]={53,12,46,68,24,89,34,56,79,15,64,78,23,44,45,76,87,37,12,98,79,21,35,69,20,36,94,11,12};
	private Context mContext = null;
	//·����Ϣ�б�
	public static List<CrossingEntity> CrossingListInner = null;
	public static List<CrossingEntity> CrossingListOut = null;
	//��ͬ����·�����ò�ͬͼƬ
	private final static int PONITS_PICS[] = {
		R.drawable.exits11,//labelid 1
		R.drawable.exits22,//labelid 2
		R.drawable.exits33,//labelid 3
		R.drawable.enter11,//labelid 4
		R.drawable.enter22,//labelid 5
		R.drawable.enter33,//labelid 6
		R.drawable.exits41,//labelid 7
		R.drawable.exits51,//labelid 8
		R.drawable.enter42,//labelid 9
		};
	
	private int pointsize = 0;//·����Ŀ
	
	/**
	 * �๹�캯��
	 * @param context
	 * @param px
	 * @param py
	 * @param radius
	 * @param num
	 * @param initangle
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public MyTurnplateView(Context context, float px, float py, float radius,int num,float initangle) throws JSONException, IOException {
		super(context);	
		setLayout(context);
		mContext = context;
		roadtype=num;
		mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth(2);
		paintCircle.setAntiAlias(true);
		paintCircle.setColor(Color.WHITE);
		mPointX = px;
		mPointY = py;
		mRadius = radius;
		Iangle=initangle;
		CrossingListInner = null;
		CrossingListOut = null;
		initPoints();
		computeCoordinates();
						
	}
	
	
	public void setLayout(Context context){}
	
	

	/**
	 * 
	 * ��������initPoints 
	 * ���ܣ���ʼ��ÿ����
	 * ������
	 * �����ˣ�zhoujun  
	 * ����ʱ�䣺2011-11-28
	 * @throws IOException 
	 * @throws JSONException 
	 */
	 
	private void initPoints() throws JSONException, IOException {
		Resources r = getResources();
		TransferData transferData = new TransferData();
		//�ж����ڻ������⻷
		if(roadtype==0){
			//�ڻ�
			CrossingListInner = transferData.getCrossingLists(mContext,roadtype);
			pointsize = CrossingListInner.size();
		}else{
			//�ڻ�
			CrossingListOut = transferData.getCrossingLists(mContext,roadtype);
			pointsize = CrossingListOut.size();
		}
		points = null;
		points = new Point[pointsize];
		Point point;
		float angle=Iangle;//��ʼ���Ƕ�
		mDegreeDelta =360.0/pointsize;
		if(roadtype==0){
			for(int index=0; index<pointsize; index++) {
				CrossingEntity entity = CrossingListInner.get(index);
				point = new Point();
				point.angle = angle;
				angle += mDegreeDelta;
				point.bitmap = loadBitmaps(r.getDrawable(PONITS_PICS[entity.getLabelid()-1]));
				point.flag=index;
				point.entity = entity;
				points[index] = point;
				Log.d("MyTurnplate",index+":"+entity.getName());
				
			}
		}else{
			for(int index=0; index<pointsize; index++) {
				CrossingEntity entity = CrossingListOut.get(index);
				point = new Point();
				point.angle = angle;
				angle += mDegreeDelta;
				point.bitmap = loadBitmaps(r.getDrawable(PONITS_PICS[entity.getLabelid()-1]));
				point.flag=index;
				point.entity = entity;
				points[index] = point;
				Log.d("MyTurnplate",index+":"+entity.getName());
			}
		}
	}
	
	
	/**
	 * 
	 * ��������loadBitmaps 
	 * ���ܣ�װ��ͼƬ
	 * ������
	 * @param d
	 * �����ˣ�zhoujun  
	 * ����ʱ�䣺2011-11-28
	 */
	public Bitmap loadBitmaps(Drawable d){
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
		return bitmap;
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
		for(int index=0; index<pointsize; index++) {			
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
		for(int index=0; index<pointsize; index++) {
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
			if(roadtype==1){//��ʾ��Ȧ
			Log.d("MyTurnplate roadtype",roadtype+" size:"+pointsize);
			for (int i = 0; i <pointsize ; i++) {
				if (i==pointsize-1) {
					if (points[i].y<event.getY()&&points[0].y>event.getY()&&points[i].x>mPointX) {
						//��������·��֮����ٶ�
						onTurnplateListener.onPointTouchRing(i,speed1[i]);
						break;
					}
				}
				if(points[i].y<event.getY()&&points[i+1].y>event.getY()&&points[i].x>mPointX&&i!=pointsize-1) {
					onTurnplateListener.onPointTouchRing(i,speed1[i]);
					break;
				}
			}
			}
			else if(roadtype==0)//��ʾ��Ȧ
			{
				Log.d("MyTurnplate roadtype",roadtype+" size:"+pointsize);
				for (int i = 0; i <pointsize ; i++) {
					if (i==pointsize-1) {
						if (points[i].y>event.getY()&&points[0].y>event.getY()&&points[i].x<mPointX) {
							//��������·��֮����ٶ�
							onTurnplateListener.onPointTouchRing(i,speed0[i]);
							break;
						}
					}
					if (points[i].y>event.getY()&&points[i+1].y<event.getY()&&points[i].x<mPointX&&i!=pointsize-1) {
						onTurnplateListener.onPointTouchRing(i,speed0[i]);      
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
		
		//��ɫ
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
		
		
		if(roadtype==1){
		for(int index=0; index<pointsize; index++) {
			if(speed1[index]<=15)//����
			{
				canvas.drawArc(oval, points[index].angle, 360/pointsize, false, paintRed);	
			}
			else if(speed1[index]>15&&speed1[index]<=30)
			{
				canvas.drawArc(oval, points[index].angle, 360/pointsize, false, paintOrange);	
			}
			else if (speed1[index]>30&&speed1[index]<=50) {
				canvas.drawArc(oval, points[index].angle, 360/pointsize, false, paintBlue);	
			}
			else 
			{
				canvas.drawArc(oval, points[index].angle, 360/pointsize, false, paintGreen);	
			}		
		}
		for (int index = 0; index < pointsize; index++) {
			drawInCenter(canvas, points[index].bitmap, points[index].x, points[index].y,points[index].flag);
			drwaText(canvas,index,points[index].x+50,points[index].y);	
		}
		}
		else if(roadtype==0){
			for(int index=0; index<pointsize; index++) {
				if(speed0[index]<=15)//����
				{
					canvas.drawArc(oval, points[index].angle, 360/pointsize, false, paintRed);	
				}
				else if(speed0[index]>15&&speed0[index]<=40)
				{
					canvas.drawArc(oval, points[index].angle, 360/pointsize, false, paintOrange);	
				}
				else if (speed0[index]>40&&speed0[index]<=60) {
					canvas.drawArc(oval, points[index].angle, 360/pointsize, false, paintBlue);	
				}
				else 
				{
					canvas.drawArc(oval, points[index].angle, 360/pointsize, false, paintGreen);	
				}
			}
			for(int index=0;index<pointsize;index++)
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
		if(roadtype==0){
			p1.setTextAlign(Align.RIGHT);//������� 
			CrossingEntity entity = CrossingListInner.get(index);
			canvas.drawText(entity.getName(), x, y, p1);
		}
		else if(roadtype==1){
			p1.setTextAlign(Align.LEFT);//���Ҷ��� 
			CrossingEntity entity = CrossingListOut.get(index);
			canvas.drawText(entity.getName(), x, y, p1);
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
	

	/**
	 *	·��ͼ�ε� 
	 * @author Administrator
	 *
	 */
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
		CrossingEntity entity;
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
