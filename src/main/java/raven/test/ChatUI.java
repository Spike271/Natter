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
import javax.swing.JPanel;

import com.client.ResourceHandler;
import com.client.userChats;

import raven.chat.component.ChatBox;
import raven.chat.model.ModelMessage;
import raven.chat.swing.Background;
import raven.chat.swing.ChatEvent;
import raven.color.theme.Theme;
import raven.resource.swing.GetAndSetColor;
import raven.resource.swing.GetImage;

public class ChatUI extends JPanel implements Theme
{
	public static String userName = "", globalSender;
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
	
	public JPanel createChatUI(String sender, String receiver, Icon senderIcon, Icon receiverIcon)
	{
		if (globalSender == null)
			globalSender = sender;
		
		userName = receiver;
		chatArea.setTitle(receiver);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
		chatArea.addChatEvent(new ChatEvent() {
			
			Icon icon = senderIcon;
			String name = ResourceHandler.readPropertiesFile("username");
			
			@Override
			public void mousePressedSendButton(ActionEvent evt)
			{
				addMessages();
			}
			
			@Override
			public void mousePressedFileButton(ActionEvent evt)
			{
			}
			
			@Override
			public void keyTyped(KeyEvent evt)
			{
			}
			
			@Override
			public void keyPressed(KeyEvent evt)
			{
				if (evt.getKeyCode() == KeyEvent.VK_ENTER && (evt.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) != 0)
				{
					addMessages();
				}
			}
			
			private void addMessages()
			{
				String inputMessage = chatArea.getText().trim();
				
				if (!inputMessage.isBlank())
				{
					String date = df.format(new Date());
					chatArea.addChatBox(new ModelMessage(icon, name, date, inputMessage), ChatBox.BoxType.RIGHT);
					chatArea.clearTextAndGrabFocus();
					
					output.println(receiver + "\n" + inputMessage + "\nEND_OF_MESSAGE");
					userChats.addUsersConversation(receiver, date, "sender", inputMessage);
					System.out.println(receiver + "\n" + inputMessage + "\nEND_OF_MESSAGE");
				}
			}
		});
		new Thread(() -> listenForMessages(receiver, receiverIcon)).start();
		return this;
	}
	
	private void listenForMessages(String user, Icon pfp)
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
		output.println(globalSender);
		
		Icon icon = pfp;
		String name = user;
		
		try
		{
			chatArea.addChatBox(new ModelMessage(icon, name, df.format(new Date()), input.readLine()),
					ChatBox.BoxType.LEFT);
			chatArea.clearTextAndGrabFocus();
			chatArea.clearChatBox();
			
			String outputMessage;
			StringBuilder messageBuilder = new StringBuilder(); // To store multi-line messages
			while ((outputMessage = input.readLine()) != null)
			{
				if (outputMessage.equals("END_OF_MESSAGE"))
				{
					String fullMessage = messageBuilder.toString();
					String date = df.format(new Date());
					chatArea.addChatBox(new ModelMessage(icon, name, date, fullMessage), ChatBox.BoxType.LEFT);
					chatArea.clearTextAndGrabFocus();
					userChats.addUsersConversation(name, date, "receiver", fullMessage);
					System.out.println(fullMessage);
					
					messageBuilder.setLength(0);
				}
				else if (outputMessage.equals("disconnected"))
				{
					isConnected = false;
					break;
				}
				else
				{
					if (messageBuilder.length() > 0)
						messageBuilder.append("\n");
					messageBuilder.append(outputMessage);
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
