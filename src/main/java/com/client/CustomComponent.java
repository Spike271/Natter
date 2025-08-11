package com.client;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public abstract class CustomComponent extends JFrame implements Theme
{
	private int xMouse, yMouse;
	public JPanel contentPane;
    private final String title;
	protected ModernButton closeButton;
	
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
		this.setResizable(false);
		
		// Create a custom title bar panel
        JPanel titleBar = createTitleBar();
		this.add(titleBar, BorderLayout.NORTH);
		
		// Main content area
		contentPane = createContentPane();
		this.add(contentPane, BorderLayout.CENTER);
		
		addCloseOperation();
	}
	
	private JPanel createTitleBar()
	{
		JPanel titleBar = new JPanel();
		titleBar.setBackground(ComponentsColor.titleBarColor);
		titleBar.setLayout(new BorderLayout());
		
		// Add icon on the tray
		setIconImage(new ImageIcon(Application.jarFilePath + "res/icons/logo32_32.png").getImage());
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
		iconLabel.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 0));
		iconLabel.setIcon(new FlatSVGIcon(new File(Application.jarFilePath + "res/icons/logo.svg"))
				 .derive(32, 32));
		return iconLabel;
	}
	
	private JLabel createTitleLabel()
	{
		JLabel titleLabel = new JLabel(title);
		titleLabel.setForeground(ComponentsColor.titleTextColor);
		titleLabel.setFont(ResourceHandler.getFont("Roboto-Medium.ttf", 18f));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		return titleLabel;
	}
	
	private JPanel createTitlePanel(JLabel titleLabel)
	{
		JPanel titlePanel = new JPanel(new MigLayout("fill, insets 0"));
		titlePanel.setOpaque(false);
		titlePanel.add(titleLabel, "gapx 80, grow");
		return titlePanel;
	}
	
	private JPanel createContentPane()
	{
		JPanel contentPane = new JPanel();
		contentPane.setBackground(ComponentsColor.frameBGColor);
		contentPane.setLayout(new MigLayout("al center, insets 0", "[fill, 65%]"));
		return contentPane;
	}
	
	private JPanel createButtonPanel()
	{
		JPanel buttonPanel = new JPanel(new MigLayout("right", "[]"));
		buttonPanel.setOpaque(false); // Make it transparent
		
		addThemeButton(buttonPanel);
		
		// Minimize button
		ModernButton minimizeButton = createMinimizeButton();
		buttonPanel.add(minimizeButton, "w 40!");
		
		// Close button
		closeButton = createCloseButton();
		buttonPanel.add(closeButton, "w 40!");
		
		return buttonPanel;
	}
	
	private ModernButton createMinimizeButton()
	{
		ModernButton minimizeButton = new ModernButton("_", ComponentsColor.minbtnBGColor, ComponentsColor.titleBarColor,
				ComponentsColor.minbtnHoverColor);
		
		minimizeButton.setForeground(ComponentsColor.minbtnColor);
		minimizeButton.setFocusPainted(false);
		minimizeButton.addActionListener(_ -> setState(CustomComponent.ICONIFIED)); // Minimize the window
		return minimizeButton;
	}
	
	private ModernButton createCloseButton()
	{
		ModernButton closeButton = new ModernButton("X", ComponentsColor.closebtnBGColor, ComponentsColor.titleBarColor,
				ComponentsColor.closebtnHoverColor);
		
		closeButton.setForeground(ComponentsColor.closebtnColor);
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
	
	protected void addCloseOperation()
	{
		closeButton.addActionListener(_ -> dispose());
	}
	
	abstract void addThemeButton(JPanel buttonPanel);
}