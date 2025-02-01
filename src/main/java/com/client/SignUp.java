package com.client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SignUp extends CustomComponent implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JButton clickableLabel;
	private Color labelColor = toggle ? Color.WHITE : Color.BLACK;
	private GradientToggleButton themeButton;
	private RoundedJButton submitButton;
	RoundedJTextField FNbox, LNbox, Userbox;
	RoundedJPasswordField Passbox, CFbox;
	
	public SignUp()
	{
		super("Sign Up");
		this.setSize(630, 600);
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
				ResourceHandler.changeSettings(themeButton.isSelected() ? "true" : "false");
				dispose();
				repaint();
			}
		});
	}
	
	private void addGuiComponents()
	{
		Font font = ResourceHandler.getFont("Roboto-Medium.ttf", 21f);
		
		JLabel Heading = new JLabel("Create account");
		Heading.setBounds(0, 30, 600, 50);
		Heading.setFont(ResourceHandler.getFont("Roboto-Bold.ttf", 36f));
		Heading.setHorizontalAlignment(SwingConstants.CENTER);
		Heading.setForeground(labelColor);
		contentPane.add(Heading);
		
		JLabel Message = new JLabel("Already have an account?");
		Message.setBounds(0, 65, 370, 50);
		Message.setFont(ResourceHandler.getFont("CLEARSANS.TTF", 16f));
		Message.setHorizontalAlignment(SwingConstants.RIGHT);
		Message.setForeground(labelColor);
		contentPane.add(Message);
		
		clickableLabel = new JButton("Sign in");
		clickableLabel.setFont(ResourceHandler.getFont("CLEARSANS.TTF", 16f));
		clickableLabel.setBounds(373, 80, 70, 20);
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
				clickableLabel.setText("<html><U>Sign in</U></html>");
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				clickableLabel.setText("<html>Sign in</html>");
			}
		});
		
		contentPane.add(clickableLabel);
		
		JLabel label1 = new JLabel("First name:");
		label1.setBounds(0, 120, 320, 50);
		label1.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setForeground(labelColor);
		contentPane.add(label1);
		
		FNbox = new RoundedJTextField("First name ", 15, font);
		FNbox.setBounds(100, 160, 200, 40);
		FNbox.setSelectionColor(Color.BLUE);
		FNbox.setSelectedTextColor(Color.WHITE);
		contentPane.add(FNbox);
		
		JLabel label2 = new JLabel("Last name:");
		label2.setBounds(320, 120, 320, 50);
		label2.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label2.setForeground(labelColor);
		contentPane.add(label2);
		
		LNbox = new RoundedJTextField("Last name ", 15, font);
		LNbox.setBounds(305, 160, 200, 40);
		LNbox.setSelectionColor(Color.BLUE);
		LNbox.setSelectedTextColor(Color.WHITE);
		contentPane.add(LNbox);
		
		JLabel label3 = new JLabel("Username:");
		label3.setBounds(0, 200, 320, 50);
		label3.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label3.setHorizontalAlignment(SwingConstants.CENTER);
		label3.setForeground(labelColor);
		contentPane.add(label3);
		
		Userbox = new RoundedJTextField("Username ", 15, font);
		Userbox.setBounds(100, 240, 405, 40);
		Userbox.setSelectionColor(Color.BLUE);
		Userbox.setSelectedTextColor(Color.WHITE);
		contentPane.add(Userbox);
		
		JLabel label4 = new JLabel("Password:");
		label4.setBounds(0, 280, 320, 50);
		label4.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label4.setHorizontalAlignment(SwingConstants.CENTER);
		label4.setForeground(labelColor);
		contentPane.add(label4);
		
		Passbox = new RoundedJPasswordField("Password ", 15, font);
		Passbox.setBounds(100, 320, 405, 40);
		Passbox.setSelectionColor(Color.BLUE);
		Passbox.setSelectedTextColor(Color.WHITE);
		contentPane.add(Passbox);
		
		JLabel label5 = new JLabel("Confirm Password:");
		label5.setBounds(33, 360, 320, 50);
		label5.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label5.setHorizontalAlignment(SwingConstants.CENTER);
		label5.setForeground(labelColor);
		contentPane.add(label5);
		
		CFbox = new RoundedJPasswordField("Confirm Password ", 15, font);
		CFbox.setBounds(100, 400, 405, 40);
		CFbox.setSelectionColor(Color.BLUE);
		CFbox.setSelectedTextColor(Color.WHITE);
		contentPane.add(CFbox);
		
		submitButton = new RoundedJButton("Sign up", 15);
		submitButton.setBounds(100, 470, 405, 40);
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
			NatterMain.signIn.setVisible(true);
		}
		else if (e.getSource() == submitButton)
		{
			new Thread(() -> {
				
				submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				submitButton.setEnabled(false);
				
				String firstName = FNbox.getText().trim();
				String lastName = LNbox.getText().trim();
				String userName = Userbox.getText().trim();
				String password = String.valueOf(Passbox.getPassword()).trim();
				String confirmPassword = String.valueOf(CFbox.getPassword()).trim();
				
				if (firstName.equalsIgnoreCase("First name") || lastName.equalsIgnoreCase("Last name")
						|| userName.equalsIgnoreCase("Username") || password.equalsIgnoreCase("Password")
						|| confirmPassword.equalsIgnoreCase("Confirm Password"))
				{
					JOptionPane.showMessageDialog(SignUp.this, "Please fill all the required fields.");
				}
				
				else if (!password.equals(confirmPassword))
				{
					JOptionPane.showMessageDialog(SignUp.this, "Confirm Password doesn't match with password.");
				}
				
				else if (!(firstName.isBlank() && lastName.isBlank() && userName.isBlank() && password.isBlank()
						&& confirmPassword.isBlank()))
				{
					final String Query = "INSERT INTO account_info VALUES (NULL, ?, ?, ?, ?, ?)";
					
					try (Connection connection = DriverManager.getConnection(DB.dbUrl, DB.username,
							DB.password); PreparedStatement preparedStatement = connection.prepareStatement(Query))
					{
						
						preparedStatement.setString(1, firstName);
						preparedStatement.setString(2, lastName);
						preparedStatement.setString(3, userName);
						preparedStatement.setString(4, password);
						preparedStatement.setString(5, getMacAddress());
						
						preparedStatement.executeUpdate();
						
						JOptionPane.showMessageDialog(this, "Your username : " + userName + "\nPassword: " + password);
						
						NatterMain.natter.setVisible(true);
						NatterMain.dbUsername = userName;
						this.setVisible(false);
						
					}
					catch (SQLException e2)
					{}
					finally
					{
						submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						submitButton.setEnabled(true);
					}
				}
			}).start();
		}
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
				{
					sb.append(String.format("%02X-", b));
				}
				
				return (sb.toString().substring(0, sb.length() - 1));
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
}