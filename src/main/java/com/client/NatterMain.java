package com.client;

import java.awt.Font;
import java.lang.reflect.InvocationTargetException;

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
	private static PasswordWindow ps;
	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException
	{
		if (ResourceHandler.alreadyAUser())
		{
			initMainUi();
			if (!ResourceHandler.readPropertiesFile("password").isBlank())
			{
				ps.setVisible(true);
			}
		}
		else
		{
			init();
			initMainUi();
		}
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
	}
	
	private static void initMainUi() throws InvocationTargetException, InterruptedException
	{
		SwingUtilities.invokeAndWait(new Runnable() {
			
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
				
				ps = new PasswordWindow();
				natter = new Natter();
			}
		});
	}
}