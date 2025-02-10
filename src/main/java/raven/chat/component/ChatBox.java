package raven.chat.component;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import net.miginfocom.swing.MigLayout;
import raven.chat.model.ModelMessage;
import raven.chat.swing.AutoWrapText;
import raven.chat.swing.ImageAvatar;
import raven.color.theme.Theme;
import raven.resource.swing.GetAndSetColor;
import raven.resource.swing.MyFont;

public class ChatBox extends JComponent implements Theme
{
	private static final long serialVersionUID = 1L;
	private final BoxType boxType;
	private final ModelMessage message;
	String mode = Theme.isDarkModeOn ? "dark_mode" : "light_mode";
	
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
		setLayout(new MigLayout("inset 1 5 1 5" + rightToLeft, "[40!]5[]", "[top]")); // left top right bottom
		ImageAvatar avatar = new ImageAvatar();
		avatar.setBorderSize(1);
		avatar.setBorderSpace(1);
		avatar.setImage(message.getIcon());
		JTextPane text = new JTextPane();
		text.setEditorKit(new AutoWrapText());
		text.setText(message.getMessage());
		text.setFont(MyFont.getFont("Roboto-Medium.ttf", 18f)); ///////////////////////////////////////////////
		text.setBackground(new Color(0, 0, 0, 0));
		text.setForeground(Color.decode(GetAndSetColor.getSettings(mode, "messageTextColor")));
		text.setSelectionColor(new Color(200, 200, 200, 100));
		text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		text.setOpaque(false);
		text.setEditable(false);
		JLabel labelDate = new JLabel(message.getName() + " | " + message.getDate());
		labelDate.setFont(MyFont.getFont("GoogleSans-Medium.ttf", 16f)); ///////////////////////////////////////
		labelDate.setForeground(Color.decode(GetAndSetColor.getSettings(mode, "dateColor")));
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
		if (boxType == BoxType.LEFT)
		{
			Area area = new Area(new RoundRectangle2D.Double(25, 25, width - 25, height - 25 - 16 - 10, 5, 5));
			area.subtract(new Area(new Ellipse2D.Double(5, 5, 45, 45)));
			g2.setPaint(new GradientPaint(0, 0, Color.decode(GetAndSetColor.getSettings(mode, "leftChatBubbleFrom")),
					width, 0, Color.decode(GetAndSetColor.getSettings(mode, "leftChatBubbleTo"))));
			g2.fill(area);
		}
		else
		{
			Area area = new Area(new RoundRectangle2D.Double(0, 25, width - 25, height - 25 - 16 - 10, 5, 5));
			area.subtract(new Area(new Ellipse2D.Double(width - 50, 5, 45, 45)));
			g2.setPaint(new GradientPaint(0, 0, Color.decode(GetAndSetColor.getSettings(mode, "rightChatBubbleFrom")),
					width, 0, Color.decode(GetAndSetColor.getSettings(mode, "rightChatBubbleTo"))));
			g2.fill(area);
		}
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
	
	public static enum BoxType
	{
		LEFT, RIGHT
	}
}
