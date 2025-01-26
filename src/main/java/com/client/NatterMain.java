package com.client;

import javax.swing.SwingUtilities;

public class NatterMain implements Theme
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
				signIn = new SignIn();
				signIn.setVisible(true);
				
				signUp = new SignUp();
				signUp.setVisible(false);
			}
		});
		
		/*
		 * try { EventQueue.invokeAndWait(new Runnable() {
		 * 
		 * @Override public void run() {
		 * FlatLaf.registerCustomDefaultsSource("res.com.themes");
		 * FlatRobotoFont.install(); UIManager.put("defaultFont", new
		 * Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
		 * 
		 * if (toggle) FlatMacDarkLaf.setup(); else FlatMacLightLaf.setup();
		 * 
		 * Natter natter = new Natter(); natter.setVisible(true);
		 * 
		 * settingPanel = new SettingPanel(); } }); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */
		
	}
}