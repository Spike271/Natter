package com.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public class Natter extends CustomComponent
{
	private static final long serialVersionUID = 1L;
	private JPanel usersPanel;
	private Color transpentColor = new Color(0, 0, 0, 0);
	
	@Override
	void addThemeButton(JPanel buttonPanel)
	{
	}
	
	private JPanel addPanel(int i)
	{
		JPanel chatItemPanel = new JPanel();
		chatItemPanel.setLayout(new MigLayout("", "[][]", "[]"));
		chatItemPanel.setBackground(transpentColor);
		
		Icon profileImage = new ImageIcon("profile/p" + (i) + ".png");
		ProfilePicture avatar = new ProfilePicture();
		avatar.setBorderSize(1);
		avatar.setBorderSpace(1);
		avatar.setImage(profileImage);
		chatItemPanel.add(avatar, "al left, w 60, h 60");
		
		// Chat details
		JPanel detailsPanel = new JPanel(new MigLayout("al left,debug, wrap, gapy 10", "[][]", "[][]"));
		detailsPanel.setBackground(transpentColor);
		
		JLabel nameLabel = new JLabel("User " + i);
		nameLabel.setForeground(Color.decode(ResourceHandler.getSettings(mode, "fontColor")));
		detailsPanel.add(nameLabel, "pushx, growx, w 150!");
		
		JLabel timestampLabel = new JLabel("10:15 AM");
		timestampLabel.setForeground(Color.decode(ResourceHandler.getSettings(mode, "fontColor")));
		detailsPanel.add(timestampLabel);
		
		JLabel messageLabel = new JLabel("Message preview for User " + i);
		messageLabel.setForeground(Color.decode(ResourceHandler.getSettings(mode, "fontColor")));
		detailsPanel.add(messageLabel, "pushx, growx, span2, w 220!");
		
		chatItemPanel.add(detailsPanel, "al center, w 230!");
		
		chatItemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		chatItemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
			
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt)
			{
				JOptionPane.showMessageDialog(null, "Opening chat with User ");
			}
		});
		
		return chatItemPanel;
	}
	
	private JScrollPane createScroll()
	{
		JScrollPane scroll = new JScrollPane();
		scroll.setBorder(null);
		scroll.setViewportBorder(null);
		return scroll;
	}
	
	public Natter()
	{
		super("Natter");
		
		ComponentResizer com = new ComponentResizer();
		com.registerComponent(this, titleBar);
		com.setMinimumSize(new Dimension(1000, 800));
		com.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		com.setSnapSize(new Dimension(5, 5));
		
		contentPane.setLayout(new BorderLayout());
		this.setSize(1100, 850);
		this.setLocationRelativeTo(null);
		this.setFocusable(false);
		addGuiComponents();
	}
	
	private void addGuiComponents()
	{
		usersPanel = new JPanel();
		usersPanel.setLayout(new MigLayout("wrap, insets 10, gapy 4", "[290:310:320]", ""));
		usersPanel.setBackground(Color.decode(ResourceHandler.getSettings(mode, "userPanel")));
		usersPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.gray)); // Top and right border only
		
		JButton btn1 = new JButton("add");
		
		btn1.addActionListener(new ActionListener() {
			
			int i = 1;
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				usersPanel.add(addPanel(i), "pushx, growx, span1");
				repaint();
				revalidate();
				i++;
			}
		});
		
		JScrollPane scrollBody = createScroll();
		scrollBody.setViewportView(usersPanel);
		scrollBody.setVerticalScrollBar(new CustomScrollBar());
		scrollBody.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBody.getViewport().setOpaque(false);
		
		JLabel userPanelHeading = new JLabel("Chats");
		// userPanelHeading.setBorder(new EmptyBorder(10, 10, 10, 10));
		userPanelHeading.setFont(ResourceHandler.getFont("Roboto-Black.ttf", 20f));
		userPanelHeading.setForeground(Color.decode(ResourceHandler.getSettings(mode, "fontColor")));
		usersPanel.add(userPanelHeading, "w 40, h 40, span, al center");
		
		contentPane.add(scrollBody, BorderLayout.WEST);
		contentPane.add(btn1, BorderLayout.NORTH);
	}
	
}
