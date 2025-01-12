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
	private static final String settingFile = "../../res/Settings/config.ini";
	
	public static String getSettings(String section, String key)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);
			
			String path = ResourceHandler.class.getResource(settingFile).getFile();
			fileHandler.load(new File(path));
			return iniConfig.getString(section + "." + key);
		}
		catch (ConfigurationException e)
		{
			System.err.println("cannot find the config file\ncalled from getSettings");
		}
		return null;
	}
	
	public static void changeSettings(String key)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);
			
			String path = ResourceHandler.class.getResource(settingFile).getFile();
			fileHandler.load(new File(path));
			
			iniConfig.setProperty("Global.isDark", key);
			iniConfig.write(new FileWriter(path));
		}
		catch (ConfigurationException | IOException e)
		{
			System.err.println("cannot find the config file\ncalled from changeSettings");
		}
	}
	
	public static boolean isDarkModeOn()
	{
		String check = getSettings("Global", "isDark");
		return check.equals("true") ? true : false;
	}
	
	public static ImageIcon loadImageIcon(String resourcePath)
	{
		try
		{
			resourcePath = ResourceHandler.class.getResource(resourcePath).getPath();
			BufferedImage image = ImageIO.read(new File(resourcePath));
			return new ImageIcon(image);
		}
		catch (IOException e)
		{
			System.out.println("could not find the image\ncalled from loadImageIcon");
		}
		return null;
	}
	
	public static Font getFont(String name, float size)
	{
		File FontFile = null;
		Font font = null;
		try
		{
			FontFile = getFontFile("../../res/Fonts/" + name);
			font = Font.createFont(Font.TRUETYPE_FONT, FontFile).deriveFont(size);
		}
		catch (Exception e)
		{
			System.out.println("could not find the font\ncalled from getFont");
		}
		return font;
	}
	
	private static File getFontFile(String resource)
	{
		String filePath = ResourceHandler.class.getResource(resource).getPath();
		
		// check to see if filepath contains any folder with spaces in the name
		
		if (filePath.contains("%20"))
		{
			filePath = ResourceHandler.class.getResource(resource).getPath().replaceAll("%20", " ");
		}
		
		return new File(filePath);
	}
}