package com.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UserChats
{
	private static final String backupFile = Application.jarFilePath + "res/backup/Conversations.json";
    private static final Logger log = LoggerFactory.getLogger(UserChats.class);

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
			catch (IOException e) {
                log.error("{}", e.toString());
            }
		}
		else
		{
			log.error("File 'Conversations.json' not found.");
		}
		
		if (data == null) return new HashMap<>(0);
		
		return data.getConversations();
	}

    public static void clearAllConversations()
    {
        File file = new File(backupFile);
        if (file.exists())
        {
            try (FileWriter writer = new FileWriter(file))
            {
                writer.write("");
            }
            catch (IOException e) {
                log.error("{}", e.toString());
            }
        }
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
		catch (IOException e) {
            log.error("{}", e.toString());
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
			catch (IOException e) {
                log.error("{}", e.toString());
            }
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
