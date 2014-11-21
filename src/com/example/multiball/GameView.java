package com.example.multiball;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.speech.tts.TextToSpeech;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import aurelienribon.tweenengine.TweenManager;

public class GameView extends SurfaceView
{
	float currentBgX;
	long lastTimeBg;
	private TweenManager manager;
	private Bitmap bgimage;
	private Animation mAnim;
	private ImageView mImageView;
	private Paint p;
	private static final float EPSILON_TIME = 1e-2f;
	private String TAG = "In GameView";
	public List<Tempball> temps;
	private Bitmap bmp;
	private Context context;
	private SurfaceHolder holder;
	public GameLoopThread gameloopthread;
	public Bitmap collision;
	public HashMap<Integer, JBall> jballs;
	private long lastClick;
	public HashMap<Integer, Point> ballpositions;
	public boolean ballscreated;
	public Display display;
	private int screenwidth;
	private int screenheight;
	public MainActivity main;
	private String userinput;
	public ContainerBox box;
	private JBall jball;
	private JBall[] jballarray;
	private float volume = 0.5f;
	public int runconfiguration = 0;
	public static final int sound1 = R.raw.pop5;
	public static final int sound2 = R.raw.pop5a;
	public static final int sound3 = R.raw.pop5b;
	public static final int sound4 = R.raw.pop5c;
	public static final int sound5 = R.raw.pop5d;
	public static SoundPool soundpool;
	public static HashMap soundpoolmap;
	public Bitmap[] bganimation = new Bitmap[5];
	int bganimationcounter;
	int counter = 0;
	int flag = 0;
	private Drawable bgdrawable;
	private RunConfiguration rc;

