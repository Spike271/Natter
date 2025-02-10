package raven.test;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import raven.chat.component.ChatBox;
import raven.chat.model.ModelMessage;
import raven.chat.swing.Background;
import raven.chat.swing.ChatEvent;
import raven.color.theme.Theme;
import raven.resource.swing.GetAndSetColor;
import raven.resource.swing.GetImage;

public class ChatUI extends JPanel implements Theme
{
	private static final long serialVersionUID = 1L;
	private Background background1;
	private raven.chat.component.ChatArea chatArea;
	private String mode = Theme.isDarkModeOn ? "dark_mode" : "light_mode";
	private PrintWriter output;
	private BufferedReader input;
	private Socket clientSocket;
	private volatile boolean isConnected = true;
	
	public ChatUI()
	{
		initComponents();
	}
	
	public JPanel createChatUI(String user, Icon pfp)
	{
		chatArea.setTitle(user);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
		chatArea.addChatEvent(new ChatEvent() {
			
			@Override
			public void mousePressedSendButton(ActionEvent evt)
			{
				Icon icon = pfp;
				String name = user;
				String date = df.format(new Date());
				String inputMessage = chatArea.getText().trim();
				chatArea.addChatBox(new ModelMessage(icon, name, date, inputMessage), ChatBox.BoxType.RIGHT);
				chatArea.clearTextAndGrabFocus();
				
				output.println(inputMessage);
			}
			
			@Override
			public void mousePressedFileButton(ActionEvent evt)
			{
				
			}
			
			@Override
			public void keyTyped(KeyEvent evt)
			{
			}
		});
		new Thread(this::listenForMessages).start();
		output.println(user);
		return this;
	}
	
	private void listenForMessages()
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
		
		Icon icon = new ImageIcon(getClass().getResource("../../libres/Profile_picture/p1.png"));
		String name = "Mayank";
		try
		{
			String outputMessage;
			while ((outputMessage = input.readLine()) != null)
			{
				chatArea.addChatBox(new ModelMessage(icon, name, df.format(new Date()), outputMessage),
						ChatBox.BoxType.LEFT);
				chatArea.clearTextAndGrabFocus();
				System.out.println(outputMessage);
				
				if (outputMessage.equalsIgnoreCase("disconnected"))
				{
					isConnected = false;
					break;
				}
			}
		}
		catch (IOException e)
		{
			if (isConnected)
			{
				System.out.println("Connection to the server was lost.");
				isConnected = false;
			}
		}
		finally
		{
			closeConnection();
		}
	}
	
	private void initComponents()
	{
		String temp = GetAndSetColor.getSettings(mode, "chatbackgroundImage");
		
		try
		{
			clientSocket = new Socket("localhost", 8000);
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			output = new PrintWriter(clientSocket.getOutputStream(), true);
		}
		catch (Exception e)
		{}
		
		try
		{
			if (temp.equals(""))
				background1 = new Background();
			else
				background1 = new Background(new File(GetImage.getSettings(temp)));
		}
		catch (Exception e)
		{
			background1 = new Background();
		}
		
		chatArea = new raven.chat.component.ChatArea();
		
		javax.swing.GroupLayout background1Layout = new javax.swing.GroupLayout(background1);
		background1.setLayout(background1Layout);
		background1Layout
				.setHorizontalGroup(background1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(background1Layout.createSequentialGroup().addContainerGap()
								.addComponent(chatArea, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
								.addContainerGap()));
		background1Layout
				.setVerticalGroup(background1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(background1Layout.createSequentialGroup().addContainerGap()
								.addComponent(chatArea, javax.swing.GroupLayout.DEFAULT_SIZE, 707, Short.MAX_VALUE)
								.addContainerGap()));
		
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(background1,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(background1,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
	}
	
	private void closeConnection()
	{
		try
		{
			if (clientSocket != null && !clientSocket.isClosed())
			{
				clientSocket.close();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		isConnected = false;
	}
}
