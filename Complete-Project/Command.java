/**
 * @author Nick Fulton
 * @title Command
 * @date 052417
 */
 

public class Command 
{
	String type;
	Object contents;
	Account recip;
	
	/**
	 * @title Command Method
	 * @param myType
	 * @param myContents
	 * @desc Makes a new command with just the type and contents.
	 */
	public Command(String myType, Object myContents)
	{
		this.type = myType;
		this.contents = myContents;
	}
	
	/**
	 * @title Command Method
	 * @param myType
	 * @param myContents
	 * @param myRecip
	 * @desc Makes a new command with the type, contents, and recipient.
	 */
	public Command(String myType, Object myContents, Account myRecip)
	{
		this.type = myType;
		this.contents = myContents;
		this.recip = myRecip;
	}
	
	/**
	 * @title getType Method
	 * @return String
	 * @desc Returns the type of command.
	 */
	public String getType()
	{
		return type;
	}
	
	/**
	 * @title getContents Method
	 * @return Object
	 * @desc Returns the contents of the command.
	 */
	public Object getContents()
	{
		return contents;
	}
	
	/**
	 * @title getRecip Method
	 * @return Account
	 * @desc Returns the recipient of the 
	 */
	public Account getRecip()
	{
		return recip;
	}
	
	
}
