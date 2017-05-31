/**
 * @author Nick Fulton
 * @title Server
 * @date 052417
 */
 

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread
{
	// Creates all the necessary objects for a server-client relationship.
	final private ServerSocket m_serverSocket;
	public static ArrayList<SubServer> m_clientConnections = new ArrayList<SubServer>();
	public static ArrayList<Account> accounts = new ArrayList<Account>();
	
	/**
	 * @title Server Method
	 * @param port
	 * @throws IOException
	 * @desc Creates a server on the given port.
	 */
	public Server(int port) throws IOException
	{
		this.m_serverSocket = new ServerSocket(port);
		start();
	}
	
	/**
	 * @title run Method
	 * @throws IOException
	 * @desc Runs on startup.
	 */
	@Override
	public void run()
	{
		while(!this.interrupted())
		{
			// Wait for clients
			Socket connection;
			try 
			{
				connection = this.m_serverSocket.accept();
				m_clientConnections.add(new SubServer(connection));
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @title SubServer
	 * @author Nick Fulton
	 * @date 052417
	 */
	protected class SubServer extends Thread
	{
		// All the required objects for a server-client relationship.
		final private Socket m_connection;
		Account user;
		ObjectInputStream is;
		ObjectOutputStream os;
		ServerClient client;
		Command c;
		
		/**
		 * @title SubServer Method
		 * @param connection
		 * @desc Creates a socket connection with an individual computer.
		 */
		public SubServer(Socket connection)
		{
			// Pull the input and output stream.
			this.m_connection = connection;
			try 
			{
				is = new ObjectInputStream(m_connection.getInputStream());
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try
			{
				os = new ObjectOutputStream(m_connection.getOutputStream());
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Start the rest of the connection proceedures.
			start();
		}
		
		/**
		 * @title run Method
		 * @desc Processes commands from the client.
		 * @throws ClassNotFoundException
		 * @throws IOException
		 */
		@Override
		public void run()
		{
			boolean found = false;
			// Pull the account when it first logs in, and write it to the known accounts.
			try 
			{
				
				user = (Account) is.readObject();
				
				if(Server.accounts.size() == 0)
				{
					
					Server.accounts.add(user);
					os.writeObject(user);
					
				}
				else
				{
					for(int x = 0; x < Server.accounts.size(); x ++)
					{
						if(user.getActNum() == Server.accounts.get(x).getActNum())
						{
							user = Server.accounts.get(x);
							os.writeObject(user);
							found = true;
						}
					}
					if(!found)
					{
						Server.accounts.add(user);
						os.writeObject(user);
					}
				}
				os.flush();
			} 
			catch (ClassNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Create a new ServerClient to process the commands.
			client = new ServerClient(user, os);
			
			// While the user is connected.
			while(!this.interrupted())
			{
				// Process a client command
				
				try 
				{
					c = (Command)is.readObject();
					if(c.getType().equals("LogOut"))
					{
						close();
					}
					else
						client.executeCommand(c);
				} 
				catch (ClassNotFoundException | IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}
		
		/**
		 * @title Account Method
		 * @return Account
		 * @desc Returns the client's account.
		 */
		public Account getUser()
		{
			return user;
		}
		
		/**
		 * @title getClient Method
		 * @return ServerClient
		 * @desc Returns the ServerClient object associated with the client.
		 */
		public ServerClient getClient()
		{
			return client;
		}
		
		/**
		 * @title close Method
		 * @throws IOException
		 * @desc Closes the connection.
		 */
		public void close()
		{
			try
			{
				this.m_connection.close();
			}
			catch(IOException e)
			{
				// Ignore
			}
		}
	}
}
