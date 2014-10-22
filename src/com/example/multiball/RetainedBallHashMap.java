package com.example.multiball;

import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RetainedBallHashMap extends Fragment
{
	private HashMap<Integer, Ball> balls;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	public void setData(HashMap<Integer, Ball> balls)
	{
		this.balls = balls;
	}

	public HashMap<Integer,Ball> getData()
	{
		return this.balls;
	}
}
