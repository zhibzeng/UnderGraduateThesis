package com.example.mytabhost;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.text.TextUtils;

public class NotificationActivity extends Activity{
	
	private ProgressDialog myDialog;// ����ProgressDialog���ͱ���
	private List<NotificationEntity>list1=null;
	private List<NotificationEntity>list2=null;
	private List<NotificationEntity>list3=null;
	private List<NotificationEntity>list4=null;
	private List<String> data1=new ArrayList<String>();
	private List<String> data2=new ArrayList<String>();
	private List<String> data3=new ArrayList<String>();
	private List<String> data4=new ArrayList<String>();
	private List<List<String>> lists = new ArrayList<List<String>>();
	private String[][] generals= new String[4][];
	private  ExpandableListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notification);
		getNotificayions();
	}
	
	
	
	/**
	 * ˢ������Hander
	 */
	final Handler listHandler = new Handler() {
		public void handleMessage(Message msg) {
	        if(list4!=null){
	        	for (int i = 0; i < list1.size(); i++) {
	        		data1.add(list1.get(i).getTitle());
	        		data2.add(list2.get(i).getTitle());
	        		data3.add(list3.get(i).getTitle());
					data4.add(list4.get(i).getTitle());
				}   	
	        }
	        else {
				Log.i("TAG", "list1~4 is null");
				NotificationActivity.this.finish();
			}
	         //װ������ͼ�б�
	        lists.add(data1);
	        lists.add(data2);
	        lists.add(data3);
	        lists.add(data4);
	        generals=toArray(lists);
	        ExpandableListConfig();
	        
		}
	};
	
	
	/**
	 * ��ȡ notifications
	 */
	public void getNotificayions(){
		myDialog = ProgressDialog.show(NotificationActivity.this, "�ף����Ե�һ��Ŷ...", "����Ŭ����������...",
				true);
		myDialog.getWindow().setGravity(Gravity.CENTER); //������ʾ�������ݶԻ���
		myDialog.setCancelable(true);
		new Thread() {
			public void run() {
				try {
					TransferData transferData = new TransferData();
					try {
						list1 = transferData.getNotificationList(1);
						list2 = transferData.getNotificationList(2);
						list3 = transferData.getNotificationList(3);
						list4 = transferData.getNotificationList(4);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						NotificationActivity.this.finish();
					}
					Thread.sleep(1500);//ģ����أ�ͣ��1.5��
					Message m = new Message();
					listHandler.sendMessage(m);
				} catch (Exception e) {
					e.printStackTrace();
					NotificationActivity.this.finish();
				} finally {
					myDialog.dismiss();
				}
			}
		}.start();
	}
	
	
	/**
	 *	ExpandableList Setting 
	 */
	
	public void ExpandableListConfig(){
		adapter = new BaseExpandableListAdapter() {
			//��������ͼ��ͼƬ
            int[] logos = new int[] { R.drawable.notification1, R.drawable.notification2,R.drawable.notification3,R.drawable.notification4};
            
			//��������ͼ����ʾ����
			private String[] generalsTypes = new String[] { "·������", "��ʾ����", "��·��Ϣ","������ʾ"};

			//�Լ�����һ�����������Ϣ�ķ���
			TextView getTextView() {
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT, 64);
				TextView textView = new TextView(
						NotificationActivity.this);
				textView.setLayoutParams(lp);
				textView.setGravity(Gravity.CENTER_VERTICAL);
				textView.setPadding(26, 0, 0, 0);
				textView.setTextSize(18);
				textView.setTextColor(Color.BLACK);
				return textView;
			}
			
			//�Լ�����һ�����������Ϣ�ķ���
			TextView getTitleTextView() {
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT, 64);
				TextView textView = new TextView(
						NotificationActivity.this);
				textView.setLayoutParams(lp);
				textView.setGravity(Gravity.CENTER_VERTICAL);
				textView.setPadding(26, 0, 0, 0);
				textView.setTextSize(18);
				textView.setTextColor(Color.BLACK);
				textView.setEllipsize(TextUtils.TruncateAt.END);
				//textView.setMarqueeRepeatLimit(4);
				textView.setSingleLine();
				return textView;
			}

			
			//��дExpandableListAdapter�еĸ�������
			@Override
			public int getGroupCount() {
				// TODO Auto-generated method stub
				return generalsTypes.length;
			}

			@Override
			public Object getGroup(int groupPosition) {
				// TODO Auto-generated method stub
				return generalsTypes[groupPosition];
			}

			@Override
			public long getGroupId(int groupPosition) {
				// TODO Auto-generated method stub
				return groupPosition;
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				// TODO Auto-generated method stub
				return generals[groupPosition].length;
			}

			@Override
			public Object getChild(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return generals[groupPosition][childPosition];
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return childPosition;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				   // TODO Auto-generated method stub
                LinearLayout ll = new LinearLayout(
                        NotificationActivity.this);
                ll.setOrientation(0);
                ImageView logo = new ImageView(NotificationActivity.this);
                logo.setImageResource(logos[groupPosition]);
                logo.setPadding(50, 0,0, 0);
                ll.addView(logo);
                TextView textView = getTextView();
                textView.setTextColor(Color.BLACK);
                textView.setText(getGroup(groupPosition).toString());
                ll.addView(textView);
                return ll;
			}

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LinearLayout ll = new LinearLayout(NotificationActivity.this);
				ll.setOrientation(0);
				TextView textView = getTitleTextView();
				textView.setText(getChild(groupPosition, childPosition)
						.toString());
				ll.addView(textView);
				return ll;
			}

			@Override
			public boolean isChildSelectable(int groupPosition,
					int childPosition) {
				// TODO Auto-generated method stub
				return true;
			}

		};
		ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.list);
		expandableListView.setAdapter(adapter);
		//����item����ļ�����
		expandableListView.setOnChildClickListener(itemClickListener);
	}

	
			
	/**
	 * ��listת���ɶ�άstring���͵�����
	 * @param lists
	 * @return
	 */
	public static String[][] toArray(List<List<String>> lists) 
	{
			
	String[][] result = new String[lists.size()][];	
			for (int i = 0; i < lists.size(); i++) {	
				result[i] = lists.get(i).toArray(new String[]{});
			}
			return result;
	}
	
	
	
	/**
	 * item �����¼�
	 */
    private OnChildClickListener itemClickListener = new OnChildClickListener() {
    	@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			Toast.makeText(
					NotificationActivity.this,
					"������" + adapter.getChild(groupPosition, childPosition),
					Toast.LENGTH_SHORT).show();
			Intent intent=new Intent();
	    	intent.setClass(NotificationActivity.this, WebShow.class);
	    	Bundle bundle=new Bundle();
	    	bundle.putInt("groupPosition", groupPosition);
	    	bundle.putInt("childPosition", childPosition);
	    	bundle.putSerializable("list1", (Serializable) list1);
	    	bundle.putSerializable("list2", (Serializable) list2);
	    	bundle.putSerializable("list3", (Serializable) list3);
	    	bundle.putSerializable("list4", (Serializable) list4);
	    	intent.putExtras(bundle);
			NotificationActivity.this.startActivity(intent);
			return false;
		}
	};
	
	
	
}

