package raven.chat.swing.scroll;

import java.awt.Dimension;

import javax.swing.JScrollBar;

public class ScrollBar extends JScrollBar
{
	private static final long serialVersionUID = 1L;
	
	public ScrollBar()
	{
		setUI(new ModernScrollBarUI());
		setPreferredSize(new Dimension(5, 5));
		setOpaque(false);
		setUnitIncrement(20);
	}
}
