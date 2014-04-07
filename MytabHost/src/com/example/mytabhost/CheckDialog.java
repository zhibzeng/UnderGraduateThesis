package com.example.mytabhost;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CheckDialog extends Activity{
	private Button check=null;
	private Button cancer=null;
	private TextView textView=null;
	private int num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_xml);
		check=(Button)findViewById(R.id.btnupdate);
		cancer=(Button)findViewById(R.id.btncancer2);
		textView=(TextView)findViewById(R.id.checktext);
		num=checkEdition();
		if (num==0) {
			textView.setText("当前版本为最新版本");
			check.setVisibility(View.GONE);
		}
		else {
			textView.setText("当前版本不是最新版本，请点击升级");
			check.setOnClickListener(new myDialogListener());
		}
		check.setVisibility(View.VISIBLE);
		
		cancer.setOnClickListener(new myDialogListener());
	}
	private int checkEdition()//判断是否需要更新版本，1代表需要，0代表不需要
	{
		return 1;
	}
	
	class myDialogListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Intent intent1=new Intent();
			if(v==check)
			{
				textView.setText("当前版本为最新版本");
				check.setVisibility(View.GONE);
			}
			else {
				finish();
			}
		}
		
	}


}
