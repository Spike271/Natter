package raven.resource.swing;

import java.awt.Font;
import java.io.File;

import raven.test.ChatUI;

public class MyFont
{
	public static Font getFont(String name, float size)
	{
		File FontFile = null;
		Font font = null;
		try
		{
			FontFile = getFontFile("../../../libres/Fonts/" + name);
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
		String filePath = MyFont.class.getResource(resource).getPath();
		
		// check to see if filepath contains any folder with spaces in the name
		
		if (filePath.contains("%20"))
		{
			filePath = ChatUI.class.getResource(resource).getPath().replaceAll("%20", " ");
		}
		
		return new File(filePath);
	}
}