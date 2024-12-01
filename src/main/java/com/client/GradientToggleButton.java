package com.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

public class GradientToggleButton extends JToggleButton
{
	private static final long serialVersionUID = 1L;
	private final Color thumbColor = Color.WHITE; // Thumb color
	private final int height = 28; // Height of the switch
	private final int width = 58; // Width of the entire switch
	
	public GradientToggleButton()
	{
		// Set initial state of the switch
		this.setSelected(false);
		this.setText(null);
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setFocusPainted(false);
		this.setPreferredSize(new Dimension(width, height));
		
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				repaint();
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		// Enable anti-aliasing
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Determine the gradient colors based on the toggle state
		Color startColor;
		Color endColor;
		
		if (isSelected())
		{
			startColor = Color.decode("#fc00ff"); // Fuchsia
			endColor = Color.decode("#00dbde"); // Bright Turquoise
		}
		else
		{
			startColor = Color.GRAY;
			endColor = Color.GRAY;
		}
		
		// Create gradient
		GradientPaint gradient = new GradientPaint(0, 0, startColor, width, 0, endColor);
		g2d.setPaint(gradient);
		
		// Draw the pill background
		g2d.fillRoundRect(0, 0, width, height, height, height); // Rounded edges for pill shape
		
		// Draw the toggle thumb
		int thumbX = isSelected() ? width - height + 2 : 4;  // Calculate thumb's X position
		g2d.setColor(thumbColor);
		g2d.fillOval(thumbX + 2, 6, height - 12, height - 12); // Draw thumb
	}
}