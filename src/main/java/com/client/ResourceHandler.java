package com.client;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

public class ResourceHandler
{
	private static final String jarFilePath = Application.jarFilePath;
	private static final String settingFile = "res/Settings/settings.ini";
	private static final String propertyFile = "res/Settings/config.properties";
	private static final String colorFile = "libres/Settings/Color.ini";

	public static Optional<String> getSettings(String section, String key)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);

			fileHandler.load(new File(jarFilePath + settingFile));
			return Optional.of(iniConfig.getString(section + "." + key));
		}
		catch (ConfigurationException e)
		{
			System.err.println("cannot find the config file\ncalled from getSettings()\n"+ jarFilePath + settingFile);
		}
        return Optional.empty();
	}
	
	public static void changeSettings(String property, String value)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);

			String settingFilePath = jarFilePath + settingFile;
			fileHandler.load(new File(settingFilePath));
			
			iniConfig.setProperty(property, value);
			iniConfig.write(new FileWriter(settingFilePath));
		}
		catch (ConfigurationException | IOException e)
		{
			System.err.println("cannot find the config file\ncalled from changeSettings()");
		}
	}
	
	public static void changeColorFileSettings(String property, String value)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);

			String colorFilePath = jarFilePath + colorFile;
			fileHandler.load(new File(colorFilePath));
			
			iniConfig.setProperty(property, value);
			iniConfig.write(new FileWriter(colorFilePath));
		}
		catch (ConfigurationException | IOException _)
		{
			System.err.println("cannot find the config file\ncalled from changeColorFileSettings()");
		}
	}
	
	public static String getColorFileSettings(String property, String value)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);
			
			fileHandler.load(new File(jarFilePath + colorFile));
			return iniConfig.getString(property + "." + value);
		}
		catch (ConfigurationException e)
		{
			System.err.println("cannot find the config file\ncalled from getColorFileSettings()");
		}
		return null;
	}
	
	public static synchronized Optional<String> readPropertiesFile(String key)
	{
		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream(jarFilePath + propertyFile))
		{
			properties.load(input);
			return Optional.ofNullable(properties.getProperty(key));
		}
		catch (IOException _)
		{
			System.err.println("Can't read the properties file");
		}
		return Optional.empty();
	}
	
	public static void writePropertiesFile(String key, String value)
	{
		Properties properties = new Properties();
		String filePath = jarFilePath + propertyFile;

		try (FileInputStream input = new FileInputStream(filePath))
		{
			properties.load(input);
		}
		catch (IOException _)
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
			System.err.println("Can't write in the properties file");
		}
	}
	
	public static boolean isDarkModeOn()
	{
		return getSettings("Global", "isDark").map("true"::equals).orElse(true);
	}
	
	public static boolean alreadyAUser()
	{
		return readPropertiesFile("alreadyAUser").map("true"::equals).orElse(false);
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

	@SuppressWarnings("unused")
	public static ImageIcon loadImageIcon(String resourcePath)
	{
		try
		{
			resourcePath += Application.jarFilePath;
			BufferedImage image = ImageIO.read(new File(resourcePath));
			return new ImageIcon(image);
		}
		catch (IOException e)
		{
			System.err.println("could not find the image\ncalled from loadImageIcon");
		}
		return new ImageIcon();
	}
	
	public static Font getFont(String name, float size)
	{
		File FontFile;
		Font font = null;
		try
		{
			FontFile = getFontFile( jarFilePath + "res/Fonts/" + name);
			font = Font.createFont(Font.TRUETYPE_FONT, FontFile).deriveFont(size);
		}
		catch (Exception e)
		{
			System.err.println("could not find the font\nCalled from getFont");
		}
		return font;
	}
	
	private static File getFontFile(String relFilePath)
	{
		if (relFilePath.contains("%20"))
		{
			relFilePath = relFilePath.replaceAll("%20", " ");
		}
		
		return new File(relFilePath);
	}
}