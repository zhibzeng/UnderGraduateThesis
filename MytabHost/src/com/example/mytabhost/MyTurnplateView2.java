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
 * 类名称：TurnplateView   
 * 类描述：   
 * 创建人： zhoujun   
 * 创建时间：2011-11-29 上午10:49:27   
 * 修改人：Administrator   
 * 修改时间：2011-11-29 上午10:49:27   
 * 修改备注：   
 * @version    
 *
 */
public class MyTurnplateView2 extends View implements  OnTouchListener{
	
	

	private OnTurnplateListener2 onTurnplateListener2;
	public void setOnTurnplateListener2(OnTurnplateListener2 onTurnplateListener2) {
		this.onTurnplateListener2 = onTurnplateListener2;
	}
	/**
	 * 画笔：点、线
	 */
	private Paint mPaint = new Paint();
	/**
	 * 画笔：圆
	 */
	private Paint paintCircle =  new Paint();
	/**
	 * 图标列表
	 */
	private static int  PONIT_NUM = 28;
	private static int  PONIT_NUM1;
	private Bitmap[] icons = new Bitmap[31];
	/**
	 * point列表
	 */
	private Point[] points;
	/**
	 * 数目
	 */
	
	/**
	 * 圆心坐标
	 */
	private float mPointX=0, mPointY=0;
	/**
	 * 半径
	 */
	private float mRadius = 0;
	/**
	 * 每两个点间隔的角度
	 */
	//private int mDegreeDelta;
	private double mDegreeDelta;
	/**
	 * 每次转动的角度差
	 */
	//private int tempDegree = 0;
	private float tempDegree=0;
	/**
	 * 选中的图标标识 999：未选中任何图标
	 */
	private int chooseBtn=999;
	private Matrix mMatrix = new Matrix();  
	//initangel为转过的角度
	
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
	 * 方法名：loadBitmaps 
	 * 功能：装载图片
	 * 参数：
	 * @param key
	 * @param d
	 * 创建人：zhoujun  
	 * 创建时间：2011-11-28
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
	 * 方法名：loadIcons 
	 * 功能：获取所有图片
	 * 参数：
	 * 创建人：zhoujun  
	 * 创建时间：2011-11-28
	 */
	public void loadIcons(){
		Resources r = getResources();
		for(int i=0;i<PONIT_NUM;i++)
	       loadBitmaps(i, r.getDrawable(R.drawable.bus));
	}
	
