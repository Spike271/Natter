package com.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raven.chatModal.ChatUI;
import raven.toast.Notifications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessagesSendAndReceive
{
    private static final Logger log = LoggerFactory.getLogger(MessagesSendAndReceive.class);
    private static PrintWriter output;
	private static BufferedReader input;
	private static Socket clientSocket;
	private volatile static boolean isConnected = false;
	private static final Thread listenMessageThread = new Thread(MessagesSendAndReceive::listen);
    private static final java.time.Duration UI_POLL_INTERVAL = java.time.Duration.ofSeconds(1);
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
        waitUntilNatterVisible();
        if (Thread.currentThread().isInterrupted()) return;

        final String user = Application.user.username();
        Notifications.getInstance().setJFrame(Application.natter);

		if (clientSocket == null)
		{
			try
			{
				clientSocket = new Socket("localhost", 8000);
				input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				output = new PrintWriter(clientSocket.getOutputStream(), true);
				input.readLine();
				output.println(user);
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER,
                        "Successfully connected to the server");
			}
			catch (Exception _)
			{
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER,
                        "Unable to connect with server.");
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
				catch (JsonSyntaxException e) {
                    log.error("{}", e.toString());
                }
			}
		}
		catch (IOException e)
		{
			if (isConnected)
			{
                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, "Connection to the server was lost.");
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
		catch (IOException e) {
            log.error("{}, {}", "Unable to close the connection!\n", e.toString());
        }

		isConnected = false;
	}

    private static void waitUntilNatterVisible()
    {
        while (!Application.natter.isVisible())
        {
            try
            {
                Thread.sleep(UI_POLL_INTERVAL);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    record ChatMessage(String sender, String receiver, String message) {}
	
	record ForwardedMessage(String sender, String message) {}
}