package com.preston.miniflappy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MiniFlappyView extends Activity
{
	
	public static MiniFlappyView context;
	LinearLayout gameView;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		context=this;
		//全屏显示
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//无标题显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mini_flappy_view);
		//获取布局容器
		gameView=(LinearLayout)findViewById(R.id.gameView);
		//添加SurfaceView组件
		gameView.addView(new FlappySurfaceView(this));
	}
	//开始新游戏
	public void playAgain()
	{
		//游戏次数加1
		SharedPreferences preferences=getSharedPreferences("gameTime",MODE_WORLD_READABLE);
		int game_time=preferences.getInt("game_time", 0);
		game_time++;
		preferences.edit().putInt("game_time", game_time).commit();
		//加载MiniFlappyView开始新游戏
		Intent intent=new Intent(MiniFlappyView.this,MiniFlappyView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	//查看成绩信息
	public void showScore()
	{
		Intent intent=new Intent(MiniFlappyView.this,ScoreView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	//刷新成绩信息
	public int saveScore(int level)
	{
		
		SharedPreferences preferences=getSharedPreferences("score",MODE_WORLD_READABLE);
		//获取前三名分数
		int best=preferences.getInt("best", 0);
		int second=preferences.getInt("second", 0);
		int third=preferences.getInt("third", 0);
		//更新前三名的分数
		if(level>best) 
		{
			preferences.edit().putInt("best", level).commit();
			preferences.edit().putInt("second", best).commit();
			preferences.edit().putInt("third", second).commit();
			return 1;
		}
		else if(level==best) return 1;
		else if(level<best && level>second) 
		{
			preferences.edit().putInt("second", level).commit();
			preferences.edit().putInt("third", second).commit();
			return 2;
		}
		else if(level==second) return 2;
		else if(level<second && level>third) 
		{
			preferences.edit().putInt("third", level).commit();
			return 3;
		}
		else if(level==third) return 3;
		else return 0;
	}
	
}
