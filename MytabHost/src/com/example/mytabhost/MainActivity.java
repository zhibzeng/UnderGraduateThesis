package com.example.mytabhost;

import com.baidu.navi.sdkdemo.DemoMainActivity;
import com.example.mytabhost.R;
import com.example.mytabhost.util.CompressImage;

import android.R.layout;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

/**
 * <一句话功能简述>定制居底的TabHost<BR>
 * <功能详细描述>
 * 
 * @author JeffreyTseng
 * @version [版本号, 2011-1-27]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity
{
    /**
     * TabHost控件
     */
    private TabHost mTabHost;

    /**
     * TabWidget控件
     */
    private TabWidget mTabWidget;
    private long exitTime = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        //full screen and no title 
  		final Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
  		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        mTabHost = this.getTabHost();
        /* 去除标签下方的白线 */
       mTabHost.setPadding(mTabHost.getPaddingLeft(),
                mTabHost.getPaddingTop(), mTabHost.getPaddingRight(),
                mTabHost.getPaddingBottom() - 5);
        Resources rs = getResources();

        Intent layout1intent = new Intent();
        layout1intent.setClass(this, RoadOne.class);
        TabHost.TabSpec layout1spec = mTabHost.newTabSpec("RoadOne");
        Drawable tab1_drawable = rs.getDrawable(R.drawable.tab1);
        float width1 = (float) (tab1_drawable.getIntrinsicWidth()*0.9);
        float height1= (float) (tab1_drawable.getIntrinsicHeight()*0.9);
        Drawable realDrawable1 = CompressImage.zoomDrawable(tab1_drawable,width1,height1);
        View indicator_1 = this.getLayoutInflater().inflate(R.layout.tab_indicator, null);
        TextView tv1 = (TextView) indicator_1.findViewById(R.id.tab_indicator_tv);
        tv1.setText("二环公交");
        ImageView iv1 = (ImageView) indicator_1.findViewById(R.id.tab_indicator_iv);
        iv1.setImageDrawable(realDrawable1);
        layout1spec.setIndicator(indicator_1);
        layout1spec.setContent(layout1intent);
        mTabHost.addTab(layout1spec);
        
        Intent layout4intent = new Intent();
        layout4intent.setClass(this,RoadTwo.class);
        TabHost.TabSpec layout4spec = mTabHost.newTabSpec("Bus");
        Drawable tab4_drawable = rs.getDrawable(R.drawable.tab4);
        float width4 = (float) (tab4_drawable.getIntrinsicWidth()*0.9);
        float height4= (float) (tab4_drawable.getIntrinsicHeight()*0.9);
        Drawable realDrawable4 = CompressImage.zoomDrawable(tab4_drawable,width4,height4);
        View indicator_4 = this.getLayoutInflater().inflate(R.layout.tab_indicator, null);
        TextView tv4 = (TextView) indicator_4.findViewById(R.id.tab_indicator_tv);
        tv4.setText("二环公交");
        ImageView iv4 = (ImageView) indicator_4.findViewById(R.id.tab_indicator_iv);
        iv4.setImageDrawable(realDrawable4);
        layout4spec.setIndicator(indicator_4);
        layout4spec.setContent(layout4intent);
        mTabHost.addTab(layout4spec);
        
        
        Intent layout5intent = new Intent();
        layout5intent.setClass(this,AppCollecions.class);
        TabHost.TabSpec layout5spec = mTabHost.newTabSpec("AppCollection");
        Drawable tab5_drawable = rs.getDrawable(R.drawable.tab5);
        float width5 = (float) (tab5_drawable.getIntrinsicWidth()*0.9);
        float height5= (float) (tab5_drawable.getIntrinsicHeight()*0.9);
        Drawable realDrawable5 = CompressImage.zoomDrawable(tab5_drawable,width5,height5);
        View indicator_5 = this.getLayoutInflater().inflate(R.layout.tab_indicator, null);
        TextView tv5 = (TextView) indicator_5.findViewById(R.id.tab_indicator_tv);
        tv5.setText("交通应用");
        ImageView iv5 = (ImageView) indicator_5.findViewById(R.id.tab_indicator_iv);
        iv5.setImageDrawable(realDrawable5);
        layout5spec.setIndicator(indicator_5);
        layout5spec.setContent(layout5intent);
        mTabHost.addTab(layout5spec);

        Intent layout2intent = new Intent();
        //layout2intent.setClass(this, SinaWeiboWebView.class);
        //layout2intent.setClass(this,NotificationActivity.class);
        layout2intent.setClass(this,DemoMainActivity.class);
        TabHost.TabSpec layout2spec = mTabHost.newTabSpec("RoadCondition");
        Drawable tab2_drawable = rs.getDrawable(R.drawable.tab2);
        float width2 = (float) (tab2_drawable.getIntrinsicWidth()*0.9);
        float height2= (float) (tab2_drawable.getIntrinsicHeight()*0.9);
        Drawable realDrawable2 = CompressImage.zoomDrawable(tab2_drawable,width2,height2);
        View indicator_2 = this.getLayoutInflater().inflate(R.layout.tab_indicator, null);
        TextView tv2 = (TextView) indicator_2.findViewById(R.id.tab_indicator_tv);
        tv2.setText("交通信息");
        ImageView iv2 = (ImageView) indicator_2.findViewById(R.id.tab_indicator_iv);
        iv2.setImageDrawable(realDrawable2);
        layout2spec.setIndicator(indicator_2);
        layout2spec.setContent(layout2intent);
        mTabHost.addTab(layout2spec);
        
        


        Intent layout3intent = new Intent();
        layout3intent.setClass(this, Setting.class);
        TabHost.TabSpec layout3spec = mTabHost.newTabSpec("Setting");
        Drawable tab3_drawable = rs.getDrawable(R.drawable.tab3);
        float width3 = (float) (tab3_drawable.getIntrinsicWidth()*0.9);
        float height3= (float) (tab3_drawable.getIntrinsicHeight()*0.9);
        Drawable realDrawable3 = CompressImage.zoomDrawable(tab3_drawable,width3,height3);
        View indicator_3 = this.getLayoutInflater().inflate(R.layout.tab_indicator, null);
        TextView tv3 = (TextView) indicator_3.findViewById(R.id.tab_indicator_tv);
        tv3.setText("软件设置");
        ImageView iv3 = (ImageView) indicator_3.findViewById(R.id.tab_indicator_iv);
        iv3.setImageDrawable(realDrawable3);
        layout3spec.setIndicator(indicator_3);
        layout3spec.setContent(layout3intent);
        mTabHost.addTab(layout3spec);
        
       
        
        
        
