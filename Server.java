import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server
{
	private static final int PORT = 8000;
	private static final String END_OF_MESSAGE = "END_OF_MESSAGE"; // Delimiter for multi-line messages
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
		{
			e.printStackTrace();
		}
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
				
				output.println("Enter your name: ");
				clientName = input.readLine();
				
				if (clientName != null && !clientName.trim().isEmpty())
				{
					synchronized (clients)
					{
						clients.put(clientName, this);
					}
					clientSockets.put(clientSocket, clientName); // Associate the client socket with the name
					output.println("Welcome, " + clientName + "! You can now send messages.");
				}
				else
				{
					output.println("Invalid name, disconnecting.");
					clientSocket.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		public void run()
		{
			try
			{
				StringBuilder messageBuilder = new StringBuilder();
				String line;
				
				while ((line = input.readLine()) != null)
				{
					if (line.equals(END_OF_MESSAGE))
					{
						String fullMessage = messageBuilder.toString();
						processMessage(fullMessage + "END_OF_MESSAGE");
						messageBuilder.setLength(0);
					}
					else
					{
						messageBuilder.append(line).append("\n");
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
		
		private void processMessage(String message)
		{
			String[] lines = message.split("\n", 2); // Split into 2 parts: recipient and text
			
			if (lines.length < 1)
			{
				output.println("Invalid message format.");
				return;
			}
			
			String recipient = lines[0].trim(); // First line is the recipient
			String text = (lines.length > 1) ? lines[1] : ""; // Remaining lines are the message
			
			if (recipient.equalsIgnoreCase("disconnect"))
			{
				output.println("Disconnecting...");
				disconnect();
			}
			else if (clients.containsKey(recipient))
			{
				ClientHandler recipientClient = clients.get(recipient);
				if (recipientClient != this)
				{
					recipientClient.output.println(text);
				}
			}
			else
			{
				output.println("Recipient '" + recipient + "' not found.");
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
			System.out.println(clientName + " has disconnected.");
		}
	}
}