package com.client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import net.miginfocom.swing.MigLayout;

public class SettingPanel extends JFrame implements Theme, ActionListener
{
	private static final long serialVersionUID = 1L;
	private JPanel apperancePanel;
	private JPanel profilePanel;
	private JPanel placeHolderPanel;
	private JPanel menuPanel;
	private JPanel securityPanel;
	private JButton changeButton, removeButton;
	private String USERNAME;
	private JLabel profilePic;
	private JPasswordField passwordField = null;
	
	public SettingPanel()
	{
		init();
		this.setIconImage(new ImageIcon(getClass().getResource("../../res/icons/logo32_32.png")).getImage());
		this.setTitle("Settings");
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				new Thread(() -> {
					dispose();
					NatterMain.settingPanel = new SettingPanel();
				}).start();
			}
		});
		this.setSize(1000, 700);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	private void init()
	{
		USERNAME = ResourceHandler.readPropertiesFile("username");
		Thread t1 = new Thread(() -> profilePanel = createProfilePanel());
		t1.start();
		
		this.setLayout(new MigLayout("fill, insets 20", "[center, fill]"));
		
		apperancePanel = createApperancePanel();
		placeHolderPanel = createPlaceHolderPanel();
		securityPanel = createSecurityPanel();
		
		menuPanel = createMenuPanel();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, placeHolderPanel);
		splitPane.putClientProperty(FlatClientProperties.STYLE, "style: plain;");
		
		this.add(splitPane, "h 500");
		
		try
		{
			t1.join();
		}
		catch (Exception e)
		{}
	}
	
	private JPanel createPlaceHolderPanel()
	{
		JPanel placeHolderPanel = new JPanel();
		placeHolderPanel.setLayout(new MigLayout("fillx", "[fill]", "[]"));
		placeHolderPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20;"
				+ "[light]background:darken(@background, 5%);" + "[dark]background:lighten(@background, 5%);");
		placeHolderPanel.setMinimumSize(new Dimension(600, getHeight()));
		
		return placeHolderPanel;
	}
	
	private JPanel createMenuPanel()
	{
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new MigLayout("wrap, gapy 10", "[230:400]"));
		menuPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20;"
				+ "[light]background:darken(@background, 5%);" + "[dark]background:lighten(@background, 5%);");
		
		JButton button1 = new JButton("Profile", new FlatSVGIcon(getClass().getResource("../../res/icons/user.svg"))
				.derive(18, 18).setColorFilter(FlatLaf.isLafDark() ? new ColorFilter(color -> Color.WHITE) : null));
		
		button1.setIconTextGap(15);
		button1.setHorizontalAlignment(SwingConstants.LEFT);
		button1.putClientProperty(FlatClientProperties.STYLE, "focusWidth: 0;" + "font:bold +3");
		button1.addActionListener(e -> {
			placeHolderPanel.removeAll();
			placeHolderPanel.add(profilePanel);
			repaint();
			revalidate();
		});
		
		JButton button2 = new JButton("Appearance",
				new FlatSVGIcon(getClass().getResource("../../res/icons/appearance.svg")).derive(18, 18)
						.setColorFilter(FlatLaf.isLafDark() ? new ColorFilter(color -> Color.WHITE) : null));
		button2.setIconTextGap(13);
		button2.setHorizontalAlignment(SwingConstants.LEFT);
		button2.putClientProperty(FlatClientProperties.STYLE, "focusWidth: 0;" + "font:bold +3");
		button2.addActionListener(e -> {
			placeHolderPanel.removeAll();
			placeHolderPanel.add(apperancePanel);
			repaint();
			revalidate();
		});
		
		JButton button3 = new JButton("Security",
				new FlatSVGIcon(getClass().getResource("../../res/icons/security.svg")).derive(18, 18)
						.setColorFilter(FlatLaf.isLafDark() ? new ColorFilter(color -> Color.WHITE) : null));
		button3.setIconTextGap(15);
		button3.setHorizontalAlignment(SwingConstants.LEFT);
		button3.putClientProperty(FlatClientProperties.STYLE, "focusWidth: 0;" + "font:bold +3");
		button3.addActionListener(e -> {
			placeHolderPanel.removeAll();
			placeHolderPanel.add(securityPanel);
			repaint();
			revalidate();
		});
		
		menuPanel.add(button1, "growx");
		menuPanel.add(button2, "growx");
		menuPanel.add(button3, "growx");
		
		return menuPanel;
	}
	
	private JPanel createApperancePanel()
	{
		JPanel apperancePanel = new JPanel(new MigLayout("wrap, fillx, insets 20 45 30 45, gapy 30", "[left][right]"));
		apperancePanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;"
				+ "[light]background:darken(@background, 5%);" + "[dark]background:lighten(@background, 5%);");
		
		JLabel settingLabel = new JLabel("Settings");
		settingLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +25;");
		apperancePanel.add(settingLabel, "span, center");
		
		JLabel themeLabel = new JLabel("Theme");
		themeLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		apperancePanel.add(themeLabel);
		
		// Create a drop down menu
		JComboBox<String> dropDown = new JComboBox<>(new String[] { "Light Mode", "Dark Mode" });
		dropDown.setSelectedIndex(toggle ? 1 : 0);
		int check = dropDown.getSelectedIndex();
		dropDown.putClientProperty(FlatClientProperties.STYLE, "font: +3;" + "arc: 1;" + "minimumWidth: 160;"
				+ "arrowType: traingle;" + "buttonStyle: none;" + "focusWidth: 0;");
		
		dropDown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String selectedItem = (String) dropDown.getSelectedItem();
				if (check != dropDown.getSelectedIndex())
				{
					changeThemes(selectedItem.startsWith("D"));
					JOptionPane.showMessageDialog(SettingPanel.this,
							"Restart the Application to take the full effect.");
					System.exit(0);
				}
			}
		});
		apperancePanel.add(dropDown);
		
		JLabel chatBackgroundImage = new JLabel("Chat Background Image");
		chatBackgroundImage.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		apperancePanel.add(chatBackgroundImage);
		
		JButton buttonBG = new JButton("Select the image File");
		buttonBG.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "arc: 1;" + "focusWidth: 0;");
		buttonBG.setEnabled(false);
		buttonBG.setToolTipText("This feature is currently unavailable.");
		apperancePanel.add(buttonBG, "w 160");
		
		JLabel gradientColorStart = new JLabel("Gradient Start Color");
		gradientColorStart.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		apperancePanel.add(gradientColorStart);
		
		JButton colorButton1 = new JButton("Pick a Color");
		colorButton1.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "arc: 1;" + "focusWidth: 0;");
		colorButton1.setToolTipText("Changes the chat background color");
		colorButton1.addActionListener(new ActionListener() {
			
			String mode = toggle ? "dark_mode" : "light_mode";
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Color selectedColor = JColorChooser.showDialog(SettingPanel.this, "Choose a chat background color",
						Color.decode(ResourceHandler.getColorFileSettings(mode, "Color1")));
				
				if (selectedColor != null)
				{
					ResourceHandler.changeColorFileSettings(mode + ".Color1", convertColorToHex(selectedColor));
				}
			}
		});
		apperancePanel.add(colorButton1, "w 160");
		
		JLabel gradientColorEnd = new JLabel("Gradient End Color");
		gradientColorEnd.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		apperancePanel.add(gradientColorEnd);
		
		JButton colorButton2 = new JButton("Pick a Color");
		colorButton2.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "arc: 1;" + "focusWidth: 0;");
		colorButton2.setToolTipText("Changes the chat background color\n"
				+ "Pick the same color if you don't want the gradient background");
		colorButton2.addActionListener(new ActionListener() {
			
			String mode = toggle ? "dark_mode" : "light_mode";
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Color selectedColor = JColorChooser.showDialog(SettingPanel.this, "Choose a chat background color",
						Color.decode(ResourceHandler.getColorFileSettings(mode, "Color2")));
				
				if (selectedColor != null)
				{
					ResourceHandler.changeColorFileSettings(mode + ".Color2", convertColorToHex(selectedColor));
				}
			}
		});
		apperancePanel.add(colorButton2, "w 160");
		
		return apperancePanel;
	}
	
	private JPanel createProfilePanel()
	{
		JPanel profilePanel = new JPanel(new MigLayout("wrap, fillx, insets 20 45 30 45", "[]"));
		profilePanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;"
				+ "[light]background:darken(@background, 5%);" + "[dark]background:lighten(@background, 5%);");
		
		BufferedImage originalImage = null;
		
		try
		{
			originalImage = loadImageWithoutExtension(USERNAME);
		}
		catch (Exception e)
		{}
		
		if (originalImage == null)
			originalImage = loadImageWithoutExtension("null");
		
		try
		{
			profilePic = new JLabel(new ImageIcon(getScaledImage(originalImage)));
		}
		catch (Exception e)
		{
			System.err.println(getPathString());
		}
		profilePanel.add(profilePic, "center");
		
		JLabel username = new JLabel(USERNAME);
		username.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		profilePanel.add(username, "center");
		
		changeButton = new JButton("Change Image");
		changeButton.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "focusWidth: 0;");
		changeButton.addActionListener(this);
		profilePanel.add(changeButton);
		
		removeButton = new JButton("Remove Image");
		removeButton.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "focusWidth: 0;");
		removeButton.addActionListener(this);
		profilePanel.add(removeButton, "gapy 5");
		
		return profilePanel;
	}
	
	private JPanel createSecurityPanel()
	{
		JPanel securityPanel = new JPanel(new MigLayout("wrap, fillx, insets 20 45 30 45, gapy 30", "[left][right]"));
		securityPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;"
				+ "[light]background:darken(@background, 5%);" + "[dark]background:lighten(@background, 5%);");
		
		JLabel settingLabel = new JLabel("Privacy");
		settingLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +25;");
		securityPanel.add(settingLabel, "span, center");
		
		JLabel themeLabel = new JLabel("Application lock");
		themeLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +6;");
		securityPanel.add(themeLabel);
		
		JCheckBox checkBox = new JCheckBox("Enable");
		checkBox.putClientProperty(FlatClientProperties.STYLE, "font:bold +6;" + "icon.focusWidth: 0;");
		checkBox.setSelected(ResourceHandler.decode(ResourceHandler.readPropertiesFile("password")).startsWith("true"));
		securityPanel.add(checkBox);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +6;");
		securityPanel.add(passwordLabel);
		
		passwordField = new JPasswordField();
		passwordField.putClientProperty(FlatClientProperties.STYLE,
				"font:bold +5;" + "showRevealButton: true;" + "focusWidth: 0;" + "showClearButton: true;");
		passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
		passwordField.setText(ResourceHandler.decode(ResourceHandler.readPropertiesFile("password")).substring(4));
		securityPanel.add(passwordField, "w 150, h 30");
		
		JButton button = new JButton("Save");
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.putClientProperty(FlatClientProperties.STYLE, "font:bold +6;" + "focusWidth: 0;"
				+ "[dark]background : darken(@accentColor,5%);" + "[light]background : lighten(@accentColor,5%)");
		button.addActionListener(e -> setPassword(checkBox));
		securityPanel.add(button, "span, right");
		
		return securityPanel;
	}
	
	private void changeThemes(boolean dark)
	{
		if (FlatLaf.isLafDark() != dark)
		{
			if (!dark)
			{
				EventQueue.invokeLater(() -> {
					FlatMacLightLaf.setup();
					FlatLaf.updateUI();
					ResourceHandler.changeColorFileSettings("ColorMode.IsDark", "false");
					ResourceHandler.changeSettings("Global.isDark", "false");
				});
			}
			else
			{
				EventQueue.invokeLater(() -> {
					FlatMacDarkLaf.setup();
					FlatLaf.updateUI();
					ResourceHandler.changeColorFileSettings("ColorMode.IsDark", "true");
					ResourceHandler.changeSettings("Global.isDark", "true");
				});
			}
		}
	}
	
	private BufferedImage loadImageWithoutExtension(String baseName)
	{
		try
		{
			String[] extensions = { "jpg", "jpeg", "png" };
			BufferedImage image = null;
			
			for (String ext : extensions)
			{
				File file = null;
				String path = getPathString() + "profile/";
				file = new File(path + baseName + "." + ext);
				if (file.exists())
				{
					image = ImageIO.read(file);
					if (image != null)
						return image;
				}
			}
		}
		catch (Exception e)
		{}
		return null;
	}
	
	private File loadFilePath(String fileName)
	{
		try
		{
			String[] extensions = { "jpg", "jpeg", "png" };
			
			for (String ext : extensions)
			{
				File file = null;
				String path = getPathString() + "profile/";
				file = new File(path + fileName + "." + ext);
				if (file.exists())
					return file;
			}
		}
		catch (Exception e)
		{}
		return null;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == changeButton)
		{
			JFileChooser chooser = new JFileChooser();
			String lastDirectory = ResourceHandler.readPropertiesFile("last_directory");
			
			if (lastDirectory != null && !lastDirectory.isBlank())
				chooser.setCurrentDirectory(new File(lastDirectory));
			
			chooser.setDialogTitle("Select the Image File");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(filter);
			int userSelection = chooser.showOpenDialog(this);
			
			if (userSelection == JFileChooser.APPROVE_OPTION)
			{
				final File file = chooser.getSelectedFile();
				String targetDirectoryPath = getPathString() + "profile/";
				
				Path sourcePath = file.toPath();
				Path targetPath = new File(targetDirectoryPath, USERNAME + getFileExtension(file.getName())).toPath();
				
				try
				{
					loadFilePath(USERNAME).delete();
				}
				catch (Exception ex)
				{}
				finally
				{
					try
					{
						Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
					}
					catch (Exception e1)
					{}
					
					ResourceHandler.writePropertiesFile("last_directory", file.getParent());
				}
				
				try (Connection conn = DriverManager.getConnection(DB.dbUrl, DB.username,
						DB.password); PreparedStatement pstmt = conn.prepareStatement(
								"UPDATE pfp SET Profile_picture = ?, Image_extension = ? WHERE Username = ?"))
				{
					
					FileInputStream fileInputStream = new FileInputStream(file);
					String fileExtension = getFileExtension(file.getName());
					byte[] imageData = new byte[fileInputStream.available()];
					fileInputStream.read(imageData);
					pstmt.setBytes(1, imageData);
					pstmt.setString(2, fileExtension);
					pstmt.setString(3, USERNAME);
					pstmt.executeUpdate();
					
					JOptionPane.showMessageDialog(this, "Profile picture sucessfully Uploaded.");
					fileInputStream.close();
					
					profilePic.setIcon(new ImageIcon(getScaledImage(loadImageWithoutExtension(USERNAME))));
					repaint();
				}
				catch (Exception e2)
				{
					JOptionPane.showMessageDialog(this, "Something went wrong.\nTry again later.");
				}
			}
		}
		
		else if (e.getSource() == removeButton)
		{
			try (Connection conn = DriverManager.getConnection(DB.dbUrl, DB.username,
					DB.password); PreparedStatement pstmt = conn.prepareStatement(
							"UPDATE pfp SET Profile_picture = NULL, Image_extension = NULL WHERE Username = ?"))
			{
				pstmt.setString(1, USERNAME);
				pstmt.executeUpdate();
				
				File file = loadFilePath(USERNAME);
				file.delete();
				
				JOptionPane.showMessageDialog(this, "Profile picture sucessfully removed.");
				profilePic.setIcon(new ImageIcon(getScaledImage(loadImageWithoutExtension("null"))));
				repaint();
			}
			catch (Exception e2)
			{
				JOptionPane.showMessageDialog(this, "Unable to delete the profile picture.");
			}
		}
	}
	
	private void setPassword(JCheckBox chk)
	{
		String password = new String(passwordField.getPassword());
		if (password != null && !password.isBlank())
		{
			if (chk.isSelected())
				password = "true" + password;
			else
				password = "fals" + password;
			
			ResourceHandler.writePropertiesFile("password", ResourceHandler.encode(password));
		}
		else
		{
			JOptionPane.showMessageDialog(SettingPanel.this, "Please enter the password!");
		}
	}
	
	private String getFileExtension(String fileName)
	{
		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex > 0)
			return fileName.substring(lastDotIndex);
		
		return "";
	}
	
	private String getPathString()
	{
		String targetDirectoryPath = getClass().getResource("SettingPanel.class").getPath();
		return targetDirectoryPath.substring(0, targetDirectoryPath.lastIndexOf("/") + 1);
	}
	
	private String convertColorToHex(Color color)
	{
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		
		String hexRed = String.format("%02X", red);
		String hexGreen = String.format("%02X", green);
		String hexBlue = String.format("%02X", blue);
		
		return "#" + hexRed + hexGreen + hexBlue;
	}
	
	private Image getScaledImage(BufferedImage originalImage)
	{
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
		return originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	}
}