//        Intent layout5intent = new Intent();
//        layout5intent.setClass(this,RoadTwo.class);
//        TabHost.TabSpec layout5spec = mTabHost.newTabSpec("Map");
//        layout5spec.setIndicator("地图",
//                rs.getDrawable(android.R.drawable.stat_sys_phone_call_on_hold));
//        layout5spec.setContent(layout5intent);
//        mTabHost.addTab(layout5spec);
        
        
     
        
        /* 对Tab标签的定制 */
        mTabWidget = mTabHost.getTabWidget();
        mTabWidget.setBackgroundColor(Color.BLACK);
        for (int i = 0; i < mTabWidget.getChildCount(); i++)
        {
            /* 得到每个标签的视图 */
            View view = mTabWidget.getChildAt(i);
            /* 设置每个标签的背景 */
            if (mTabHost.getCurrentTab() == i)
            {
                //view.setBackgroundDrawable(getResources().getDrawable(
                  //      R.drawable.number_bg_pressed));
                
                view.setBackgroundColor(Color.rgb(48, 189, 243));
            }
            else
            {
                //view.setBackgroundDrawable(getResources().getDrawable(
                 //       R.drawable.number_bg)); 
            	view.setBackgroundColor(Color.BLACK);
            }
            /* 设置Tab间分割竖线的颜色 */
            mTabWidget.setBackgroundColor(Color.BLACK);
            /* 设置Tab间分割竖线的背景图片 */
           // mTabWidget.setBackgroundResource(0);
            /* 设置tab的高度 */
            mTabWidget.getChildAt(i).getLayoutParams().height = 80;
//            TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(
//                    android.R.id.title);
//            /* 设置tab内字体的颜色 */
//            tv.setTextColor(Color.WHITE);
//            tv.setTextSize(14);
        }

        /* 当点击Tab选项卡的时候，更改当前Tab标签的背景 */
        mTabHost.setOnTabChangedListener(new OnTabChangeListener()
        {
        	
            @Override
            public void onTabChanged(String tabId)
            {
            	mTabWidget.setBackgroundColor(Color.BLACK);
                for (int i = 0; i < mTabWidget.getChildCount(); i++)
                {
                    View view = mTabWidget.getChildAt(i);
                    if (mTabHost.getCurrentTab() == i)
                    {
                       //view.setBackgroundDrawable(getResources().getDrawable(
                       //         R.drawable.number_bg_pressed));
                        view.setBackgroundColor(Color.rgb(48, 189, 243));
                    	//view.setBackgroundColor(Color.GRAY);
                    }
                    else
                    {
                       // view.setBackgroundDrawable(getResources().getDrawable(
                       //       R.drawable.number_bg));
                    	view.setBackgroundColor(Color.BLACK);
                    }
                    mTabWidget.setBackgroundColor(Color.BLACK);
                }
            }
           
        });
        mTabWidget.setBackgroundColor(Color.BLACK);
        mTabHost.setCurrentTab(2);
    }
    
    
    
	/**
	 * 按下返回键	
	 */
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
	         if((System.currentTimeMillis()-exitTime) > 2000){  
	             Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	             exitTime = System.currentTimeMillis();   
	         } else {
	        	 android.os.Process.killProcess(android.os.Process.myPid());    //获取PID 
	        	 System.exit(0); 
	         }
	         return false;   
	     }
	     return super.onKeyDown(keyCode, event);
	 }
}