	/**
	 * 
	 * 方法名：initPoints 
	 * 功能：初始化每个点
	 * 参数：
	 * 创建人：zhoujun  
	 * 创建时间：2011-11-28
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
	 * 方法名：resetPointAngle 
	 * 功能：重新计算每个点的角度
	 * 参数：
	 * @param x
	 * @param y
	 * 创建人：zhoujun  
	 * 创建时间：2011-11-28
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
	 * 方法名：computeCoordinates 
	 * 功能：计算每个点的坐标
	 * 参数：
	 * 创建人：zhoujun  
	 * 创建时间：2011-11-28
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
	 * 方法名：computeMigrationAngle 
	 * 功能：计算偏移角度
	 * 参数：
	 * @param x
	 * @param y
	 * 创建人：zhoujun  
	 * 创建时间：2011-11-28
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
	 * 方法名：computeCurrentDistance 
	 * 功能：计算触摸的位置与各个元点的距离
	 * 参数：
	 * @param x
	 * @param y
	 * @return
	 * 创建人：zhoujun  
	 * 创建时间：2011-11-29
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
		
		
		float offset=mRadius/100;//因线太细，所以扩展圆环中相应的宽度
		float eventx=mPointX-event.getX();
		float eventy=mPointY-event.getY();
		
		if ((eventx*eventx+eventy*eventy)>=(mRadius-offset)*(mRadius-offset)
				&&(eventx*eventx+eventy*eventy)<=(mRadius+offset)*(mRadius+offset)) {//在点击在圆环上
			if(PONIT_NUM1==28){//表示k1
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
			else if(PONIT_NUM1==29)//表示外圈k2
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
	        	resetPointAngle(event.getX(), event.getY());//转动
	    		computeCoordinates();
	    		invalidate();
	            break;
	        case MotionEvent.ACTION_UP:
	        	switchScreen(event);
	        	tempDegree = 0;
	        	invalidate();
	            break;
	        case MotionEvent.ACTION_CANCEL:
	        	//系统在运行到一定程度下无法继续响应你的后续动作时会产生此事件。
	        	//一般仅在代码中将其视为异常分支情况处理
	            break;
	        }
		return true;
	}
	
  	@Override
	public void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.rgb(48, 189, 243));
		paint.setAntiAlias(true);//消除锯齿
 		paint.setStyle(Paint.Style.STROKE);// 风格为圆环
		paint.setStrokeWidth(10); // 圆环宽度
		
		Paint paint1 = new Paint();
		paint1.setColor(Color.RED);
		paint1.setAntiAlias(true);//消除锯齿
 		paint1.setStyle(Paint.Style.STROKE);// 风格为圆环
		paint1.setStrokeWidth(10); // 圆环宽度
		
		Paint paint2 = new Paint();
		paint2.setColor(Color.rgb(40, 112, 33));//墨绿色
		paint2.setAntiAlias(true);//消除锯齿
 		paint2.setStyle(Paint.Style.STROKE);// 风格为圆环
		paint2.setStrokeWidth(10); // 圆环宽度
		paint2.setStyle(Paint.Style.FILL);
		paint2.setTextSize(20);
		
		Paint p=new Paint();
		p.setColor(Color.BLACK);//墨绿色
		p.setTextSize(30);
		p.setAntiAlias(true);//消除锯齿  

		//画箭头，即行车方向
				float widthofscreen=0;
				if (mPointX>0) {
					widthofscreen=mPointX-mRadius-(float)(mRadius/2.6);
				}
				else {
					widthofscreen=mPointX+mRadius+(float)(mRadius/2.6);
				}
				canvas.drawLine(widthofscreen, mPointY-50, widthofscreen, mPointY+50, paint2);
				//画三角
				Path path=new Path();
				path.moveTo(widthofscreen-15, mPointY-50);
				path.lineTo(widthofscreen+15, mPointY-50);
				path.lineTo(widthofscreen,mPointY-90);
				path.close();
				canvas.drawPath(path,paint2);
	
		
		if (mPointX>0) {
			p.setTextAlign(Align.RIGHT);
			canvas.drawText("行", widthofscreen-15, mPointY-60, p);
			canvas.drawText("进", widthofscreen-15, mPointY-25, p);
			canvas.drawText("方", widthofscreen-15, mPointY+10, p);
			canvas.drawText("向", widthofscreen-15, mPointY+45, p);
		}
		else {
			p.setTextAlign(Align.LEFT);
			canvas.drawText("行", widthofscreen+15, mPointY-60, p);
			canvas.drawText("进", widthofscreen+15, mPointY-25, p);
			canvas.drawText("方", widthofscreen+15, mPointY+10, p);
			canvas.drawText("向", widthofscreen+15, mPointY+45, p);
		}
		
		
		
		
		canvas.drawCircle( mPointX, mPointY, mRadius, paint);
//		Bitmap bitmap = ((BitmapDrawable)(getResources().getDrawable(R.drawable.circle_bg))).getBitmap();
	//	Bitmap backgroundBitmap = ((BitmapDrawable)(getResources().getDrawable(R.drawable.hot))).getBitmap();
		//在中间添加图片
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
		p1.setAntiAlias(true);//消除锯齿  
		if(PONIT_NUM1==28){
			p1.setTextAlign(Align.RIGHT);//靠右对齐 
		switch(index)
		{
		case 28:canvas.drawText("双桥子北站", x, y, p1);break;
		case 27:canvas.drawText("双桥子南站", x, y, p1);break;
		case 26:canvas.drawText("牛市口站", x, y, p1);break;
		case 25:canvas.drawText("莲桂东路口站", x, y, p1);break;
		case 24:canvas.drawText("龙舟路口站", x, y, p1);break;
		case 23:canvas.drawText("成仁公交站", x, y, p1);break;
		case 22:canvas.drawText("东湖公园站", x, y, p1);break;
		case 21:canvas.drawText("科华路口站", x, y, p1);break;
		case 20:canvas.drawText("人南立交东站", x, y, p1);break;
		case 19:canvas.drawText("紫荆北路口站", x, y, p1);break;		
		case 18:canvas.drawText("红牌楼东站",x, y, p1);break;
		case 17:canvas.drawText("红牌楼西站", x, y, p1);break;
		case 16:canvas.drawText("少陵路口站", x, y, p1);break;
		case 15:canvas.drawText("清水河站", x, y, p1);break;
		case 14:canvas.drawText("光华村街口站", x, y, p1);break;
		case 13:canvas.drawText("府南新区站", x, y, p1);break;
		case 12:canvas.drawText("蜀汉路口北站", x, y, p1); break;
		case 11:canvas.drawText("营门口北站", x, y, p1);break;
		case 10:canvas.drawText("西南交大站", x, y, p1);break;
		case 9:canvas.drawText("商贸大道口站", x, y, p1);break;
		case 8:canvas.drawText("火车北站西站", x, y, p1);break;
		case 7:canvas.drawText("高笋塘站", x, y, p1);break;
		case 6:canvas.drawText("三友路口站", x, y, p1);break;
		case 5:canvas.drawText("桃蹊路口站", x, y, p1);break;
		case 4:canvas.drawText("建设路口站", x, y, p1);break;
		case 3:canvas.drawText("建设南口路站", x, y, p1);break;
		case 2:canvas.drawText("杉板桥站", x, y, p1);break;
		case 1:canvas.drawText("双林北支路口站", x, y, p1);break;
		case 0:canvas.drawText("万年场站", x, y, p1);break;
		}
		}
		else if(PONIT_NUM1==29){
			p1.setTextAlign(Align.LEFT);//靠左对齐 
			switch(index)
			{
			case 0:canvas.drawText("建设南路口站", x, y, p1);break;
			case 1:canvas.drawText("建设路口站", x, y, p1);break;
			case 2:canvas.drawText("桃蹊路口站", x, y, p1);break;
			case 3:canvas.drawText("三友路口站", x, y, p1);break;
			case 4:canvas.drawText("高笋塘站", x, y, p1);break;
			case 5:canvas.drawText("火车北站西站", x, y, p1);break;
			case 6:canvas.drawText("商贸大道口站", x, y, p1);break;
			case 7:canvas.drawText("西南交大站", x, y, p1);break;
			case 8:canvas.drawText("营门口北站", x, y, p1);break;
			case 9:canvas.drawText("蜀汉路口北站", x, y, p1);break;		
			case 10:canvas.drawText("府南新区站",x, y, p1);break;
			case 11:canvas.drawText("光华村街口站", x, y, p1);break;
			case 12:canvas.drawText("清水河站", x, y, p1);break;
			case 13:canvas.drawText("少陵路口站", x, y, p1);break;
			case 14:canvas.drawText("红牌楼西站", x, y, p1);break;
			case 15:canvas.drawText("红牌楼东站", x, y, p1);break;
			case 16:canvas.drawText("紫荆北路口站", x, y, p1); break;
			case 17:canvas.drawText("人南立交东站", x, y, p1);break;
			case 18:canvas.drawText("科华路口站", x, y, p1);break;
			case 19:canvas.drawText("东湖公园站", x, y, p1);break;
			case 20:canvas.drawText("成仁公交站", x, y, p1);break;
			case 21:canvas.drawText("龙舟路口站", x, y, p1);break;
			case 22:canvas.drawText("莲桂东路口站", x, y, p1);break;
			case 23:canvas.drawText("牛市口站", x, y, p1);break;
			case 24:canvas.drawText("双桥子南站", x, y, p1);break;
			case 25:canvas.drawText("双桥子北站", x, y, p1);break;
			case 26:canvas.drawText("万年场站", x, y, p1);break;
			case 27:canvas.drawText("双林北支路口站",x, y, p1);break;
			case 28:canvas.drawText("杉板桥站",x, y, p1);break;
			}
		}
	}
	/**
	 * 
	 * 方法名：drawInCenter 
	 * 功能：把点放到图片中心处
	 * 参数：
	 * @param canvas
	 * @param bitmap
	 * @param left
	 * @param top
	 * 创建人：zhoujun  
	 * 创建时间：2011-11-28
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
		 * 位置标识
		 */
		int flag;
		/**
		 * 图片
		 */
		Bitmap bitmap;
		
		/**
		 * 角度
		 */
		//int angle;
		float angle;
		
		/**
		 * x坐标
		 */
		float x;
		
		/**
		 * y坐标
		 */
		float y;
		
		/**
		 * 点与圆心的中心x坐标
		 */
		float x_c;
		/**
		 * 点与圆心的中心y坐标
		 */
		float y_c;
		
		boolean isCheck;
	}

	 public static interface OnTurnplateListener2 {
	       
	     public void onPointTouch2(Point point);//路口相应函数
	     public void onPointTouchRing2(int i);//线的响应函数
	       	             	        
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
