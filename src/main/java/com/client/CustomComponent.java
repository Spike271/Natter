package com.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
	protected final String mode = toggle ? "dark_mode" : "light_mode";
	private ModernButton maximizeButton;
	public JPanel contentPane;
	private String title = "title";
	// private JPanel buttonPanel;
	protected ModernButton closeButton;
	
	// Track if the window is maximized
	private boolean isMaximized = false;
	
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
		// Set frame properties
		this.setUndecorated(true); // Removes the default title bar
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a custom title bar panel
		JPanel titleBar = createTitleBar();
		
		// Add components to the frame
		this.add(titleBar, BorderLayout.NORTH);
		
		// Main content area
		contentPane = createContentPane();
		this.add(contentPane, BorderLayout.CENTER);
		
		closeOperation();
	}
	
	private JPanel createTitleBar()
	{
		JPanel titleBar = new JPanel();
		titleBar.setBackground(Color.decode(ResourceHandler.getColor(mode, "titleBarColor")));
		titleBar.setLayout(new BorderLayout());
		
		// Add icon on the left side
		setIconImage(new ImageIcon(ResourceHandler.getColor(mode, "iconPath")).getImage());
		JLabel iconLabel = createIconLabel();
		titleBar.add(iconLabel, BorderLayout.WEST);
		
		// Title label
		JLabel titleLabel = createTitleLabel();
		JPanel titlePanel = createTitlePanel(titleLabel);
		titleBar.add(titlePanel, BorderLayout.CENTER);
		
		// Button panel
		JPanel buttonPanel = createButtonPanel();
		titleBar.add(buttonPanel, BorderLayout.EAST);
		
		// Add mouse listeners for dragging
		addMouseListeners(titleBar);
		
		return titleBar;
	}
	
	private JLabel createIconLabel()
	{
		ImageIcon appIcon = new ImageIcon(ResourceHandler.getColor(mode, "iconPath"));
		JLabel iconLabel = new JLabel(appIcon);
		iconLabel.setPreferredSize(new Dimension(45, 35));
		iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
		return iconLabel;
	}
	
	private JLabel createTitleLabel()
	{
		JLabel titleLabel = new JLabel(title);
		titleLabel.setForeground(Color.decode(ResourceHandler.getColor(mode, "titleTextColor")));
		titleLabel.setFont(ResourceHandler.getFont("Roboto-Medium.ttf", 18f));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		return titleLabel;
	}
	
	private JPanel createTitlePanel(JLabel titleLabel)
	{
		JPanel titlePanel = new JPanel(new GridBagLayout());
		titlePanel.setOpaque(false); // Make it transparent
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; // Column = 0
		gbc.gridy = 0; // Row = 0
		gbc.anchor = GridBagConstraints.CENTER; // Center the component
		titlePanel.add(titleLabel, gbc);
		return titlePanel;
	}
	
	private JPanel createContentPane()
	{
		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.decode(ResourceHandler.getColor(mode, "frameBGColor")));
		contentPane.setLayout(null);
		return contentPane;
	}
	
	private JPanel createButtonPanel()
	{
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setOpaque(false); // Make it transparent
		
		addThemeButton(buttonPanel);
		
		// Minimize button
		ModernButton minimizeButton = createMinimizeButton();
		buttonPanel.add(minimizeButton);
		
		// Maximize button
		maximizeBtn();
		maxBtnAdd(buttonPanel);
		
		// Close button
		closeButton = createCloseButton();
		buttonPanel.add(closeButton);
		
		return buttonPanel;
	}
	
	private ModernButton createMinimizeButton()
	{
		ModernButton minimizeButton = new ModernButton("_",
				Color.decode(ResourceHandler.getColor(mode, "minbtnBGColor")),
				Color.decode(ResourceHandler.getColor(mode, "minbtnHoverColor")));
		
		minimizeButton.setForeground(Color.decode(ResourceHandler.getColor(mode, "minbtnColor")));
		minimizeButton.setFocusPainted(false);
		minimizeButton.addActionListener(e -> setState(CustomComponent.ICONIFIED)); // Minimize the window
		return minimizeButton;
	}
	
	private ModernButton createCloseButton()
	{
		ModernButton closeButton = new ModernButton("X",
				Color.decode(ResourceHandler.getColor(mode, "closebtnBGColor")),
				Color.decode(ResourceHandler.getColor(mode, "closebtnHoverColor")));
		
		closeButton.setForeground(Color.decode(ResourceHandler.getColor(mode, "closebtnColor")));
		return closeButton;
	}
	
	private void addMouseListeners(JPanel titleBar)
	{
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
	}
	
	protected void closeOperation()
	{
		closeButton.addActionListener(e -> dispose());
	}
	
	protected void maximizeBtn()
	{
		maximizeButton = new ModernButton("◻", Color.decode(ResourceHandler.getColor(mode, "minbtnBGColor")),
				Color.decode(ResourceHandler.getColor(mode, "minbtnHoverColor")));
		
		maximizeButton.setForeground(Color.decode(ResourceHandler.getColor(mode, "minbtnColor")));
		maximizeButton.setFocusPainted(false);
		maximizeButton.addActionListener(e -> {
			if (isMaximized)
			{
				this.setExtendedState(JFrame.NORMAL);
				isMaximized = false;
			}
			else
			{
				this.setExtendedState(JFrame.MAXIMIZED_BOTH);
				isMaximized = true;
			}
		});
	}
	
	protected void maxBtnAdd(JPanel buttonPanel)
	{
		buttonPanel.add(maximizeButton);
	}
	
	abstract void addThemeButton(JPanel buttonPanel);
}