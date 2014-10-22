package com.example.multiball;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class GameLoopThread extends Thread
{

	static final long FPS = 20;
	private GameView view;
	private boolean running = false;

	public GameLoopThread(GameView view)
	{
		this.view = view;
	}

	public void setRunning(boolean run)
	{
		running = run;
	}

	@SuppressLint("WrongCall")
	@Override
	public void run()
	{
		long ticksPS = 1000 /FPS;
		long starttime;
		long sleeptime;
		
		while (running)
		{
			Canvas c = null;
			starttime = System.currentTimeMillis();
			try
			{
				c = view.getHolder().lockCanvas();
				synchronized (view.getHolder())
				{
					view.onDraw(c);
				}
			} finally
			{
				if (c != null)
				{
					view.getHolder().unlockCanvasAndPost(c);
				}
			}
			sleeptime = ticksPS - (System.currentTimeMillis()-starttime);
			try
			{
				if(sleeptime>0)
				{
					sleep(sleeptime);
				}
				else
				{
					sleep(10);
				}
			}
			catch(Exception e){}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		}
	}

}
