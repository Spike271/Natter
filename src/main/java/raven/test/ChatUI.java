package raven.test;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JPanel;

import com.client.MessagesSendAndReceive;
import com.client.ResourceHandler;
import com.client.userChats;

import raven.chat.component.ChatBox;
import raven.chat.model.ModelMessage;
import raven.chat.swing.Background;
import raven.chat.swing.ChatEvent;
import raven.color.theme.ChatComponentsColor;
import raven.resource.swing.GetImage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class ChatUI extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static String userName = "";
	private Background background1;
	public static ArrayList<ChatBoxList> chatBoxLists = new ArrayList<>();
	public raven.chat.component.ChatArea chatArea;
	public static Sinks.Many<String> sink = Sinks.many().multicast().directBestEffort();
	private static Flux<String> flux = sink.asFlux().share();
	private Icon receiverIcon;
	
	public ChatUI()
	{
		initComponents();
	}
	
	public JPanel createChatUI(String sender, String receiver, Icon senderIcon, Icon receiverIcon)
	{
		this.receiverIcon = receiverIcon;
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
				
				if (MessagesSendAndReceive.isConnected())
				{
					if (!inputMessage.isBlank())
					{
						String date = df.format(new Date());
						chatArea.addChatBox(new ModelMessage(icon, name, date, inputMessage), ChatBox.BoxType.RIGHT);
						chatArea.clearTextAndGrabFocus();
						chatArea.scrollToBottom();
						
						MessagesSendAndReceive.sendMessage(sender, receiver, inputMessage);
						userChats.addUsersConversation(receiver, date, "sender", inputMessage);
					}
				}
			}
		});
		
		var msg = loadExistingMessages(receiver);
		if (msg != null)
			addMessagesToList(msg, sender, receiver, senderIcon, receiverIcon);
		
		Thread.startVirtualThread(() -> listenMessageAndAddToUi(receiver));
		
		return this;
	}
	
	private void listenMessageAndAddToUi(String receiver)
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
		
		flux.subscribe(msg -> {
			if (msg.startsWith(receiver))
			{
				String date = df.format(new Date());
				String content = msg.substring(msg.indexOf(" ") + 1);
				
				chatArea.addChatBox(new ModelMessage(receiverIcon, receiver, date, content), ChatBox.BoxType.LEFT);
				chatArea.scrollToBottom();
			}
		});
	}
	
	private List<userChats.Message> loadExistingMessages(String userID)
	{
		Map<String, List<userChats.Message>> conversations = userChats.readAllConversations();
		
		if (conversations != null)
		{
			for (Map.Entry<String, List<userChats.Message>> entry : conversations.entrySet())
			{
				String userId = entry.getKey();
				List<userChats.Message> messages = entry.getValue();
				
				if (userId.equals(userID))
					return messages;
			}
		}
		return null;
	}
	
	private void addMessagesToList(List<userChats.Message> messages, String sender, String receiver, Icon senderIcon,
			Icon receiverIcon)
	{
		for (var msg : messages)
		{
			if (msg.getType().equals("sender"))
			{
				chatBoxLists.add(new ChatBoxList(new ModelMessage(senderIcon, sender, msg.getDate(), msg.getContent()),
						ChatBox.BoxType.RIGHT));
			}
			else
			{
				chatBoxLists
						.add(new ChatBoxList(new ModelMessage(receiverIcon, receiver, msg.getDate(), msg.getContent()),
								ChatBox.BoxType.LEFT));
			}
		}
	}
	
	private void initComponents()
	{
		String temp = ChatComponentsColor.chatbackgroundImage;
		
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
	
	public class ChatBoxList
	{
		ModelMessage modal;
		ChatBox.BoxType type;
		
		public ChatBoxList(ModelMessage modal, ChatBox.BoxType type)
		{
			this.modal = modal;
			this.type = type;
		}
		
		public ModelMessage getModal()
		{
			return modal;
		}
		
		public ChatBox.BoxType getType()
		{
			return type;
		}
	}
}