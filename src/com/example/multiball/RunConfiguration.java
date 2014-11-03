package com.example.multiball;

import java.util.HashMap;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class RunConfiguration
{

	public JBall[] jballarray;
	public JBall jball;
	public GameView gameview;
	public float speed;
	public int angleInDegree;
	public HashMap<Integer, Integer> bigballs = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> smallballs = new HashMap<Integer, Integer>();

	public RunConfiguration(int whichconfiguration, GameView gameview)
	{
		this.gameview = gameview;
		gameview.box = new ContainerBox(0, 0, gameview.display.getWidth(), gameview.display.getHeight()
				- gameview.main.tv1.getHeight());
		
		if(gameview.display.getWidth()<900){
		bigballs.put(1, R.drawable.circle1_ll);
		bigballs.put(2, R.drawable.circle2_ll);
		bigballs.put(3, R.drawable.circle3_ll);
		bigballs.put(4, R.drawable.circle4_ll);
		bigballs.put(5, R.drawable.circle5_ll);
		bigballs.put(6, R.drawable.circle6_ll);
		bigballs.put(7, R.drawable.circle7_ll);
		bigballs.put(8, R.drawable.circle8_ll);
		bigballs.put(9, R.drawable.circle9_ll);
		bigballs.put(0, R.drawable.circle0_ll);

		smallballs.put(1, R.drawable.circle1_llsm);
		smallballs.put(2, R.drawable.circle2_llsm);
		smallballs.put(3, R.drawable.circle3_llsm);
		smallballs.put(4, R.drawable.circle4_llsm);
		smallballs.put(5, R.drawable.circle5_llsm);
		smallballs.put(6, R.drawable.circle6_llsm);
		smallballs.put(7, R.drawable.circle7_llsm);
		smallballs.put(8, R.drawable.circle8_llsm);
		smallballs.put(9, R.drawable.circle9_llsm);
		smallballs.put(0, R.drawable.circle0_llsm);
		}

		else
		{
			bigballs.put(1, R.drawable.circle1_ll_bigger);
			bigballs.put(2, R.drawable.circle2_ll_bigger);
			bigballs.put(3, R.drawable.circle3_ll_bigger);
			bigballs.put(4, R.drawable.circle4_ll_bigger);
			bigballs.put(5, R.drawable.circle5_ll_bigger);
			bigballs.put(6, R.drawable.circle6_ll_bigger);
			bigballs.put(7, R.drawable.circle7_ll_bigger);
			bigballs.put(8, R.drawable.circle8_ll_bigger);
			bigballs.put(9, R.drawable.circle9_ll_bigger);
			bigballs.put(0, R.drawable.circle0_ll_bigger);

			smallballs.put(1, R.drawable.circle1_ll);
			smallballs.put(2, R.drawable.circle2_ll);
			smallballs.put(3, R.drawable.circle3_ll);
			smallballs.put(4, R.drawable.circle4_ll);
			smallballs.put(5, R.drawable.circle5_ll);
			smallballs.put(6, R.drawable.circle6_ll);
			smallballs.put(7, R.drawable.circle7_ll);
			smallballs.put(8, R.drawable.circle8_ll);
			smallballs.put(9, R.drawable.circle9_ll);
			smallballs.put(0, R.drawable.circle0_ll);
		}
		
		
		Random ran = new Random();
		switch (whichconfiguration)
		{
		default:
			jballarray = new JBall[10];
			for (int i = 0; i < jballarray.length; i++)
			{
				speed = ran.nextFloat() * 1.0f;
				angleInDegree = ran.nextInt(360);
				jballarray[i] = createJBall(i, bigballs.get(i));
				jballarray[i].setSpeeds(speed, angleInDegree);
			}
//			jballarray = new JBall[10];
//			for (int i = 0; i < jballarray.length; i++)
//			{
//				speed = ran.nextFloat() * 1.0f;
//				angleInDegree = ran.nextInt(360);
//				jballarray[i] = createJBall(i, smallballs.get(i));
//				jballarray[i].setSpeeds(speed, angleInDegree);
//			}
			break;

		case 1:
			jballarray = new JBall[10];
						
			for (int i = 0; i < jballarray.length; i++)
			{
				jballarray[i] = createJBall(i, bigballs.get(i));
			}
			int width = gameview.display.getWidth();
			int height = gameview.display.getHeight() - gameview.main.getLl_hor().getHeight();
			int spaceinbetween = 20;
			int radius = (int) jballarray[0].radius;
			int boxleftx = (width - 5 * radius * 2 - 4 * spaceinbetween) / 2;
			int boxlefty = (height - 2 * radius * 2 - spaceinbetween) / 2;
			int startx = boxleftx + radius;
			int starty = boxlefty + radius;
			int startxx = startx;
			int startyy = starty;
			for (int i = 0; i < jballarray.length; i++)
			{
				jballarray[i].x = startxx;
				jballarray[i].y = startyy;
				startxx += 2 * radius + spaceinbetween;
				if (i == 4)
				{
					startxx = startx;
					startyy += 2 * radius + spaceinbetween;
				}
				jballarray[i].ballturns = false;
			}
			break;
		}
	}

	public JBall[] getJballarray()
	{
		return jballarray;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public void setAngleInDegree(int angleInDegree)
	{
		this.angleInDegree = angleInDegree;
	}

	private JBall createJBall(int ballnumber, int resid)
	{
		Drawable d = gameview.getResources().getDrawable(resid);
		Bitmap bmp = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		d.draw(new Canvas(bmp));
		Random ran = new Random();
		float x;
		float y;
		boolean collision = false;
		do
		{
			x = ran.nextInt(gameview.display.getWidth() - 3 * bmp.getWidth() / 2) + 2 * bmp.getWidth() / 2;
			y = ran.nextInt(gameview.display.getHeight() - 3 * bmp.getHeight() / 2) + 2 * bmp.getHeight() / 2;
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

		return new JBall(x, y, (bmp.getWidth() / 2), 0.0f, 0, bmp, gameview.display.getWidth(),
				gameview.display.getHeight(), gameview.main.getLl_hor().getHeight(), ballnumber, resid, this.gameview);
	}

}
