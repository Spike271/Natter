package com.client;

import com.formdev.flatlaf.FlatClientProperties;
import global.ResourceHandler;
import global.Theme;
import org.jdesktop.swingx.JXHyperlink;
import raven.toast.Notifications;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SignUp extends CustomJFrame implements ActionListener
{
	private JXHyperlink clickableLabel;
	private GradientToggleButton themeButton;
	private JButton submitButton;
	private JTextField firstNameField, lastNameField, userNameField;
	private JPasswordField passwordField, confirmPasswordField;

    public SignUp()
	{
		super("Sign Up");
		this.setSize(630, 600);
		this.setLocationRelativeTo(null);
		this.setFocusable(true);
		addGuiComponents();
        this.requestFocus();
    }
	
	@Override
	void addThemeButton(JPanel buttonPanel)
	{
		// Theme Button
		themeButton = new GradientToggleButton();
        themeButton.setSelected(Application.currentTheme == Theme.DARK_MODE);
        themeButton.setToolTipText("Switch Themes");
        themeButton.addActionListener(_ -> Application.changeThemes());
		buttonPanel.add(themeButton);
	}
	
	@Override
	protected void addCloseOperation()
	{
		closeButton.addActionListener(_ -> {
            ResourceHandler.changeSettings("Global", "isDark", themeButton.isSelected() ? "true" : "false");
            dispose();
            repaint();
            System.exit(0);
        });
	}
	
	private void addGuiComponents()
	{
		Font font = ResourceHandler.getFont("Roboto-Medium.ttf", 21f);
		
		// Heading
		JLabel Heading = new JLabel("Create account", JLabel.CENTER);
		Heading.setFont(ResourceHandler.getFont("Roboto-Bold.ttf", 36f));
        Heading.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #000000; " +
                "[dark]foreground: #FFFFFF");
		contentPane.add(Heading, "gapy 30 0, wrap");
		
		// Below Heading Text
		JLabel Message = new JLabel("Already have an account?", JLabel.RIGHT);
		Message.setFont(ResourceHandler.getFont("CLEARSANS.TTF", 16f));
        Message.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #000000; " +
                "[dark]foreground: #FFFFFF");
		contentPane.add(Message, "gapx 2 3, split 2");
		
		// color link
		clickableLabel = new JXHyperlink();
		clickableLabel.setText("Sign in");
		clickableLabel.setFont(ResourceHandler.getFont("CLEARSANS.TTF", 16f));
		clickableLabel.setForeground(new Color(0, 200, 250));
		clickableLabel.setClickedColor(new Color(0, 200, 250));
		clickableLabel.setUnclickedColor(new Color(0, 200, 250));
		clickableLabel.setFocusable(false);
		clickableLabel.addActionListener(this);
		contentPane.add(clickableLabel, "gapy 0 5, wrap");
		
		// First name label
		JLabel label1 = new JLabel("First name:");
		label1.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
        label1.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #000000; " +
                "[dark]foreground: #FFFFFF");
		contentPane.add(label1, "gapx 10, gapy 10, split");
		
		// Last name label
		JLabel label2 = new JLabel("Last name:");
		label2.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
        label2.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #000000; " +
                "[dark]foreground: #FFFFFF");
		contentPane.add(label2, "gapx 10, gapy 10, wrap");
		
		// First name text box
		firstNameField = new JTextField();
		firstNameField.setSelectedTextColor(Color.WHITE);
		firstNameField.setSelectionColor(Color.decode("#00c8fa"));
		firstNameField.setFont(font);
		makeTextFieldAcceptCharacterOnly(firstNameField);
		firstNameField.putClientProperty(FlatClientProperties.STYLE,
				"arc: 15;" + "borderWidth: 1;" + "borderColor: #808080;" + "focusWidth : 1;"
						+ "focusColor : @accentColor;" + "[light]background:lighten(@background, 5%);"
						+ "[dark]background:darken(@background, 0%);" + "placeholderForeground: #808080;"
						+ "margin : 5, 10, 5, 10");
		firstNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "First Name");
		contentPane.add(firstNameField, "gapx 0 10, h 35, split");
		
		// Last name text box
		lastNameField = new JTextField();
		lastNameField.setSelectedTextColor(Color.WHITE);
		lastNameField.setSelectionColor(Color.decode("#00c8fa"));
		lastNameField.setFont(font);
		makeTextFieldAcceptCharacterOnly(lastNameField);
		lastNameField.putClientProperty(FlatClientProperties.STYLE,
				"arc: 15;" + "borderWidth: 1;" + "borderColor: #808080;" + "focusWidth : 1;"
						+ "focusColor : @accentColor;" + "[light]background:lighten(@background, 5%);"
						+ "[dark]background:darken(@background, 0%);" + "placeholderForeground: #808080;"
						+ "margin : 5, 10, 5, 10");
		lastNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Last Name");
		contentPane.add(lastNameField, "h 35, wrap");
		
		// Username label
		JLabel label3 = new JLabel("Username:");
		label3.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
        label3.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #000000; " +
                "[dark]foreground: #FFFFFF");
		contentPane.add(label3, "gapx 4, gapy 12, wrap");
		
		// Username textbox
		userNameField = new JTextField();
		userNameField.setSelectedTextColor(Color.WHITE);
		userNameField.setSelectionColor(Color.decode("#00c8fa"));
		userNameField.setFont(font);
		userNameField.putClientProperty(FlatClientProperties.STYLE,
				"arc: 15;" + "borderWidth: 1;" + "borderColor: #808080;" + "focusWidth : 1;"
						+ "focusColor : @accentColor;" + "[light]background:lighten(@background, 5%);"
						+ "[dark]background:darken(@background, 0%);" + "placeholderForeground: #808080;"
						+ "margin : 5, 10, 5, 10");
		userNameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");
		firstCharacterOfTheTextFieldShouldBeALetter(userNameField);
		contentPane.add(userNameField, "wrap, h 35");
		
		// Password label
		JLabel label4 = new JLabel("Password:");
		label4.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
        label4.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #000000; " +
                "[dark]foreground: #FFFFFF");
		contentPane.add(label4, "gapx 4, gapy 12, wrap");
		
		// Password field
		passwordField = new JPasswordField();
		passwordField.setSelectedTextColor(Color.WHITE);
		passwordField.setSelectionColor(Color.decode("#00c8fa"));
		passwordField.setFont(font);
		passwordField.putClientProperty(FlatClientProperties.STYLE,
				"arc: 15;" + "borderWidth: 1;" + "borderColor: #808080;" + "focusWidth : 1;"
						+ "focusColor : @accentColor;" + "[light]background:lighten(@background, 5%);"
						+ "[dark]background:darken(@background, 0%);" + "placeholderForeground: #808080;"
						+ "margin : 5, 10, 5, 10;" + "showRevealButton: true;" + "showCapsLock: false;");
		passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
		contentPane.add(passwordField, "wrap, h 35");
		
		// Password Strength Status
        PasswordStrengthStatus passwordStrengthStatus = new PasswordStrengthStatus();
		passwordStrengthStatus.initPasswordField(passwordField);
		contentPane.add(passwordStrengthStatus, "wrap");
		
		// Confirm password label
		JLabel label5 = new JLabel("Confirm Password:");
		label5.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
        label5.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #000000; " +
                "[dark]foreground: #FFFFFF");
		contentPane.add(label5, "gapx 4, gapy 5, wrap");
		
		// Confirm password field
		confirmPasswordField = new JPasswordField();
		confirmPasswordField.setSelectedTextColor(Color.WHITE);
		confirmPasswordField.setSelectionColor(Color.decode("#00c8fa"));
		confirmPasswordField.setFont(font);
		confirmPasswordField.putClientProperty(FlatClientProperties.STYLE,
				"arc: 15;" + "borderWidth: 1;" + "borderColor: #808080;" + "focusWidth : 1;"
						+ "focusColor : @accentColor;" + "[light]background:lighten(@background, 5%);"
						+ "[dark]background:darken(@background, 0%);" + "placeholderForeground: #808080;"
						+ "margin : 5, 10, 5, 10;" + "showRevealButton: true;" + "showCapsLock: false;");
		confirmPasswordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Confirm Password");
		contentPane.add(confirmPasswordField, "wrap, h 35");
		
		// Sign up button
		submitButton = new JButton("Sign up");
		submitButton.setFont(font);
		submitButton.setBackground(new Color(0, 50, 255));
		submitButton.setForeground(Color.white);
		submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		submitButton.setFocusPainted(false);
		submitButton.setBorderPainted(false);
		submitButton.addActionListener(this);
		submitButton.putClientProperty(FlatClientProperties.STYLE, "arc: 15;" + "disabledBackground: #0033ff;");
		contentPane.add(submitButton, "gapy 30, h 35");
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == clickableLabel)
		{
			this.setVisible(false);
			Application.signIn.setVisible(true);
		}
		
		else if (e.getSource() == submitButton)
		{
			String firstName = firstNameField.getText().trim();
			String lastName = lastNameField.getText().trim();
			String confirmPassword = String.valueOf(confirmPasswordField.getPassword()).trim();
			String password = String.valueOf(passwordField.getPassword()).trim();
			String userName = userNameField.getText().trim();
			
			if (firstName.isBlank() || lastName.isBlank() || userName.isBlank() || password.isBlank() || confirmPassword.isBlank())
			{
				JOptionPane.showMessageDialog(SignUp.this, "Please fill all the required fields.");
			}
			else
			{
				Thread.startVirtualThread(() -> {

                    Notifications.getInstance().setJFrame(SignUp.this);
					
					if (password.length() < 8)
					{
                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Password should be at least 8 characters long.");
					}

					else if (!password.equals(confirmPassword))
					{
                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Confirm Password doesn't match with password.");
					}

					else if (checkIfUserAlreadyExist(userName))
					{
                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "This username already exist.\nTry something else.");
					}
					
					else
					{
						submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						submitButton.setEnabled(false);
						repaint();
						
						final String Query = "INSERT INTO account_info VALUES (NULL, ?, ?, ?, ?, ?)";
						
						try (Connection connection = DriverManager.getConnection(DB.dbUrl, DB.username, DB.password);
							 PreparedStatement preparedStatement = connection.prepareStatement(Query))
						{
							preparedStatement.setString(1, firstName);
							preparedStatement.setString(2, lastName);
							preparedStatement.setString(3, userName);
							preparedStatement.setString(4, password);
							preparedStatement.setString(5, getMacAddress());
							
							preparedStatement.executeUpdate();
							
							JOptionPane.showMessageDialog(this, "Your username : " + userName + "\nPassword: " + password);

                            ResourceHandler.createLocalDB();
                            ResourceHandler.insertDataInLocalDB(userName, null);
                            Application.userDetails = ResourceHandler.getLocalData();

                            Application.natter.setVisible(true);
							this.setVisible(false);
						}
						catch (Exception _)
						{
							JOptionPane.showMessageDialog(SignUp.this, "Something Went wrong");
						}
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
	}
	
	private boolean checkIfUserAlreadyExist(String username)
	{
		final String query = "Select * from account_info where BINARY Username = ?";
		
		try (Connection connection = DriverManager.getConnection(DB.dbUrl, DB.username,DB.password);
			 PreparedStatement preparedStatement = connection.prepareStatement(query))
		{
			preparedStatement.setString(1, username);
			return preparedStatement.executeQuery().next();
		}
		catch (Exception _) {}
		
		return false;
	}
	
	private String getMacAddress()
	{
		try
		{
			InetAddress inetAddress = InetAddress.getLocalHost();
			NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
			networkInterface.getHardwareAddress();
			
			byte[] macAddress = networkInterface.getHardwareAddress();
			if (macAddress != null)
			{
				StringBuilder sb = new StringBuilder();
				for (byte b : macAddress)
					sb.append(String.format("%02X-", b));
				
				return (sb.substring(0, sb.length() - 1));
			}
			else
			{
				System.out.println("No MAC address found for the network interface.");
			}
		}
		catch (Exception e)
		{
			System.err.println("Error retrieving network interfaces: " + e.getMessage());
		}
		return null;
	}
	
	private void makeTextFieldAcceptCharacterOnly(JTextField textField)
	{
		PlainDocument doc = (PlainDocument) textField.getDocument();
		
		// Set a DocumentFilter to restrict input to a-z and A-Z
		doc.setDocumentFilter(new DocumentFilter() {
			
			@Override
			public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
					throws BadLocationException
			{
				StringBuilder sb = new StringBuilder();
				for (char c : text.toCharArray())
				{
					if (Character.isLetter(c) || Character.isWhitespace(c))
						sb.append(c);
				}
				super.insertString(fb, offset, sb.toString(), attr);
			}
			
			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException
			{
				StringBuilder sb = new StringBuilder();
				for (char c : text.toCharArray())
				{
					if (Character.isLetter(c) || Character.isWhitespace(c))
						sb.append(c);
				}
				super.replace(fb, offset, length, sb.toString(), attrs);
			}
		});
	}
	
	private void firstCharacterOfTheTextFieldShouldBeALetter(JTextField textField)
	{
		PlainDocument doc = (PlainDocument) textField.getDocument();
		
		doc.setDocumentFilter(new DocumentFilter() {

			@Override
			public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
					throws BadLocationException
			{
				String newText = validateInput(fb.getDocument().getText(0, fb.getDocument().getLength()), offset);
				super.insertString(fb, offset, newText, attr);
			}
			
			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException
			{
				String newText = validateInput(fb.getDocument().getText(0, fb.getDocument().getLength()), offset);
				super.replace(fb, offset, length, newText, attrs);
			}
			
			private String validateInput(String newText, int offset)
			{
				if (offset == 0 && !newText.isEmpty())
				{
					char firstChar = newText.charAt(0);
					if (!Character.isLetter(firstChar)) return "";
				}
				return newText;
			}
		});
	}
}