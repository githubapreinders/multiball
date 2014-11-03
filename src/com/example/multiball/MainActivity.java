package com.example.multiball;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity
{
	public Handler handler;
	public LinearLayout ll_hor;
	public TextView tv1;
	public TextView tv2;
	public ImageView img1;
	private Display display;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		handler = new Handler();
		setContentView(R.layout.activity_main);
		LinearLayout ll = (LinearLayout) findViewById(R.id.llmain);
		LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 3.5f);
		LayoutParams params2 = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params3.weight = 3.0f;
		params3.gravity = Gravity.CENTER_HORIZONTAL;
		LayoutParams params4 = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 3.0f);

		// LinearLayout ll1 = (LinearLayout)findViewById(R.id.ll1);
		// ll1.setLayoutParams(params);
		SurfaceView sf = new GameView(this, MainActivity.this);
		sf.setLayoutParams(params2);
		ll.addView(sf);

		ll_hor = (LinearLayout) findViewById(R.id.ll_hor);
		ll_hor.setLayoutParams(params);

		img1 = new ImageView(this);
		img1.setImageDrawable(getResources().getDrawable(R.drawable.menubutton));
		img1.setLayoutParams(params3);
		img1.setOnClickListener(new menubuttonOnClickListener());

		tv1 = new TextView(this);
		tv1.setText("XYZ");
		tv1.setLayoutParams(params4);
		tv1.setGravity(Gravity.CENTER);
		tv2 = new TextView(this);
		tv2.setText("FGH");
		tv2.setGravity(Gravity.CENTER);
		tv2.setLayoutParams(params4);
		ll_hor.addView(tv1);
		ll_hor.addView(tv2);
		ll_hor.addView(img1);

		WindowManager wm = (WindowManager) (getSystemService("window"));
		display = wm.getDefaultDisplay();

		// ((MultiBall)getApplication()).setScoreboardheight(tv1.getHeight());

	}

	public LinearLayout getLl_hor()
	{
		return ll_hor;
	}

	public TextView getTv1()
	{
		return tv1;
	}

	private class menubuttonOnClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			((MultiBall) getApplication()).setUserinput("");
			tv2.setText("");
			final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.menupopup, null, false),
					(int) (display.getWidth() * 0.8), (int) (display.getHeight() * 0.8), true);
			pw.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 25);
			View mypopupview = pw.getContentView();

			final ImageView image1 = (ImageView)mypopupview.findViewById(R.id.imageView1);
			image1.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					
					Thread.
					pw.dismiss();					
				}
			});
			
		}

	}

	public void clearinput()
	{
		tv2.setText("");
	}

}
