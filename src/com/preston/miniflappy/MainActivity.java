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
		//全屏显示
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//无标题显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		//获取开始游戏和查看分数的两个按钮
		Button button_play=(Button)findViewById(R.id.play);
		Button button_score=(Button)findViewById(R.id.score);
		//开始游戏按钮的点击事件
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
		//查看成绩信息按钮的点击事件
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
	//获取游戏最高分
	public int getBestScore()
	{
		SharedPreferences preferences=getSharedPreferences("score",MODE_WORLD_READABLE);
		int best=preferences.getInt("best", 0);
		return best;
	}
	//有按键被按下的处理事件
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event) 
	{
		//当按下返回键时退出游戏
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
