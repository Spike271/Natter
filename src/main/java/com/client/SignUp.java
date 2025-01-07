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

public class SignUp extends CustomComponent implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JButton clickableLabel;
	private Color labelColor = toggle ? Color.WHITE : Color.BLACK;
	private GradientToggleButton themeButton;
	
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
	protected void closeOperation()
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
				clickableLabel.setText("Sign in");
			}
		});
		
		contentPane.add(clickableLabel);
		
		JLabel label1 = new JLabel("First name:");
		label1.setBounds(0, 120, 320, 50);
		label1.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setForeground(labelColor);
		contentPane.add(label1);
		
		RoundedJTextField textbox1 = new RoundedJTextField("First name", 15, font);
		textbox1.setBounds(100, 160, 200, 40);
		textbox1.setSelectionColor(Color.BLUE);
		textbox1.setSelectedTextColor(Color.WHITE);
		contentPane.add(textbox1);
		
		JLabel label2 = new JLabel("Last name:");
		label2.setBounds(320, 120, 320, 50);
		label2.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label2.setForeground(labelColor);
		contentPane.add(label2);
		
		RoundedJTextField textbox2 = new RoundedJTextField("Last name", 15, font);
		textbox2.setBounds(305, 160, 200, 40);
		textbox2.setSelectionColor(Color.BLUE);
		textbox2.setSelectedTextColor(Color.WHITE);
		contentPane.add(textbox2);
		
		JLabel label3 = new JLabel("Username:");
		label3.setBounds(0, 200, 320, 50);
		label3.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label3.setHorizontalAlignment(SwingConstants.CENTER);
		label3.setForeground(labelColor);
		contentPane.add(label3);
		
		RoundedJTextField textbox3 = new RoundedJTextField("Username", 15, font);
		textbox3.setBounds(100, 240, 405, 40);
		textbox3.setSelectionColor(Color.BLUE);
		textbox3.setSelectedTextColor(Color.WHITE);
		contentPane.add(textbox3);
		
		JLabel label4 = new JLabel("Password:");
		label4.setBounds(0, 280, 320, 50);
		label4.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label4.setHorizontalAlignment(SwingConstants.CENTER);
		label4.setForeground(labelColor);
		contentPane.add(label4);
		
		RoundedJPasswordField textbox4 = new RoundedJPasswordField("Password", 15, font);
		textbox4.setBounds(100, 320, 405, 40);
		textbox4.setSelectionColor(Color.BLUE);
		textbox4.setSelectedTextColor(Color.WHITE);
		contentPane.add(textbox4);
		
		JLabel label5 = new JLabel("Confirm Password:");
		label5.setBounds(33, 360, 320, 50);
		label5.setFont(ResourceHandler.getFont("ARIALBD_1.ttf", 16f));
		label5.setHorizontalAlignment(SwingConstants.CENTER);
		label5.setForeground(labelColor);
		contentPane.add(label5);
		
		RoundedJPasswordField textbox5 = new RoundedJPasswordField("Confirm Password", 15, font);
		textbox5.setBounds(100, 400, 405, 40);
		textbox5.setSelectionColor(Color.BLUE);
		textbox5.setSelectedTextColor(Color.WHITE);
		contentPane.add(textbox5);
		
		RoundedJButton button1 = new RoundedJButton("Sign up", 15);
		button1.setBounds(100, 470, 405, 40);
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
			NatterMain.signIn.setVisible(true);
		}
	}
}