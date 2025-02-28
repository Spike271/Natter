package com.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;

import javax.swing.AbstractButton;
import javax.swing.Icon;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.AnimatedIcon;
import com.formdev.flatlaf.util.UIScale;

public class PasswordRevealIcon implements AnimatedIcon
{
	private final Icon icon;
	private final int space;
	
	public PasswordRevealIcon()
	{
		FlatSVGIcon svgIcon = new FlatSVGIcon(getClass().getResource("../../res/icons/eye.svg")).derive(20, 20);
		this.icon = svgIcon;
		this.space = 3;
	}
	
	@Override
	public void paintIconAnimated(Component c, Graphics g, int x, int y, float animatedValue)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		int s = UIScale.scale(space);
		icon.paintIcon(c, g2, x, y);
		if (animatedValue > 0)
		{
			float startX = x + s;
			float startY = y + getIconHeight() - s;
			
			float endX = x + getIconWidth() - s;
			float endY = y + s;
			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			
			Shape shape = new Line2D.Float(startX, startY, startX + (endX - startX) * animatedValue,
					startY + (endY - startY) * animatedValue);
			
			drawLine(g2, shape, c.getParent().getBackground(), 4f);
			drawLine(g2, shape, new Color(150, 150, 150), 1.5f);
		}
		
		g2.dispose();
	}
	
	private void drawLine(Graphics2D g2, Shape shape, Color color, float size)
	{
		g2.setColor(color);
		g2.setStroke(new BasicStroke(UIScale.scale(size), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.draw(shape);
	}
	
	@Override
	public float getValue(Component c)
	{
		return ((AbstractButton) c).isSelected() ? 0 : 1;
	}
	
	@Override
	public int getIconWidth()
	{
		return icon.getIconWidth();
	}
	
	@Override
	public int getIconHeight()
	{
		return icon.getIconHeight();
	}
	
}