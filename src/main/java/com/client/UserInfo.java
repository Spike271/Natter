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
import java.util.List;

public class UserInfo
{
	private static final String backupFile = Application.jarFilePath + "res/backup/Receivers.json";
    private static final Logger log = LoggerFactory.getLogger(UserInfo.class);

    public static void addNewUser(String user, String time)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		File file = new File(backupFile);
		
		List<Receiver> receivers = readOrCreateReceivers(gson, file);
		
		Receiver newReceiver = new Receiver(user, time);
		receivers.add(newReceiver);
		
		writeReceiversToFile(gson, file, receivers);
	}
	
	public static ArrayList<Receiver> readExistingUsers()
	{
		Gson gson = new GsonBuilder().create();
		File file = new File(backupFile);
		ArrayList<Receiver> users = new ArrayList<>();
		
		if (file.exists())
		{
			try (FileReader reader = new FileReader(file))
			{
				ReceiversWrapper wrapper = gson.fromJson(reader, ReceiversWrapper.class);
				
				if (wrapper != null)
				{
					List<Receiver> receivers = wrapper.Receivers;
					if (receivers != null)
					{
                        users.addAll(receivers);
					}
				}
			}
			catch (IOException e) {
			    log.error(e.toString());
            }
			return users;
		}
		else
		{
			log.error("File 'receivers.json' not found.");
			return new ArrayList<>(0);
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
			catch (IOException _) {
                receivers = new ArrayList<>(0);
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
		catch (IOException e) {
            log.error(e.toString());
        }
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
                log.error(e.toString());
            }
        }
    }
	
	static class ReceiversWrapper
	{
		List<Receiver> Receivers = new ArrayList<>();
	}
	
	public record Receiver(String name, String time) {}
}