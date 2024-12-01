package com.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public abstract class CustomComponent extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static final boolean toggle = ResourceHandler.isDarkModeOn();
	private int xMouse, yMouse;
	private String mode = toggle ? "dark_mode" : "light_mode";
	public JPanel contentPane;
	private String title = "title";
	protected JPanel buttonPanel;
	protected ModernButton closeButton;
	
	public CustomComponent()
	{
		initialize();
	}
	
	public CustomComponent(String title)
	{
		this.title = title;
		initialize();
	}
	
	private void initialize()
	{
		// Set the frame properties
		this.setUndecorated(true); // Removes the default title bar
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a custom title bar panel
		JPanel titleBar = new JPanel();
		titleBar.setBackground(Color.decode(ResourceHandler.getSettings(mode, "titleBarColor")));
		titleBar.setLayout(new BorderLayout());
		
		// Add icon on the left side
		ImageIcon appIcon = new ImageIcon(ResourceHandler.getSettings(mode, "iconPath"));
		this.setIconImage(appIcon.getImage());
		
		// add icon and give some padding from left side
		JLabel iconLabel = new JLabel(appIcon);
		iconLabel.setPreferredSize(new Dimension(45, 35));
		iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
		titleBar.add(iconLabel, BorderLayout.WEST);
		
		// Title label
		JLabel titleLabel = new JLabel(title);
		titleLabel.setForeground(Color.decode(ResourceHandler.getSettings(mode, "titleTextColor")));
		titleLabel.setFont(ResourceHandler.getFont("Roboto-Medium.ttf", 18f));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Center title vertically and horizontally
		JPanel titlePanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for centering
		titlePanel.setOpaque(false); // Make it transparent
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; // Column = 0
		gbc.gridy = 0; // Row = 0
		gbc.anchor = GridBagConstraints.CENTER; // Center the component
		titlePanel.add(titleLabel, gbc);
		
		titleBar.add(titlePanel, BorderLayout.CENTER);
		
		// Minimize button
		ModernButton minimizeButton = new ModernButton("_",
				Color.decode(ResourceHandler.getSettings(mode, "minbtnBGColor")),
				Color.decode(ResourceHandler.getSettings(mode, "minbtnHoverColor")));
		
		minimizeButton.setForeground(Color.decode(ResourceHandler.getSettings(mode, "minbtnColor")));
		minimizeButton.setFocusPainted(false);
		minimizeButton.addActionListener(e -> setState(CustomComponent.ICONIFIED)); // Minimize the window
		
		// Close button
		closeButton = new ModernButton("X", Color.decode(ResourceHandler.getSettings(mode, "closebtnBGColor")),
				Color.decode(ResourceHandler.getSettings(mode, "closebtnHoverColor")));
		
		closeButton.setForeground(Color.decode(ResourceHandler.getSettings(mode, "closebtnColor")));
		
		// Add close button to the title bar
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setOpaque(false); // Make it transparent
		
		addThemeButton();
		buttonPanel.add(minimizeButton);
		buttonPanel.add(closeButton);
		titleBar.add(buttonPanel, BorderLayout.EAST);
		
		// Add mouse listeners for dragging
		titleBar.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				xMouse = e.getX();
				yMouse = e.getY();
			}
		});
		
		titleBar.addMouseMotionListener(new MouseAdapter() {
			
			@Override
			public void mouseDragged(MouseEvent e)
			{
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - xMouse, y - yMouse);
			}
		});
		
		// Add components to the frame
		this.add(titleBar, BorderLayout.NORTH);
		
		// Main content area
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode(ResourceHandler.getSettings(mode, "frameBGColor")));
		contentPane.setLayout(null);
		this.add(contentPane, BorderLayout.CENTER);
		
		closeOperation();
	}
	
	abstract void addThemeButton();
	
	protected void closeOperation()
	{
		closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
	}
}