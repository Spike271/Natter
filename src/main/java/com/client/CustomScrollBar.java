package com.client;

import raven.chat.swing.scroll.ModernScrollBarUI;

import javax.swing.*;
import java.awt.*;

public class CustomScrollBar extends JScrollBar
{
    public CustomScrollBar()
    {
        this.update();
    }

    @Override
    public void updateUI()
    {
        this.update();
    }

    private void update()
    {
        setUI(new ModernScrollBarUI());
        setPreferredSize(new Dimension(10, 5));
        setOpaque(false);
        setUnitIncrement(20);
    }
}