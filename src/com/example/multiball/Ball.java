package com.example.multiball;

import java.util.Map;
import java.util.Random;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

public class Ball
{
	public Bitmap getBmp()
	{
		return bmp;
	}

	private String TAG = "Ball_Class";
	private int ballid;
	private Display mDisplay;
	private Point point;
	private int x;
	private int y;
	Paint p;
	private int xSpeed;
	private int ySpeed;
	private int justborn;
	private int newx1Speed;
	private int newy1Speed;
	private int newx2Speed;
	private int newy2Speed;
	private GameView gameView;
	private Bitmap bmp;
	private static final float ROT_STEP = 1.0f;
	private float rotation;
	private boolean ballcreated;
	private int collisionPointX;
	private int collisionPointY;
	private int ballnumber;

	public Ball()
	{
	}

	public Ball(Display mDisplay, GameView gameView, Bitmap bmp, int id, int ballnumber)
	{
		this.gameView = gameView;
		this.bmp = bmp;
		this.mDisplay = mDisplay;
		this.ballid = id;
		this.ballnumber = ballnumber;
		this.p = new Paint();
		Random ran = new Random();
		rotation = ran.nextFloat() * 360;
		xSpeed = ran.nextInt(5) + 1;
		ySpeed = ran.nextInt(5) + 1;
		point = new Point();
		int pointx;
		int pointy;

		pointx = mDisplay.getWidth();
		pointy = mDisplay.getHeight() - gameView.main.getTv1().getHeight();
		point.set(pointx, pointy);
		boolean collision = false;
		do
		{
			this.x = ran.nextInt(point.x - 3 * radius()) + 2 * radius();
			this.y = ran.nextInt(point.y - 3 * radius()) + 2 * radius();
			for (Map.Entry<Integer, Ball> ball : gameView.balls.entrySet())
			{
				if (isEdgeCollision2(ball.getValue()))
				{
					collision = true;
					break;
				} else
				{
					collision = false;
				}
			}
		} while (collision == true);
	}

	public int getBallid()
	{
		return ballid;
	}

	public boolean isCollision(float x2, float y2)
	{
		x2 = x2 + radius();
		y2 = y2 + radius();
		return x2 >= x && x2 <= x + 2 * radius() && y2 >= y && y2 <= y + 2 * radius();
	}

	// checks for collision
	public boolean isEdgeCollision2(Ball secondBall)
	{
		Ball firstBall = this;
		if (firstBall.ballid == secondBall.ballid)
		{
			return false;
		}
		boolean flag = false;
		// bitmaps overlap in any case
		if (firstBall.x + 2 * firstBall.radius() + secondBall.radius() > secondBall.x + secondBall.radius()
				&& firstBall.x < secondBall.x + secondBall.radius() + secondBall.radius()
				&& firstBall.y + 2 * firstBall.radius() + secondBall.radius() > secondBall.y + secondBall.radius()
				&& firstBall.y < secondBall.y + secondBall.radius() + secondBall.radius())
		{
			// bitmaps have collided
			double distance = Math
					.sqrt(((firstBall.x + firstBall.radius() - secondBall.x) * ((firstBall.x + firstBall.radius()) - secondBall.x))
							+ (((firstBall.y + firstBall.radius()) - secondBall.y) * (firstBall.y + firstBall.radius() - secondBall.y)));
			flag = true;

		} else
		{
		}

		return flag;

	}

	// checks for collision and makes an animation
	public boolean isEdgeCollision(Ball secondBall)
	{
		Ball firstBall = this;
		if (firstBall.ballid == secondBall.ballid)
		{
			return false;
		}
		boolean flag = false;
		// bitmaps overlap in any case
		if (firstBall.x + 2 * firstBall.radius() + secondBall.radius() > secondBall.x
				&& firstBall.x < secondBall.x + secondBall.radius()
				&& firstBall.y + 2 * firstBall.radius() + secondBall.radius() > secondBall.y
				&& firstBall.y < secondBall.y + secondBall.radius())
		{
			double distance = Math
					.sqrt(((firstBall.x + firstBall.radius() - secondBall.x) * ((firstBall.x + firstBall.radius()) - secondBall.x))
							+ (((firstBall.y + firstBall.radius()) - secondBall.y) * (firstBall.y + firstBall.radius() - secondBall.y)));
			// balls have collided
			if (distance <= firstBall.radius() + secondBall.radius())
			{
				flag = true;
				if (gameView.ballscreated)
				{
					collisionPointX = (firstBall.x + firstBall.radius() + secondBall.x) / 2;
					collisionPointY = (firstBall.y + firstBall.radius() + secondBall.y) / 2;
					gameView.temps.add(new Tempball(gameView.temps, gameView, (int) collisionPointX
							- firstBall.radius(), (int) collisionPointY - firstBall.radius(), gameView.collision));
					newx1Speed = (firstBall.xSpeed * (firstBall.radius() - secondBall.radius()) + (2 * secondBall
							.radius() * secondBall.xSpeed)) / (firstBall.radius() + secondBall.radius());
					newy1Speed = (firstBall.ySpeed * (firstBall.radius() - secondBall.radius()) + (2 * secondBall
							.radius() * secondBall.ySpeed)) / (firstBall.radius() + secondBall.radius());
					newx2Speed = (secondBall.xSpeed * (secondBall.radius() - firstBall.radius()) + (2 * firstBall
							.radius() * firstBall.xSpeed)) / (firstBall.radius() + secondBall.radius());
					newy2Speed = (secondBall.ySpeed * (secondBall.radius() - firstBall.radius()) + (2 * firstBall
							.radius() * firstBall.ySpeed)) / (firstBall.radius() + secondBall.radius());
					Log.d(TAG, "Collision : xy=" + x + "," + y);
				}
			}

		} else
		{

		}

		return flag;
	}