	public GameView(Context context, MainActivity main)
	{
		super(context);
		this.context = context;
		this.main = main;
		this.userinput = "";
		this.box = null;
		this.jballarray = new JBall[10];
		this.userinput = ((MultiBall) main.getApplication()).getUserinput();

		gameloopthread = new GameLoopThread(this);
		((MultiBall) main.getApplication()).setGameloopthread(gameloopthread);
		gameloopthread.onPause();
		jballs = new HashMap<Integer, JBall>();
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

			@SuppressWarnings("deprecation")
			@Override
			public void surfaceCreated(SurfaceHolder holder)
			{
				currentBgX = screenwidth;
				lastTimeBg = System.currentTimeMillis();
				rc = new RunConfiguration(runconfiguration, GameView.this);
				jballarray = rc.getJballarray();
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
		this.screenwidth = display.getWidth();
		this.screenheight = display.getHeight();

		bgdrawable = getResources().getDrawable(R.drawable.background_animation4);
		bgimage = drawableToBitmap(getResources().getDrawable(R.drawable.background_animation3));

		Drawable d = getResources().getDrawable(R.drawable.circle1);
		collision = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		d.draw(new Canvas(collision));
		temps = new ArrayList<Tempball>();

		bganimation[0] = drawableToBitmap(main.getResources().getDrawable(R.drawable.background_animation1));
		bganimation[1] = drawableToBitmap(main.getResources().getDrawable(R.drawable.background_animation2));
		bganimation[2] = drawableToBitmap(main.getResources().getDrawable(R.drawable.background_animation3));
		bganimation[3] = drawableToBitmap(main.getResources().getDrawable(R.drawable.background_animation4));
		bganimation[4] = drawableToBitmap(main.getResources().getDrawable(R.drawable.background_animation5));
		p = new Paint();

		initSounds(context);

	}

	public void setRunconfiguration()
	{
		this.runconfiguration = ((MultiBall) main.getApplication()).getRunconfiguration();
		rc = new RunConfiguration(runconfiguration, GameView.this);
		jballarray = rc.getJballarray();

		try
		{
			main.image1.setEnabled(true);
			main.image2.setEnabled(true);
			main.image3.setEnabled(true);
			main.image4.setEnabled(true);
			main.img1.setEnabled(true);
		} catch (NullPointerException e)
		{

		}
	}

	public static void initSounds(Context context)
	{
		soundpool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
		soundpoolmap = new HashMap(4);
		soundpoolmap.put(sound1, soundpool.load(context, R.raw.pop1, 1));
		soundpoolmap.put(sound2, soundpool.load(context, R.raw.pop2, 2));
		soundpoolmap.put(sound3, soundpool.load(context, R.raw.pop3, 3));
		soundpoolmap.put(sound4, soundpool.load(context, R.raw.pop4, 4));
		soundpoolmap.put(sound5, soundpool.load(context, R.raw.pop4, 5));

	}

	public void setBox(ContainerBox box)
	{
		this.box = box;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (System.currentTimeMillis() - lastClick > 500)
		{
			jball = null;
			Integer i = null;
			lastClick = System.currentTimeMillis();
			synchronized (getHolder())
			{
				for (int k = 0; k < jballarray.length; k++)
				{
					jball = jballarray[k];
					if (jball.isCollision(event.getX(), event.getY()))
					{
						i = k;
						soundpool.play((int) soundpoolmap.get(sound4), volume, volume, 1, 0, 1f);
						break;
					}
				}
				if (i != null)
				{
					if (runconfiguration == 0)
					{
						jball = createJBall(jball.getBallid(), jball.getBallnumber());
						jballarray[i] = jball;
					}
					main.handler.post(new Runnable()
					{
						@Override
						public void run()
						{
							userinput = ((MultiBall) main.getApplication()).getUserinput();
							userinput += String.valueOf(jball.getBallnumber());
							((MultiBall) main.getApplication()).setUserinput(userinput);
							main.tv2.setTextSize(40);
							main.tv2.setTypeface(null, Typeface.BOLD);
							main.tv2.setGravity(Gravity.CENTER);
							main.tv2.setText(userinput);
							checkAnswer(Integer.parseInt(userinput));
						}
					});

				}
			}
		}
		return true;
	}

	private void checkAnswer(int input)
	{
		Opgave opg = gameloopthread.getOpgave();
		int answer = opg.getCorrectanswer();
		int numofdigits = (int) Math.log10(answer) + 1;

		if (numofdigits == 1)
		{
			gameloopthread.getOpgave().setUseranser(input);
			gameloopthread.setAnswergiven(true);
			if (Integer.parseInt((String) main.tv2.getText()) == answer)
			{
				soundpool.play((int) soundpoolmap.get(sound2), volume, volume, 1, 0, 1f);
			}
			((MultiBall) main.getApplication()).setUserinput("");
		}
		if (numofdigits == 2)
		{
			switch (flag)
			{
			case 0:
				if (Integer.parseInt((String) main.tv2.getText()) == answer)
				{
					gameloopthread.getOpgave().setUseranser(input);
					gameloopthread.setAnswergiven(true);
					soundpool.play((int) soundpoolmap.get(sound2), volume, volume, 1, 0, 1f);
					((MultiBall) main.getApplication()).setUserinput("");
					flag = 0;
					break;
				}
				flag = 1;
				break;
			case 1:
				gameloopthread.getOpgave().setUseranser(input);
				gameloopthread.setAnswergiven(true);
				if (Integer.parseInt((String) main.tv2.getText()) == answer)
				{
					soundpool.play((int) soundpoolmap.get(sound2), volume, volume, 1, 0, 1f);
				}
				((MultiBall) main.getApplication()).setUserinput("");
				flag = 0;
				break;
			}
		}
	}

	private JBall createJBall(int resid, int ballnumber)
	{
		Drawable d = getResources().getDrawable(resid);
		Bitmap bmp = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		d.draw(new Canvas(bmp));
		Random ran = new Random();
		int angleInDegree = ran.nextInt(360);
		float speed = ran.nextFloat() * (rc.SPEEDFACTOR);

		float x;
		float y;
		boolean collision = false;
		do
		{
			x = ran.nextInt(display.getWidth() - 3 * bmp.getWidth() / 2) + 2 * bmp.getWidth() / 2;
			y = ran.nextInt(display.getHeight() - 3 * bmp.getHeight() / 2) + 2 * bmp.getHeight() / 2;
			for (int i = 0; i < jballarray.length; i++)
			{
				jball = jballarray[i];
				if (jball == null)
					break;
				{
					if (jball.isCollision(x, y))
					{
						collision = true;
						break;
					} else
					{
						collision = false;
					}
				}
			}
		} while (collision == true);

		return new JBall(x, y, (bmp.getWidth() / 2), speed, angleInDegree, bmp, display.getWidth(),
				display.getHeight(), main.getLl_hor().getHeight(), ballnumber, resid, this);
	}

	@SuppressLint("WrongCall")
	@Override
	protected void onDraw(Canvas canvas)
	{

		if (System.currentTimeMillis() - lastTimeBg > 30000)
		{
			currentBgX -= 2;
			lastTimeBg = System.currentTimeMillis();
		}
		if (currentBgX == (0))
		{
			currentBgX = screenwidth;
		}
		//
		canvas.drawColor(Color.BLACK);
		// bgdrawable.draw(canvas);
		// canvas.drawBitmap(bgimage, 0 - (screenwidth - currentBgX), 0, null);

		// canvas.drawBitmap(bgimage, currentBgX, 200, null);
		// canvas.drawBitmap(bgimage, 0, 0,null);
		for (int i = 0; i < jballarray.length; i++)
		{
			jballarray[i].onDraw(canvas);
		}
		for (int i = temps.size() - 1; i >= 0; i--)
		{
			// temps.get(i).onDraw(canvas);
		}

	}

	public Bitmap drawableToBitmap(Drawable drawable)
	{
		Bitmap b = Bitmap.createBitmap(screenwidth, screenheight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(b);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return b;
	}

	public void updateBalls2()
	{
		float timeLeft = 5.0f;
		for (int i = 0; i < jballarray.length; i++)
		{
			jballarray[i].moveOneStepWithCollisionDetection(box);
		}
		do
		{
			float earliestCollisionTime = timeLeft;

			for (int i = 0; i < jballarray.length; ++i)
			{
				for (int j = 0; j < jballarray.length; ++j)
				{
					if (i < j)
					{
						jballarray[i].intersect(jballarray[j], earliestCollisionTime);
						if (jballarray[i].earliestCollisionResponse.t < earliestCollisionTime)
						{
							earliestCollisionTime = jballarray[i].earliestCollisionResponse.t;
						}
					}
				}
			}
			for (int i = 0; i < jballarray.length; i++)
			{
				jballarray[i].update(earliestCollisionTime);
			}
			timeLeft -= earliestCollisionTime;
		} while (timeLeft > EPSILON_TIME);

	}
}
