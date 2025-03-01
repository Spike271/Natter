package com.client;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

public class ResourceHandler
{
	private static final String settingFile = "../../res/Settings/settings.ini";
	private static final String propertieFile = "../../res/Settings/config.properties";
	private static final String colorFile = "../../libres/Settings/Color.ini";
	
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
	
	public static void changeSettings(String property, String value)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);
			
			String path = ResourceHandler.class.getResource(settingFile).getFile();
			fileHandler.load(new File(path));
			
			iniConfig.setProperty(property, value);
			iniConfig.write(new FileWriter(path));
		}
		catch (ConfigurationException | IOException e)
		{
			System.err.println("cannot find the config file\ncalled from changeSettings");
		}
	}
	
	public static void changeColorFileSettings(String property, String value)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);
			
			String path = ResourceHandler.class.getResource(colorFile).getFile();
			fileHandler.load(new File(path));
			
			iniConfig.setProperty(property, value);
			iniConfig.write(new FileWriter(path));
		}
		catch (ConfigurationException | IOException e)
		{
			System.err.println("cannot find the config file\ncalled from changeSettings");
		}
	}
	
	public static String readPropertiesFile(String key)
	{
		Properties properties = new Properties();
		
		String filePath = ResourceHandler.class.getResource(propertieFile).getFile();
		
		try (FileInputStream input = new FileInputStream(filePath))
		{
			properties.load(input);
			
			return properties.getProperty(key);
		}
		catch (IOException ex)
		{
			System.err.println("Can't read the propertie file");
		}
		return null;
	}
	
	public static void writePropertiesFile(String key, String value)
	{
		Properties properties = new Properties();
		String filePath = ResourceHandler.class.getResource(propertieFile).getFile();
		
		try (FileInputStream input = new FileInputStream(filePath))
		{
			properties.load(input);
		}
		catch (IOException ex)
		{
			System.err.println("File not found or unable to read. Creating a new file.");
		}
		
		properties.setProperty(key, value);
		
		try (FileOutputStream output = new FileOutputStream(filePath))
		{
			properties.store(output, "Configuration File");
		}
		catch (IOException ex)
		{
			System.err.println("Can't write in the propertie file");
		}
	}
	
	public static boolean isDarkModeOn()
	{
		return getSettings("Global", "isDark").equals("true") ? true : false;
	}
	
	public static boolean alreadyAUser()
	{
		return readPropertiesFile("alreadyAUser").equals("true") ? true : false;
	}
	
	public static String encode(String text)
	{
		return Base64.getEncoder().encodeToString(text.getBytes());
	}
	
	public static String decode(String encodedText)
	{
		byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
		return new String(decodedBytes);
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
			System.out.println("could not find the font\nCalled from getFont");
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