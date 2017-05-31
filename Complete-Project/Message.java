/**
 * @author Nick Fulton
 * @title Message
 * @date 052417
 */

 

public class Message 
{
	String msg;
	Account from;
	
	/**
	 * @title Message Method
	 * @param myMsg
	 * @param myFrom
	 * @desc Creates a method with the message and the account that it's from.
	 */
	public Message(String myMsg, Account myFrom)
	{
		this.msg = myMsg;
		this.from = myFrom;
	}
	
	/**
	 * @title getMsg Method
	 * @return String
	 * @desc Returns the message.
	 */
	public String getMsg()
	{
		return msg;
	}
	
	/**
	 * @title getFrom Method
	 * @return Account
	 * @desc Returns the account that the message is from.
	 */
	public Account getFrom()
	{
		return from;
	}
}
