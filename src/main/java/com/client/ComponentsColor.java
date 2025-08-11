package com.client;

import java.awt.Color;

public interface ComponentsColor
{
	Color titleTextColor = Color.decode((ResourceHandler.getSettings(Theme.currentTheme, "titleTextColor")).orElse("#000000"));
	Color titleBarColor = Color.decode((ResourceHandler.getSettings(Theme.currentTheme, "titleBarColor").orElse("#FFFFFF")));
	Color frameBGColor = Color.decode((ResourceHandler.getSettings(Theme.currentTheme, "frameBGColor").orElse("#F5F5F5")));
	Color minbtnColor = Color.decode((ResourceHandler.getSettings(Theme.currentTheme, "minbtnColor").orElse("#000000")));
	Color minbtnBGColor = Color.decode((ResourceHandler.getSettings(Theme.currentTheme, "minbtnBGColor")).orElse("#FFFFFF"));
	Color minbtnHoverColor = Color.decode((ResourceHandler.getSettings(Theme.currentTheme, "minbtnHoverColor")).orElse("#DDDDDD"));
	Color closebtnColor = Color.decode((ResourceHandler.getSettings(Theme.currentTheme, "closebtnColor")).orElse("#000000"));
	Color closebtnBGColor = Color.decode((ResourceHandler.getSettings(Theme.currentTheme, "closebtnBGColor")).orElse("#FFFFFF"));
	Color closebtnHoverColor = Color.decode((ResourceHandler.getSettings(Theme.currentTheme, "closebtnHoverColor")).orElse("#FF0000"));
	Color fontColor = Color.decode((ResourceHandler.getSettings(Theme.currentTheme, "fontColor")).orElse("#000000"));
	Color userPanel = Color.decode((ResourceHandler.getSettings(Theme.currentTheme, "userPanel")).orElse("#FCFCFC"));
}