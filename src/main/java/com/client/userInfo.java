package com.client;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class userInfo
{
	private static final String backupFile = "../../res/backup/Receivers.json";
	
	public static void addNewUser(String user, String time)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		File file = new File(userInfo.class.getResource(backupFile).getFile());
		
		List<Receiver> receivers = readOrCreateReceivers(gson, file);
		
		Receiver newReceiver = new Receiver(user, time);
		receivers.add(newReceiver);
		
		writeReceiversToFile(gson, file, receivers);
	}
	
	public static ArrayList<Receiver> readExistingUsers()
	{
		Gson gson = new GsonBuilder().create();
		File file = new File(userInfo.class.getResource(backupFile).getFile());
		ArrayList<Receiver> users = new ArrayList<Receiver>();
		
		if (file.exists())
		{
			try (FileReader reader = new FileReader(file))
			{
				ReceiversWrapper wrapper = gson.fromJson(reader, ReceiversWrapper.class);
				
				if (wrapper != null)
				{
					List<Receiver> receivers = wrapper.Receivers;
					
					for (Receiver receiver : receivers)
						users.add(receiver);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			return users;
		}
		else
		{
			System.err.println("File 'receivers.json' not found.");
			return null;
		}
	}
	
	private static List<Receiver> readOrCreateReceivers(Gson gson, File file)
	{
		List<Receiver> receivers = new ArrayList<>();
		if (file.exists())
		{
			try (FileReader reader = new FileReader(file))
			{
				ReceiversWrapper wrapper = gson.fromJson(reader, ReceiversWrapper.class);
				if (wrapper != null && wrapper.Receivers != null)
				{
					receivers = wrapper.Receivers;
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return receivers;
	}
	
	private static void writeReceiversToFile(Gson gson, File file, List<Receiver> receivers)
	{
		try (FileWriter writer = new FileWriter(file))
		{
			ReceiversWrapper wrapper = new ReceiversWrapper();
			wrapper.Receivers = receivers;
			gson.toJson(wrapper, writer);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	static class ReceiversWrapper
	{
		List<Receiver> Receivers = new ArrayList<>();
	}
	
	static class Receiver
	{
		private String time;
		private String name;
		
		public Receiver(String name, String time)
		{
			this.name = name;
			this.time = time;
		}
		
		public String getTime()
		{
			return time;
		}
		
		public String getName()
		{
			return name;
		}
	}
}