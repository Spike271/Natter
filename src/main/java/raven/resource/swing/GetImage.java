package raven.resource.swing;

public class GetImage
{
	public static String getSettings(String imgPath)
	{
		try
		{
			imgPath = "../../../libres/background/" + imgPath;
			String path = GetImage.class.getResource(imgPath).getFile();
			return path;
		}
		catch (Exception e)
		{
			System.err.println("can't open the config file!");
		}
		return null;
	}
}