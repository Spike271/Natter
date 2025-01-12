package com.client;

import javax.swing.SwingUtilities;

public class NatterMain
{
	public static SignIn signIn;
	public static SignUp signUp;
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run()
			{
				// signIn = new SignIn();
				// signIn.setVisible(true);
				//
				// signUp = new SignUp();
				// signUp.setVisible(false);
				
				Natter natter = new Natter();
				natter.setVisible(true);
			}
		});
	}
}