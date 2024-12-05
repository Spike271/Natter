package com.client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SignIn extends CustomComponent implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JButton clickableLabel;
	private Color labelColor = toggle ? Color.WHITE : Color.BLACK;
	private GradientToggleButton themeButton;
	
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
	protected void closeOperation()
	{
		closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ResourceHandler.changeColor(themeButton.isSelected() ? "true" : "false");
				dispose();
				repaint();
			}
		});
	}
	
	@Override
	protected void maximizeBtn()
	{
	}
	
	@Override
	protected void maxBtnAdd(JPanel buttonPanel)
	{
	}
	
	private void addGuiComponents()
	{
		Font font = ResourceHandler.getFont("arial.ttf", 22f);
		
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
		clickableLabel.setBounds(367, 80, 58, 20);
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
				clickableLabel.setText("Sign Up");
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
		RoundedJTextField textbox1 = new RoundedJTextField("Username", 15, font);
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
		
		RoundedJPasswordField textbox2 = new RoundedJPasswordField("Password", 15, font);
		textbox2.setBounds(110, 250, 400, 50);
		textbox2.setSelectedTextColor(Color.WHITE);
		textbox2.setSelectionColor(Color.BLUE);
		contentPane.add(textbox2);
		
		RoundedJButton button1 = new RoundedJButton("Login", 15);
		button1.setBounds(110, 400, 405, 50);
		button1.setFont(font);
		button1.setBackground(new Color(0, 50, 255));
		button1.setForeground(Color.white);
		button1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(button1);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == clickableLabel)
		{
			this.setVisible(false);
			NatterMain.signUp.setVisible(true);
		}
	}
}