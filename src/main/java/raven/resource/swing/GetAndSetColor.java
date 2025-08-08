package raven.resource.swing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import com.client.Application;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

public class GetAndSetColor
{
	private static final String settingFile = "libres/Settings/Color.ini";
	
	public static Optional<String> getSettings(String section, String key)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);
			
			String path = Application.jarFilePath + settingFile;
			fileHandler.load(new File(path));
			return Optional.of(iniConfig.getString(section + "." + key));
		}
		catch (Exception e)
		{
			System.err.println("can't open the config file!");
		}
		return Optional.empty();
	}

	@SuppressWarnings("unused")
	public static void changeSettings(String key)
	{
		String path = Application.jarFilePath + settingFile;
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);
			fileHandler.load(new File(path));
			
			iniConfig.setProperty("ColorMode.IsDark", key);
			iniConfig.write(new FileWriter(path));
		}
		catch (ConfigurationException | IOException e)
		{
			System.err.println("can't open the config file!");
		}
	}
	
	public static boolean isDarkModeOn()
	{
		return getSettings("ColorMode", "IsDark").map("true"::equals).orElse(true);
	}
}