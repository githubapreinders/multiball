package com.example.multiball;

import java.util.Formatter;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

public class JBall
{
	private float ROT_STEP;
	public static String TAG = "JBALL";
	private float rotation;
	private Bitmap bitmap;
	private int dpwidth;
	private int dpheight;
	private int tvheight;
	private int ballid;
	private int justborn;
	private long lastPlop;
	float ballMinX, ballMinY, ballMaxX, ballMaxY;
	float x, y; // Ball's center x and y (package access)
	float speedX, speedY; // Ball's speed per step in x and y (package access)
	float radius; // Ball's radius (package access)
	Matrix matrix;
	private int ballnumber;
	private Paint p;
	public CollisionResponse earliestCollisionResponse = new CollisionResponse();
	private CollisionResponse tempResponse = new CollisionResponse();
	private CollisionResponse thisResponse = new CollisionResponse();
	private CollisionResponse anotherResponse = new CollisionResponse();
	private GameView gameview;
	private float volume = 0.30f;
	public boolean ballturns;

	/**
	 * Constructor: For user friendliness, user specifies velocity in speed and
	 * moveAngle in usual Cartesian coordinates. Need to convert to speedX and
	 * speedY in Java graphics coordinates for ease of operation.
	 */
	public JBall(float x, float y, float radius, float speed, float angleInDegree, Bitmap bitmap, int displaywidth,
			int displayheight, int textviewheight, int ballnumber, int ballid, GameView gameview)
	{
		this.x = x;
		this.y = y;
		// Convert (speed, angle) to (x, y), with y-axis inverted
		this.speedX = (float) (speed * Math.cos(Math.toRadians(angleInDegree)));
		this.speedY = (float) (-speed * (float) Math.sin(Math.toRadians(angleInDegree)));
		this.radius = radius;
		this.bitmap = bitmap;
		this.dpwidth = displaywidth;
		this.dpheight = displayheight;
		this.tvheight = textviewheight;
		this.ballMinX = radius;
		this.ballMinY = radius;
		this.ballMaxX = dpwidth - radius;
		this.ballMaxY = dpheight - tvheight - radius;
		this.ballnumber = ballnumber;
		this.ballid = ballid;
		this.ROT_STEP = 1.0f;
		this.p = new Paint();
		Random ran = new Random();
		this.rotation = ran.nextFloat() * 360;
		this.gameview = gameview;
		this.ballturns = true;
	}

	public JBall(float x, float y, float radius, float speed, float angleInDegree, Bitmap bitmap, int displaywidth,
			int displayheight, int textviewheight, int ballnumber, int ballid, GameView gameview, boolean turns)
	{
		this.x = x;
		this.y = y;
		// Convert (speed, angle) to (x, y), with y-axis inverted
		this.speedX = (float) (speed * Math.cos(Math.toRadians(angleInDegree)));
		this.speedY = (float) (-speed * (float) Math.sin(Math.toRadians(angleInDegree)));
		this.radius = radius;
		this.bitmap = bitmap;
		this.dpwidth = displaywidth;
		this.dpheight = displayheight;
		this.tvheight = textviewheight;
		this.ballMinX = radius;
		this.ballMinY = radius;
		this.ballMaxX = dpwidth - radius;
		this.ballMaxY = dpheight - tvheight - radius;
		this.ballnumber = ballnumber;
		this.ballid = ballid;
		this.ROT_STEP = 1.0f;
		this.p = new Paint();
		this.ballturns = turns;
		Random ran = new Random();
		this.rotation = ran.nextFloat() * 360;
		this.gameview = gameview;
	}

	public void setSpeeds(float speed, int angleInDegree)
	{
		this.speedX = (float) (speed * Math.cos(Math.toRadians(angleInDegree)));
		this.speedY = (float) (-speed * (float) Math.sin(Math.toRadians(angleInDegree)));
	}

	public int getBallid()
	{
		return ballid;
	}

	public int getBallnumber()
	{
		return ballnumber;
	}

	public void setBallnumber(int ballnumber)
	{
		this.ballnumber = ballnumber;
	}

	public Bitmap getBitmap()
	{
		return bitmap;
	}

	public void intersect(ContainerBox box, float timeLimit)
	{
		// Call movingPointIntersectsRectangleOuter, which returns the
		// earliest collision to one of the 4 borders, if collision detected.
		CollisionPhysics.pointIntersectsRectangleOuter(this.x, this.y, this.speedX, this.speedY, this.radius, box.minX,
				box.minY, box.maxX, box.maxY, timeLimit, tempResponse);

		if (tempResponse.t < earliestCollisionResponse.t)
		{
			earliestCollisionResponse.copy(tempResponse);
		}
	}

	public void intersect(JBall another, float timeLimit)
	{
		// Call movingPointIntersectsMovingPoint() with timeLimit.
		// Use thisResponse and anotherResponse, as the working copies, to store
		// the
		// responses of this ball and another ball, respectively.
		// Check if this collision is the earliest collision, and update the
		// ball's
		// earliestCollisionResponse accordingly.

		CollisionPhysics.pointIntersectsMovingPoint(this.x, this.y, this.speedX, this.speedY, this.radius, another.x,
				another.y, another.speedX, another.speedY, another.radius, timeLimit, thisResponse, anotherResponse);

		if (anotherResponse.t < another.earliestCollisionResponse.t)
		{
			another.earliestCollisionResponse.copy(anotherResponse);
			another.ROT_STEP *= -1.0f;
			if (System.currentTimeMillis() - lastPlop > 300)
			{
				gameview.soundpool.play((int) gameview.soundpoolmap.get(gameview.sound3), volume, volume, 1, 0, 1f);
				lastPlop = System.currentTimeMillis();
			}
		}
		if (thisResponse.t < this.earliestCollisionResponse.t)
		{
			this.earliestCollisionResponse.copy(thisResponse);
			this.ROT_STEP *= -1.0f;
			// gameview.soundpool.play((int)gameview.soundpoolmap.get(gameview.sound3),
			// volume, volume, 1, 0, 1f);
		}

	}

