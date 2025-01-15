package com.client;

import java.awt.EventQueue;

import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class NatterMain
{
	public static SignIn signIn;
	public static SignUp signUp;
	public static SettingPanel settingPanel;
	
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
		
		try
		{
			EventQueue.invokeAndWait(new Runnable() {
				
				@Override
				public void run()
				{
					FlatLaf.registerCustomDefaultsSource("res.com.themes");
					FlatMacDarkLaf.setup();
					
					settingPanel = new SettingPanel();
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}