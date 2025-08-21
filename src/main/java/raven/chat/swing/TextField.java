package raven.chat.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.client.Application;
import com.formdev.flatlaf.FlatClientProperties;
import global.Theme;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class TextField extends JTextPane
{
	public String getHint()
	{
		return hint;
	}
	
	public void setHint(String hint)
	{
		this.hint = hint;
		repaint();
	}
	
	private String hint = "";
	private final Animator animator;
	private float animate;
	private boolean show = true;
	
	public TextField()
	{
		setOpaque(false);
        switchTheme(Application.currentTheme);
		setBorder(new EmptyBorder(9, 1, 9, 1));
		setBackground(new Color(0, 0, 0, 0));
        putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #121212;" + "[dark]foreground: #FFFFFF;");
		setFont(global.ResourceHandler.getFont("GoogleSans-Regular.ttf", 17f)); //////////////////////////////////////////////////
		setSelectionColor(new Color(200, 200, 200, 100));
		autoWrapText();
		animator = new Animator(350, new TimingTargetAdapter() {
			@Override
			public void timingEvent(float fraction)
			{
				if (show)
				{
					animate = fraction;
				}
				else
				{
					animate = 1f - fraction;
				}
				repaint();
			}
			
			@Override
			public void end()
			{
				show = !show;
				repaint();
			}
			
		});
		animator.setResolution(0);
		animator.setAcceleration(.5f);
		animator.setDeceleration(.5f);
		getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				if (!getText().isBlank())
				{
					if (show)
					{
						if (!animator.isRunning())
						{
							stop();
							animator.start();
						}
					}
					else if (animator.isRunning())
					{
						stop();
						animator.start();
					}
				}
			}
			
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				if (getText().isBlank())
				{
					stop();
					animator.start();
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {}
		});
	}
	
	private void autoWrapText()
	{
		setEditorKit(new AutoWrapText());
	}
	
	private void stop()
	{
		if (animator.isRunning())
		{
			float f = animator.getTimingFraction();
			animator.stop();
			animator.setStartFraction(1f - f);
		}
		else
		{
			animator.setStartFraction(0f);
		}
	}
	
	@Override
	public void paint(Graphics g)
	{
		if (!hint.isBlank())
		{
			Graphics2D g2 = (Graphics2D) g.create();
			int h = getHeight();
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			Insets ins = getInsets();
			FontMetrics fm = g.getFontMetrics();
			g2.setColor(UIManager.getColor("Component.placeHolderTextColor"));
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - animate));
			g2.drawString(hint, ins.left + (animate * 30), (float) h / 2 + (float) fm.getAscent() / 2 - 1);
			g2.dispose();
		}
		super.paint(g);
	}

    interface CustomLightColorScheme {
        Color PLACE_HOLDER_TEXT_COLOR = Color.BLACK;
    }

    interface CustomDarkColorScheme {
        Color PLACE_HOLDER_TEXT_COLOR = Color.WHITE;
    }

    private static void applyColorScheme(Class<?> colorSchemeClass)
    {
        try {
            UIManager.put("Component.placeHolderTextColor", colorSchemeClass.getField("PLACE_HOLDER_TEXT_COLOR").get(null));
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
}
