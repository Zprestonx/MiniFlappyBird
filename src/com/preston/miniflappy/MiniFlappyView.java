package com.preston.miniflappy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
		saveScore(level);
		Intent intent=new Intent(MiniFlappyView.this,MiniFlappyView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	private void saveScore(int level)
	{
		SharedPreferences preferences=getSharedPreferences("score",MODE_WORLD_READABLE);
		preferences.edit().putInt("last", level).commit();
		int lastTop=preferences.getInt("best", 0);
		if(level>lastTop) preferences.edit().putInt("best", level).commit();
	}
	
}
