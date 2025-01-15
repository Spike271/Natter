package com.client;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import net.miginfocom.swing.MigLayout;

public class SettingPanel extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel apperancePanel;
	private JPanel profilePanel;
	private JPanel placeHolderPanel;
	private JPanel menuPanel;
	
	public SettingPanel()
	{
		init();
		this.setTitle("Settings");
		this.setIconImage(
				ResourceHandler.loadImageIcon(ResourceHandler.getSettings("light_mode", "iconPath")).getImage());
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setSize(1000, 700);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	private void init()
	{
		this.setLayout(new MigLayout("fill, insets 20", "[center, fill]"));
		
		apperancePanel = createApperancePanel();
		profilePanel = createProfilePanel();
		placeHolderPanel = createPlaceHolderPanel();
		
		menuPanel = createMenuPanel();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, placeHolderPanel);
		splitPane.putClientProperty(FlatClientProperties.STYLE, "style:plain;");
		
		this.add(splitPane, "h 500");
	}
	
	private JPanel createPlaceHolderPanel()
	{
		JPanel placeHolderPanel = new JPanel();
		placeHolderPanel.setLayout(new MigLayout("fillx", "[fill]", "[]"));
		placeHolderPanel.putClientProperty(FlatClientProperties.STYLE,
				"arc:20;" + "[light]background:darken(@background,5%);" + "[dark]background:lighten(@background,5%);");
		placeHolderPanel.setMinimumSize(new Dimension(400, getHeight()));
		
		return placeHolderPanel;
	}
	
	private JPanel createApperancePanel()
	{
		JPanel apperancePanel = new JPanel(new MigLayout("wrap, fillx, insets 20 45 30 45, gapy 30", "[left][right]"));
		apperancePanel.putClientProperty(FlatClientProperties.STYLE,
				"arc:20;" + "[light]background:darken(@background,5%);" + "[dark]background:lighten(@background,5%);");
		
		JLabel settingLabel = new JLabel("Settings");
		settingLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +25;");
		apperancePanel.add(settingLabel, "span, center");
		
		JLabel themeLabel = new JLabel("Theme");
		themeLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		apperancePanel.add(themeLabel);
		
		// Create a dropdown menu
		JComboBox<String> dropDown = new JComboBox<>(new String[] { "Light Mode", "Dark Mode" });
		dropDown.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "arc: 1;" + "minimumWidth:150;"
				+ "arrowType:traingle;" + "buttonStyle:none;" + "focusWidth:0;");
		
		dropDown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String selectedItem = (String) dropDown.getSelectedItem();
				JOptionPane.showMessageDialog(null, "You selected: " + selectedItem);
			}
		});
		apperancePanel.add(dropDown);
		
		JLabel chatBackgroundImage = new JLabel("Chat Background Image");
		chatBackgroundImage.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		apperancePanel.add(chatBackgroundImage);
		
		JButton buttonBG = new JButton("Select the image File");
		buttonBG.putClientProperty(FlatClientProperties.STYLE,
				"font: +2;" + "arc:1;" + "focusWidth:0;" + "minimumWidth:150;");
		apperancePanel.add(buttonBG);
		
		JLabel gradientColorStart = new JLabel("Gradient Start Color");
		gradientColorStart.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		apperancePanel.add(gradientColorStart);
		
		JButton colorButton1 = new JButton("Pick a Color");
		colorButton1.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "arc:1;" + "focusWidth:0;");
		apperancePanel.add(colorButton1);
		
		JLabel gradientColorEnd = new JLabel("Gradient End Color");
		gradientColorEnd.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		apperancePanel.add(gradientColorEnd);
		
		JButton colorButton2 = new JButton("Pick a Color");
		colorButton2.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "arc:1;" + "focusWidth:0;");
		apperancePanel.add(colorButton2);
		
		return apperancePanel;
	}
	
	private JPanel createMenuPanel()
	{
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new MigLayout("wrap, gapy 10", "[230:400]"));
		menuPanel.putClientProperty(FlatClientProperties.STYLE,
				"arc:20;" + "[light]background:darken(@background,5%);" + "[dark]background:lighten(@background,5%);");
		
		JButton button1 = new JButton("Profile");
		button1.setHorizontalAlignment(SwingConstants.LEFT);
		button1.putClientProperty(FlatClientProperties.STYLE, "focusWidth:0;" + "font:bold +3");
		button1.addActionListener(e -> {
			placeHolderPanel.removeAll();
			placeHolderPanel.add(profilePanel);
			repaint();
			revalidate();
		});
		
		JButton button2 = new JButton("Appearance");
		button2.setHorizontalAlignment(SwingConstants.LEFT);
		button2.putClientProperty(FlatClientProperties.STYLE, "focusWidth:0;" + "font:bold +3");
		button2.addActionListener(e -> {
			placeHolderPanel.removeAll();
			placeHolderPanel.add(apperancePanel);
			repaint();
			revalidate();
		});
		
		menuPanel.add(button1, "growx");
		menuPanel.add(button2, "growx");
		
		return menuPanel;
	}
	
	private JPanel createProfilePanel()
	{
		JPanel profilePanel = new JPanel(new MigLayout("wrap, fillx, insets 20 45 30 45", "[]"));
		profilePanel.putClientProperty(FlatClientProperties.STYLE,
				"arc:20;" + "[light]background:darken(@background,5%);" + "[dark]background:lighten(@background,5%);");
		
		try
		{
			BufferedImage originalImage = ImageIO.read(new File(getClass().getResource("01.jpg").getPath()));
			// Desired dimensions
			int maxWidth = 400;
			int maxHeight = 350;
			
			// Get original dimensions
			int originalWidth = originalImage.getWidth();
			int originalHeight = originalImage.getHeight();
			
			// Calculate the scaling factor
			double widthRatio = (double) maxWidth / originalWidth;
			double heightRatio = (double) maxHeight / originalHeight;
			double scaleFactor = Math.min(widthRatio, heightRatio);
			
			// Calculate new dimensions
			int newWidth = (int) (originalWidth * scaleFactor);
			int newHeight = (int) (originalHeight * scaleFactor);
			
			// Scale the image
			Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			JLabel profilePic = new JLabel(new ImageIcon(scaledImage));
			
			profilePanel.add(profilePic, "center");
		}
		catch (Exception e)
		{
			JLabel profilePic = new JLabel("*No Image*");
			profilePic.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
			profilePanel.add(profilePic, "h 100, w 100, center");
		}
		
		JLabel username = new JLabel("@Username");
		username.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		profilePanel.add(username, "center");
		
		JButton changeButton = new JButton("Change Image");
		changeButton.putClientProperty(FlatClientProperties.STYLE, "font: +1;" + "focusWidth:0;");
		profilePanel.add(changeButton);
		
		JButton removeButton = new JButton("Remove Image");
		removeButton.putClientProperty(FlatClientProperties.STYLE, "font: +1;" + "focusWidth:0;");
		profilePanel.add(removeButton, "gapy 5");
		
		return profilePanel;
	}
}