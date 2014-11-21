package com.example.multiball;

import java.util.HashMap;

import android.app.Application;
import android.speech.tts.TextToSpeech;

public class MultiBall extends Application
{
	public int runconfiguration;
	String userinput;
	GameLoopThread gameloopthread;
	TextToSpeech speechengine;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		this.userinput = "";
		this.runconfiguration = 0;

	}
	
	public TextToSpeech getSpeechengine()
	{
		return speechengine;
	}

	public void setSpeechengine(TextToSpeech speechengine)
	{
		this.speechengine = speechengine;
	}

	public int getRunconfiguration()
	{
		return runconfiguration;
	}

	public void setRunconfiguration(int runconfiguration)
	{
		this.runconfiguration = runconfiguration;
	}



	public String getUserinput()
	{
		return userinput;
	}

	public void setUserinput(String userinput)
	{
		this.userinput = userinput;
	}

	public GameLoopThread getGameloopthread()
	{
		return gameloopthread;
	}

	public void setGameloopthread(GameLoopThread gameloopthread)
	{
		this.gameloopthread = gameloopthread;
	}
	

		

}
