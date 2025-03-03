package com.client;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class userChats
{
	private static final String backupFile = "../../res/backup/Conversations.json";
	
	public static void addUsersConversation(String id, String date, String type, String message)
	{
		Map<String, Message> newMessages = new HashMap<>();
		
		Message Message = new Message(type, date, message);
		newMessages.put(id, Message);
		
		appendToConversations(newMessages);
	}
	
	public static Map<String, List<Message>> readAllConversations()
	{
		Gson gson = new Gson();
		File file = new File(userChats.class.getResource(backupFile).getFile());
		ConversationData data = new ConversationData();
		
		if (file.exists())
		{
			try (FileReader reader = new FileReader(file))
			{
				data = gson.fromJson(reader, ConversationData.class);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.err.println("File 'Conversations.json' not found.");
		}
		
		return data.getConversations();
	}
	
	private static void appendToConversations(Map<String, Message> newMessages)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		File file = new File(userChats.class.getResource(backupFile).getFile());
		
		ConversationData data = readExistingData(gson, file);
		
		for (Entry<String, Message> entry : newMessages.entrySet())
		{
			String user = entry.getKey();
			Message messages = entry.getValue();
			
			data.getConversations().computeIfAbsent(user, k -> new ArrayList<>()).add(messages);
		}
		
		try (FileWriter writer = new FileWriter(file))
		{
			gson.toJson(data, writer);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static ConversationData readExistingData(Gson gson, File file)
	{
		ConversationData data = new ConversationData();
		if (file.exists())
		{
			try (FileReader reader = new FileReader(file))
			{
				data = gson.fromJson(reader, ConversationData.class);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return data != null ? data : new ConversationData();
	}
	
	static class ConversationData
	{
		private Map<String, List<Message>> conversations;
		
		public ConversationData()
		{
			this.conversations = new HashMap<>();
		}
		
		public Map<String, List<Message>> getConversations()
		{
			return conversations;
		}
	}
	
	public static class Message
	{
		private String type;
		private String date;
		private String content;
		
		public Message(String type, String date, String content)
		{
			this.type = type;
			this.date = date;
			this.content = content;
		}
		
		public String getType()
		{
			return type;
		}
		
		public String getContent()
		{
			return content;
		}
		
		public String getDate()
		{
			return date;
		}
	}
}