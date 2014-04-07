package com.example.mytabhost;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ClearDialog extends Activity{
	private Button clear=null;
	private Button cancer=null;
	private TextView textView=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clear_xml);
		clear=(Button)findViewById(R.id.btnclear);
		cancer=(Button)findViewById(R.id.btncancer);
		textView=(TextView)findViewById(R.id.cleartext);
		textView.setText("当前缓存为200M");
		clear.setOnClickListener(new myDialogListener());
		cancer.setOnClickListener(new myDialogListener());
	}
	class myDialogListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Intent intent1=new Intent();
			if(v==clear)
			{
				textView.setText("当前缓存为0M");
			}
			else {
				finish();
			}
		}
		
	}


}
