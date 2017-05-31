 
/**
 * Account class for the OYea messaging project.
 * Contains info about a single account whose info is saved to a database containing every account.
 * 
 * @author Drew Howell 
 * @version 1.0.0
 */
import java.util.*;
import java.sql.*;

public class Account
{
    //private instance variables
    private String myName;
    private String myEmail;//used for login
    private String myPassword;//used for login
    private String myProfilePic;
    private int myActNum;
    private boolean isOnline;
    private LinkedList<Account> myRequests;
    
    //public instance variables
    public ArrayList messages;
    public ArrayList friendList;
    public int indexInServer;
    
    //database variables
    private final String url = "jdbc:mysql://sql3.freemysqlhosting.net:3306/";
    private final String database = "sql3174116";
    private final String driver = "com.mysql.jdbc.Driver";
    private final String username = "sql3174116";
    private final String password = "2vYrUmtIXf";
    
    /**
     * Defauls constructor.
     */
    public Account()
    {
        myName = "";
        myEmail = "";
        myPassword = "";
        myProfilePic = "";
        myActNum = getActNum();
        myRequests = new LinkedList<Account>();
        isOnline = false;
        friendList = new ArrayList();
        messages = new ArrayList();
        //saveAll();
    }
    
    /**
     * Basic constructor, requires at least an email so that a row on the database can be created for the rest
     * of this user's info can be loaded in the correct spot.
     */
    public Account(String email, String pass)
    {
        myEmail = email;
        myPassword = pass;
        myName = "";
        myProfilePic = "";
        myActNum = getActNum();
        myRequests = new LinkedList<Account>();
        isOnline = false;
        friendList = new ArrayList();
        messages = new ArrayList();
        saveAll();
        /*if(checkAccount(email, pass) == false)//if the account doesnt exist
        {
            saveNewAct();
            myName = "";
            myProfilePic = "";
            myActNum = getActNum();
            myRequests = new LinkedList<Account>();
            isOnline = false;
            friendList = new ArrayList();
            messages = new ArrayList();
        }
        
        //else//if the acount does exist
        {
            Account temp = getDBInfo(getActNum());
            myName = temp.getName();
            myProfilePic = temp.getProfilePic();
            myActNum = getActNum();
            myRequests = new LinkedList<Account>();
            isOnline = false;
            friendList = new ArrayList();
            messages = new ArrayList();
            //saveAll();
        }
        */

            
    }
    
    /**
     * Account constructor
     * 
     * @param   name    The user's name.
     *          email   The user's email.
     *          pass    The user's password.
     *          pic     The filepath to the user's profile picture.
     */
    public Account(String name, String email, String pass, String pic)
    {
        myName = name;
        myEmail = email;
        myPassword = pass;
        myProfilePic = pic;
        myActNum = getActNum();
        isOnline = false;
        friendList = new ArrayList();
        messages = new ArrayList();
        saveAll();
    }
    
    //accessors
    /**
     * Gives this account's account number.
     * 
     * @return  myActNum    This account's email converted to a hash code with String.hashCode().
     */
    public int getActNum(){return myEmail.hashCode();}

    /**
     * Accessor method for the user's name.
     * 
     * @return  myName  This user's current name.
     */
    public String getName(){return myName;}
    
    /**
     * Setter method for the user's name.
     *
     * @param   newName This user's new name.
     */
    public void setName(String newName){myName = newName; saveName();}
    
    /**
     * Accessor method for the user's password.
     * 
     * @return  myPassword  This user's current password.
     */
    public String getPassword(){return myPassword;}
    
    /**
     * Setter method for the user's password.
     *
     * @param   newPass This user's new password.
     */
    public void setPassword(String newPass){myPassword = newPass; savePassword();}
    
    /**
     * Accessor method for the user's email in string format.
     *
     * @return  myEmail This user's current email.
     */
    public String getEmail(){return myEmail;}
    
    /**
     * Setter method for this user's email.
     * 
     * @param   email   Thius user's email.
     */
    private void setEmail(String email){myEmail = email;}
    
    /**
     * Getter method for the user's profile picture.
     * 
     * @return  myPicture   This filepth to this user's current profile picture.
     */
    
    public String getProfilePic(){return myProfilePic;}
    
    /**
     * Setter method for this user's profile picture.
     * 
     * @param   newPic  The fielpath to the new profile picture to be set.
     */
    public void setProfilePic(String newPic){myProfilePic = newPic; savePicture();}
    
    /**
     * Sets the online status of this user.
     * 
     * @param   status  True if user is online, false if not.
     */
    public void setOnlineStatus(boolean status){isOnline = status;}
    
    /**
     * Returns if this account is online or not.
     * 
     * @return  isOnline    True if user is online, falshe if not.
     */
    public boolean getOnlineStatus(){return isOnline;}
    
    /**
     * Returns a LinkedList of accounts that are requesting to be friends with this user.
     * 
     * @return  myRequests  The accounts that want to be friends with this user.
     */
    public LinkedList<Account> getRequests(){return myRequests;}
    
