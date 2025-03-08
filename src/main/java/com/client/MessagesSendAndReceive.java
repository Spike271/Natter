package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import raven.test.ChatUI;

public class MessagesSendAndReceive
{
	private static PrintWriter output;
	private static BufferedReader input;
	private static Socket clientSocket;
	private volatile static boolean isConnected = true;
	private static final Thread listenMessageThread = new Thread(() -> listen());
	
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
			while ((outputMessage = input.readLine()) != null)
			{
				String fullMessage = outputMessage;
				ChatUI.messageQueue.put(fullMessage);
				System.out.println(fullMessage);
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
				System.out.println("Connection to the server was lost.");
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
}