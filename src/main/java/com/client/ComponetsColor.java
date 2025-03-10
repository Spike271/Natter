package com.client;

import java.awt.Color;

public interface ComponetsColor
{
	Color titleTextColor = Color.decode(ResourceHandler.getSettings(Theme.currentTheme, "titleTextColor"));
	Color titleBarColor = Color.decode(ResourceHandler.getSettings(Theme.currentTheme, "titleBarColor"));
	Color frameBGColor = Color.decode(ResourceHandler.getSettings(Theme.currentTheme, "frameBGColor"));
	Color minbtnColor = Color.decode(ResourceHandler.getSettings(Theme.currentTheme, "minbtnColor"));
	Color minbtnBGColor = Color.decode(ResourceHandler.getSettings(Theme.currentTheme, "minbtnBGColor"));
	Color minbtnHoverColor = Color.decode(ResourceHandler.getSettings(Theme.currentTheme, "minbtnHoverColor"));
	Color closebtnColor = Color.decode(ResourceHandler.getSettings(Theme.currentTheme, "closebtnColor"));
	Color closebtnBGColor = Color.decode(ResourceHandler.getSettings(Theme.currentTheme, "closebtnBGColor"));
	Color closebtnHoverColor = Color.decode(ResourceHandler.getSettings(Theme.currentTheme, "closebtnHoverColor"));
	Color fontColor = Color.decode(ResourceHandler.getSettings(Theme.currentTheme, "fontColor"));
	Color userPanel = Color.decode(ResourceHandler.getSettings(Theme.currentTheme, "userPanel"));
}