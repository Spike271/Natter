package com.client;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Optional;

public class SettingPanel extends JFrame implements ActionListener
{
	private JPanel appearancePanel;
	private JPanel profilePanel;
	private JPanel placeHolderPanel;
    private JPanel securityPanel;
	private JButton changeButton, removeButton;
	private String USERNAME;
	private JLabel profilePic;
	private JPasswordField passwordField = null;
	private Thread thread;

	public SettingPanel()
	{
		init();
		this.setIconImage(new ImageIcon(Application.jarFilePath + "res/icons/logo32_32.png").getImage());
		this.setTitle("Settings");
		this.setSize(1000, 700);
		this.setResizable(false);
		this.setFocusable(true);
		this.setLocationRelativeTo(null);
	}
	
	private void init()
	{
		USERNAME = ResourceHandler.readPropertiesFile("username").orElseThrow();
		Thread t1 = Thread.ofVirtual().start((() -> profilePanel = createProfilePanel()));
		
		this.setLayout(new MigLayout("fill, insets 20", "[center, fill]"));
		
		appearancePanel = createAppearancePanel();
		placeHolderPanel = createPlaceHolderPanel();
		securityPanel = createSecurityPanel();

        JPanel menuPanel = createMenuPanel();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, placeHolderPanel);
		splitPane.putClientProperty(FlatClientProperties.STYLE, "style: plain;");
		
		this.add(splitPane, "h 500");

		try
		{
			t1.join();
			thread.start();
		}
		catch (Exception _) {}
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
		
		JButton button1 = new JButton("Profile", new FlatSVGIcon(new File(Application.jarFilePath + "res/icons/user.svg"))
				.derive(18, 18).setColorFilter(FlatLaf.isLafDark() ? new ColorFilter(_ -> Color.WHITE) : null));
		
		button1.setIconTextGap(15);
		button1.setHorizontalAlignment(SwingConstants.LEFT);
		button1.putClientProperty(FlatClientProperties.STYLE, "focusWidth: 0;" + "font:bold +3");
		button1.addActionListener(_ -> {
			placeHolderPanel.removeAll();
			placeHolderPanel.add(profilePanel);
			repaint();
			revalidate();
		});
		
		JButton button2 = new JButton("Appearance",
				new FlatSVGIcon(new File(Application.jarFilePath + "res/icons/appearance.svg")).derive(18, 18)
						.setColorFilter(FlatLaf.isLafDark() ? new ColorFilter(_ -> Color.WHITE) : null));
		button2.setIconTextGap(13);
		button2.setHorizontalAlignment(SwingConstants.LEFT);
		button2.putClientProperty(FlatClientProperties.STYLE, "focusWidth: 0;" + "font:bold +3");
		button2.addActionListener(_ -> {
			placeHolderPanel.removeAll();
			placeHolderPanel.add(appearancePanel);
			repaint();
			revalidate();
		});
		
		JButton button3 = new JButton("Security",
				new FlatSVGIcon(new File(Application.jarFilePath + "res/icons/security.svg")).derive(18, 18)
						.setColorFilter(FlatLaf.isLafDark() ? new ColorFilter(_ -> Color.WHITE) : null));
		button3.setIconTextGap(15);
		button3.setHorizontalAlignment(SwingConstants.LEFT);
		button3.putClientProperty(FlatClientProperties.STYLE, "focusWidth: 0;" + "font:bold +3");
		button3.addActionListener(_ -> {
			placeHolderPanel.removeAll();
			placeHolderPanel.add(securityPanel);
			repaint();
			revalidate();
		});
		
		menuPanel.add(button1, "growx");
		menuPanel.add(button2, "growx");
		menuPanel.add(button3, "growx");

		thread = Thread.ofVirtual().unstarted(button1::doClick);
		
