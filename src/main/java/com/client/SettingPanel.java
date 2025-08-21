package com.client;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import global.ResourceHandler;
import global.Theme;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

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

public class SettingPanel extends JDialog implements ActionListener
{
	private JPanel appearancePanel;
	private JPanel profilePanel;
	private JPanel placeHolderPanel;
    private JPanel securityPanel;
	private JButton changeButton, removeButton, logOutButton;
	private String USERNAME;
	private JLabel profilePic;
	private JPasswordField passwordField = null;
	private Thread thread;
    private final JButton[] buttons = new JButton[3];

	public SettingPanel(JFrame parent)
	{
        super(parent, "Settings", true);
		init();
		this.setIconImage(new ImageIcon(Application.jarFilePath + "res/icons/logo32_32.png").getImage());
		this.setSize(1000, 700);
		this.setResizable(false);
		this.setFocusable(true);
	}
	
	private void init()
	{
		USERNAME = Application.userDetails.username();
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
		menuPanel.setLayout(new MigLayout("wrap, gapy 0", "[230:400]"));
		menuPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20;"
				+ "[light]background:darken(@background, 5%);" + "[dark]background:lighten(@background, 5%);");

		buttons[0] = createJButton("Profile", "user");
        buttons[0].setIconTextGap(15);
		buttons[0].setHorizontalAlignment(SwingConstants.LEFT);
		buttons[0].putClientProperty(FlatClientProperties.STYLE, "focusWidth: 0;" + "font:bold +3");
		buttons[0].addActionListener(_ -> {
			placeHolderPanel.removeAll();
			placeHolderPanel.add(profilePanel);
			repaint();
			revalidate();
            SwingUtilities.updateComponentTreeUI(profilePanel);
		});

		buttons[1] = createJButton("Appearance", "appearance");
        buttons[1].setIconTextGap(13);
		buttons[1].setHorizontalAlignment(SwingConstants.LEFT);
		buttons[1].putClientProperty(FlatClientProperties.STYLE, "focusWidth: 0;" + "font:bold +3");
		buttons[1].addActionListener(_ -> {
			placeHolderPanel.removeAll();
			placeHolderPanel.add(appearancePanel);
			repaint();
			revalidate();
		});
		
		buttons[2] = createJButton("Security", "security");
        buttons[2].setIconTextGap(15);
		buttons[2].setHorizontalAlignment(SwingConstants.LEFT);
		buttons[2].putClientProperty(FlatClientProperties.STYLE, "focusWidth: 0;" + "font:bold +3");
		buttons[2].addActionListener(_ -> {
			placeHolderPanel.removeAll();
			placeHolderPanel.add(securityPanel);
            repaint();
            revalidate();
            SwingUtilities.updateComponentTreeUI(securityPanel);
        });
		
		menuPanel.add(buttons[0], "growx, gapy rel 0:1:null");
        menuPanel.add(buttons[1], "growx, gapy rel 0:1:null");
		menuPanel.add(buttons[2], "growx, gapy rel 0:1:null");

		thread = Thread.ofVirtual().unstarted(() -> {
            buttons[0].doClick();
            buttons[0].requestFocus();
        });
		
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
		dropDown.setSelectedIndex(Application.currentTheme.value);
		dropDown.putClientProperty(FlatClientProperties.STYLE, "font: +3;" + "arc: 1;" + "minimumWidth: 160;"
				+ "arrowType: triangle;" + "buttonStyle: none;" + "focusWidth: 0;");
		
