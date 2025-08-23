package com.client;

import java.awt.Cursor;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;

import net.miginfocom.swing.MigLayout;

public class PasswordWindow extends JFrame
{
	private final JPasswordField passwordField;
	private final JLabel messageLabel;
	
	public PasswordWindow()
	{
		this.setIconImage(new ImageIcon(Application.jarFilePath + "res/icons/logo32_32.png").getImage());
		this.setTitle("Password Window");
		this.setSize(340, 170);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		JPanel panel = new JPanel(new MigLayout("wrap 2", "[][grow]", "[]15[][][]"));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.putClientProperty(FlatClientProperties.STYLE,
				"[light]background:darken(@background, 5%);" + "[dark]background:lighten(@background, 5%);");
		
		JLabel label = new JLabel("Enter your password:");
		label.putClientProperty(FlatClientProperties.STYLE, "font: +3;");
		
		passwordField = new JPasswordField();
		passwordField.putClientProperty(FlatClientProperties.STYLE,
				"font: +2;" + "arc: 1;" + "focusWidth: 1;" + "showClearButton: true;" + "showRevealButton: true;");
		
		JButton submitButton = new JButton("Submit");
		submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		submitButton.putClientProperty(FlatClientProperties.STYLE, "font: +2;" + "arc: 1;" + "focusWidth: 1;");
		messageLabel = new JLabel(" ");
		messageLabel.putClientProperty(FlatClientProperties.STYLE, "foreground: #FF0000;");

		panel.add(label, "right");
		panel.add(passwordField, "growx");
		panel.add(submitButton, "span 2, align center");
		panel.add(messageLabel, "span 2, align center");
		
		submitButton.addActionListener(_ -> {
			
			String password = new String(passwordField.getPassword());
			if (!password.equals(Application.userDetails.password()))
			{
				messageLabel.setText("Incorrect password!");
                new Timer(2500, _ -> messageLabel.setText(" ")).start();
			}
			else
			{
				Application.natter.setVisible(true);
				dispose();
			}
		});
		
		this.add(panel);
	}
}