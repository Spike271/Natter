package com.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ModernButton extends JButton
{
	private static final long serialVersionUID = 1;
	private Color defaultColor;
	private Color hoverColor;
	
	public ModernButton(String label, Color defaultColor, Color hoverColor)
	{
		super(label);
		this.defaultColor = defaultColor;
		this.hoverColor = hoverColor;
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusPainted(false);
		setOpaque(false); // Make it transparent
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				setBackground(hoverColor);
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				setBackground(defaultColor);
			}
		});
	}
	
	public ModernButton(ImageIcon icon, Color defaultColor, Color hoverColor)
	{
		super(icon);
		this.defaultColor = defaultColor;
		this.hoverColor = hoverColor;
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusPainted(false);
		setOpaque(false); // Make it transparent
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				setBackground(hoverColor);
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				setBackground(defaultColor);
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		if (getModel().isArmed() || getModel().isRollover())
		{
			g.setColor(hoverColor);
		}
		else
		{
			g.setColor(defaultColor);
		}
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}
}