package com.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JToggleButton;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;

public class GradientToggleButton extends JToggleButton
{
	private static final long serialVersionUID = 1L;
	private final Color thumbColor = Color.WHITE;
	private final int height = UIScale.scale(28); // Scale for DPI
	private final int width = UIScale.scale(58);
	
	private final Color activeStartColor = Color.decode("#fc00ff");
	private final Color activeEndColor = Color.decode("#00dbde");
	private final Color inactiveColor = Color.GRAY;
	
	public GradientToggleButton()
	{
		setSelected(false);
		setText(null);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setPreferredSize(new Dimension(width, height));
		
		setMargin(new Insets(UIScale.scale(4), UIScale.scale(8), UIScale.scale(4), UIScale.scale(8)));
		
		putClientProperty(FlatClientProperties.STYLE, "buttonType: roundRect;" + "arc: 999");
		
		addActionListener(e -> repaint());
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		GradientPaint gradient = new GradientPaint(0, 0, isSelected() ? activeStartColor : inactiveColor, width, 0,
				isSelected() ? activeEndColor : inactiveColor);
		g2d.setPaint(gradient);
		g2d.fillRoundRect(0, 0, width, height, height, height);
		
		int thumbSize = height - UIScale.scale(12);
		int thumbX = isSelected() ? width - height + UIScale.scale(4) : UIScale.scale(6);
		int thumbY = (height - thumbSize) / 2;
		
		g2d.setColor(new Color(0, 0, 0, 20));
		g2d.fillOval(thumbX + UIScale.scale(1), thumbY + UIScale.scale(1), thumbSize, thumbSize);
		
		g2d.setColor(thumbColor);
		g2d.fillOval(thumbX, thumbY, thumbSize, thumbSize);
	}
}