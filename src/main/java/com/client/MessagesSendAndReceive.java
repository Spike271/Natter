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

import raven.chatModal.ChatUI;

public class MessagesSendAndReceive
{
	private static PrintWriter output;
	private static BufferedReader input;
	private static Socket clientSocket;
	private volatile static boolean isConnected = false;
	private static final Thread listenMessageThread = new Thread(MessagesSendAndReceive::listen);
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
	
	public static boolean isConnected()
	{
		return isConnected;
	}
	
	public static void sendMessage(String sender, String receiver, String messageContent)
	{
		ChatMessage chatMessage = new ChatMessage(sender, receiver, messageContent);
		
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(chatMessage);
		
		output.println(json);
		output.flush();
	}
	
	private static void listen()
	{
		while (true)
		{
			if (!Application.natter.isVisible())
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException _)
				{}
			}
			else
				break;
		}
		
		final String user = ResourceHandler.readPropertiesFile("username").orElseThrow();
		
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
				return;
			}
		}
		
		try
		{
			isConnected = true;
			String outputMessage;
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
			while ((outputMessage = input.readLine()) != null)
			{
				try
				{
					ForwardedMessage receivedMessage = gson.fromJson(outputMessage, ForwardedMessage.class);
					String date = df.format(new Date());
					String receiver = receivedMessage.sender();
					String finalMessage = receivedMessage.message();
					UserChats.addUsersConversation(receiver, date, "receiver", finalMessage);
					
					ChatUI.sink.tryEmitNext(receiver + ": " + finalMessage);
				}
				catch (JsonSyntaxException e)
				{
					JOptionPane.showMessageDialog(null, outputMessage);
				}
			}
		}
		catch (IOException e)
		{
			if (isConnected)
			{
				JOptionPane.showMessageDialog(null, "Connection to the server was lost.");
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
			if (clientSocket != null)
			{
				if(!clientSocket.isClosed())
				{
					clientSocket.close();
				}
			}
		}
		catch (IOException _) {}
		isConnected = false;
	}
	
	record ChatMessage(String sender, String receiver, String message) {}
	
	record ForwardedMessage(String sender, String message) {}
}