		return menuPanel;
	}
	
	private JPanel createAppearancePanel()
	{
		JPanel appearancePanel = new JPanel(new MigLayout("wrap, fillx, insets 20 45 30 45, gapy 30", "[left][right]"));
		appearancePanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;"
				+ "[light]background:darken(@background, 5%);" + "[dark]background:lighten(@background, 5%);");
		
		JLabel settingLabel = new JLabel("Settings");
		settingLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +25;");
		appearancePanel.add(settingLabel, "span, center");
		
		JLabel themeLabel = new JLabel("Application Theme");
		themeLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		appearancePanel.add(themeLabel);
		
		// Create a drop-down menu
		JComboBox<String> dropDown = new JComboBox<>(new String[] { "Light Mode", "Dark Mode" });
		dropDown.setSelectedIndex(Theme.isDarkModeOn ? 1 : 0);
		int check = dropDown.getSelectedIndex();
		dropDown.putClientProperty(FlatClientProperties.STYLE, "font: +3;" + "arc: 1;" + "minimumWidth: 160;"
				+ "arrowType: triangle;" + "buttonStyle: none;" + "focusWidth: 0;");
		
		dropDown.addActionListener(_ -> {
            String selectedItem = (String) dropDown.getSelectedItem();

            if (selectedItem != null && check != dropDown.getSelectedIndex())
			{
				changeThemes(selectedItem.startsWith("D"));
				JOptionPane.showMessageDialog(SettingPanel.this,
						"Restart the Application to take the full effect.");
				MessagesSendAndReceive.stopMessageListening();
				System.exit(0);
            }
        });
		appearancePanel.add(dropDown);
		
		JLabel chatBackgroundImage = new JLabel("Chat Background Image");
		chatBackgroundImage.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		appearancePanel.add(chatBackgroundImage);
		
		JButton buttonBG = new JButton("Select the image File");
		buttonBG.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "arc: 1;" + "focusWidth: 0;");
		buttonBG.setEnabled(false);
		buttonBG.setToolTipText("This feature is currently unavailable.");
		appearancePanel.add(buttonBG, "w 160");
		
		JLabel gradientColorStart = new JLabel("Gradient Start Color");
		gradientColorStart.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		appearancePanel.add(gradientColorStart);
		
		JButton colorButton1 = new JButton("Pick a Color");
		colorButton1.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "arc: 1;" + "focusWidth: 0;");
		colorButton1.setToolTipText("Changes the chat background color");
		colorButton1.addActionListener(new ActionListener() {
			
			final String mode = Theme.isDarkModeOn ? "dark_mode" : "light_mode";
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Color selectedColor = JColorChooser.showDialog(SettingPanel.this, "Choose a chat background color",
						Color.decode(Objects.requireNonNull(ResourceHandler.getColorFileSettings(mode, "Color1"))));
				
				if (selectedColor != null)
				{
					ResourceHandler.changeColorFileSettings(mode + ".Color1", convertColorToHex(selectedColor));
				}
			}
		});
		appearancePanel.add(colorButton1, "w 160");
		
		JLabel gradientColorEnd = new JLabel("Gradient End Color");
		gradientColorEnd.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		appearancePanel.add(gradientColorEnd);
		
		JButton colorButton2 = new JButton("Pick a Color");
		colorButton2.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "arc: 1;" + "focusWidth: 0;");
		colorButton2.setToolTipText("Changes the chat background color\n"
				+ "Pick the same color if you don't want the gradient background");
		colorButton2.addActionListener(new ActionListener() {
			
			final String mode = Theme.isDarkModeOn ? "dark_mode" : "light_mode";
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Color selectedColor = JColorChooser.showDialog(SettingPanel.this, "Choose a chat background color",
						Color.decode(Objects.requireNonNull(ResourceHandler.getColorFileSettings(mode, "Color2"))));
				
				if (selectedColor != null)
				{
					ResourceHandler.changeColorFileSettings(mode + ".Color2", convertColorToHex(selectedColor));
				}
			}
		});
		appearancePanel.add(colorButton2, "w 160");
		
		return appearancePanel;
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
		catch (Exception _) {}

		if (originalImage == null)
			originalImage = loadImageWithoutExtension("null");
		
		try
		{
            assert originalImage != null;
            profilePic = new JLabel(new ImageIcon(getScaledImage(originalImage)));
		}
		catch (Exception _) {}

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
		checkBox.setSelected(ResourceHandler.decode(ResourceHandler.readPropertiesFile("password").orElseThrow())
				.startsWith("true"));
		securityPanel.add(checkBox);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +6;");
		securityPanel.add(passwordLabel);
		
		passwordField = new JPasswordField();
		passwordField.putClientProperty(FlatClientProperties.STYLE,
				"font:bold +5;" + "showRevealButton: true;" + "focusWidth: 0;" + "showClearButton: true;");
		passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
		passwordField.setText(ResourceHandler.decode(ResourceHandler.readPropertiesFile("password")
				.orElseThrow()).substring(4));
		securityPanel.add(passwordField, "w 150, h 30");
		
		JButton button = new JButton("Save");
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.putClientProperty(FlatClientProperties.STYLE, "font:bold +6;" + "focusWidth: 0;"
				+ "[dark]background : darken(@accentColor,5%);" + "[light]background : lighten(@accentColor,5%)");
		button.addActionListener(_ -> setPassword(checkBox));
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
			BufferedImage image;
			
			for (String ext : extensions)
			{
				File file;
				String path = getPathString() + "profile/";
				file = new File(path + baseName + "." + ext);
				if (file.exists())
				{
					image = ImageIO.read(file);
					if (image != null) return image;
				}
			}
		}
		catch (Exception _) {}

		return null;
	}
	
	private Optional<File> loadFilePath(String fileName)
	{
		try
		{
			String[] extensions = { "jpg", "jpeg", "png" };
			
			for (String ext : extensions)
			{
				File file;
				String path = getPathString() + "profile/";
				file = new File(path + fileName + "." + ext);

				if (file.exists()) return Optional.of(file);
			}
		}
		catch (Exception _) {}

		return Optional.empty();
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == changeButton)
		{
			JFileChooser chooser = new JFileChooser();
			String lastDirectory = ResourceHandler.readPropertiesFile("last_directory").orElse("");
			
			if (!lastDirectory.isBlank()) chooser.setCurrentDirectory(new File(lastDirectory));
			
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
					if (loadFilePath(USERNAME).orElseThrow().delete())
					{
						Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
						ResourceHandler.writePropertiesFile("last_directory", file.getParent());
					}
				}
				catch (Exception _) {}
				
				try (Connection conn = DriverManager.getConnection(DB.dbUrl, DB.username,DB.password);
					 PreparedStatement pstmt = conn.prepareStatement("UPDATE pfp SET Profile_picture = ?, Image_extension = ? WHERE Username = ?"))
				{
					FileInputStream fileInputStream = new FileInputStream(file);
					String fileExtension = getFileExtension(file.getName());
					byte[] imageData = new byte[fileInputStream.available()];
					pstmt.setBytes(1, imageData);
					pstmt.setString(2, fileExtension);
					pstmt.setString(3, USERNAME);
					pstmt.executeUpdate();
					
					JOptionPane.showMessageDialog(this, "Profile picture successfully Uploaded.");
					fileInputStream.close();
					
					profilePic.setIcon(new ImageIcon(getScaledImage(Objects.requireNonNull(loadImageWithoutExtension(USERNAME)))));
					repaint();
				}
				catch (Exception e2)
				{
					JOptionPane.showMessageDialog(this, "Something went wrong.\nPlease, Try again later.");
				}
			}
		}
		
		else if (e.getSource() == removeButton)
		{
			try (Connection conn = DriverManager.getConnection(DB.dbUrl, DB.username, DB.password);
				 PreparedStatement pstmt = conn.prepareStatement(
							"UPDATE pfp SET Profile_picture = NULL, Image_extension = NULL WHERE Username = ?"))
			{
				pstmt.setString(1, USERNAME);
				pstmt.executeUpdate();
				
				File file = loadFilePath(USERNAME).orElseThrow();

                if (file.delete())
				{
					JOptionPane.showMessageDialog(this, "Profile picture successfully removed.");
					profilePic.setIcon(new ImageIcon(getScaledImage(Objects.requireNonNull(loadImageWithoutExtension("null")))));
                }
				else
				{
					JOptionPane.showMessageDialog(this, "Unable to delete the profile picture.\nPlease try again later.");
				}
				repaint();
			}
			catch (Exception _)
			{
				JOptionPane.showMessageDialog(this, "Unable to delete the profile picture.");
			}
		}
	}
	
	private void setPassword(JCheckBox chk)
	{
		String password = new String(passwordField.getPassword());
		if (!password.isBlank())
		{
			if (chk.isSelected()) password = "true" + password;
			else password = "fals" + password;
			
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
		return Application.jarFilePath;
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