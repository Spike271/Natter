package com.client;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

public class ResourceHandler
{
	public static String getColor(String section, String key)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);
			
			fileHandler.load(new File("./src/main/java/com/Settings/config.ini"));
			return iniConfig.getString(section + "." + key);
		}
		catch (ConfigurationException e)
		{}
		return null;
	}
	
	public static void changeColor(String key)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);
			fileHandler.load(new File("./src/main/java/com/Settings/config.ini"));
			
			iniConfig.setProperty("Natter.isDark", key);
			iniConfig.write(new FileWriter("./src/main/java/com/Settings/config.ini"));
		}
		catch (ConfigurationException | IOException e)
		{}
		
	}
	
	public static boolean isDarkModeOn()
	{
		String check = getColor("Natter", "isDark");
		return check.equals("true") ? true : false;
	}
	
	public static ImageIcon loadImageIcon(String resourcePath)
	{
		try
		{
			BufferedImage image = ImageIO.read(new File(resourcePath));
			return new ImageIcon(image);
		}
		catch (IOException e)
		{
			System.out.println("could not find the file");
		}
		
		return null;
	}
	
	public static Font getFont(String name, float size)
	{
		File FontFile = null;
		Font font = null;
		try
		{
			FontFile = getFontFile("./../Fonts/" + name);
			font = Font.createFont(Font.TRUETYPE_FONT, FontFile).deriveFont(size);
		}
		catch (Exception e)
		{}
		return font;
	}
	
	private static File getFontFile(String resource)
	{
		String filePath = ResourceHandler.class.getResource(resource).getPath();
		
		// check to see if filepath contains any folder with spaces in the name
		
		if (filePath.contains("%20"))
		{
			filePath = SignUp.class.getResource(resource).getPath().replaceAll("%20", " ");
		}
		
		return new File(filePath);
	}
}