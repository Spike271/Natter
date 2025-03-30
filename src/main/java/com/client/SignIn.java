package com.client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXHyperlink;

import com.formdev.flatlaf.FlatClientProperties;

class SignIn extends CustomComponent implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Color labelColor = Theme.isDarkModeOn ? Color.WHITE : Color.BLACK;
	private GradientToggleButton themeButton;
	private JXHyperlink clickableLabel;
	private JButton submitButton;
	private JTextField textbox1;
	private JPasswordField textbox2;
	
	public SignIn()
	{
		super("Sign in");
		this.setSize(630, 550);
		this.setLocationRelativeTo(null);
		this.setFocusable(true);
		addGuiComponents();
	}
	
	@Override
	void addThemeButton(JPanel buttonPanel)
	{
		// Theme Button
		themeButton = new GradientToggleButton();
		themeButton.setSelected(Theme.isDarkModeOn);
		themeButton.setToolTipText("Dark Mode");
		themeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showConfirmDialog(SignIn.this, "Restart the Natter to apply new theme.",
						"Apply the new Theme?", JOptionPane.DEFAULT_OPTION);
			}
		});
		
		buttonPanel.add(themeButton);
	}
	
	@Override
	protected void addcloseOperation()
	{
		closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ResourceHandler.changeColorFileSettings("ColorMode.IsDark",
						themeButton.isSelected() ? "true" : "false");
				ResourceHandler.changeSettings("Global.isDark", themeButton.isSelected() ? "true" : "false");
				System.exit(0);
			}
		});
	}
	
	private void addGuiComponents()
	{
		Font font = ResourceHandler.getFont("Roboto-Medium.ttf", 22f);
		
		// Heading
		JLabel Heading = new JLabel("Member Login", JLabel.CENTER);
		Heading.setFont(ResourceHandler.getFont("Roboto-Bold.ttf", 36f));
		Heading.setForeground(labelColor);
		contentPane.add(Heading, "gapy 30 0, wrap");
		
		// Below heading text
		JLabel Message = new JLabel("Don't have a account!", JLabel.RIGHT);
		Message.setFont(ResourceHandler.getFont("CLEARSANS.TTF", 16f));
		Message.setForeground(labelColor);
		contentPane.add(Message, "gapx 98 3, split 2, sg g1");
		
		// color link
		clickableLabel = new JXHyperlink();
		clickableLabel.setText("Sign Up");
		clickableLabel.setFont(ResourceHandler.getFont("CLEARSANS.TTF", 16f));
		clickableLabel.setClickedColor(new Color(0, 200, 250));
		clickableLabel.setForeground(new Color(0, 200, 250));
		clickableLabel.setFocusable(false);
		clickableLabel.addActionListener(this);
		contentPane.add(clickableLabel, "wrap, sg g1");
		
		// User name label
		JLabel label1 = new JLabel("Username:");
		label1.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label1.setForeground(labelColor);
		contentPane.add(label1, "gapx 5, gapy 15, wrap");
		
		// User name text box
		textbox1 = new JTextField();
		textbox1.setSelectedTextColor(Color.WHITE);
		textbox1.setSelectionColor(Color.decode("#00c8fa"));
		textbox1.setFont(font);
		textbox1.putClientProperty(FlatClientProperties.STYLE,
				"arc: 15;" + "borderWidth: 1;" + "borderColor: #808080;" + "focusWidth : 1;"
						+ "focusColor : @accentColor;" + "[light]background:lighten(@background, 5%);"
						+ "[dark]background:darken(@background, 0%);" + "placeholderForeground: #808080;"
						+ "margin : 5, 10, 5, 10");
		textbox1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");
		contentPane.add(textbox1, "wrap, h 45");
		
		// password label
		JLabel label2 = new JLabel("Password:");
		label2.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label2.setForeground(labelColor);
		contentPane.add(label2, "gapx 5, gapy 15, wrap");
		
		textbox2 = new JPasswordField();
		textbox2.setSelectedTextColor(Color.WHITE);
		textbox2.setSelectionColor(Color.decode("#00c8fa"));
		textbox2.setFont(font);
		textbox2.putClientProperty(FlatClientProperties.STYLE,
				"arc: 15;" + "borderWidth: 1;" + "borderColor: #808080;" + "focusWidth : 1;"
						+ "focusColor : @accentColor;" + "[light]background:lighten(@background, 5%);"
						+ "[dark]background:darken(@background, 0%);" + "placeholderForeground: #808080;"
						+ "margin : 5, 10, 5, 10;" + "showRevealButton: true;" + "showCapsLock: false;");
		textbox2.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
		contentPane.add(textbox2, "wrap, h 45");
		
		submitButton = new JButton("Login");
		submitButton.setFont(font);
		submitButton.setBackground(new Color(0, 50, 255));
		submitButton.setForeground(Color.white);
		submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		submitButton.setFocusPainted(false);
		submitButton.setBorderPainted(false);
		submitButton.addActionListener(this);
		submitButton.putClientProperty(FlatClientProperties.STYLE, "arc: 15;" + "disabledBackground: #0033ff;");
		contentPane.add(submitButton, "gapy 85, h 50");
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == clickableLabel)
		{
			this.setVisible(false);
			NatterMain.signUp.setVisible(true);
		}
		
		else if (e.getSource() == submitButton)
		{
			Thread.startVirtualThread(() -> {
				
				String username = textbox1.getText().trim();
				String password = String.valueOf(textbox2.getPassword()).trim();
				
				if (username.isBlank() || password.isBlank())
				{
					JOptionPane.showMessageDialog(SignIn.this, "Please fill all the required fields.");
				}
				
				else
				{
					submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					submitButton.setEnabled(false);
					
					final String query = "SELECT * FROM account_info where BINARY Username = '" + username
							+ "' and Password = '" + password + "'";
					
					try (Connection connection = DriverManager.getConnection(DB.dbUrl, DB.username,
							DB.password); PreparedStatement preparedStatement = connection.prepareStatement(query))
					{
						
						if (preparedStatement.executeQuery().next())
						{
							String targetDirectoryPath = getClass().getResource("SignIn.class").getPath();
							targetDirectoryPath = targetDirectoryPath.substring(0,
									targetDirectoryPath.lastIndexOf("/") + 1);
							
							ResourceHandler.writePropertiesFile("username", username);
							ResourceHandler.writePropertiesFile("alreadyAUser", "true");
							
							if (!checkIfPfpAlreadyExistOrNot(username))
							{
								String query1 = "select Profile_Picture, Image_extension from pfp where Username = ?";
								
								try (PreparedStatement getPfp = connection.prepareStatement(query1))
								{
									getPfp.setString(1, username);
									ResultSet rs = getPfp.executeQuery();
									
									if (rs.next())
									{
										byte[] imageData = rs.getBytes("Profile_picture");
										
										if (imageData != null)
										{
											String imageExtension = rs.getString("Image_extension");
											String imagePath = getPathString() + "profile/" + username + imageExtension;
											OutputStream outputStream = new FileOutputStream(imagePath);
											outputStream.write(imageData);
											outputStream.close();
										}
									}
								}
								catch (Exception e2)
								{}
							}
							
							NatterMain.natter.setVisible(true);
							this.setVisible(false);
						}
						else
						{
							JOptionPane.showMessageDialog(this, "Wrong username or password.");
						}
					}
					catch (Exception e1)
					{}
					finally
					{
						submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						submitButton.setEnabled(true);
						repaint();
					}
				}
			});
			// VT Close
		}
	}
	
	private boolean checkIfPfpAlreadyExistOrNot(String fileName)
	{
		String targetDirectoryPath = getPathString();
		
		String[] extensions = { "jpg", "jpeg", "png" };
		
		for (String ext : extensions)
		{
			File file = null;
			String path = targetDirectoryPath + "profile/";
			file = new File(path + fileName + "." + ext);
			if (file.exists())
				return true;
		}
		return false;
	}
	
	private String getPathString()
	{
		String targetDirectoryPath = getClass().getResource("SettingPanel.class").getPath();
		return targetDirectoryPath.substring(0, targetDirectoryPath.lastIndexOf("/") + 1);
	}
}
