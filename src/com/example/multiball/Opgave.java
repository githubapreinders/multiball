package com.example.multiball;

import java.util.Random;

public class Opgave
{

	private String opgavetekst;
	private int correctanswer;
	private int useranser; 
	
	public Opgave(int runconfiguration,MainActivity main)
	{
		
		switch(runconfiguration)
		{
		case 1 : String[] digits = main.getResources().getStringArray(R.array.digits_one_to_ten);
		Random ran = new Random();
		this.correctanswer = ran.nextInt(10)+1;
		this.opgavetekst = digits[correctanswer-1];
		default : break;

		}
	}

	public String getOpgavetekst()
	{
		return opgavetekst;
	}

	public void setOpgavetekst(String opgavetekst)
	{
		this.opgavetekst = opgavetekst;
	}

	public int getCorrectanswer()
	{
		return correctanswer;
	}

	public void setCorrectanswer(int correctanswer)
	{
		this.correctanswer = correctanswer;
	}

	public int getUseranser()
	{
		return useranser;
	}

	public void setUseranser(int useranser)
	{
		this.useranser = useranser;
	}
	
	
	
}
