package com.example.multiball;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private String TAG = "In MainActivity";
	public TextToSpeech ttobj;
	public boolean flag = true;
	public TransitionDrawable trans;
	public Animation mAnim;
	public ImageView mImageView;
	public int screenwidth;
	public int screenheight;
	public Handler handler;
	public LinearLayout ll_hor;
	//public TextView tv1;
	public TextView tv2;
	public ImageView img1;
	private Display display;
	public ImageView image1, image2, image3, image4;
	public int[] bgimages;
	private ImageView backgroundimage;
	private ImageView backgroundimage_surfaceview;
	private ImageView statuslight;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		handler = new Handler();
		createSpeechEngineInstance();
		setContentView(R.layout.activity_main);
		LinearLayout ll = (LinearLayout) findViewById(R.id.llmain);
		LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 3.5f);
		LayoutParams params2 = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
		
		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT-20);
		params3.weight = 3.0f;
		params3.gravity = Gravity.CENTER;
		
		
		LinearLayout.LayoutParams paramsstatuslight = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT-10,3.0f);
		paramsstatuslight.gravity = Gravity.CENTER;
		paramsstatuslight.weight = 3.0f;
		
		LayoutParams params4 = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 3.0f);
		bgimages = getBgImages();
		backgroundimage = (ImageView) findViewById(R.id.bgimage);

		trans = (TransitionDrawable) getResources().getDrawable(R.drawable.transition);
		trans.setCrossFadeEnabled(true);
		backgroundimage.setImageDrawable(trans);

		SurfaceView sf = new GameView(this, MainActivity.this);
		sf.setLayoutParams(params2);
		ll.addView(sf);

		ll_hor = (LinearLayout) findViewById(R.id.ll_hor);
		ll_hor.setLayoutParams(params);

		img1 = new ImageView(this);
		img1.setImageDrawable(getResources().getDrawable(R.drawable.menubutton));
		img1.setLayoutParams(params3);
		img1.setOnClickListener(new menubuttonOnClickListener());

		statuslight = new ImageView(this);
		statuslight.setImageDrawable(getResources().getDrawable(R.drawable.status_light_orange));
		statuslight.setLayoutParams(paramsstatuslight);
		statuslight.setOnClickListener(new statuslightOnClickListener());

