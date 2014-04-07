package com.example.mytabhost;

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
        int index=bundle.getInt("index");    
        int round=bundle.getInt("round");
        if(round==1){
        switch (index) {//此时是内圈
        case 0:imageView.setImageResource(R.drawable.k2_1);break;
		case 1:imageView.setImageResource(R.drawable.k2_2);break;
		case 2:imageView.setImageResource(R.drawable.k2_3);break;
		case 3:imageView.setImageResource(R.drawable.k2_4);break;
		case 4:imageView.setImageResource(R.drawable.k2_5);break;
		case 5:imageView.setImageResource(R.drawable.k2_6);break;
		case 6:imageView.setImageResource(R.drawable.k2_7);break;
		case 7:imageView.setImageResource(R.drawable.k2_8);break;
		case 8:imageView.setImageResource(R.drawable.k2_9);break;
		case 9:imageView.setImageResource(R.drawable.k2_10);break;
		case 10:imageView.setImageResource(R.drawable.k2_11);break;
		case 11:imageView.setImageResource(R.drawable.k2_12);break;
		case 12:imageView.setImageResource(R.drawable.k2_13);break;
		case 13:imageView.setImageResource(R.drawable.k2_14);break;
		case 14:imageView.setImageResource(R.drawable.k2_15);break;
		case 15:imageView.setImageResource(R.drawable.k2_16);break;
		case 16:imageView.setImageResource(R.drawable.k2_17);break;
		case 17:imageView.setImageResource(R.drawable.k2_18);break;
		case 18:imageView.setImageResource(R.drawable.k2_19);break;	
		case 19:imageView.setImageResource(R.drawable.k2_20);break;
		case 20:imageView.setImageResource(R.drawable.k2_21);break;
		case 21:imageView.setImageResource(R.drawable.k2_22);break;
		case 22:imageView.setImageResource(R.drawable.k2_23);break;
		case 23:imageView.setImageResource(R.drawable.k2_24);break;
		case 24:imageView.setImageResource(R.drawable.k2_25);break;
		case 25:imageView.setImageResource(R.drawable.k2_26);break;
		case 26:imageView.setImageResource(R.drawable.k2_27);break;
		case 27:imageView.setImageResource(R.drawable.k2_28);break;
		case 28:imageView.setImageResource(R.drawable.k2_29);break;
		case 29:imageView.setImageResource(R.drawable.k2_30);break;
		default:
			break;
		}
        }
        else {//此时是外圈
        	  switch (index) {//此时是内圈
            case 0:imageView.setImageResource(R.drawable.k1_1);break;
      		case 1:imageView.setImageResource(R.drawable.k1_2);break;
      		case 2:imageView.setImageResource(R.drawable.k1_3);break;
      		case 3:imageView.setImageResource(R.drawable.k1_4);break;
      		case 4:imageView.setImageResource(R.drawable.k1_5);break;
      		case 5:imageView.setImageResource(R.drawable.k1_6);break;
      		case 6:imageView.setImageResource(R.drawable.k1_7);break;
      		case 7:imageView.setImageResource(R.drawable.k1_8);break;
      		case 8:imageView.setImageResource(R.drawable.k1_9);break;
      		case 9:imageView.setImageResource(R.drawable.k1_10);break;
      		case 10:imageView.setImageResource(R.drawable.k1_11);break;
      		case 11:imageView.setImageResource(R.drawable.k1_12);break;
      		case 12:imageView.setImageResource(R.drawable.k1_13);break;
      		case 13:imageView.setImageResource(R.drawable.k1_14);break;
      		case 14:imageView.setImageResource(R.drawable.k1_15);break;
      		case 15:imageView.setImageResource(R.drawable.k1_16);break;
      		case 16:imageView.setImageResource(R.drawable.k1_17);break;
      		case 17:imageView.setImageResource(R.drawable.k1_18);break;
      		case 18:imageView.setImageResource(R.drawable.k1_19);break;	
      		case 19:imageView.setImageResource(R.drawable.k1_20);break;
      		case 20:imageView.setImageResource(R.drawable.k1_21);break;
      		case 21:imageView.setImageResource(R.drawable.k1_22);break;
      		case 22:imageView.setImageResource(R.drawable.k1_23);break;
      		case 23:imageView.setImageResource(R.drawable.k1_24);break;
      		case 24:imageView.setImageResource(R.drawable.k1_25);break;
      		case 25:imageView.setImageResource(R.drawable.k1_26);break;
      		case 26:imageView.setImageResource(R.drawable.k1_27);break;
      		case 27:imageView.setImageResource(R.drawable.k1_28);break;
      		case 28:imageView.setImageResource(R.drawable.k1_29);break;
      		case 29:imageView.setImageResource(R.drawable.k1_29);break;
      		case 30:imageView.setImageResource(R.drawable.k1_30);break;
      		default:
      			break;
      		}
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
