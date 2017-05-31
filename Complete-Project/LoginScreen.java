 
/**

 * @title LoginScreen Class
 * @author Nick Fulton & Drew Howell
 * @date 050517
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginScreen 
{
    private JFrame frame;
    private Dimension screenSize;
    private Icon LOGO = new ImageIcon("OyeaLogo.png");
    
    //input fields
    private JTextField usernameField = new JTextField("Username", 20);
    private JPasswordField passwordField = new JPasswordField("Password", 20);
    private JButton loginButton = new JButton("Login");
    private JButton newActButton = new JButton("New Account");
    
    public String[] args;
    
    //info being read
    private String username;
    private String password;
    private Account myAccount;
    private Account testerAccount;
    
    int count = 0;
    
    /**
     * @title LoginScreen() Constructor
     * @desc Creates the JFrame and all of it's contents.
     */
    public LoginScreen(String[] a)
    {
        username = "";
        password = "";
        loginButton.setActionCommand("login");
        newActButton.setActionCommand("newAct");
        createFrame();
        packFrame();
        myAccount = null;
        testerAccount = new Account();
        args = a;
    }
    
    /**
     * @title createFrame() method
     * @desc Builds the frame
     */
    private void createFrame()
    {        
        Container content;
        JLabel logo = new JLabel(LOGO, JLabel.CENTER);
        JLabel instructions = new JLabel("Please Login Below", JLabel.CENTER);

        
        JPanel login = new JPanel();
        login.setLayout(new FlowLayout());
        
        instructions.setBounds(100, 10, 100, 25);
        usernameField.setBounds(100, 10, 100, 25);
        passwordField.setBounds(100, 10, 100, 25);

        usernameField.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                usernameField.setText("");
            }
        });
        
        passwordField.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                passwordField.setText("");
            }
        });
        
        loginButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                readInfo();
            }
        });
        
        newActButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                newAct();
            }
        });

        // Frame Stuff
        frame = new JFrame("Oyea - Login");
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(300, 250));
        frame.setMinimumSize(new Dimension(300, 250));
        frame.setMaximumSize(new Dimension(300, 250));
        frame.setLocation(screenSize.width / 2 - 150, screenSize.height / 2 - 250);
        content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        
        // Add to the "Login" pane
        login.add(instructions);
        login.add(usernameField);
        login.add(passwordField);
        login.add(loginButton);
        login.add(newActButton);
        
        // Start adding panels
        content.add(logo, BorderLayout.NORTH);
        content.add(login, BorderLayout.CENTER);
        
    }
    
    /**
     * @title packFrame() method
     * @desc Creates an instance of the frame and makes it visible.
     */
    private void packFrame()
    {
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * @title newAct() method
     * @desc Creates a new account with the new info being entered.
     */
    private void newAct()
    {
        username = usernameField.getText();
        password = passwordField.getText();
        myAccount = new Account(username, password);
        sendAccount();
    }
    
    /**
     * @title processFrame() method
     * @desc Gets the info entered into the text fields when the login button is pressed.
     */
    private void readInfo()
    {
        //System.out.println("Nice");
        username = usernameField.getText();
        password = passwordField.getText();
        //System.out.println(username+"   "+password);
        checkAccount();
    }
    
    /**
     * @title checkAccount() method
     * @desc Checks id the account exists.
     */
    private void checkAccount()
    {
        if(testerAccount.checkAccount(username, password))//if the entered account exists
        {
            //log into main client
            myAccount = new Account(username, password);
            sendAccount();
            //System.out.println("It worked!");
        }
        else//if the entered account doesn't exist
        {
            /*
             * tell user account doesn't exist (pop-up window)
             */
            JFrame newFrame;
            Dimension screenSize;
            Container errorContent;
            JLabel instructions = new JLabel("<html>ONo! That Username or Password is not correct!<br>Please enter a new Username"+
                                            " or Password.<br>Or create a new account!</html>", JLabel.CENTER);
    
            
            JPanel notExist = new JPanel();
            notExist.setLayout(new FlowLayout());
            
            instructions.setBounds(100, 10, 100, 25);
    
            // Frame Stuff
            newFrame = new JFrame("ONo!");
            screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            
            //newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            newFrame.setPreferredSize(new Dimension(325, 100));
            newFrame.setMinimumSize(new Dimension(325, 100));
            newFrame.setMaximumSize(new Dimension(325, 100));
            newFrame.setLocation(screenSize.width / 2 - 125, screenSize.height / 2 - 225);
            errorContent = newFrame.getContentPane();
            errorContent.setLayout(new BorderLayout());
            
            // Add to the "Login" pane
            notExist.add(instructions);
            
            newFrame.pack();
            newFrame.setVisible(true);
            
            errorContent.add(notExist, BorderLayout.CENTER);
        }
    }
    
    /**
     * Sends the account.
     */
    private void sendAccount()
    {
        System.out.println("Wowee this is the main client opening");

        System.out.println(myAccount.getName());
		OyeaGUI g = new OyeaGUI(myAccount);
		g.launch(args);
        /*
         * Sends the account object created from the login screen to the main client.
         */
    }
}
