package raven.chat.swing.scroll;

import java.awt.Dimension;

import javax.swing.JScrollBar;

public class ScrollBar extends JScrollBar
{
	public ScrollBar()
	{
        update();
	}

    @Override
    public void updateUI()
    {
        update();
    }

    private void update()
    {
        setUI(new ModernScrollBarUI());
        setPreferredSize(new Dimension(10, 5));
        setOpaque(false);
        setUnitIncrement(20);
    }
}
