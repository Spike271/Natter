package raven.chat.swing.scroll;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.client.Application;
import global.Theme;

public class ModernScrollBarUI extends BasicScrollBarUI
{
	private static final int SCROLL_BAR_ALPHA_ROLLOVER = 100;
	private static final int SCROLL_BAR_ALPHA = 50;
	private static final int THUMB_SIZE = 8;

	public ModernScrollBarUI() { switchTheme(Application.currentTheme); }

	@Override
	protected JButton createDecreaseButton(int orientation)
	{
		return new InvisibleScrollBarButton();
	}
	
	@Override
	protected JButton createIncreaseButton(int orientation)
	{
		return new InvisibleScrollBarButton();
	}
	
	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {}
	
	@Override
	protected Dimension getMinimumThumbSize()
	{
		return new Dimension(0, 75);
	}
	
	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
	{
		int alpha = isThumbRollover() ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
		int orientation = scrollbar.getOrientation();
		int x = thumbBounds.x;
		int y = thumbBounds.y;
		
		int width = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : thumbBounds.width;
		width = Math.max(width, THUMB_SIZE);
		
		int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height : THUMB_SIZE;
		height = Math.max(height, THUMB_SIZE);
		
		Graphics2D graphics2D = (Graphics2D) g.create();
        Color color = UIManager.getColor("Component.thumbColor");
        graphics2D.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
		graphics2D.fillRect(x, y, width, height);
		graphics2D.dispose();
	}

    interface CustomLightColorScheme {
        Color THUMB_COLOR = Color.BLACK;
    }

    interface CustomDarkColorScheme {
        Color THUMB_COLOR = Color.GRAY;
    }

    private static void applyColorScheme(Class<?> colorSchemeClass)
    {
        try {
            UIManager.put("Component.thumbColor", colorSchemeClass.getField("THUMB_COLOR").get(null));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void switchTheme(Theme currentTheme)
    {
        if (currentTheme == Theme.LIGHT_MODE)
            applyColorScheme(CustomLightColorScheme.class);
        else
            applyColorScheme(CustomDarkColorScheme.class);
    }
	
	private static class InvisibleScrollBarButton extends JButton
	{
		private InvisibleScrollBarButton()
		{
			setOpaque(false);
			setFocusable(false);
			setFocusPainted(false);
			setBorderPainted(false);
			setBorder(BorderFactory.createEmptyBorder());
		}
	}
}