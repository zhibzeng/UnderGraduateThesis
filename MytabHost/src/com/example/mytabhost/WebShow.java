package com.example.mytabhost;

import java.util.List;

import org.json.JSONException;

import com.example.mytabhost.R.string;
import com.example.mytabhost.entity.NotificationEntity;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.iflytek.ui.SynthesizerDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.method.TextKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WebShow extends Activity implements OnClickListener,SynthesizerPlayerListener{

	private SharedPreferences mSharedPreferences;
	private static SynthesizerPlayer mSynthesizerPlayer;
	private Toast mToast;
	private int mPercentForBuffering = 0;
	private int mPercentForPlaying = 0;
	private SynthesizerDialog ttsDialog;
	private TextView mywebtext,contentTitle;
	private  List<NotificationEntity> list1 = null;
	private  List<NotificationEntity> list2 = null;
	private  List<NotificationEntity> list3 = null;
	private  List<NotificationEntity> list4 = null;
	private Button ttsButton=null;
	private Button returnButton=null;
	private Button soundConfig = null;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.mywebview);
	        contentTitle = (TextView)findViewById(R.id.notification_content_title);
	        mywebtext=(TextView)findViewById(R.id.webview);
	        mywebtext.setMovementMethod(ScrollingMovementMethod.getInstance());  
	        ttsButton=(Button)findViewById(R.id.MyWebViewSpeak);
	        returnButton=(Button)findViewById(R.id.MyWebViewReturn);
	        ttsButton.setOnClickListener(this);
	        returnButton.setOnClickListener(this);
	        soundConfig = (Button)findViewById(R.id.MyWebViewConfig);
	        soundConfig.setOnClickListener(this);
	        Bundle bundle=this.getIntent().getExtras();
	        int groupPosition=bundle.getInt("groupPosition");
	        int childPosition=bundle.getInt("childPosition");
	        list1 = (List<NotificationEntity>) getIntent().getSerializableExtra("list1");
	        list2 = (List<NotificationEntity>) getIntent().getSerializableExtra("list2");
	        list3 = (List<NotificationEntity>) getIntent().getSerializableExtra("list3");
	        list4 = (List<NotificationEntity>) getIntent().getSerializableExtra("list4");
	        
	        
	        switch (groupPosition) {
			case 0:
				Spanned sp1 = Html.fromHtml(list1.get(childPosition).getContent());
				mywebtext.setText(sp1);
				contentTitle.setText(list1.get(childPosition).getTitle());
				break;
			case 1:
				Spanned sp2 = Html.fromHtml(list2.get(childPosition).getContent());
				mywebtext.setText(sp2);
				contentTitle.setText(list2.get(childPosition).getTitle());
				break;
			case 2:
				Spanned sp3 = Html.fromHtml(list3.get(childPosition).getContent());
				mywebtext.setText(sp3);
				contentTitle.setText(list3.get(childPosition).getTitle());
				break;
			case 3:
				Spanned sp4 = Html.fromHtml(list4.get(childPosition).getContent());
				mywebtext.setText(sp4);
				contentTitle.setText(list4.get(childPosition).getTitle());
				break;

			default:
				break;
			}
        
	        mywebtext.setKeyListener(TextKeyListener.getInstance());
			mywebtext.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
			mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		    mToast = Toast.makeText(WebShow.this, String.format(getString(R.string.tts_toast_format), 0, 0), Toast.LENGTH_LONG);
			ttsDialog = new SynthesizerDialog(WebShow.this, "appid=" + getString(R.string.app_id));
			synthetizeInSilence();		
	 }

	 
	 /**
		 * SynthesizerPlayerListener锟斤拷"停止锟斤拷锟斤拷"锟截碉拷锟接匡拷.
		 * 
		 * @param
		 */
		@Override
		protected void onStop() {
			mToast.cancel();
			if (null != mSynthesizerPlayer) {
				mSynthesizerPlayer.cancel();
			}

			super.onStop();
		}

		/**
		 * 锟斤拷钮锟斤拷锟斤拷录锟��.
		 * 
		 * @param v
		 */
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.MyWebViewSpeak:
				boolean show = mSharedPreferences.getBoolean(getString(R.string.preference_key_tts_show), true);
				if (show) {
					showSynDialog();
				} else {
					synthetizeInSilence1();
				}
				break;
			case R.id.MyWebViewConfig://设置语音，待扩展，还未定义此按钮
				startActivity(new Intent(this, TtsPreferenceActiviity.class));
				break;
			case R.id.MyWebViewReturn:
				finish();
				break;
			default:
				break;
			}
		}

		private void synthetizeInSilence() {
			mSynthesizerPlayer = SynthesizerPlayer.createSynthesizerPlayer(this, "appid=" + getString(R.string.app_id));
			String role = mSharedPreferences.getString(getString(R.string.preference_key_tts_role),
					getString(R.string.preference_default_tts_role));
			mSynthesizerPlayer.setVoiceName(role);
			int speed = mSharedPreferences.getInt(getString(R.string.preference_key_tts_speed), 50);
			mSynthesizerPlayer.setSpeed(speed);
			int volume = mSharedPreferences.getInt(getString(R.string.preference_key_tts_volume), 50);
			mSynthesizerPlayer.setVolume(volume);
			String music = mSharedPreferences.getString(getString(R.string.preference_key_tts_music),
					getString(R.string.preference_default_tts_music));
			mSynthesizerPlayer.setBackgroundSound(music);
		}
		public static void play(String text){
			mSynthesizerPlayer.playText(text, null, new SynthesizerPlayerListener() {
				
				@Override
				public void onPlayResumed() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPlayPercent(int arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPlayPaused() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPlayBegin() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onEnd(SpeechError arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onBufferPercent(int arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
			} );
		}

		public void synthetizeInSilence1() {
			if (null == mSynthesizerPlayer) {
				mSynthesizerPlayer = SynthesizerPlayer.createSynthesizerPlayer(this, "appid=" + getString(R.string.app_id));
			}

			String role = mSharedPreferences.getString(getString(R.string.preference_key_tts_role),
					getString(R.string.preference_default_tts_role));
			mSynthesizerPlayer.setVoiceName(role);

			int speed = mSharedPreferences.getInt(getString(R.string.preference_key_tts_speed), 50);
			mSynthesizerPlayer.setSpeed(speed);
			int volume = mSharedPreferences.getInt(getString(R.string.preference_key_tts_volume), 50);
			mSynthesizerPlayer.setVolume(volume);

			String music = mSharedPreferences.getString(getString(R.string.preference_key_tts_music),
					getString(R.string.preference_default_tts_music));
			mSynthesizerPlayer.setBackgroundSound(music);

			String editable = mywebtext.getText().toString();
			String source = null;
			if (null != editable) {
				source = editable.toString();
			}

		    mSynthesizerPlayer.playText(source, null,this);
			mToast.setText(String.format(getString(R.string.tts_toast_format), 0, 0));
			mToast.show();
		}

		public void showSynDialog() {
			String editable=mywebtext.getText().toString();
			String source = null;
			if (null != editable) {
				source = editable.toString();
			}
			ttsDialog.setText(source, null);

			String role = mSharedPreferences.getString(getString(R.string.preference_key_tts_role),
					getString(R.string.preference_default_tts_role));
			ttsDialog.setVoiceName(role);

			int speed = mSharedPreferences.getInt(getString(R.string.preference_key_tts_speed), 50);
			ttsDialog.setSpeed(speed);

			int volume = mSharedPreferences.getInt(getString(R.string.preference_key_tts_volume), 50);
			ttsDialog.setVolume(volume);

			String music = mSharedPreferences.getString(getString(R.string.preference_key_tts_music),
					getString(R.string.preference_default_tts_music));
			ttsDialog.setBackgroundSound(music);
			
			ttsDialog.setTitle("交通信息语音播报");
			ttsDialog.show();
		}

		@Override
		public void onBufferPercent(int percent, int beginPos, int endPos) {
			mPercentForBuffering = percent;
			mToast.setText(String.format(getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
			mToast.show();
		}

		@Override
		public void onPlayBegin() {
		}

		@Override
		public void onPlayPaused() {
		}

		@Override
		public void onPlayPercent(int percent, int beginPos, int endPos) {
			mPercentForPlaying = percent;
			mToast.setText(String.format(getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
			mToast.show();
		}

		@Override
		public void onPlayResumed() {
		}


		@Override
		public void onEnd(SpeechError arg0) {
			// TODO Auto-generated method stub

		}
}
