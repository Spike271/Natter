package com.client;

import java.awt.Dimension;

import javax.swing.JScrollBar;

public class CustomScrollBar extends JScrollBar
{
	public CustomScrollBar()
	{
		setUI(new ModernScrollBarUI());
		setPreferredSize(new Dimension(10, 10));
		setOpaque(false);
		setUnitIncrement(20);
	}
}