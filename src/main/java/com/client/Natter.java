package com.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Natter extends CustomComponent
{
	private static final long serialVersionUID = 1L;
	private JPanel usersPanel, messagePanel;
	private Color transpentColor = new Color(0, 0, 0, 0);
	
	@Override
	void addThemeButton(JPanel buttonPanel)
	{
	}
	
	public Natter()
	{
		super("Natter");
		contentPane.setLayout(new BorderLayout());
		this.setSize(1200, 850);
		this.setLocationRelativeTo(null);
		this.setFocusable(false);
		addGuiComponents();
	}
	
	private void addGuiComponents()
	{
		usersPanel = new JPanel();
		usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
		usersPanel.setBackground(Color.decode(ResourceHandler.getSettings("dark_mode", "secondaryColor")));
		usersPanel.setPreferredSize(new Dimension(250, getHeight()));
		usersPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.darkGray)); // Top border only
		
		JLabel userPanelHeading = new JLabel("Chats");
		userPanelHeading.setBorder(new EmptyBorder(10, 10, 10, 10));
		userPanelHeading.setFont(ResourceHandler.getFont("Roboto-Medium.ttf", 18f));
		userPanelHeading.setForeground(Color.decode(ResourceHandler.getSettings("dark_mode", "fontColor")));
		usersPanel.add(userPanelHeading);
		
		contentPane.add(usersPanel, BorderLayout.WEST);
		
		////////////////////////////////////////////////////
		
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		chatPanel.setBackground(transpentColor);
		
		contentPane.add(chatPanel, BorderLayout.CENTER);
		
		messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messagePanel.setBorder(new EmptyBorder(50, 50, 0, 50));
		messagePanel.setBackground(transpentColor);
		
		////////////////////////////////////////////////////
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		inputPanel.setLayout(new BorderLayout()); // no gaps between the components
		inputPanel.setBackground(transpentColor);
		
		JTextArea inputField = new JTextArea(2, 1);
		// inputField.setLineWrap(true);
		// inputField.setWrapStyleWord(true);
		
		inputField.setBackground(Color.decode(ResourceHandler.getSettings("dark_mode", "secondaryColor")));
		inputField.setForeground(Color.decode(ResourceHandler.getSettings("dark_mode", "fontColor")));
		inputField.setFont(ResourceHandler.getFont("Roboto-Medium.ttf", 18f));
		inputField.setCaretColor(Color.decode(ResourceHandler.getSettings("dark_mode", "fontColor")));
		inputField.setBorder(new EmptyBorder(0, 5, 0, 5));
		// inputField.setPreferredSize(new Dimension(inputPanel.getWidth(), 50));
		
		inputField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e)
			{
			}
			
			@Override
			public void keyReleased(KeyEvent e)
			{
			}
			
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown())
				{
					String input = inputField.getText();
					
					if (!input.trim().isEmpty())
					{
						inputField.setText("");
						messagePanel.add(createChatMessageComponent(input));
						messagePanel.add(Box.createVerticalStrut(10));
						
						repaint();
						revalidate();
					}
				}
			}
		});
		
		JScrollPane inputScrollPane = new JScrollPane(inputField);
		inputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		inputPanel.add(inputScrollPane, BorderLayout.CENTER);
		
		ModernButton sendButton = new ModernButton(new ImageIcon(ResourceHandler.getSettings(mode, "sendIconPath")),
				Color.black, Color.cyan);
		sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		sendButton.setPreferredSize(new Dimension(40, inputField.getHeight()));
		sendButton.setFocusable(false);
		sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String input = inputField.getText();
				
				if (!input.trim().isEmpty())
				{
					inputField.setText("");
					messagePanel.add(createChatMessageComponent(input));
					messagePanel.add(Box.createVerticalStrut(10));
					
					repaint();
					revalidate();
				}
			}
		});
		
		inputPanel.add(sendButton, BorderLayout.EAST);
		
		chatPanel.add(inputPanel, BorderLayout.SOUTH);
		chatPanel.add(messagePanel, BorderLayout.CENTER);
	}
	
	private JPanel createChatMessageComponent(String message)
	{
		JPanel chatMessage = new JPanel();
		chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));
		chatMessage.setBorder(new EmptyBorder(10, 20, 10, 20));
		
		JLabel usernameLabel = new JLabel("User1 :");
		usernameLabel.setFont(ResourceHandler.getFont("Roboto-Medium.ttf", 18f));
		usernameLabel.setForeground(Color.white);
		chatMessage.add(usernameLabel);
		
		JLabel messageLabel = new JLabel("<html>" + message.replaceAll("\n", "<br>") + "</html>");
		messageLabel.setFont(ResourceHandler.getFont("Roboto-Medium.ttf", 18f));
		messageLabel.setForeground(Color.white);
		chatMessage.add(messageLabel);
		chatMessage.setBackground(transpentColor);
		
		return chatMessage;
	}
}