    /**
     * Adds a new account to the list of accounts requesting friendship with this user.
     * 
     * @param   newRequest  The account to be added to the list of requests.
     */
    public void addRequest(Account newRequest){myRequests.add(newRequest);}
    
    /**
     * Updates the friend's list.
     */
    public void updateFriends(ArrayList newList)
    {
        friendList = newList;
    }
    
    /**
     * Accessor method for the user's list of friends.
     *
     * @return  friendList  A list of this user's confirmed friends.
     */
    public ArrayList getFriendList()
    {
        /*
        ArrayList newFriendList = new ArrayList();
        newFriendList.add(new Account());
        
        //load accounts into the new list
        for(int x = 0; x < friendList.size(); x++)
        {
            newFriendList.add(getDBInfo((Integer)friendList.get(x)));
        }
        
        //sort by name
        for(int outer = 1; outer < newFriendList.size(); outer++)
        {
            int position = outer;
            String key = ((Account)newFriendList.get(position)).getName();
            while(position > 0 && ((Account)newFriendList.get(position - 1)).getName().compareTo(key) > 0)//newFriendList.get(position - 1) > key
            {
                newFriendList.set(position, newFriendList.get(position - 1));
                position--;
            }
            newFriendList.set(position, key);
        }
        return newFriendList;
        */
        return friendList;
    }
    
    /**
     * Adds a new friend to the list of this user's confirmed friends.
     * 
     * @param   newFriend   The new friend to be added.
     */
    public void addFriend(Account newFriend){friendList.add(newFriend.getActNum());}
    
