package com.example.multiball;

import java.util.HashMap;

import android.app.Application;

public class MultiBall extends Application
{
	String userinput;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		this.userinput = "";

	}

	public String getUserinput()
	{
		return userinput;
	}

	public void setUserinput(String userinput)
	{
		this.userinput = userinput;
	}
	

		

}
