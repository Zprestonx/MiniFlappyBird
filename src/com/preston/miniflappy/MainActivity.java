package com.preston.miniflappy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity 
{
	
	public static MainActivity context;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		context=this;
		//ȫ����ʾ
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//�ޱ�����ʾ
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		//��ȡ��ʼ��Ϸ�Ͳ鿴������������ť
		Button button_play=(Button)findViewById(R.id.play);
		Button button_score=(Button)findViewById(R.id.score);
		//��ʼ��Ϸ��ť�ĵ���¼�
		button_play.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent=new Intent(MainActivity.this,MiniFlappyView.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		});
		//�鿴�ɼ���Ϣ��ť�ĵ���¼�
		button_score.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent(MainActivity.this,ScoreView.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
	//��ȡ��Ϸ��߷�
	public int getBestScore()
	{
		SharedPreferences preferences=getSharedPreferences("score",MODE_WORLD_READABLE);
		int best=preferences.getInt("best", 0);
		return best;
	}
	//�а��������µĴ����¼�
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event) 
	{
		//�����·��ؼ�ʱ�˳���Ϸ
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			try
			{
				MiniFlappyView.context.finish();
			}
			catch(Exception e)
			{
				
			}
			finish();
			System.exit(0);
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
