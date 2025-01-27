package com.client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
				ResourceHandler.changeSettings(themeButton.isSelected() ? "true" : "false");
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
				clickableLabel.setText("<html><U>Sign Up</U></html>");
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
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
			Thread t1 = new Thread(() -> {
				
				String username = textbox1.getText().trim();
				String password = String.valueOf(textbox2.getPassword()).trim();
				
				if (username.equalsIgnoreCase("Username") || password.equalsIgnoreCase("Password"))
				{
					JOptionPane.showMessageDialog(SignIn.this, "Please fill all the required fields.");
				}
				
				else if (!(username.isBlank() && password.isBlank()))
				{
					final String URL = "jdbc:mysql://localhost:3306/Natter";
					final String USER = "root";
					final String PASSWORD = "8529499264";
					
					try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD))
					{
						String sql = "SELECT * FROM account_info where Username = '" + username + "' and Password = '"
								+ password + "'";
						
						try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
						{
							ResultSet resultSet = preparedStatement.executeQuery();
							
							if (resultSet.next())
								System.out.println("Welcome");
						}
						catch (SQLException e1)
						{}
					}
					catch (SQLException e2)
					{}
				}
			});
			
			t1.start();
		}
	}
}
