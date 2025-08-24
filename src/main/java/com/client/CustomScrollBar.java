package com.client;

import raven.chat.swing.scroll.ModernScrollBarUI;

import java.awt.Dimension;

import javax.swing.JScrollBar;

public class CustomScrollBar extends JScrollBar
{
    public CustomScrollBar()
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