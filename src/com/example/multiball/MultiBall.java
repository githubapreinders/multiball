package com.example.multiball;

import java.util.HashMap;

import android.app.Application;
import android.app.FragmentManager;

public class MultiBall extends Application
{
	static FragmentManager fm;
	HashMap<Integer, Ball> balls;

	@Override
	public void onCreate()
	{
		super.onCreate();

	}

	public HashMap<Integer, Ball> getBalls()
	{
		return balls;
	}

	public void setBalls(HashMap<Integer, Ball> balls)
	{
		this.balls = balls;
	}
	

}
