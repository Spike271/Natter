package raven.resource.swing;

import com.client.Application;

public class GetImage
{
    public static String getSettings(String imgPath)
    {
        return Application.jarFilePath + "libres/background/" + imgPath;
    }
}