    //database interaction methods
    /**
     * Reads info from database and assembles it into a complete account object.
     * 
     * @param   localNum    The account number who's info is being loaded.
     * @return  newAct      The account being assembled form the database info.
     */
    private Account getDBInfo(int localNum)
    {
        Account newAct = null;
        ResultSet result;
        String name = "";
        String email = "";
        String pass = "";
        String pic = "";
        //save info in this Account to the database
        try
        {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url+database,username,password);
            Statement state = conn.createStatement();
            
            result = state.executeQuery("SELECT Name FROM Accounts WHERE ActNum="+getActNum()%251+";");
            name = result.getString(1);
            result = state.executeQuery("SELECT Email FROM Accounts WHERE ActNum="+getActNum()%251+";");
            email = result.getString(1);
            result = state.executeQuery("SELECT Password FROM Accounts WHERE ActNum="+getActNum()%251+";");
            pass = result.getString(1);
            result = state.executeQuery("SELECT Picture FROM Accounts WHERE ActNum="+getActNum()%251+";");
            pic = result.getString(1);
            newAct = new Account(name, email, pass, pic);
            
            state.close();
            result.close();
            conn.close();
        }
        catch(SQLException se)
        {
            while(se != null)
            {
                System.out.println( "State  : " + se.getSQLState()) ;
                System.out.println( "Message: " + se.getMessage()) ;
                System.out.println( "Error  : " + se.getErrorCode()) ;
                se = se.getNextException();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        //reading database and loading data into newAct
        return newAct;
    }
    
    /**
     * Checks if an account exists in the database.
     * 
     * @param   pass    The password to be checked.
     *          email   The username to be checked.
     * @return  isAct   True if the account exists, false if not.          
     */
    public boolean checkAccount(String email, String pass)
    {
        ResultSet emailResult;
        ResultSet passResult;
        boolean isAct = false;
        //save info in this Account to the database
        try
        {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url+database,username,password);
            Statement state1 = conn.createStatement();
            Statement state2 = conn.createStatement();
            emailResult = state1.executeQuery("SELECT Email FROM Accounts WHERE Email='"+email+"';");
            passResult = state2.executeQuery("SELECT Password FROM Accounts WHERE Password='"+pass+"';");
            
            if((emailResult.first() ==true && passResult.first() == true) && (emailResult.getString("Email")).equals(email) && (passResult.getString("Password")).equals(pass))//if the account doesnt already exists in the database
            {
               isAct = true;
            }
            
            else//if the account doesn't exist
            {
                //state.executeUpdate("INSERT INTO Accounts (ActNum, Name, Email, Password) VALUES ("+getActNum()+
                //                    ",'"+getName()+"','"+getEmail()+"','"+getPassword()+"','"+getProfilePic()+"');");
                isAct = false;
            }
            
            state1.close();
            state2.close();
            emailResult.close();
            passResult.close();
            conn.close();
        }
        catch(SQLException se)
        {
            while(se != null)
            {
                System.out.println( "State  : " + se.getSQLState()) ;
                System.out.println( "Message: " + se.getMessage()) ;
                System.out.println( "Error  : " + se.getErrorCode()) ;
                se = se.getNextException();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        return isAct;
    }
    
    /**
     * Creates a new account in the database with only the user's email and password.
     */
    private void saveNewAct()
    {
        
        try
        {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url+database,username,password);
            Statement state = conn.createStatement();
            
            state.executeUpdate("INSERT INTO Accounts (ActNum, Email, Password) VALUES ("+getActNum()%251+"','"+getEmail()+"','"
                            +getPassword()+"');");
            state.close();
            conn.close();
        }
        catch(SQLException se)
        {
            while(se != null)
            {
                System.out.println( "State  : " + se.getSQLState()) ;
                System.out.println( "Message: " + se.getMessage()) ;
                System.out.println( "Error  : " + se.getErrorCode()) ;
                se = se.getNextException();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    /**
     * Saves all the data in this Account to the database. Does not require the account ot already exist in the database.
     * If the account is not already in the database, a new entry is created with this accout's info.
     */
    private void saveAll()
    {
        ResultSet result;
        //save info in this Account to the database
        try
        {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url+database,username,password);
            Statement state = conn.createStatement();
            result = state.executeQuery("SELECT ActNum FROM Accounts WHERE ActNum = "+getActNum()%251+";");

            if(result.first() == false || result.getInt("ActNum") != getActNum()%251)//if the account doesnt already exists in the database
            {                    
                state.executeUpdate("INSERT INTO Accounts (ActNum, Name, Email, Password, Picture) VALUES ("+getActNum()%251+
                                    ",'"+getName()+"','"+getEmail()+"','"+getPassword()+"','"+getProfilePic()+"');");
            }
            
            else//if the account exists in the database -> save email and password
            {
                state.executeUpdate("UPDATE Accounts SET Name = '"+getName()+"', Email = '"+getEmail()+"', Password = '"+getPassword()+
                                    "', Picture = '"+getProfilePic()+"' WHERE ActNum = "+getActNum()%251+";");
                
                //state.executeUpdate("UPDATE Accounts SET Email = '"+getEmail()+"', Password = '"+getPassword()+
                //                    "' WHERE ActNum = "+getActNum()+";");
            }
            state.close();
            result.close();
            conn.close();
        }
        catch(SQLException se)
        {
            while(se != null)
            {
                System.out.println( "State  : " + se.getSQLState()) ;
                System.out.println( "Message: " + se.getMessage()) ;
                System.out.println( "Error  : " + se.getErrorCode()) ;
                se = se.getNextException();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    /**
     * Saves Name in this Account to the database.
     * Requires the account to already exist in the database.
     */
    private void saveName()
    {
        //save info in this Account to the database
        try
        {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url+database,username,password);
            Statement state = conn.createStatement();

            state.executeUpdate("UPDATE Accounts SET Name = '"+getName()+"' WHERE ActNum = "+getActNum()%251+";");
            
            state.close();
            conn.close();
        }
        catch(SQLException se)
        {
            while(se != null)
            {
                System.out.println( "State  : " + se.getSQLState()) ;
                System.out.println( "Message: " + se.getMessage()) ;
                System.out.println( "Error  : " + se.getErrorCode()) ;
                se = se.getNextException();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    /**
     * Saves the Password in this Account to the database.
     * Requires the account to already exist in the database.
     */
    private void savePassword()
    {
        //save info in this Account to the database
        try
        {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url+database,username,password);
            Statement state = conn.createStatement();

            state.executeUpdate("UPDATE Accounts SET Password = '"+getPassword()+"' WHERE ActNum = "+getActNum()%251+";");
            
            state.close();
            conn.close();
        }
        catch(SQLException se)
        {
            while(se != null)
            {
                System.out.println( "State  : " + se.getSQLState()) ;
                System.out.println( "Message: " + se.getMessage()) ;
                System.out.println( "Error  : " + se.getErrorCode()) ;
                se = se.getNextException();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    /**
     * Saves the location of the profile picture in this Account to the database.
     * Requires the account to already exist in the database.
     */
    private void savePicture()
    {
        //save info in this Account to the database
        try
        {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url+database,username,password);
            Statement state = conn.createStatement();

            state.executeUpdate("UPDATE Accounts SET Picture = '"+getProfilePic()+"' WHERE ActNum = "+getActNum()%251+";");
            
            state.close();
            conn.close();
        }
        catch(SQLException se)
        {
            while(se != null)
            {
                System.out.println( "State  : " + se.getSQLState()) ;
                System.out.println( "Message: " + se.getMessage()) ;
                System.out.println( "Error  : " + se.getErrorCode()) ;
                se = se.getNextException();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    /**
     * Deletes this account's info from the database.
     */
    public void deleteAccount()
    {
        //save info in this Account to the database
        try
        {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url+database,username,password);
            Statement state = conn.createStatement();

            state.executeUpdate("DELETE FROM Accounts [WHERE ActNum = "+getActNum()%251+"];");
            
            state.close();
            conn.close();
        }
        catch(SQLException se)
        {
            while(se != null)
            {
                System.out.println( "State  : " + se.getSQLState()) ;
                System.out.println( "Message: " + se.getMessage()) ;
                System.out.println( "Error  : " + se.getErrorCode()) ;
                se = se.getNextException();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
}    