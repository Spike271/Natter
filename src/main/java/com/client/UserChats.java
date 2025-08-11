package com.client;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserChats
{
	private static final String backupFile = Application.jarFilePath + "res/backup/Conversations.json";
	
	public static void addUsersConversation(String id, String date, String type, String content)
	{
		Map<String, Message> newMessages = new HashMap<>();
		
		Message Message = new Message(type, date, content);
		newMessages.put(id, Message);
		
		appendToConversations(newMessages);
	}
	
	public static Map<String, List<Message>> readAllConversations()
	{
		Gson gson = new Gson();
		File file = new File(backupFile);
		ConversationData data = new ConversationData();
		
		if (file.exists())
		{
			try (FileReader reader = new FileReader(file))
			{
				data = gson.fromJson(reader, ConversationData.class);
			}
			catch (IOException _) {}
		}
		else
		{
			System.err.println("File 'Conversations.json' not found.");
		}
		
		if (data == null) return new HashMap<>(0);
		
		return data.getConversations();
	}
	
	private static void appendToConversations(Map<String, Message> newMessages)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		File file = new File(backupFile);
		
		ConversationData data = readExistingData(gson, file);
		
		for (Entry<String, Message> entry : newMessages.entrySet())
		{
			String user = entry.getKey();
			Message messages = entry.getValue();
			
			data.getConversations().computeIfAbsent(user, _ -> new ArrayList<>()).add(messages);
		}
		
		try (FileWriter writer = new FileWriter(file))
		{
			gson.toJson(data, writer);
		}
		catch (IOException _) {}
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
			catch (IOException _) {}
		}
		return data != null ? data : new ConversationData();
	}
	
	static class ConversationData
	{
		private final Map<String, List<Message>> conversations;
		
		public ConversationData()
		{
			this.conversations = new HashMap<>();
		}
		
		public Map<String, List<Message>> getConversations()
		{
			return conversations;
		}
	}
	
	public record Message(String type, String date, String content) {}
}
