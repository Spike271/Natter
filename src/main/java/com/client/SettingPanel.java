package com.client;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
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
	private JButton changeButton, removeButton;
	private String USERNAME;
	private Thread t1;
	
	public SettingPanel()
	{
		init();
		this.setTitle("Settings");
		this.addWindowListener(new WindowAdapter() {
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
		USERNAME = getUsername();
		t1 = new Thread(() -> profilePanel = createProfilePanel());
		t1.start();
		
		this.setLayout(new MigLayout("fill, insets 20", "[center, fill]"));
		
		apperancePanel = createApperancePanel();
		
		placeHolderPanel = createPlaceHolderPanel();
		
		menuPanel = createMenuPanel();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, placeHolderPanel);
		splitPane.putClientProperty(FlatClientProperties.STYLE, "style:plain;");
		
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
		placeHolderPanel.putClientProperty(FlatClientProperties.STYLE,
				"arc:20;" + "[light]background:darken(@background,5%);" + "[dark]background:lighten(@background,5%);");
		placeHolderPanel.setMinimumSize(new Dimension(600, getHeight()));
		
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
		
		// Create a drop down menu
		JComboBox<String> dropDown = new JComboBox<>(new String[] { "Light Mode", "Dark Mode" });
		dropDown.setSelectedIndex(toggle ? 1 : 0);
		int check = dropDown.getSelectedIndex();
		dropDown.putClientProperty(FlatClientProperties.STYLE, "font: +3;" + "arc: 1;" + "minimumWidth:160;"
				+ "arrowType:traingle;" + "buttonStyle:none;" + "focusWidth:0;");
		
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
				}
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
			BufferedImage originalImage = loadImageWithoutExtension("01");
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
		
		JLabel username = new JLabel(USERNAME);
		username.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
		profilePanel.add(username, "center");
		
		changeButton = new JButton("Change Image");
		changeButton.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "focusWidth:0;");
		changeButton.addActionListener(this); // Button
		profilePanel.add(changeButton);
		
		removeButton = new JButton("Remove Image");
		removeButton.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "focusWidth:0;");
		removeButton.addActionListener(this); // Button
		profilePanel.add(removeButton, "gapy 5");
		
		return profilePanel;
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
					ResourceHandler.changeSettings("false");
				});
			}
			else
			{
				EventQueue.invokeLater(() -> {
					FlatMacDarkLaf.setup();
					FlatLaf.updateUI();
					ResourceHandler.changeSettings("true");
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
				file = new File(getClass().getResource(baseName + "." + ext).getPath());
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
				file = new File(getClass().getResource(fileName + "." + ext).getPath());
				if (file.exists())
					return file;
			}
		}
		catch (Exception e)
		{}
		
		return null;
	}
	
	private String getUsername()
	{
		String targetDirectoryPath = getClass().getResource("SettingPanel.class").getPath();
		targetDirectoryPath = targetDirectoryPath.substring(0, targetDirectoryPath.lastIndexOf("/") + 1);
		
		try (BufferedReader reader = new BufferedReader(new FileReader(targetDirectoryPath + "Username.txt")))
		{
			return reader.readLine();
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
			chooser.setDialogTitle("Select the Image File");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(filter);
			int userSelection = chooser.showOpenDialog(this);
			
			if (userSelection == JFileChooser.APPROVE_OPTION)
			{
				final File file = chooser.getSelectedFile();
				String targetDirectoryPath = getClass().getResource("SettingPanel.class").getPath();
				targetDirectoryPath = targetDirectoryPath.substring(0, targetDirectoryPath.lastIndexOf("/"));
				
				Path sourcePath = file.toPath();
				Path targetPath = new File(targetDirectoryPath, file.getName()).toPath();
				
				try
				{
					Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
				}
				catch (IOException ex)
				{}
				
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
				
				File file = loadFilePath("01");
				file.delete();
				
				JOptionPane.showMessageDialog(this, "Profile picture sucessfully removed.");
			}
			catch (Exception e2)
			{
				JOptionPane.showMessageDialog(this, "Unable to delete the profile picture.");
			}
		}
	}
	
	private String getFileExtension(String fileName)
	{
		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex > 0)
			return fileName.substring(lastDotIndex);
		
		return "";
	}
}