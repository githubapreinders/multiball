package com.example.multiball;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;

public class BackgroundAnimation extends Thread
{

TransitionDrawable trans;
ImageView backgroundimage;
	
	public BackgroundAnimation(GameView gameview)
	{
		backgroundimage = new ImageView (gameview.getContext());
		trans = (TransitionDrawable) gameview.main.getResources().getDrawable(R.drawable.transition);
		trans.setCrossFadeEnabled(true);
		backgroundimage.setImageDrawable(trans);
	}
	
	
	
	@Override
	public void run()
	{
		
	}

	
	
}
