package com.example.mytabhost;

import com.example.mytabhost.R;
import com.example.mytabhost.R.string;
import com.iflytek.a.b;

import android.app.Activity;
import android.gesture.Gesture;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoShow extends Activity implements OnGestureListener
{
	GestureDetector detector;
	ImageView imageView;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photoshow);
        imageView=(ImageView)findViewById(R.id.photoshow);  
        //imageView.setImageResource(R.drawable.k2_1);
        Bundle bundle=this.getIntent().getExtras();
        int index=bundle.getInt("crossingid");    
        int round=bundle.getInt("round");
    	TransferData t = new TransferData();
        if(round==0){
        	imageView.setImageBitmap(t.getCrossingBitmap(this, round, index));
        }
        else if(round==1){//此时是外圈
        	imageView.setImageBitmap(t.getCrossingBitmap(this, round, index));
		}
        else{
        	//显示入口照片
        	imageView.setImageResource(R.drawable.rukou);
        }
        
        detector=new GestureDetector(this);  
        
    }
    public boolean onTouchEvent(MotionEvent me) {
	    return detector.onTouchEvent(me);
	}
	@Override
	public boolean onDown(MotionEvent e) { 
		// TODO Auto-generated method stub
		Toast.makeText(this, "以长按或者下滑关闭图片浏览", 8000).show();
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		if(e2.getY()-e1.getY()>40)//下滑
		{
			finish();
		}
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		finish();
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
