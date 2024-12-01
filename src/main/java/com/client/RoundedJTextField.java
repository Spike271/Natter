package com.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

public class RoundedJTextField extends JTextField
{
	private static final long serialVersionUID = 1L;
	private int radius;
	private Color placeholderColor = Color.GRAY;
	private Font placeholderFont;
	private Color borderColor = Color.GRAY; // Default border color
	private Color focusedBorderColor = new Color(0, 200, 250); // Border color when focused
	
	public RoundedJTextField(String placeholder, int radius, Font originalFont)
	{
		this.radius = radius;
		placeholderFont = originalFont.deriveFont(Font.ITALIC);
		this.setForeground(placeholderColor);
		this.setFont(placeholderFont);
		this.setText(placeholder);
		this.setOpaque(false);
		
		this.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusGained(FocusEvent e)
			{
				if (getText().equals(placeholder))
				{
					setText("");
					setForeground(Color.black);
					setFont(placeholderFont.deriveFont(Font.PLAIN));
				}
				
				borderColor = focusedBorderColor;
				repaint();
			}
			
			@Override
			public void focusLost(FocusEvent e)
			{
				if (getText().isEmpty())
				{
					setText(placeholder);
					setForeground(placeholderColor);
					setFont(placeholderFont);
				}
				
				borderColor = Color.GRAY;
				repaint();
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Paint rounded background
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
		
		// Paint text
		super.paintComponent(g);
	}
	
	@Override
	public Insets getInsets()
	{
		return new Insets(5, 10, 5, 10);
	}
	
	@Override
	protected void paintBorder(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(borderColor);
		g2.setStroke(new BasicStroke(1));
		g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
	}
}