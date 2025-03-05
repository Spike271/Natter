package com.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

public abstract class CustomComponent extends JFrame implements Theme
{
	private static final long serialVersionUID = 1L;
	
	private int xMouse, yMouse;
	protected final String mode = toggle ? "dark_mode" : "light_mode";
	public JPanel contentPane;
	private JPanel titleBar;
	private String title = "title";
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
		// Set frame properties
		this.setUndecorated(true); // Removes the default title bar
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a custom title bar panel
		titleBar = createTitleBar();
		this.add(titleBar, BorderLayout.NORTH);
		
		// Main content area
		contentPane = createContentPane();
		this.add(contentPane, BorderLayout.CENTER);
		
		addcloseOperation();
	}
	
	private JPanel createTitleBar()
	{
		JPanel titleBar = new JPanel();
		titleBar.setBackground(Color.decode(ResourceHandler.getSettings(mode, "titleBarColor")));
		titleBar.setLayout(new BorderLayout());
		
		// Add icon on the tray
		setIconImage(new ImageIcon(getClass().getResource("../../res/icons/logo32_32.png")).getImage());
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
		JLabel iconLabel = new JLabel();
		iconLabel.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 0));
		iconLabel.setPreferredSize(new Dimension(45, 38));
		
		BufferedImage originalImage = null;
		try
		{
			originalImage = ImageIO.read(new File(getClass().getResource("../../res/icons/logo32_32.png").getPath()));
		}
		catch (IOException e)
		{
			return null;
		}
		
		Image scaledImage = originalImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		iconLabel.setIcon(new ImageIcon(scaledImage));
		return iconLabel;
	}
	
	private JLabel createTitleLabel()
	{
		JLabel titleLabel = new JLabel(title);
		titleLabel.setForeground(Color.decode(ResourceHandler.getSettings(mode, "titleTextColor")));
		titleLabel.setFont(ResourceHandler.getFont("Roboto-Medium.ttf", 18f));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		return titleLabel;
	}
	
	private JPanel createTitlePanel(JLabel titleLabel)
	{
		JPanel titlePanel = new JPanel(new MigLayout("fill, insets 0"));
		titlePanel.setOpaque(false);
		titlePanel.add(titleLabel, "align center, grow");
		return titlePanel;
	}
	
	private JPanel createContentPane()
	{
		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.decode(ResourceHandler.getSettings(mode, "frameBGColor")));
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
		
		// Close button
		closeButton = createCloseButton();
		buttonPanel.add(closeButton);
		
		return buttonPanel;
	}
	
	private ModernButton createMinimizeButton()
	{
		ModernButton minimizeButton = new ModernButton("_",
				Color.decode(ResourceHandler.getSettings(mode, "minbtnBGColor")),
				Color.decode(ResourceHandler.getSettings(mode, "minbtnHoverColor")));
		
		minimizeButton.setForeground(Color.decode(ResourceHandler.getSettings(mode, "minbtnColor")));
		minimizeButton.setFocusPainted(false);
		minimizeButton.addActionListener(e -> setState(CustomComponent.ICONIFIED)); // Minimize the window
		return minimizeButton;
	}
	
	private ModernButton createCloseButton()
	{
		ModernButton closeButton = new ModernButton("X",
				Color.decode(ResourceHandler.getSettings(mode, "closebtnBGColor")),
				Color.decode(ResourceHandler.getSettings(mode, "closebtnHoverColor")));
		
		closeButton.setForeground(Color.decode(ResourceHandler.getSettings(mode, "closebtnColor")));
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
	
	protected void addcloseOperation()
	{
		closeButton.addActionListener(e -> dispose());
	}
	
	abstract void addThemeButton(JPanel buttonPanel);
}