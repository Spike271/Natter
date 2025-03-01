package com.client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.SwingConstants;

public class SignIn extends CustomComponent implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Color labelColor = toggle ? Color.WHITE : Color.BLACK;
	private GradientToggleButton themeButton;
	private JButton clickableLabel;
	private RoundedJButton submitButton;
	private RoundedJTextField textbox1;
	private RoundedJPasswordField textbox2;
	
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
		themeButton.setSelected(toggle);
		themeButton.setToolTipText("Dark Mode");
		themeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showConfirmDialog(null, "Restart the Natter to apply new theme.", "Apply the new Theme?",
						JOptionPane.DEFAULT_OPTION);
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
				dispose();
				repaint();
			}
		});
	}
	
	private void addGuiComponents()
	{
		Font font = ResourceHandler.getFont("Roboto-Medium.ttf", 22f);
		
		// Heading
		JLabel Heading = new JLabel("Member Login");
		Heading.setBounds(0, 30, 630, 50);
		Heading.setFont(ResourceHandler.getFont("Roboto-Bold.ttf", 36f));
		Heading.setHorizontalAlignment(SwingConstants.CENTER);
		Heading.setForeground(labelColor);
		contentPane.add(Heading);
		
		// Below heading text
		JLabel Message = new JLabel("Don't have a account!");
		Message.setBounds(0, 65, 362, 50);
		Message.setFont(ResourceHandler.getFont("CLEARSANS.TTF", 16f));
		Message.setHorizontalAlignment(SwingConstants.RIGHT);
		Message.setForeground(labelColor);
		contentPane.add(Message);
		
		// color link
		clickableLabel = new JButton("Sign Up");
		clickableLabel.setFont(ResourceHandler.getFont("CLEARSANS.TTF", 16f));
		clickableLabel.setBounds(367, 80, 68, 20);
		clickableLabel.setHorizontalAlignment(SwingConstants.LEFT);
		clickableLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		clickableLabel.setForeground(new Color(0, 200, 250));
		clickableLabel.setContentAreaFilled(false);
		clickableLabel.setOpaque(false);
		clickableLabel.setBorderPainted(false);
		clickableLabel.setFocusable(false);
		clickableLabel.setBorder(null);
		clickableLabel.addActionListener(this);
		clickableLabel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				clickableLabel.setFont(ResourceHandler.getFont("CLEARSANS.TTF", 16f));
				clickableLabel.setText("<html><U>Sign Up</U></html>");
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				clickableLabel.setFont(ResourceHandler.getFont("CLEARSANS.TTF", 16f));
				clickableLabel.setText("<html>Sign Up</html>");
			}
		});
		contentPane.add(clickableLabel);
		
		// User name label
		JLabel label1 = new JLabel("Username:");
		label1.setBounds(0, 120, 330, 50);
		label1.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setForeground(labelColor);
		contentPane.add(label1);
		
		// User name text box
		textbox1 = new RoundedJTextField("Username ", 15, font);
		textbox1.setBounds(110, 160, 400, 50);
		textbox1.setSelectedTextColor(Color.WHITE);
		textbox1.setSelectionColor(Color.BLUE);
		contentPane.add(textbox1);
		
		// password label
		JLabel label2 = new JLabel("Password:");
		label2.setBounds(0, 210, 330, 50);
		label2.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setForeground(labelColor);
		contentPane.add(label2);
		
		textbox2 = new RoundedJPasswordField("Password ", 15, font);
		textbox2.setBounds(110, 250, 400, 50);
		textbox2.setSelectedTextColor(Color.WHITE);
		textbox2.setSelectionColor(Color.BLUE);
		contentPane.add(textbox2);
		
		submitButton = new RoundedJButton("Login", 15);
		submitButton.setBounds(110, 400, 405, 50);
		submitButton.setFont(font);
		submitButton.setBackground(new Color(0, 50, 255));
		submitButton.setForeground(Color.white);
		submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		submitButton.addActionListener(this);
		contentPane.add(submitButton);
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
			new Thread(() -> {
				
				String username = textbox1.getText().trim();
				String password = String.valueOf(textbox2.getPassword()).trim();
				
				if (username.equals("Username") && password.equals("Password"))
				{
					JOptionPane.showMessageDialog(SignIn.this, "Please fill all the required fields.");
				}
				
				else if (!(username.isBlank() && password.isBlank()))
				{
					submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					submitButton.setEnabled(false);
					repaint();
					
					final String query = "SELECT * FROM account_info where Username = '" + username
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
			}).start();
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