//		tv1 = new TextView(this);
//		tv1.setText("XYZ");
//		tv1.setLayoutParams(params4);
//		tv1.setGravity(Gravity.CENTER);
		tv2 = new TextView(this);
		tv2.setText("");
		tv2.setGravity(Gravity.CENTER);
		tv2.setLayoutParams(params4);
		ll_hor.addView(statuslight);
		ll_hor.addView(tv2);
		ll_hor.addView(img1);

		WindowManager wm = (WindowManager) (getSystemService("window"));
		display = wm.getDefaultDisplay();
		screenwidth = display.getWidth();
		screenheight = display.getHeight();

		// ControlPanel background animation
		handlechange1();

		
		
	    Log.v(TAG, "onCreate");
		getIntent().setAction("Already created");
	}

	private class statuslightOnClickListener implements View.OnClickListener
	{


		@Override
		public void onClick(View v)
		{
			
			
			
			if (!((MultiBall) getApplication()).getGameloopthread().ismPaused())
			{
				statuslight.setImageDrawable(getResources().getDrawable(R.drawable.status_light_orange));
				((MultiBall) getApplication()).getGameloopthread().onPause();
				
				
			}

			else
			{
				statuslight.setImageDrawable(getResources().getDrawable(R.drawable.status_light_green));
				((MultiBall) getApplication()).getGameloopthread().onResume();
				
				
				
			}

		}

	}

	public void createSpeechEngineInstance()
	{
		this.ttobj = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
		{
			@Override
			public void onInit(int status)
			{

				if (status != TextToSpeech.ERROR)
				{
					ttobj.setLanguage(ttobj.getDefaultLanguage());
				}
			}
		});
		ttobj.getDefaultEngine();
		((MultiBall) getApplication()).setSpeechengine(ttobj);
	}

	public int[] getBgImages()
	{
		bgimages = new int[5];
		bgimages[0] = R.drawable.background_animation1;
		bgimages[1] = R.drawable.background_animation2;
		bgimages[2] = R.drawable.background_animation3;
		bgimages[3] = R.drawable.background_animation4;
		bgimages[4] = R.drawable.background_animation5;
		return bgimages;
	}

	void handlechange1()
	{
		Handler hand = new Handler();
		hand.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				change();
			}

			private void change()
			{
				if (flag)
				{
					trans.startTransition(8000);
					flag = false;
				} else
				{
					trans.reverseTransition(8000);
					flag = true;
				}
				handlechange1();
			}
		}, 8000);

	}

	public LinearLayout getLl_hor()
	{
		return ll_hor;
	}


	private class menubuttonOnClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			// Opgave opg = new Opgave(1,MainActivity.this);
			// ttobj.speak(opg.getOpgavetekst(), TextToSpeech.QUEUE_FLUSH,
			// null);
			//
			img1.setEnabled(false);
			((MultiBall) getApplication()).setUserinput("");
			tv2.setText("");
			((MultiBall) getApplication()).getGameloopthread().onPause();

			final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.menupopup, null, false),
					(int) (display.getWidth() * 0.7), (int) (display.getHeight() * 0.7), true);
			pw.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
			View mypopupview = pw.getContentView();
			image1 = (ImageView) mypopupview.findViewById(R.id.imageView1);
			image1.setOnClickListener(new image1OnClickListener(pw));
			image2 = (ImageView) mypopupview.findViewById(R.id.imageView2);
			image2.setOnClickListener(new image2OnClickListener(pw));
			image3 = (ImageView) mypopupview.findViewById(R.id.imageView3);
			image3.setOnClickListener(new image3OnClickListener(pw));
			image4 = (ImageView) mypopupview.findViewById(R.id.imageView4);
			image4.setOnClickListener(new image4OnClickListener(pw));

		}

	}

	private class image1OnClickListener implements View.OnClickListener
	{

		PopupWindow pw;

		public image1OnClickListener(final PopupWindow pw)
		{
			this.pw = pw;
		}

		@Override
		public void onClick(View v)
		{
			v.setEnabled(false);
			((MultiBall) getApplication()).setRunconfiguration(0);
			((MultiBall) getApplication()).getGameloopthread().onResume();
			;
			pw.dismiss();
		}

	}

	private class image2OnClickListener implements View.OnClickListener
	{

		PopupWindow pw;

		public image2OnClickListener(final PopupWindow pw)
		{
			this.pw = pw;
		}

		@Override
		public void onClick(View v)
		{
			v.setEnabled(false);
			((MultiBall) getApplication()).setRunconfiguration(1);
			((MultiBall) getApplication()).getGameloopthread().onResume();
			;
			pw.dismiss();
		}

	}

	private class image3OnClickListener implements View.OnClickListener
	{

		PopupWindow pw;

		public image3OnClickListener(final PopupWindow pw)
		{
			this.pw = pw;
		}

		@Override
		public void onClick(View v)
		{
			v.setEnabled(false);
			((MultiBall) getApplication()).setRunconfiguration(1);
			((MultiBall) getApplication()).getGameloopthread().onResume();
			;
			pw.dismiss();
		}

	}

	private class image4OnClickListener implements View.OnClickListener
	{

		PopupWindow pw;

		public image4OnClickListener(final PopupWindow pw)
		{
			this.pw = pw;
		}

		@Override
		public void onClick(View v)
		{
			v.setEnabled(false);
			((MultiBall) getApplication()).setRunconfiguration(1);
			((MultiBall) getApplication()).getGameloopthread().onResume();
			;
			pw.dismiss();
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

	public void clearinput()
	{
		tv2.setText("");
	}

	@Override
	protected void onPause()
	{

		
		((MultiBall) getApplication()).getGameloopthread().onPause();
		
		if (ttobj != null)
		{
			ttobj.stop();
			ttobj.shutdown();
		}
		super.onPause();
	}

	@Override
	protected void onResume()
	{
	    
	    super.onResume();
	}

}
