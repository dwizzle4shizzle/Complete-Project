  

/**
 * ChatClient class for the OYea messaging project.
 * Receives objects for and sends objects from the chat client
 * 
 * @author Jessica Tian 
 * @version 1.0.0
 * 
 * http://pirate.shu.edu/~wachsmut/Teaching/CSAS2214/Virtual/Lectures/
 * http://www.oracle.com/technetwork/java/socket-140484.html#client
 */
import java.net.*;
import java.io.*;
import java.util.*;

public class ChatClient
{ 
    //instance variables
    private Socket socket              = null; 
    private Thread thread              = null; 
    private ObjectInputStream  streamIn   = null; //from server
    private ObjectOutputStream streamOut = null; //to server
    private Account account;
    private OyeaGUI cGUI;
    
    /**
     * constructor ChatClient - creates a socket to connect to the server
     * 
     * @param   serverName  name of the server
     * @param   serverPort  the port number
     * @param   account     the account to be logged in
     * @param   cgui        the client gui
     */
    public ChatClient(String serverName, int serverPort, Account account, OyeaGUI cgui)
    {
        cGUI.appendMessage("Establishing connection. Please wait ...");
        
        this.cGUI = cgui;
        try
        {
            socket = new Socket(serverName, serverPort); 
            cGUI.appendMessage("Connected: " + socket);
            start();
        }
        catch(UnknownHostException uhe)
        {
            cGUI.appendMessage("Host unknown: " + uhe.getMessage());
        }
        catch(IOException ioe)
        {
           cGUI.appendMessage("Unexpected exception: " + ioe.getMessage());
        }
    }
    
    /**
     * Starts communication with the server and logs the client in
     * 
     * @return  boolean true if successfully starts
     */
    public boolean start() throws IOException
    {
        MsgRefresher mRefresh = new MsgRefresher();
        
        try
        {
            streamIn   = new ObjectInputStream(socket.getInputStream());
            streamOut = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException eIO)
        {
            cGUI.appendMessage("Exception creating new Input/output Streams: " + eIO);
            return false;
        }
       
        // Send the current account to be updated
        try
        {
            streamOut.writeObject(account);
            account = (Account) streamIn.readObject(); //receive updated account
        }
        catch(ClassNotFoundException cnf)
        {
            cGUI.appendMessage("Exception doing login : " + cnf.getMessage());
            stop();
            return false;
        }
        catch (IOException eIO)
        {
            System.out.print("Exception doing login : " + eIO);
            stop();
            return false;
        }
        
        //creates a thread to refresh new messages
        mRefresh.start();

        return true; //successfully log in
    }
    
    /**
     * Terminates connection with the server and closes the socket
     */
    public void stop()
    {
        try
        {
            if (streamIn   != null)
                streamIn.close();
            if (streamOut != null)
                streamOut.close();
            if (socket    != null)
                socket.close();
        }
        catch(IOException ioe)
        {
            cGUI.appendMessage("Error closing ...");
        }
    }
    
    /**
     * Sends messages to the GUI, with info about the sender, to be displayed in the JTextArea
     * 
     * @param   msg     the message to be appended to the Client GUI JTextArea
    */
    public void displayMessage(Message msg)
    {
         cGUI.appendMessage(msg);//append to the ClientGUI JTextArea
    }
    
    /**
     * Sends the message from the client to all of the clients online
     * 
     * @param   msg         the message to be sent
     */
    public void sendMessage(Message msg)
    {
        try
        {
            Command c = new Command("SendMessage", msg);
            streamOut.writeObject(c);
            streamOut.flush();
        }
        catch(IOException ioe)
        {
            cGUI.appendMessage("Error sending message :" + ioe.getMessage());
        }
    }
    
    /**
     * Sends the message from the client to the given recipient
     * 
     * @param   msg         the message to be sent
     * @param   recipient   the recipient of the message
     */
    public void sendMessage(Message msg, Account recipient)
    {
        try
        {
            Command c = new Command("SendMessage", msg, recipient);
            streamOut.writeObject(c);
            streamOut.flush();
        }
        catch(IOException ioe)
        {
            cGUI.appendMessage("Error sending message :" + ioe.getMessage());
        }
    }
    
    /**
     * Sends the username of the friend the client would like to add
     * 
     * @param   username    the username of the user who the client would like to be friends with
     */
    public void addFriend(String username)
    {
        try
        {
            Command c = new Command("AddFriend", username);
            streamOut.writeObject(c);
            streamOut.flush();
        }
        catch(IOException ioe)
        {
            cGUI.appendMessage("Error adding friend :" + ioe.getMessage());
        }
        
        try
        {
            account.updateFriends((ArrayList) streamIn.readObject());
        }
        catch(IOException ioe)
        {
            cGUI.appendMessage("Error adding friend :" + ioe.getMessage());
        }
        catch(ClassNotFoundException e2)
        {
        }
    }
    
    /**
     * Sends the username of the friend the client would like to remove
     * 
     * @param   username    the username of the user the client no longer wants to be friends with
     */
    public void removeFriend(String username)
    {
        try
        {
            Command c = new Command("RemoveFriend", username);
            streamOut.writeObject(c);
            streamOut.flush();
        }
        catch(IOException ioe)
        {
            cGUI.appendMessage("Error sending message :" + ioe.getMessage());
        }
        
        try
        {
            account.updateFriends((ArrayList) streamIn.readObject());
        }
        catch(IOException ioe)
        {
            cGUI.appendMessage("Error adding friend :" + ioe.getMessage());
        }
        catch(ClassNotFoundException e2)
        {
        }
    }
    
    /**
     * Lets the server know that the client is logging out and then stops the client
     */
    public void logOut()
    {
        try
        {
            Command c = new Command("LogOut", account);
            streamOut.writeObject(c);
            streamOut.flush();
        }
        catch(IOException ioe)
        {
            cGUI.appendMessage("Error sending message :" + ioe.getMessage());
        }
        
        stop();
    }
    
    class MsgRefresher extends Thread
    {
        public void run()
        {
            while(true)
            {
                try
                {
                    //ask for new messages
                    
                    //if there are new messages then send them to client gui
                    String msg = (String) streamIn.readObject();
                    // if console mode print the message and add back the prompt
                    
                    cGUI.appendMessage(msg);
                }
                catch(IOException e)
                {
                    cGUI.appendMessage("Server has close the connection: " + e);
                    //if(cgui != null)
                    //cgui.connectionFailed();
                    //break;
                }
                // can't happen with a String object but need the catch anyhow
                catch(ClassNotFoundException e2)
                {
                }
            }
        }
    }
}


