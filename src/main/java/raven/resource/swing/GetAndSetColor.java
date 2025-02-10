package raven.resource.swing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

public class GetAndSetColor
{
	private static final String settingFile = "../../../libres/Settings/Color.ini";
	
	public static String getSettings(String section, String key)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);
			
			String path = GetAndSetColor.class.getResource(settingFile).getFile();
			fileHandler.load(new File(path));
			return iniConfig.getString(section + "." + key);
		}
		catch (Exception e)
		{
			System.err.println("can't open the config file!");
		}
		return null;
	}
	
	public static void changeSettings(String key)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);
			fileHandler.load(new File(GetAndSetColor.class.getResource(settingFile).getFile()));
			
			iniConfig.setProperty("ColorMode.IsDark", key);
			iniConfig.write(new FileWriter(GetAndSetColor.class.getResource(settingFile).getFile()));
		}
		catch (ConfigurationException | IOException e)
		{
			System.err.println("can't open the config file!");
		}
	}
	
	public static boolean isDarkModeOn()
	{
		String check;
		try
		{
			check = getSettings("ColorMode", "IsDark");
		}
		catch (Exception e)
		{
			return false;
		}
		return check.equals("true") ? true : false;
	}
}