	public void update(float time)
	{
		// Check the earliest collision detected for this ball stored in
		// earliestCollisionResponse.
		if (earliestCollisionResponse.t <= time)
		{ // Collision detected
			// This ball collided, get the new position and speed
			// Log.e(TAG, "ball limits" + String.valueOf(ballMinX ) + " , " +
			// String.valueOf(ballMinY )+ " , " + String.valueOf(ballMaxX )+
			// " , "+ String.valueOf(ballMaxY ));
			if (this.x < ballMinX || this.x > ballMaxX || this.y > ballMaxY || this.y < ballMinY)
			{
				Log.i(TAG, "Ball " + String.valueOf(this.ballnumber) + "coordinates (" + String.valueOf((int) this.x)
						+ " , " + String.valueOf((int) this.y) + ")");
			}
			this.x = earliestCollisionResponse.getNewX(this.x, this.speedX);
			this.y = earliestCollisionResponse.getNewY(this.y, this.speedY);
			this.speedX = (float) earliestCollisionResponse.newSpeedX;
			this.speedY = (float) earliestCollisionResponse.newSpeedY;

		} else
		{ // No collision in this coming time-step
			// Make a complete move
			this.x += this.speedX;
			this.y += this.speedY;
		}
		// Clear for the next collision detection
		earliestCollisionResponse.reset();
		justborn++;
	}

	/** Draw itself using the given graphics context. */
	public void onDraw(Canvas canvas)
	{
		// roteert de bal;
		if (ballturns)
		{
			matrix = new Matrix();
			rotation += ROT_STEP;
			matrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
			matrix.postRotate(rotation);
			matrix.postTranslate(x, y);
			if (justborn < 46)
			{
				p.setAlpha(justborn * 5);
			} else
			{
				p = null;
			}
			canvas.drawBitmap(bitmap, matrix, p);
		}
		else
		{
			canvas.drawBitmap(bitmap, x-radius,y-radius, p);
		}
		


		// canvas.drawBitmap(bitmap, x-radius,y-radius, null);
	}

	/**
	 * Make one move, check for collision and react accordingly if collision
	 * occurs.
	 * 
	 * @param box
	 *            : the container (obstacle) for this ball.
	 */

	public boolean isCollision(float x2, float y2)
	{
		return x2 >= this.x - radius && x2 <= this.x + radius && y2 >= this.y - radius && y2 <= this.y + radius;
	}

	/** Return the magnitude of speed. */
	public float getSpeed()
	{
		return (float) Math.sqrt(speedX * speedX + speedY * speedY);
	}

	/** Return the direction of movement in degrees (counter-clockwise). */
	public float getMoveAngle()
	{
		return (float) Math.toDegrees(Math.atan2(-speedY, speedX));
	}

	/** Return mass */
	public float getMass()
	{
		return radius * radius * radius / 1000f; // Normalize by a factor
	}

	/** Return the kinetic energy (0.5mv^2) */
	public float getKineticEnergy()
	{
		return 0.5f * getMass() * (speedX * speedX + speedY * speedY);
	}

	/** Describe itself. */
	public String toString()
	{
		sb.delete(0, sb.length());
		formatter.format("@(%3.0f,%3.0f) r=%3.0f V=(%2.0f,%2.0f) " + "S=%4.1f \u0398=%4.0f KE=%3.0f", x, y, radius,
				speedX, speedY, getSpeed(), getMoveAngle(), getKineticEnergy()); // \u0398
																					// is
																					// theta
		return sb.toString();
	}

	// Re-use to build the formatted string for toString()
	private StringBuilder sb = new StringBuilder();
	private Formatter formatter = new Formatter(sb);

	public void moveOneStepWithCollisionDetection(ContainerBox box)
	{
		// Get the ball's bounds, offset by the radius of the ball
		float ballMinX = box.minX + radius;
		float ballMinY = box.minY + radius;
		float ballMaxX = box.maxX - radius;
		float ballMaxY = box.maxY - radius;

		// Calculate the ball's new position
		x += speedX;
		y += speedY;
		// Check if the ball moves over the bounds. If so, adjust the position
		// and speed.
		if (x < ballMinX)
		{
			speedX = -speedX; // Reflect along normal
			x = ballMinX;
			this.ROT_STEP *= -1.0f;
			// gameview.soundpool.play((int)gameview.soundpoolmap.get(gameview.sound5),
			// volume, volume, 1, 0, 1f);
			// Re-position the ball at the edge
		} else if (x > ballMaxX)
		{
			speedX = -speedX;
			x = ballMaxX;
			this.ROT_STEP *= -1.0f;
			// gameview.soundpool.play((int)gameview.soundpoolmap.get(gameview.sound5),
			// volume, volume, 1, 0, 1f);
		}
		// May cross both x and y bounds
		if (y < ballMinY)
		{
			speedY = -speedY;
			y = ballMinY;
			this.ROT_STEP *= -1.0f;
			// gameview.soundpool.play((int)gameview.soundpoolmap.get(gameview.sound5),
			// volume, volume, 1, 0, 1f);
		} else if (y > ballMaxY)
		{
			speedY = -speedY;
			y = ballMaxY;
			this.ROT_STEP *= -1.0f;
			// gameview.soundpool.play((int)gameview.soundpoolmap.get(gameview.sound5),
			// volume, volume, 1, 0, 1f);

		}

	}
}
