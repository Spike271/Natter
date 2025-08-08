package com.client;

import java.awt.Color;

import javax.swing.JButton;

import com.formdev.flatlaf.FlatClientProperties;

public class ModernButton extends JButton
{
	public ModernButton(String label, Color defaultColor, Color bgColor, Color hoverColor)
	{
		super(label);
		
		setBorderPainted(false);
		setFocusPainted(false);
		setOpaque(false);
		
		putClientProperty(FlatClientProperties.STYLE, "arc: 1;" + "foreground: " + toHex(hoverColor) + ";background: "
				+ toHex(bgColor) + ";hoverBackground: " + toHex(hoverColor) + ";");
		
		putClientProperty("JButton.foreground", Color.red);
		putClientProperty("JButton.hoverForeground", Color.red);
	}
	
	private static String toHex(Color color)
	{
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}
}