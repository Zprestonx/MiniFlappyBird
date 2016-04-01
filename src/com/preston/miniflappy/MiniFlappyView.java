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
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mini_flappy_view);
		gameView=(LinearLayout)findViewById(R.id.gameView);
		//gameView.setBackground(getResources().getDrawable(R.drawable.bg));
		gameView.addView(new FlappySurfaceView(this));
	}
	
	public void showMessage(int level)
	{
		//saveScore(level);
		SharedPreferences preferences=getSharedPreferences("gameTime",MODE_WORLD_READABLE);
		int game_time=preferences.getInt("game_time", 0);
		game_time++;
		preferences.edit().putInt("game_time", game_time).commit();
		Intent intent=new Intent(MiniFlappyView.this,MiniFlappyView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	public void showScore()
	{
		Intent intent=new Intent(MiniFlappyView.this,ScoreView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	public int saveScore(int level)
	{
		
		SharedPreferences preferences=getSharedPreferences("score",MODE_WORLD_READABLE);
		int best=preferences.getInt("best", 0);
		int second=preferences.getInt("second", 0);
		int third=preferences.getInt("third", 0);
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
