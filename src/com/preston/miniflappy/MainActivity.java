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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		int score[]=getScore();
		//View game_message=(View)findViewById(R.id.game_message);
		Button button_play=(Button)findViewById(R.id.play);
		//TextView level_message=(TextView)findViewById(R.id.level_message);
		//level_message.setText("BEST:"+score[0]+"\nLAST:"+score[1]);
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
	}
	
	private int[] getScore()
	{
		SharedPreferences preferences=getSharedPreferences("score",MODE_WORLD_READABLE);
		int best=preferences.getInt("best", 0);
		int last=preferences.getInt("last", 0);
		return new int[]{best,last};
	}
	
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event) 
	{
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
