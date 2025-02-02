package com.client;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

public class NatterMain implements Theme
{
	public static SignIn signIn;
	public static SignUp signUp;
	public static Natter natter;
	public static SettingPanel settingPanel;
	
	public static void main(String[] args)
	{
		init();
	}
	
	private static void init()
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run()
			{
				signIn = new SignIn();
				signIn.setVisible(true);
				
				signUp = new SignUp();
				signUp.setVisible(false);
			}
		});
		
		try
		{
			EventQueue.invokeAndWait(new Runnable() {
				
				@Override
				public void run()
				{
					FlatLaf.registerCustomDefaultsSource("res.com.themes");
					FlatRobotoFont.install();
					UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
					
					if (toggle)
						FlatMacDarkLaf.setup();
					else
						FlatMacLightLaf.setup();
					
					natter = new Natter();
					// natter.setVisible(true);
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}