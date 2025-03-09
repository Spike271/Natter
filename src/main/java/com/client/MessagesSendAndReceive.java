package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import raven.test.ChatUI;

public class MessagesSendAndReceive
{
	private static PrintWriter output;
	private static BufferedReader input;
	private static Socket clientSocket;
	private volatile static boolean isConnected = true;
	private static final Thread listenMessageThread = new Thread(() -> listen());
	private static final Gson gson = new GsonBuilder().create();
	
	public static void startMessageListening()
	{
		if (!listenMessageThread.isAlive())
		{
			listenMessageThread.start();
		}
	}
	
	public static void stopMessageListening()
	{
		closeConnection();
	}
	
	public static void sendMessage(String sender, String receiver, String messageContent)
	{
		ChatMessage chatMessage = new ChatMessage(sender, receiver, messageContent);
		
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(chatMessage);
		
		output.println(json);
		output.flush();
		System.out.println(json);
	}
	
	private static void listen()
	{
		while (true)
		{
			if (!NatterMain.natter.isVisible())
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{}
			}
			else
				break;
		}
		
		final String user = ResourceHandler.readPropertiesFile("username");
		
		if (clientSocket == null)
		{
			try
			{
				clientSocket = new Socket("localhost", 8000);
				input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				output = new PrintWriter(clientSocket.getOutputStream(), true);
				input.readLine();
				output.println(user);
			}
			catch (Exception _)
			{
				JOptionPane.showMessageDialog(null, "Unable to connect with server.");
			}
		}
		
		try
		{
			String outputMessage;
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
			while ((outputMessage = input.readLine()) != null)
			{
				try
				{
					ForwardedMessage receivedMessage = gson.fromJson(outputMessage, ForwardedMessage.class);
					String date = df.format(new Date());
					String receiver = receivedMessage.getSender();
					String finalMessage = receivedMessage.getMessage();
					userChats.addUsersConversation(receiver, date, "receiver", finalMessage);
					
					ChatUI.messageQueue.put(receiver + ": " + finalMessage);
					System.out.println(finalMessage);
				}
				catch (JsonSyntaxException e)
				{
					System.err.println("Invalid message format: " + outputMessage);
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
		catch (InterruptedException e)
		{
			if (isConnected)
			{
				System.out.println("Connection interrupted.");
				isConnected = false;
			}
		}
		finally
		{
			closeConnection();
		}
	}
	
	private static void closeConnection()
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
	
	static class ChatMessage
	{
		private String sender;
		private String receiver;
		private String message;
		
		public ChatMessage(String sender, String receiver, String message)
		{
			this.sender = sender;
			this.receiver = receiver;
			this.message = message;
		}
		
		public String getSender()
		{
			return sender;
		}
		
		public String getReceiver()
		{
			return receiver;
		}
		
		public String getMessage()
		{
			return message;
		}
	}
	
	static class ForwardedMessage
	{
		private String sender;
		private String message;
		
		public String getSender()
		{
			return sender;
		}
		
		public String getMessage()
		{
			return message;
		}
	}
}