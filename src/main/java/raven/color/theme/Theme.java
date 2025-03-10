package raven.color.theme;

import raven.resource.swing.GetAndSetColor;

public interface Theme
{
	boolean isDarkModeOn = GetAndSetColor.isDarkModeOn();
	
	String mode = isDarkModeOn ? "dark_mode" : "light_mode";
}