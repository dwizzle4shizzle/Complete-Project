
 
/**
 * @author Nick Fulton
 * @title Server Applet
 * @date 052417 
 */

import java.applet.Applet;
import java.io.IOException;

public class ServerApplet extends Applet
{
	/**
	 * 
	 */

	/**
	 * @title init Method
	 * @desc Runs on startup
	 */
	public void init()
	{
		// Launches a new server on port 8080.
		try {
			Server server = new Server(8080);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void main()
	{
		
	}
}