	private int radius()
	{
		return bmp.getWidth() / 2;
	}

	private synchronized void update()
	{

		if (x > point.x - radius())
		{
			xSpeed *= -1;
			// Log.i(TAG,"Right side : xy=" +x +","+y );
		}
		if (x < radius())
		{
			xSpeed *= -1;
			// Log.i(TAG,"Left side : xy=" +x +","+y );
		}
		x = x + xSpeed;

		if (y > point.y - radius())
		{
			ySpeed *= -1;
			// Log.i(TAG,"Bottom side : xy=" +x +","+y );
		}
		if (y < radius())
		{
			ySpeed *= -1;
			// Log.i(TAG,"Upper side : xy=" +x +","+y );
		}
		y = y + ySpeed;

		justborn++;
	}

	public void checkCol()
	{
		// let op hij neemt zichzelf mee en rekent een collision voor zichzelf
		if (gameView.balls.size() == gameView.ballpositions.size())
		{
			for (Map.Entry<Integer, Point> p : gameView.ballpositions.entrySet())
			{

				Ball ball2 = new Ball();
				ball2.x = p.getValue().x;
				ball2.y = p.getValue().y;
				ball2.xSpeed = gameView.balls.get(p.getKey()).xSpeed;
				ball2.ySpeed = gameView.balls.get(p.getKey()).ySpeed;
				ball2.bmp = this.bmp;
				ball2.ballid = p.getKey();

				{
					if (isEdgeCollision(ball2))
					{
						x = x + newx1Speed;
						y = y + newy1Speed;
						xSpeed = newx1Speed;
						ySpeed = newy1Speed;
						Ball b = gameView.balls.get(p.getKey());
						// b.xSpeed = b.xSpeed * -1;
						// b.ySpeed = b.ySpeed * -1;
						b.x = b.x + newx2Speed;
						b.y = b.y + newy2Speed;
						b.xSpeed = newx2Speed;
						b.ySpeed = newy2Speed;
						gameView.balls.put(p.getKey(), b);
						gameView.ballpositions.put(p.getKey(), new Point(b.x, b.y));
						return;
					}
				}
			}
		}
	}

	public void startAnimation(View view)
	{
		ImageView aniView = (ImageView) view;

		ObjectAnimator animation3 = ObjectAnimator.ofFloat(aniView, "alpha", 0.01f, 1, 1);
		animation3.setDuration(2000);
		animation3.start();
	}

	public Matrix rotateBitmap()
	{
		Matrix matrix = new Matrix();
		rotation += ROT_STEP;
		matrix.postTranslate(-bmp.getWidth() / 2, -bmp.getHeight() / 2);
		matrix.postRotate(rotation);
		matrix.postTranslate(x, y);
		return matrix;
	}

	public synchronized void onDraw(Canvas canvas)
	{
		update();
		checkCol();
		if (justborn < 23)
		{
			p.setAlpha(justborn * 10);
		} else
		{
			p = null;
		}
		Matrix matrix = rotateBitmap();
		canvas.drawBitmap(bmp, matrix, p);
		Point p = new Point(x + radius(), y + radius());
		gameView.ballpositions.put(ballid, p);
		// Log.d(TAG, "Ball number" + String.valueOf(ballid)+" x= "+x
		// +" y= "+y);
	}

	public int getBallnumber()
	{
		return ballnumber;
	}

	public void setBallnumber(int ballnumber)
	{
		this.ballnumber = ballnumber;
	}

}
