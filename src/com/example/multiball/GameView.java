package com.example.multiball;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GameView extends SurfaceView
{

	public List<Tempball> temps;
	private String TAG = "In GameView";
	private Bitmap bmp;
	private Context context;
	private SurfaceHolder holder;
	private GameLoopThread gameloopthread;
	private Ball ball;
	public Bitmap collision;
	public HashMap<Integer, Ball> balls ;
	private long lastClick;
	public HashMap<Integer, Point> ballpositions;
	public boolean ballscreated;
	private Display display;

	public GameView(Context context)
	{
		super(context);
		this.context = context;
		gameloopthread = new GameLoopThread(this);
		balls = new HashMap<Integer,Ball>();
		ballpositions = new HashMap<Integer, Point>();
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback()
		{

			@Override
			public void surfaceDestroyed(SurfaceHolder holder)
			{
				boolean retry = true;
				gameloopthread.setRunning(false);
				while (retry)
				{
					try
					{
						gameloopthread.join();
						retry = false;
					} catch (InterruptedException e)
					{
					}
				}
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder)
			{
				createBalls();
				gameloopthread.setRunning(true);
				gameloopthread.start();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
			{

			}
		});
		WindowManager wm = (WindowManager) (context.getSystemService("window"));
		display = wm.getDefaultDisplay();
		
		Drawable d = getResources().getDrawable(R.drawable.circle1);
		collision = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		d.draw(new Canvas(collision));
		temps = new ArrayList<Tempball>();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (System.currentTimeMillis() - lastClick > 500)
		{
			Integer i = null;
			lastClick = System.currentTimeMillis();
			synchronized (getHolder())
			{
				for (Map.Entry<Integer, Ball> b : balls.entrySet())
				{
					Ball ball = b.getValue();
					if (ball.isCollision(event.getX(), event.getY()))
					{
						i = b.getKey();
						break;
					}
				}
				if (i != null)
				{
					Ball b = balls.get(i);
					Bitmap bmp = b.getBmp();
					bmp.recycle();
					bmp = null;
					balls.remove(i);
					ballpositions.remove(i);
				}
			}
		}
		return true;
	}

	private void createBalls()
	{
		
		if(display.getWidth()>900)
		{
		Ball b;
		b = createBall(R.drawable.circle1_ll);
		balls.put(R.drawable.circle1_ll, (b));
		b = createBall(R.drawable.circle2_ll);
		balls.put(R.drawable.circle2_ll, (b));
		b = createBall(R.drawable.circle3_ll);
		balls.put(R.drawable.circle3_ll, (b));
		b = createBall(R.drawable.circle4_ll);
		balls.put(R.drawable.circle4_ll, (b));
		b = createBall(R.drawable.circle5_ll);
		balls.put(R.drawable.circle5_ll, (b));
		
		ballscreated = true;
		}
		else
		{
			Ball b;
			b = createBall(R.drawable.circle1_llsm);
			balls.put(R.drawable.circle1_llsm, (b));
			b = createBall(R.drawable.circle2_llsm);
			balls.put(R.drawable.circle2_llsm, (b));
			b = createBall(R.drawable.circle3_llsm);
			balls.put(R.drawable.circle3_llsm, (b));
			b = createBall(R.drawable.circle4_llsm);
			balls.put(R.drawable.circle4_llsm, (b));
			//b = createBall(R.drawable.circle5_llsm);
			//balls.put(R.drawable.circle5_llsm, (b));
			//b = createBall(R.drawable.circle6_llsm);
			//balls.put(R.drawable.circle6_llsm, (b));
			//b = createBall(R.drawable.circle7_llsm);
			//balls.put(R.drawable.circle7_llsm, (b));
			ballscreated = true;
	
		}
		
		
	}

	private Ball createBall(int resid)
	{
		Drawable d = getResources().getDrawable(resid);
		Bitmap bmp = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		d.draw(new Canvas(bmp));
		return new Ball(display, this, bmp, resid);
	}

	@SuppressLint("WrongCall")
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.drawColor(Color.BLACK);

		for (Map.Entry<Integer, Ball> b : balls.entrySet())
		{
			b.getValue().onDraw(canvas);
		}
		for (int i = temps.size() - 1; i >= 0; i--)
		{
			temps.get(i).onDraw(canvas);
		}

	}

}
