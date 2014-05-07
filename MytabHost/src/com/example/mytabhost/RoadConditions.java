package com.example.mytabhost;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import com.example.mytabhost.R;
import com.example.mytabhost.entity.RoadConditionEntity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RoadConditions extends Activity{
	
	private ListView roadConditionList;
	private ArrayList<HashMap<String, Object>> listItem;
	private SimpleAdapter listItemAdapter;
	private ProgressDialog myDialog;// 声明ProgressDialog类型变量
	private List<RoadConditionEntity> lists = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.road_condition);
		roadConditionList = (ListView)findViewById(R.id.roadconditionlist);
		getRoadCondition();
	}
	
	
	/**
	 * initialize the traffic condition list view
	 */
	public void initListView(){
		listItem = new ArrayList<HashMap<String, Object>>();
		for(int i=0;i<lists.size();i++)
	     {
	        HashMap<String, Object> map = new HashMap<String, Object>();
//	        map.put("road_condition_image", R.drawable.about);//图像资源的ID,无图片则设为null
	        map.put("road_condition_title", lists.get(i).getContent()+" "+lists.get(i).getDatetime());
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
	
	
	/**
	 * 获取 roadcondition
	 */
	public void getRoadCondition(){
		myDialog = ProgressDialog.show(RoadConditions.this, "亲，请稍等一会哦...", "正在努力加载数据...",
				true);
		myDialog.getWindow().setGravity(Gravity.CENTER); //居中显示加载数据对话框
		myDialog.setCancelable(true);
		new Thread() {
			public void run() {
				try {
					TransferData transferData = new TransferData();
					try {
						lists = transferData.getRoadConditionList();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						RoadConditions.this.finish();
					}
					Thread.sleep(1500);//模拟加载，停留1.5秒
					Message m = new Message();
					listHandler.sendMessage(m);
				} catch (Exception e) {
					e.printStackTrace();
					RoadConditions.this.finish();
				} finally {
					myDialog.dismiss();
				}
			}
		}.start();
	}
	
	
	/**
	 * 刷新数据Hander
	 */
	final Handler listHandler = new Handler() {
		public void handleMessage(Message msg) {
	        if(lists != null){
	        	initListView();
	        }
	        else {
				Log.i("TAG", "lists is null");
				RoadConditions.this.finish();
			}
	        
		}
	};
	


}


