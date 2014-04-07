package com.example.mytabhost;

import java.util.Timer;

import com.example.mytabhost.MyTurnplateView.Point;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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
public class TotalRoadBus extends View implements  OnTouchListener{
	
    private float width;
    private float height;
    private float mPointX;//Բ��x
    private float mPointY;//Բ��y
    private float mRadius;//�뾶
    private Paint paint1;
    private Paint paint2;

	private MyTouchListener2 MyTouchListener2;
	public void setMyTouchListener2(MyTouchListener2 MyTouchListener2) {
		this.MyTouchListener2 = MyTouchListener2;
	}
	public TotalRoadBus(Context context, float px, float py) {
		super(context);	
		setLayout(context);
	    mPointX=px/2;
	    mPointY=py/2;
	    mRadius=(float)(px/2.2);
	    
		paint1=new Paint();//���廭��
		paint1.setAntiAlias(true);
		paint1.setColor(Color.RED);
		paint1.setTextSize(6);
		
		
		paint2=new Paint();
		paint2.setAntiAlias(true);
		paint2.setColor(Color.YELLOW);
		paint2.setStyle(Paint.Style.STROKE);// ���ΪԲ��
		paint2.setStrokeWidth(5);
	}
	public void setLayout(Context context)
	{
		
	}
	
	protected void onDraw(Canvas canvas)
	{
	  //  Bitmap backgroundBitmap = ((BitmapDrawable)(getResources().getDrawable(R.drawable.hot))).getBitmap();
	  //  canvas.drawBitmap(backgroundBitmap,0, 0, null);
		Drawable drawable=getResources().getDrawable(R.drawable.hot2);
		drawable.setBounds(0, 120, 480, 640);
		//canvas.drawBitmap(drawable, 0, 0,null);
		drawable.draw(canvas);
		RectF oval=new RectF(mPointX-mRadius,mPointY-mRadius,mPointX+mRadius,mPointY+mRadius);
		for (int i = 0; i < 7; i++) {
			canvas.drawArc(oval, i*60, 60, true, paint2);
		}						
	} 	
	
	
	private void TouchEvent(MotionEvent event){
		POINT point=new POINT();
		point.x=event.getX();
		point.y=event.getY();
		MyTouchListener2.onMyPointTouch2(point);
		}

	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {	//��Ӧ����	
		 int action = event.getAction();
	        switch (action) {
	        case MotionEvent.ACTION_DOWN:
	            break;
	        case MotionEvent.ACTION_MOVE:
	            break;
	        case MotionEvent.ACTION_UP:
	            TouchEvent(event);
	            break;
	        case MotionEvent.ACTION_CANCEL:
	        	//ϵͳ�����е�һ���̶����޷�������Ӧ��ĺ�������ʱ��������¼���
	        	//һ����ڴ����н�����Ϊ�쳣��֧�������
	            break;
	        }
		return true;
	}
	
	
	
	class POINT{
		
		float x;
		float y;
	}

	 public static interface MyTouchListener2 {
	       
	     public void onMyPointTouch2(POINT point);
	       	             	        
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
