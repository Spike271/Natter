package raven.resource.swing;

import java.awt.Font;
import java.io.File;

import com.client.Application;

public class MyFont
{
	private static final String path = Application.jarFilePath + "libres/Fonts/";
	
	public static Font getFont(String name, float size)
	{
		File FontFile = null;
		Font font = null;
		try
		{
			FontFile = getFontFile(path + name);
			font = Font.createFont(Font.TRUETYPE_FONT, FontFile).deriveFont(size);
		}
		catch (Exception e)
		{
			System.err.println("couldn't load the font");
		}
		return font;
	}
	
	private static File getFontFile(String resource)
	{
		return new File(resource);
	}
}