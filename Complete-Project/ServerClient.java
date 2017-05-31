/**
 * @title ServerClient
 * @author Nick Fulton
 * @date 052417
 */
 

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ServerClient 
{
	// Creates all objects necessary.
	Account user;
	Command c;
	ObjectOutputStream os;
	int indexInServer;
	
	/**
	 * @title ServerClient Constructor
	 * @param myUser
	 * @param myOs
	 */
	public ServerClient(Account myUser, ObjectOutputStream myOs)
	{
		this.user = myUser;
		this.os = myOs;
		findIndex();
	}
	
	/**
	 * @title sendMessage Method
	 * @param message
	 * @param rec
	 * @desc Sends a message to the server for the account.
	 */
	public void sendMessage(String message, Account rec)
	{
		Server.accounts.get(rec.indexInServer).messages.add(new Message(message, user));
	}
	
	/**
	 * @title recieveMessage Method
	 * @desc Pulls all of the messages for this client stored on the server.
	 */
	public void recieveMessage()
	{
		try 
		{
			os.writeObject(Server.accounts.get(user.indexInServer).messages);
			Server.accounts.get(user.indexInServer).messages.clear();
			os.flush();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @title addFriend Method
	 * @param friend
	 * @throws IOException 
	 * @desc Adds each of the accounts as friends.
	 */
	public void addFriend(String friendEmail) throws IOException
	{
		Account friend;
		for(int x = 0; x < Server.accounts.size(); x++)
		{
			if(Server.accounts.get(x).getEmail().equals(friendEmail))
			{
				friend = Server.accounts.get(x);
				x = Server.accounts.size();

				user.friendList.add(Server.accounts.get(friend.indexInServer));
				Server.accounts.get(friend.indexInServer).addFriend(Server.accounts.get(user.indexInServer));
				
				os.writeObject(user.friendList);
				try {
					os.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * @title removeFriend Method
	 * @param friend
	 * @desc Removes each of the accounts from each other's friend lists.
	 */
	public void removeFriend(Account friend)
	{
		for(int i = 0; i < Server.accounts.get(friend.indexInServer).friendList.size(); i++)
		{
			if(Server.accounts.get(friend.indexInServer).friendList.get(i) == user)
			{
				Server.accounts.get(friend.indexInServer).friendList.remove(i);
			}
		}
		for(int i = 0; i < Server.accounts.get(indexInServer).friendList.size(); i++)
		{
			if(Server.accounts.get(user.indexInServer).friendList.get(i) == Server.accounts.get(friend.indexInServer))
			{
				Server.accounts.get(user.indexInServer).friendList.remove(i);
			}
		}
	}
	
	/**
	 * @title findIndex Method
	 * @desc Finds the index of this client on the Server's list.
	 */
	public void findIndex()
	{
		for(int x = 0; x < Server.accounts.size(); x++)
		{
			if(user.getActNum() == Server.accounts.get(x).getActNum())
			{
				user.indexInServer = x;
				x = Server.accounts.size();
			}
		}
	}
	
	/**
	 * @title checkOnline Method
	 * @param list
	 * @throws IOException
	 * @desc Checks to see which friends are online, and updates the friend list.
	 */
	public void checkOnline(ArrayList<Account> list) throws IOException // Checks for friends that are online
	{
		for(int i = 0; i < list.size(); i++)
		{
			for(int j = 0; j < Server.m_clientConnections.size(); j++)
			{
				if(Server.m_clientConnections.get(i).getUser().getActNum() == list.get(i).getActNum())
				{
					list.get(i).setOnlineStatus(true);
					j = Server.m_clientConnections.size();
				}
				else
				{
					list.get(i).setOnlineStatus(false);
				}
			}
		}
		
		os.writeObject(new Command("OnlineChecked", list));
		os.flush();
		
	}
	
	/**
	 * @title executeCommand Method
	 * @param myCommand
	 * @throws IOException
	 * @desc Pulls the command, and processes it.
	 */
	public void executeCommand(Command myCommand) throws IOException
	{
		this.c = myCommand;
		String type = c.getType();
	
			if(type.equals("SendMessage"))
			{
				sendMessage((String)c.getContents(), (Account)c.getRecip());
			}
			else
				if(type.equals("AddFriend"))
				{
					addFriend((String)c.getContents());
				}
				else
					if(type.equals("RemoveFriend"))
					{
						removeFriend((Account)c.getContents());
					}
					else
						if(type.equals("FriendsList"))
						{
							checkOnline(user.getFriendList());
						}
						else
							if(type.equals("RecieveMessage"))
							{
								recieveMessage();
							}
	}
}
