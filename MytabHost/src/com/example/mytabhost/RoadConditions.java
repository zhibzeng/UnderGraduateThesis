package com.example.mytabhost;
import java.util.ArrayList;
import java.util.HashMap;
import com.example.mytabhost.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RoadConditions extends Activity{
	
	private ListView roadConditionList;
	private ArrayList<HashMap<String, Object>> listItem;;
	private SimpleAdapter listItemAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.road_condition);
		roadConditionList = (ListView)findViewById(R.id.roadconditionlist);
		initListView();
		
	}
	
	
	/**
	 * initialize the traffic condition list view
	 */
	public void initListView(){
		listItem = new ArrayList<HashMap<String, Object>>();
		for(int i=0;i<10;i++)
	     {
	        HashMap<String, Object> map = new HashMap<String, Object>();
//	        map.put("road_condition_image", R.drawable.about);//图像资源的ID,无图片则设为null
	        map.put("road_condition_title", "天府广场至锦江宾馆,由北往南,车辆排行较长，行驶速度缓慢。  2014-04-11");
//	        map.put("road_condition_date","2014-04-11");
	        listItem.add(map);
	     }
		 //生成适配器的Item和动态数组对应的元素
	     listItemAdapter = new SimpleAdapter(this,listItem,//数据源 
	            R.layout.road_condition_list_item,//ListItem的XML实现
	            //动态数组与ImageItem对应的子项        
	            new String[] {"road_condition_title"},
	            new int[] {R.id.road_condition_title}
	     );
	     roadConditionList.setAdapter(listItemAdapter);
	     }


}


