/**
 * 
 */
package com.preston.miniflappy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class ScoreView extends Activity 
{
	//设备屏幕的高度和宽度
	public static int screenW,screenH;
	//存放成绩各位数上的数值
	ArrayList<Integer> score=new ArrayList<Integer>();
	Bitmap info_panel,copyright;
	Bitmap scoreNumber[]=new Bitmap[10];
	int bitmap_resource[]={R.drawable.zero,R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,
			R.drawable.five,R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine};
	int gameInfo[];
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//全屏显示
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//无标题显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.score_view);
		//获取设备屏幕的高度和宽度
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenW=dm.widthPixels;
		screenH=dm.heightPixels;
		LinearLayout infoView=(LinearLayout)findViewById(R.id.infoView);
		gameInfo=getGameInfo();
		info_panel=BitmapFactory.decodeResource(getResources(), R.drawable.game_info);
		copyright=BitmapFactory.decodeResource(getResources(), R.drawable.copyright);
		for(int i=0;i<10;i++)
		{
			scoreNumber[i]=BitmapFactory.decodeResource(getResources(), bitmap_resource[i]);
		}
		infoView.addView(new GameInfoView(this));
	}
	
	class GameInfoView extends View
	{      
                                                                                                                                  
        public GameInfoView(Context context) 
        {   
            super(context);   
/*            Paint paint=new Paint();
    		paint.setColor(Color.BLACK);
    		paint.setAntiAlias(true);
    		paint.setTextSize(50);
    		paint.setStyle(Style.STROKE);  */ 
        } 
        
        @Override   
        protected void onDraw(Canvas canvas) 
        {   	
        	//成绩信息的绘制区域
        	Rect panel_area=new Rect(dp2px(10),dp2px(180),screenW-dp2px(10),screenH-dp2px(180));
        	canvas.drawBitmap(info_panel, null, panel_area, null);
    		int position=dp2px(140);
    		for(int i=0;i<4;i++)
    		{
    			if(i==3) position+=dp2px(40);
    			else position+=dp2px(70);
    			drawText(gameInfo[i],canvas,screenW/2,position,FlappySurfaceView.L_SCORE_STEP);
    		}
    		canvas.drawBitmap(copyright, screenW/2, screenH-dp2px(50), null);
        }   
        
        private ArrayList<Integer> getScoreNumber(int level_value)
    	{
    		if(score!=null) score.clear();
    		if(level_value>=0 && level_value<=9)
    		{
    			score.add(level_value);
    			return score;
    		}
    		int r=level_value,m;
    		while(r!=0)
    		{
    			m=r%10;
    			r=r/10;
    			score.add(m);
    		}
    		return score;
    	}
    	
    	//将dp换算成px
    	private int dp2px(float dp)
    	{
    		int px=Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    		return px;
    	}
    	
    	public void drawText(int level_value,Canvas canvas,int locationX,int locationY,int score_step)
    	{
    		getScoreNumber(level_value);
    		int position=dp2px(score_step)*score.size()/2;
    		for(int i=0;i<score.size();i++)
    		{
    			canvas.drawBitmap(scoreNumber[score.get(i)], locationX+position,locationY, null);
    			position-=dp2px(score_step);
    		}
    	}
                                                                                                                                  
    }
	//获取成绩和游戏信息
	private int[] getGameInfo()
	{
		int gameInfo[]=new int[4];
		SharedPreferences preferences1=getSharedPreferences("score", MODE_WORLD_READABLE);
		gameInfo[0]=preferences1.getInt("best", 0);
		gameInfo[1]=preferences1.getInt("second", 0);
		gameInfo[2]=preferences1.getInt("third", 0);
		SharedPreferences preferences2=getSharedPreferences("gameTime",MODE_WORLD_READABLE);
		gameInfo[3]=preferences2.getInt("game_time", 0);
		return gameInfo;
	}
	
	
	
}
