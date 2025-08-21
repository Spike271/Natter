package raven.chat.component;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;

import com.client.Application;
import com.formdev.flatlaf.FlatClientProperties;
import global.Theme;
import net.miginfocom.swing.MigLayout;
import raven.chat.model.ModelMessage;
import raven.chat.swing.AutoWrapText;
import raven.chat.swing.ImageAvatar;

public class ChatBox extends JComponent
{
	private final BoxType boxType;
	private final ModelMessage message;
	
	public ChatBox(BoxType boxType, ModelMessage message)
	{
		this.boxType = boxType;
		this.message = message;
		init();
	}
	
	private void init()
	{
		initBox();
	}
	
	private void initBox()
	{
		String rightToLeft = boxType == BoxType.RIGHT ? ",rtl" : "";
        switchTheme(Application.currentTheme);
		setLayout(new MigLayout("inset 1 5 1 5" + rightToLeft, "[40!]5[]", "[top]")); // left top right bottom

		ImageAvatar avatar = new ImageAvatar();
		avatar.setBorderSize(1);
		avatar.setBorderSpace(1);
		avatar.setImage(message.getIcon());

		JTextPane text = new JTextPane();
		text.setEditorKit(new AutoWrapText());
		text.setText(message.getMessage());
		text.setFont(global.ResourceHandler.getFont("Roboto-Medium.ttf", 18f)); //
		text.setBackground(new Color(0, 0, 0, 0));
        text.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #FFFFFF;" +
                "[dark]foreground: #121212;");
		text.setSelectionColor(new Color(200, 200, 200, 100));
		text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		text.setOpaque(false);
		text.setEditable(false);

		JLabel labelDate = new JLabel(message.getName() + " | " + message.getDate());
		labelDate.setFont(global.ResourceHandler.getFont("GoogleSans-Medium.ttf", 16f)); //
        labelDate.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #121212;" +
                "[dark]foreground: #FFFFFF;");
		add(avatar, "height 40,width 40");
		add(text, "gapy 20, wrap");
		add(labelDate, "gapx 20, span 2");
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int width = getWidth();
		int height = getHeight();
        Area area;
        if (boxType == BoxType.LEFT)
		{
            area = new Area(new RoundRectangle2D.Double(25, 25, width - 25, height - 25 - 16 - 10, 5, 5));
			area.subtract(new Area(new Ellipse2D.Double(5, 5, 45, 45)));
			g2.setPaint(new GradientPaint(0, 0, UIManager.getColor("Component.leftChatBubbleFrom"), width, 0,
                    UIManager.getColor("Component.leftChatBubbleTo")));
        }
		else
		{
            area = new Area(new RoundRectangle2D.Double(0, 25, width - 25, height - 25 - 16 - 10, 5, 5));
			area.subtract(new Area(new Ellipse2D.Double(width - 50, 5, 45, 45)));
			g2.setPaint(new GradientPaint(0, 0, UIManager.getColor("Component.rightChatBubbleFrom"), width, 0,
                    UIManager.getColor("Component.rightChatBubbleTo")));
        }
        g2.fill(area);
        g2.dispose();
		super.paintComponent(g);
	}
	
	public BoxType getBoxType()
	{
		return boxType;
	}
	
	public ModelMessage getMessage()
	{
		return message;
	}
	
	public enum BoxType
	{
		LEFT, RIGHT
	}

    interface CustomLightColorScheme {
        Color LEFT_CHAT_BUBBLE_FROM = Color.decode("#00B4DB");
        Color LEFT_CHAT_BUBBLE_TO = Color.decode("#0083B0");
        Color RIGHT_CHAT_BUBBLE_FROM = Color.decode("#8E2DE2");
        Color RIGHT_CHAT_BUBBLE_TO = Color.decode("#4A00E0");
    }

    interface CustomDarkColorScheme {
        Color LEFT_CHAT_BUBBLE_FROM = Color.decode("#B721FF");
        Color LEFT_CHAT_BUBBLE_TO = Color.decode("#21d4fd");
        Color RIGHT_CHAT_BUBBLE_FROM = Color.decode("#EE0979");
        Color RIGHT_CHAT_BUBBLE_TO = Color.decode("#FF6A00");
    }

    private static void applyColorScheme(Class<?> colorSchemeClass)
    {
        try {
            UIManager.put("Component.leftChatBubbleFrom", colorSchemeClass.getField("LEFT_CHAT_BUBBLE_FROM").get(null));
            UIManager.put("Component.leftChatBubbleTo", colorSchemeClass.getField("LEFT_CHAT_BUBBLE_TO").get(null));
            UIManager.put("Component.rightChatBubbleFrom", colorSchemeClass.getField("RIGHT_CHAT_BUBBLE_FROM").get(null));
            UIManager.put("Component.rightChatBubbleTo", colorSchemeClass.getField("RIGHT_CHAT_BUBBLE_TO").get(null));
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
