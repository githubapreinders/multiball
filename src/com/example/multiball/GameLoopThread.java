package com.example.multiball;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class GameLoopThread extends Thread
{

//	static final long FPS = 30;
	private GameView view;
	private boolean running = false;
	private static final int UPDATE_RATE = 100;
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
		
		while (running)
		{
			Canvas c = null;
			long beginTimeMillis, timeTakenMillis, timeLeftMillis;
            beginTimeMillis = System.currentTimeMillis();

			try
			{
				view.updateBalls2();
				c = view.getHolder().lockCanvas();
				synchronized (view.getHolder())
				{
					view.onDraw(c);
				}
				timeTakenMillis = System.currentTimeMillis() - beginTimeMillis;
	               timeLeftMillis = 1000L / UPDATE_RATE - timeTakenMillis;
	               if (timeLeftMillis < 5) timeLeftMillis = 5; 

			} finally
			{
				if (c != null)
				{
					view.getHolder().unlockCanvasAndPost(c);
				}
			}
			try
			{
				Thread.sleep(timeLeftMillis);
			}
			catch(Exception e){}
		}
	}

}
