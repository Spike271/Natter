package com.client;

public interface Theme
{
	boolean isDarkModeOn = ResourceHandler.isDarkModeOn();
	String currentTheme = isDarkModeOn ? "dark_mode" : "light_mode";
}