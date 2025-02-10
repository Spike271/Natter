import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Server
{
	private static final int PORT = 8000;
	private static Map<String, ClientHandler> clients = new HashMap<>();
	private static Map<Socket, String> clientSockets = new HashMap<>();
	
	public static void main(String[] args)
	{
		System.out.println("Chat Server started...");
		try (ServerSocket serverSocket = new ServerSocket(PORT))
		{
			while (true)
			{
				Socket clientSocket = serverSocket.accept();
				System.out.println("New client connected");
				ClientHandler clientHandler = new ClientHandler(clientSocket);
				new Thread(clientHandler).start();
				clientSockets.put(clientSocket, null); // Initially, no client name is associated
			}
		}
		catch (IOException e)
		{}
	}
	
	static class ClientHandler implements Runnable
	{
		private Socket clientSocket;
		private String clientName;
		private BufferedReader input;
		private PrintWriter output;
		
		public ClientHandler(Socket clientSocket)
		{
			this.clientSocket = clientSocket;
			try
			{
				this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				this.output = new PrintWriter(clientSocket.getOutputStream(), true);
				
				// Ask for the client's name
				output.println("Enter your name: ");
				clientName = input.readLine();
				
				if (clientName != null && !clientName.trim().isEmpty())
				{
					synchronized (clients)
					{
						clients.put(clientName, this);
					}
					output.println("Welcome " + clientName + "!" + "\nEnter the message:");
					clientSockets.put(clientSocket, clientName); // Associate the client socket with the name
				}
				else
				{
					output.println("Invalid name, disconnecting.");
					clientSocket.close();
				}
			}
			catch (IOException e)
			{}
		}
		
		public void run()
		{
			String message;
			try
			{
				while ((message = input.readLine()) != null)
				{
					String[] parts = message.split(" ");
					String recipient = parts[0];
					String text = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
					
					if (recipient.equalsIgnoreCase("disconnect"))
					{
						output.println("disconnected");
						break;
					}
					else if (clients.containsKey(recipient))
					{
						ClientHandler recipientClient = clients.get(recipient);
						if (recipientClient != this)
						{
							recipientClient.output.println(clientName + " says: " + text);
						}
					}
					else
					{
						output.println("User not found.");
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				disconnect();
			}
		}
		
		private void disconnect()
		{
			try
			{
				clientSocket.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			synchronized (clients)
			{
				if (clientName != null)
				{
					clients.remove(clientName);
				}
			}
			synchronized (clientSockets)
			{
				clientSockets.remove(clientSocket);
			}
		}		
	}
}