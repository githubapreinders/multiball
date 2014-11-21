package com.example.multiball;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class GameLoopThread extends Thread
{

	// static final long FPS = 30;
	private String TAG = "GameLoopThread";
	private GameView view;
	private boolean running = false;
	private static final int UPDATE_RATE = 500;
	private TextToSpeech tts;
	private Object mPauseLock;
	private boolean mPaused;
	public boolean answergiven = true;
	public Opgave opgave;
	public int SPEECHSPEED = 4000;
	private long lasttimespeech;

	public GameLoopThread(GameView view)
	{
		this.view = view;
		this.mPauseLock = new Object();
		this.mPaused = false;
		this.tts = ((MultiBall) view.main.getApplication()).getSpeechengine();
		this.lasttimespeech = System.currentTimeMillis();

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
			synchronized (mPauseLock)
			{
				while (mPaused)
				{
					try
					{
						mPauseLock.wait();
					} catch (InterruptedException e)
					{
					}
				}
			}
			Log.v(TAG, "Verschil" +String.valueOf (System.currentTimeMillis() - lasttimespeech));
			Canvas c = null;
			long beginTimeMillis, timeTakenMillis, timeLeftMillis;
			beginTimeMillis = System.currentTimeMillis();
			try
			{
				if (System.currentTimeMillis() - lasttimespeech > 3000)
				{

					if (answergiven)
					{
						opgave = new Opgave(1, view.main);
						answergiven = false;
					}
					else
					{
						view.main.handler.post(new Runnable()
						{
							
							@Override
							public void run()
							{
								view.main.ttobj.speak(opgave.getOpgavetekst(), TextToSpeech.QUEUE_FLUSH, null);
							}
						});
						lasttimespeech =System.currentTimeMillis();
					}
				}

				view.updateBalls2();
				c = view.getHolder().lockCanvas();
				synchronized (view.getHolder())
				{
					view.onDraw(c);
				}
				timeTakenMillis = System.currentTimeMillis() - beginTimeMillis;
				timeLeftMillis = 1000L / UPDATE_RATE - timeTakenMillis;
				if (timeLeftMillis < 5)
					timeLeftMillis = 5;

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
			} catch (Exception e)
			{

			}
		}
	}

	public void onPause()
	{
		synchronized (mPauseLock)
		{
			mPaused = true;
		}
	}

	public void onResume()
	{
		synchronized (mPauseLock)
		{
			mPaused = false;
			mPauseLock.notifyAll();
			view.setRunconfiguration();

		}
	}

	public boolean ismPaused()
	{
		return mPaused;
	}

	public void setAnswergiven(boolean answergiven)
	{
		this.answergiven = answergiven;
	}

	public int getSPEECHSPEED()
	{
		return SPEECHSPEED;
	}

	public void setSPEECHSPEED(int sPEECHSPEED)
	{
		SPEECHSPEED = sPEECHSPEED;
	}

	public Opgave getOpgave()
	{
		return opgave;
	}

}
