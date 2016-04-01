package com.preston.miniflappy;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.renderscript.Sampler.Value;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class FlappySurfaceView extends SurfaceView implements Callback,Runnable 
{

	private SurfaceHolder sfh;
	//画笔
	private Paint paint;
	//线程
	private Thread thread;
	//游戏运行标志
	private boolean flag;
	//画布
	private Canvas canvas;
	//设备屏幕的宽度和高度
	public static int screenW,screenH;
	//定义游戏状态
	private final static int GAME_MENU=0;//游戏准备
	private final static int GAMEING=1;//游戏中
	private final static int GAME_OVER=-1;//游戏结束	
	//游戏初始状态
	private static int gameState=GAME_MENU;
	//地板（即下界）的坐标
	private int floor[]=new int[2];
	//绘制地板的间隔
	private int floor_width=15;
	//游戏速度
	private int speed=3;
	//显示当前成绩控件的坐标
	private int level[]=new int[2];
	//初始成绩为0
	private int level_value=0;
	//bird的坐标
	private int bird[]=new int[2];
	//bird的宽度(由于是个圆，所以高度跟宽度一样)
	private int bird_width=20;
	//???
	private int bird_v=0;
	private int bird_a=2;
	private int bird_vUp=-16;
	//需要绘制的水管坐标
	private ArrayList<int[]> walls=new ArrayList<int[]>();
	//超出屏幕不需绘制的水管坐标
	private ArrayList<int[]> remove_walls=new ArrayList<int[]>();
	//水管的宽度和高度
	private int wall_width=50;
	private int wall_height=115;
	//水管的间隔
	private int wall_step=30;
	
	public final static int L_SCORE_STEP=15;
	private final static int S_SCORE_STEP=10;
	
	Bitmap bg,floor_img;
	Bitmap bird_middle,bird_up,bird_down;
	Bitmap pipe_down,pipe_up;
	Bitmap scoreNumber1[]=new Bitmap[10];
	Bitmap scoreNumber2[]=new Bitmap[10];
	Bitmap medals[]=new Bitmap[4];
	Bitmap text_ready,tutorial,text_game_over,score_panel,button_play,button_score;
	
	int bitmap_resource[]={R.drawable.zero,R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,
			R.drawable.five,R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine};
	int score_resource[]={R.drawable.number_score_00,R.drawable.number_score_01,R.drawable.number_score_02,R.drawable.number_score_03
			,R.drawable.number_score_04,R.drawable.number_score_05,R.drawable.number_score_06,R.drawable.number_score_07
			,R.drawable.number_score_08,R.drawable.number_score_09};
	int medal_resource[]={R.drawable.medals_0,R.drawable.medals_1,R.drawable.medals_2,R.drawable.medals_3};
	
	private Rect bg_area,floor_area1,floor_area2,bird_area,pipe_up_area,pipe_down_area;
	int right;
	
	private ArrayList<Integer> score=new ArrayList<Integer>();
	
	public FlappySurfaceView(Context context)
	{
		super(context);
		sfh=this.getHolder();
		setZOrderOnTop(true);
		sfh.setFormat(PixelFormat.TRANSLUCENT);
		sfh.addCallback(this);
		paint=new Paint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setTextSize(50);
		paint.setStyle(Style.STROKE);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setKeepScreenOn(true);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder)
	{
		screenW=this.getWidth();
		right=0;
		screenH=this.getHeight();
		bg_area=new Rect(0,0,screenW,screenH-dp2px(150));
		screenH=screenH-dp2px(150);
		bg=BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		floor_img=BitmapFactory.decodeResource(getResources(), R.drawable.floor);
		bird_middle=BitmapFactory.decodeResource(getResources(), R.drawable.bird_middle);
		bird_up=BitmapFactory.decodeResource(getResources(), R.drawable.bird_up);
		bird_down=BitmapFactory.decodeResource(getResources(), R.drawable.bird_down);
		pipe_down=BitmapFactory.decodeResource(getResources(), R.drawable.pipe_down);
		pipe_up=BitmapFactory.decodeResource(getResources(), R.drawable.pipe_up);
		text_ready=BitmapFactory.decodeResource(getResources(), R.drawable.text_ready);
		tutorial=BitmapFactory.decodeResource(getResources(), R.drawable.tutorial);
		text_game_over=BitmapFactory.decodeResource(getResources(), R.drawable.text_game_over);
		score_panel=BitmapFactory.decodeResource(getResources(), R.drawable.score_panel);
		button_play=BitmapFactory.decodeResource(getResources(), R.drawable.button_play);	
		button_score=BitmapFactory.decodeResource(getResources(), R.drawable.button_score);
		for(int i=0;i<10;i++)
		{
			scoreNumber1[i]=BitmapFactory.decodeResource(getResources(), bitmap_resource[i]);
			scoreNumber2[i]=BitmapFactory.decodeResource(getResources(), score_resource[i]);
		}
		for(int i=0;i<4;i++)
		{
			medals[i]=BitmapFactory.decodeResource(getResources(), medal_resource[i]);
		}
		initGame();
		flag=true;
		thread=new Thread(this);
		thread.start();
	}
	
	//游戏初始化
	private void initGame()
	{
		if(gameState==GAME_MENU)
		{
			//初始化地板（下界）的坐标
			floor[0]=0;
			//floor[1]=screenH-screenH/5;
			floor[1]=screenH;
			//初始化显示当前成绩的坐标
			level[0]=screenW/2;
			level[1]=screenH/5;
			//初始化成绩
			level_value=0;
			//初始化bird的坐标
			bird[0]=screenW/3;
			bird[1]=screenH/3;
			//清除上轮游戏留下的水管信息
			if(null!=walls) walls.clear();
			//将各个数据从dp换算成px
			floor_width=dp2px(15);
			speed=dp2px(3);
			bird_width=dp2px(10);
			bird_a=dp2px(2);
			bird_vUp=-dp2px(16);
			wall_width=dp2px(45);
			wall_height=dp2px(100);
			wall_step=wall_width*3;	
		}
	}
	//将dp换算成px
	private int dp2px(float dp)
	{
		int px=Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
		return px;
	}
	
	@Override
	public void run()
	{
		while(flag)
		{
			long start=System.currentTimeMillis();
			myDraw();
			logic();
			long end=System.currentTimeMillis();
			try
			{
				if(end-start<50)
				{
					thread.sleep(50-(end-start));
				}
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void myDraw()
	{
		try
		{
			canvas=sfh.lockCanvas();
			if(canvas!=null)
			{
				right=right-speed;
				if(right<=0) right=screenW/2;			
				canvas.drawBitmap(bg, null, bg_area, null);				
				if(gameState==GAMEING)
				{				
					canvas.drawBitmap(floor_img,right,screenH,null);
					canvas.drawBitmap(floor_img,right-screenW/2,screenH,null);
				}
				else if(gameState==GAME_MENU)
				{
					Rect text_area=new Rect(dp2px(80),dp2px(50),screenW-dp2px(80),dp2px(100));
					Rect tutorial_area=new Rect(screenW/2-dp2px(100),screenH/2,screenW/2+dp2px(100),screenH/2+dp2px(200));
					canvas.drawBitmap(text_ready,null,text_area,null);
					canvas.drawBitmap(tutorial,null,tutorial_area,null);
					canvas.drawBitmap(floor_img,screenW/2,screenH,null);
					canvas.drawBitmap(floor_img,0,screenH,null);
				}
				else 
				{
					canvas.drawBitmap(floor_img,screenW/2,screenH,null);
					canvas.drawBitmap(floor_img,0,screenH,null);			
				}
				//绘制水管
				for(int i=0;i<walls.size();i++)
				{
					int wall[]=walls.get(i);
					pipe_down_area=new Rect(wall[0],0,wall[0]+wall_width,wall[1]);
					pipe_up_area=new Rect(wall[0],wall[1]+wall_height,wall[0]+wall_width,screenH);
					canvas.drawBitmap(pipe_down, null, pipe_down_area, null);
					canvas.drawBitmap(pipe_up,null,pipe_up_area,null);					
				}
				bird_area=new Rect(bird[0]-bird_width,bird[1]-bird_width,bird[0]+bird_width,bird[1]+bird_width);
				int b=right%128;
				if(b>=0 && b<32)
				{
					canvas.drawBitmap(bird_middle, null, bird_area, null);
				}
				else if(b>=32 && b<64)
				{
					canvas.drawBitmap(bird_up, null, bird_area, null);
				}
				else if(b>=64 && b<96)
				{
					canvas.drawBitmap(bird_middle, null, bird_area, null);
				}
				else if(b>=96 && b<128)
				{
					canvas.drawBitmap(bird_down, null, bird_area, null);
				}
				drawText(level_value, canvas,scoreNumber1,level[0],level[1],L_SCORE_STEP);
				if(gameState==GAME_OVER)
				{				
					int medal=MiniFlappyView.context.saveScore(level_value);
					Rect text_area=new Rect(dp2px(80),dp2px(50),screenW-dp2px(80),dp2px(100));
					Rect panel_area=new Rect(dp2px(10),dp2px(100),screenW-dp2px(10),dp2px(280));
					Rect button_play_area=new Rect(dp2px(50),dp2px(300),dp2px(150),dp2px(350));
					Rect button_score_area=new Rect(dp2px(210),dp2px(300),dp2px(310),dp2px(350));
					canvas.drawBitmap(text_game_over,null,text_area,null);
					canvas.drawBitmap(drawImageOnImage(MainActivity.context.getBestScore(), level_value,medals[medal]), null, panel_area,null);
					canvas.drawBitmap(button_play,null,button_play_area,null);
					canvas.drawBitmap(button_score,null,button_score_area,null);
				}
			}
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			if(canvas!=null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}
	
	private int move_step=0;
	public void logic()
	{
		switch(gameState)
		{
		case GAME_MENU:
			break;
		case GAMEING:
			bird_v+=bird_a;
			bird[1]+=bird_v;
			if(bird[1]>floor[1]-bird_width)
			{
				bird[1]=floor[1]-bird_width;
				gameState=GAME_OVER;
			}
			if(floor[0]<-floor_width)
			{
				floor[0]+=floor_width*2;
			}
			floor[0]-=speed;
			remove_walls.clear();
			for(int i=0;i<walls.size();i++)
			{
				int wall[]=walls.get(i);
				wall[0]-=speed;
				if(wall[0]<-wall_width)
				{
					remove_walls.add(wall);
				}
				else if(bird[0]+bird_width>=wall[0] && bird[0]-bird_width<=wall[0]+wall_width &&
						(bird[1]<=wall[1]+bird_width || bird[1]+bird_width>=wall[1]+wall_height))
				{
					gameState=GAME_OVER;
				}
				int pass=wall[0]+wall_width+bird_width-bird[0];
				//避过一根水管加1，且只加一次
				if(pass<0 && -pass<=speed)
				{
					level_value++;
				}
			}
			//超出屏幕的水管不再绘制
			if(remove_walls.size()>0)
			{
				walls.removeAll(remove_walls);
			}
			move_step+=speed;
			if(move_step>wall_step)
			{
				int wall[]=new int[]{screenW,(int)(Math.random()*(floor[1]-2*wall_height)+0.5*wall_height)};
				walls.add(wall);
				move_step=0;
			}
			break;
		case GAME_OVER:
			if(bird[1]+bird_width<floor[1])
			{
				bird_v+=bird_a;
				bird[1]+=bird_v;
				if(bird[1]+bird_width>floor[1])
				{
					bird[1]=floor[1]-bird_width;
				}
				else
				{
					/*Rect text_area=new Rect(dp2px(80),dp2px(50),dp2px(280),dp2px(100));
					Rect panel_area=new Rect(dp2px(50),dp2px(100),dp2px(190),dp2px(300));
					Rect button_area=new Rect(dp2px(70),dp2px(350),dp2px(170),dp2px(400));
					canvas.drawBitmap(text_game_over,null,text_area,null);
					canvas.drawBitmap(score_panel,null,panel_area,null);
					canvas.drawBitmap(button_play,null,button_area,null);*/
					//MiniFlappyView.context.showMessage(level_value);
					//gameState=GAME_MENU;
					//initGame();
				}
			}
			break;
		}
	}
	
	public void getScoreNumber(int level_value)
	{
		if(score!=null) score.clear();
		if(level_value>=0 && level_value<=9)
		{
			score.add(level_value);
			return;
		}
		int r=level_value,m;
		while(r!=0)
		{
			m=r%10;
			r=r/10;
			score.add(m);
		}
	}
	
	public void drawText(int level_value,Canvas canvas,Bitmap scoreNumber[],int locationX,int locationY,int score_step)
	{
		getScoreNumber(level_value);
		int position=dp2px(score_step)*score.size()/2;
		for(int i=0;i<score.size();i++)
		{
			canvas.drawBitmap(scoreNumber[score.get(i)], locationX+position,locationY, null);
			position-=dp2px(score_step);
		}
	}
	
	public Bitmap drawImageOnImage(int best_level,int score_level,Bitmap medal)
	{
		Bitmap newBitmap=score_panel;
		if(!newBitmap.isMutable())
		{
			newBitmap=score_panel.copy(Bitmap.Config.ARGB_8888, true);
		}
		Canvas canvas2=new Canvas(newBitmap);
		Rect medal_area=new Rect(dp2px(20),dp2px(30),dp2px(50),dp2px(60));
		canvas2.drawBitmap(medal, null, medal_area, null);
		drawText(score_level,canvas2,scoreNumber2,dp2px(120),dp2px(25),S_SCORE_STEP);
		drawText(best_level,canvas2,scoreNumber2,dp2px(120),dp2px(50),S_SCORE_STEP);
		canvas2.save(Canvas.ALL_SAVE_FLAG);
		canvas2.restore();
		return newBitmap;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			switch(gameState)
			{
			case GAME_MENU:
				gameState=GAMEING;
				break;
			case GAMEING:
				bird_v=bird_vUp;
				break;
			case GAME_OVER:
				if(bird[1]+bird_width>=floor[1])
				{
					float x=event.getX();
					float y=event.getY();
					if(x>=dp2px(50) && x<=dp2px(150) && y>=dp2px(300) && y<=dp2px(350))
					{
						MiniFlappyView.context.showMessage(level_value);
						gameState=GAME_MENU;
						initGame();
					}
					else if(x>=dp2px(210) && x<=dp2px(310) && y>=dp2px(300) && y<=dp2px(350))
					{
						MiniFlappyView.context.showScore();
					}
				}
				break;
			}
		}
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			MiniFlappyView.context.finish();
			System.exit(0);
			return true;
		}
		return super.onKeyDown(keyCode,event);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder,int format,int width,int height)
	{
		
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder)
	{
		flag=false;
	}
}