		dropDown.addActionListener(_ -> {
            String selectedItem = (String) dropDown.getSelectedItem();

            if (selectedItem != null && Application.currentTheme.value != dropDown.getSelectedIndex())
			{
				Application.changeThemes();
                EventQueue.invokeLater(this::updateIconFiltersForCurrentTheme);
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
		
		/* JLabel gradientColorStart = new JLabel("Gradient Start Color");
		gradientColorStart.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		appearancePanel.add(gradientColorStart);
		
		JButton colorButton1 = new JButton("Pick a Color");
		colorButton1.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "arc: 1;" + "focusWidth: 0;");
		colorButton1.setToolTipText("Changes the chat background color");
		colorButton1.addActionListener(new ActionListener() {
			
			final String mode = Application.currentTheme == Theme.LIGHT_MODE ? "light_mode" : "dark_mode";
			
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
			
			final String mode = Application.currentTheme == Theme.LIGHT_MODE ? "light_mode" : "dark_mode";
			
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
		appearancePanel.add(colorButton2, "w 160"); */
		
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

        JPanel line = new JPanel();
		line.putClientProperty(FlatClientProperties.STYLE, "[light]background:lighten($Label.foreground, 70%);" +
                "[dark]background:darken($Label.foreground, 50%);");
        profilePanel.add(line, "span, growx, h 2!, gaptop 10, gapbottom 10");

        logOutButton = new JButton("Log Out");
        logOutButton.putClientProperty(FlatClientProperties.STYLE, "font: +3;" + "arc: 10;" + "focusWidth: 0;" +
                "foreground: #E32636");
        logOutButton.setFocusPainted(true);
        logOutButton.addActionListener(this);
        profilePanel.add(logOutButton, "w 130, h 30");

        JLabel label = new JLabel("Chat history on this computer will be cleared when you log out.");
        label.putClientProperty(FlatClientProperties.STYLE, "font: +1;");
        profilePanel.add(label, "gapy 10");

		return profilePanel;
	}
	
	private JPanel createSecurityPanel()
	{
		JPanel securityPanel = new JPanel(new MigLayout("wrap, fillx, insets 20 45 30 45, gapy 30", "[left][right]"));
		securityPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20;"
				+ "[light]background:darken(@background, 5%);" + "[dark]background:lighten(@background, 5%);");
		
		JLabel settingLabel = new JLabel("Privacy");
		settingLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +25;");
		securityPanel.add(settingLabel, "span, center");
		
		JLabel themeLabel = new JLabel("Application lock");
		themeLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +6;");
		securityPanel.add(themeLabel);
		
		JCheckBox checkBox = new JCheckBox("Enable");
		checkBox.putClientProperty(FlatClientProperties.STYLE, "font:bold +6;" + "icon.focusWidth: 0;");
		checkBox.setSelected(Application.userDetails.isPasswordEnabled());
		securityPanel.add(checkBox);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +6;");
		securityPanel.add(passwordLabel);
		
		passwordField = new JPasswordField();
		passwordField.putClientProperty(FlatClientProperties.STYLE,
				"font:bold +5;" + "showRevealButton: true;" + "focusWidth: 0;" + "showClearButton: true;");
		passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
		passwordField.setText(Application.userDetails.password());
		securityPanel.add(passwordField, "w 150, h 30");
		
		JButton button = new JButton("Save");
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.putClientProperty(FlatClientProperties.STYLE, "font:bold +6;" + "focusWidth: 0;"
				+ "[dark]background : darken(@accentColor, 5%);" + "[light]background : lighten(@accentColor, 5%)");
		button.addActionListener(_ -> setPassword(checkBox));
		securityPanel.add(button, "span, right");
		
		return securityPanel;
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
			String lastDirectory = Application.userDetails.last_directory();
            if (lastDirectory == null) lastDirectory = "";
			
			if (!lastDirectory.isBlank()) chooser.setCurrentDirectory(new File(lastDirectory));
			
			chooser.setDialogTitle("Select the Image File");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(filter);
			int userSelection = chooser.showOpenDialog(this);
			
			if (userSelection == JFileChooser.APPROVE_OPTION)
			{
				final File filePath = chooser.getSelectedFile();
				String targetDirectoryPath = getPathString() + "profile/";

				Path sourcePath = filePath.toPath();
				Path targetPath = new File(targetDirectoryPath, USERNAME + getFileExtension(filePath.getName())).toPath();
				
				try
				{
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    ResourceHandler.updatePathInLocalDB(filePath.getParent());
				}
				catch (Exception e1) {
                    System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
                }
				
				try (Connection conn = DriverManager.getConnection(DB.dbUrl, DB.username, DB.password);
					 PreparedStatement pstmt = conn.prepareStatement("UPDATE pfp SET Profile_picture = ?, Image_extension = ? WHERE Username = ?"))
				{
					try (FileInputStream fileInputStream = new FileInputStream(filePath))
                    {
                        String fileExtension = getFileExtension(filePath.getName());
                        byte[] imageData = new byte[fileInputStream.available()];
                        pstmt.setBytes(1, imageData);
                        pstmt.setString(2, fileExtension);
                        pstmt.setString(3, USERNAME);
                        pstmt.executeUpdate();
                    }

					profilePic.setIcon(new ImageIcon(getScaledImage(Objects.requireNonNull(loadImageWithoutExtension(USERNAME)))));
					repaint();

                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Profile picture successfully Uploaded.");
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
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.BOTTOM_RIGHT, "Profile picture successfully removed.");
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
        else if (e.getSource() == logOutButton)
        {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Log Out",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION)
            {
                ResourceHandler.deleteLocalDB();
                UserChats.clearAllConversations();
                UserInfo.clearAllConversations();
                System.exit(0);
            }
        }
	}
	
	private void setPassword(JCheckBox chk)
	{
		String password = new String(passwordField.getPassword());
        ResourceHandler.updateIsEnabledInLocalDB(chk.isSelected());
        ResourceHandler.updatePassword(Application.userDetails.username(), password);
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
		int maxWidth = 270;
		int maxHeight = 230;
		
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

    private JButton createJButton(String text, String iconName)
    {
        return new JButton(text, new FlatSVGIcon(new File(Application.jarFilePath + "res/icons/" + iconName + ".svg"))
                .derive(18, 18).setColorFilter(filterForCurrentTheme()));
    }

    private void updateIconFiltersForCurrentTheme()
    {
        FlatSVGIcon.ColorFilter filter = filterForCurrentTheme();
        for (JButton btn : buttons)
        {
            if (btn == null) continue;
            Icon icon = btn.getIcon();

            if (icon instanceof FlatSVGIcon svg)
                svg.setColorFilter(filter);

            btn.repaint();
        }
    }

    private FlatSVGIcon.ColorFilter filterForCurrentTheme()
    {
        return Application.currentTheme == Theme.DARK_MODE ? new FlatSVGIcon.ColorFilter(_ -> Color.WHITE) : null;
    }
}