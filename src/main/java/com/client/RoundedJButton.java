package com.client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class RoundedJButton extends JButton
{
	private static final long serialVersionUID = 1L;
	
	private int borderRadius;
	private boolean isHovered = false;
	private boolean isPressed = false;
	
	public RoundedJButton(String label, int borderRadius)
	{
		super(label);
		this.borderRadius = borderRadius;
		this.setContentAreaFilled(false);
		this.setFocusPainted(false);
		this.setBorderPainted(false);
		
		// Mouse Listener for hover and press effects
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				isHovered = true;
				repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				isHovered = false;
				repaint();
			}
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				isPressed = true;
				repaint();
			}
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				isPressed = false;
				repaint();
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Determine color based on states
		if (isPressed)
		{
			g2.setColor(getBackground().darker()); // Darker when pressed
		}
		else if (isHovered)
		{
			g2.setColor(getBackground().brighter()); // Brighter when hovered
		}
		else
		{
			g2.setColor(getBackground());
		}
		
		// Fill rounded rectangle
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);
		
		super.paintComponent(g); // Paint the label text
	}
	
	@Override
	protected void paintBorder(Graphics g)
	{
		
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(100, 40);
	}
}