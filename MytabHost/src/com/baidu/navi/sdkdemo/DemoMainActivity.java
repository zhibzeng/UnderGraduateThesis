package com.baidu.navi.sdkdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mytabhost.NotificationActivity;
import com.example.mytabhost.R;
import com.example.mytabhost.RoadConditions;
import com.example.mytabhost.SinaWeiboWebView;
import com.example.mytabhost.entity.RoadConditionEntity;


public class DemoMainActivity extends ListActivity {

	public void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);

		setContentView(R.layout.activity_main);
		
		//创建Demo视图
		initViews();
	}

	private void initViews() {
		setListAdapter(new SimpleAdapter(this, getListData(),
				android.R.layout.simple_list_item_1, new String[] { "title" },
				new int[] {android.R.id.text1 }));
		getListView().setTextFilterEnabled(true);
	}

	private ArrayList<Map<String, Object>> getListData() {
		ArrayList<Map<String, Object>> maps = new ArrayList<Map<String, Object>>(
				0);
		Map<String, Object> item1 = new HashMap<String, Object>(2);
		item1.put("title","交通公告信息");
		Intent intent1 = new Intent();
		intent1.setClass(this,NotificationActivity.class);
		item1.put("intent",intent1);
		maps.add(item1);
		
		Map<String, Object> item2 = new HashMap<String, Object>(2);
		item2.put("title","微博路况信息");
		Intent intent2 = new Intent();
		intent2.setClass(this,SinaWeiboWebView.class);
		item2.put("intent",intent2);
		maps.add(item2);
		
		Map<String, Object> item3 = new HashMap<String, Object>(2);
		item3.put("title","实时交通路况");
		Intent intent3 = new Intent();
		intent3.setClass(this,RoadConditions.class);
		item3.put("intent",intent3);
		maps.add(item3);
		
		return maps;
	}



	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map<String, Object> map = (Map<String, Object>)l.getItemAtPosition(position);
		Intent intent = (Intent) map.get("intent");
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}


}
