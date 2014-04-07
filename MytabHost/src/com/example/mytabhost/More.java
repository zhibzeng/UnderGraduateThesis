package com.example.mytabhost;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class More extends Activity
{
   // private TextView textView = null;
    private float width;
    private float height;
    private float mPointX;//圆心x
    private float mPointY;//圆心y
    private float mRadius;//半径
    private Intent intent;
    private Bundle bundle;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(new TotoalRoad(this));
    	DisplayMetrics metric = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(metric);
    	height=metric.heightPixels;
		width=metric.widthPixels;
		mPointX=width/2;
		mPointY=height/2;
		mRadius=(float)(width/2.2);
		
		
		intent=new Intent();
		intent.setClass(this,RoadTwo.class);			
		//Bundle bundle=new Bundle();	
    }
    class TotoalRoad extends View
    {
    	Path path= new Path();
    	Paint paint1,paint2;	
		public TotoalRoad(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			paint1=new Paint();//字体画笔
			paint1.setAntiAlias(true);
			paint1.setColor(Color.RED);
			paint1.setTextSize(6);
			
			
			paint2=new Paint();
			paint2.setAntiAlias(true);
			paint2.setColor(Color.YELLOW);
			paint2.setStyle(Paint.Style.STROKE);// 风格为圆环
			paint2.setStrokeWidth(15);
		}
		@Override
		protected void onDraw(Canvas canvas)
		{
			RectF oval=new RectF(mPointX-mRadius,mPointY-mRadius,mPointX+mRadius,mPointY+mRadius);
			for (int i = 0; i < 7; i++) {
				canvas.drawArc(oval, i*60, 60, true, paint2);
			}						
		} 	
		@Override
		
		
		
		public boolean onTouchEvent(MotionEvent event)//相应函数
		{	
			More aMore=new More();
			float x=event.getX();
			float y=event.getY();
			double distance=Math.sqrt((event.getY()-mPointY)*(event.getY()-mPointY)+(event.getX()-mPointX)*(event.getX()-mPointX));
			if (distance>mRadius) {
				return true;
			}
			if (event.getX()!=mPointX) {
				double d=(mPointY-event.getY())/(event.getX()-mPointX);
				double tanx=Math.atan(d);
				double angle=180*tanx/Math.PI;
				Toast.makeText(More.this, angle+"'", 1000).show();
				if(angle<60&&angle>=0&&y<mPointY)//表示点击到第一块区域
				{							
			    	//bundle.putInt("More_flag",1);
    				//intent.putExtras(bundle);
					//More.this.startActivity(intent);
    				paint2.setColor(Color.GREEN);			
				}
				else if((angle>=60&&angle<90&&y<=mPointY)||angle<-60&&angle>-90&&y<=mPointY){
					//More.this.startActivity(intent);
					paint2.setColor(Color.RED);
				}
				else if(angle>=-60&&angle<0&&y<=mPointY){
					//More.this.startActivity(intent);
					paint2.setColor(Color.BLACK);
				}
				else if(angle<60&&angle>=0&&y>=mPointY){
					//More.this.startActivity(intent);
					paint2.setColor(Color.BLUE);
				}
				else if((angle>=60&&angle<90&&y>=mPointY)||(angle<-60&&angle>-90&&y>=mPointY)){
				    //More.this.startActivity(intent);
					paint2.setColor(Color.CYAN);
				}
				else if(angle>=-60&&angle<0&&y>=mPointY){
					//More.this.startActivity(intent);
					paint2.setColor(Color.LTGRAY);
				}
			}
			else {
				if (event.getY()>mPointY) {//第5区域
					//More.this.startActivity(intent);
					paint2.setColor(Color.CYAN);
				}
				else if(event.getY()<mPointY){//第2区域
					//More.this.startActivity(intent);
					paint2.setColor(Color.RED);
				}
			}
			invalidate();
			return true;
		}
    }
}
