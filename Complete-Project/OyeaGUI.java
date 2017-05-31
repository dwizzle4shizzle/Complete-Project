/**
 * Class Summary - The Main GUI for the Oyea Messenger
 * @author(Kevyn Higbee)
 * @version(0.01)
 */

import javafx.application.Platform;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.Group;
import javafx.scene.layout.FlowPane;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.collections.ObservableList;
public class OyeaGUI extends Application
{
    private boolean DEBUG = true;  // this doesn't do anything right now
    
    // Formatting variables
    /** Main Stage Resizable Window */
    private final boolean allowResize = false;
    /** Horizontal Resolution for GUI */
    private final int hRes = 1080;
    /** Vertical Resolution for GUI */
    private final int vRes = 720;
    /** Resolution for Friend Window */
    private final int friendWinRes = 400;
    /** Resolution for the profile picture */
    private final int profilePicRes = 125;
    /** Resolution for the contact  profile picture */
    private final int contactPicRes = 50;
    /** Minimum Width of Contact Buttons */
    private final int contactMinWidth = 250;
    /** Maximum Width of Contact Buttons */
    private final int contactMaxWidth = 250;
    /** Number of rows in textBox */
    private final int numRows = 5;
    /** Number of collumns in textBox */
    private final int numCols = 50;
    /** Padding size for the Friend Window */
    private final int friendWindowPadding = 50;
    /** defualt size of conversation pane */
    private final int vpHeight = 555;
    /** port for the server */
    private final int serverPort = 8080;
    /** server name/address */
    private final String serverName = "localhost";
    // objects being displayed
    /** pane holding all the other panes */
    private GridPane mainPane = new GridPane();
    /** the pane that displays the account name and profile picture */
    private GridPane accountInfoPane; 
    /** the pane showing each contact */
    private ScrollPane contactPane;
    /** the pane with logout and add friend buttons */
    private GridPane buttonPane;
    /** the text area where sent messages will recieve input */
    private TextArea textBox;
    /** stack of panes accessed through contactPane buttons*/
    private StackPane conversationStack;
    /** the current visible pane in conversationStack */
    private Node currentNode = new GridPane();
    /** the current conversation pane index */
    private int index = 0;
    /** the previous conversation pane index */
    private int prevIndex;
    /** the previous pane in the list */
    private Node previousNode;
    /** Scene */
    private Scene scene;
    /** Stage for the main window */
    private Stage mainStage;
    /** The signed in account */
    private Account signedInAccount;
    /** pane where chat is displayed */
    private VBox vBox;
    /** Chat client */
    ChatClient client;
    /**
     * Constructor for OyeaGUI class
     * @param acc - the account that is signed in
     */
    public OyeaGUI(Account acc)
    {
        signedInAccount = acc;
        client = new ChatClient(serverName, serverPort , acc, this);
        client.start();
        initializePanes();
    }
    public OyeaGUI()
    {
        signedInAccount = new Account();
        initializePanes();
    }
    /**
     * Main method for launching gui
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    /**
     * creates and displays gui
     */
    @Override
    public void start(Stage primaryStage)
    {
        ArrayList friends = new ArrayList();
        signedInAccount = new Account();
        signedInAccount.setName("First Last");
        
        for(int x = 0; x < 50; x++)
        {
            Account tmp = new Account();
            tmp.setName("Name " + x);
            signedInAccount.addFriend(tmp);
        }


        mainStage = primaryStage;
        mainStage.setTitle("Oyea Messenger");
        mainStage.getIcons().add(new Image("/images/OyeaLogo_t.png"));
        initializePanes();
        addPanes(mainStage);
        mainStage.setResizable(allowResize);
        mainStage.show();
    }
    // creates every pane and adds them to the anchor pane
    private void initializePanes()
    {
        // create all panes
        initAccountInfoPane();
        initContactPane();
        initButtonPane();
        initConversationStack();
    }
    // adds all panes onto the anchor
    private void addPanes(Stage stage)
    {
        GridPane leftPane = new GridPane();
        leftPane.setGridLinesVisible(DEBUG);
        leftPane.add(accountInfoPane,0,0);
        leftPane.add(contactPane,0,1);
        leftPane.add(buttonPane,0,2);
        
        mainPane.add(leftPane,0,0);
        currentNode = friendRequestPane();
        vBox = getVBox((GridPane)currentNode);
        mainPane.add(currentNode,1,0);
        
        scene = new Scene(mainPane, hRes, vRes, Color.DIMGREY);
        
        stage.setScene(scene);
    }
    // creates a pane with all incoming friends requests
    private GridPane friendRequestPane()
    {
        int x = 0;
        int y = 1;
        GridPane gPane = new GridPane();
        gPane.setGridLinesVisible(DEBUG);
        List<Account> requests = signedInAccount.getRequests();
        
        Label label = new Label("Friend Requests");
        gPane.add(label, 0, 0);
        for(Account tmp : requests)
        {
            gPane.add(acceptFriendRequest(tmp), x++, y);
            if(x > 5)
            {
                y++;
                x = 0;
            }
        }
        
        return gPane;
    }
    /* returns the vbox with the text
     * @return the vbox to put text on
     */
    private VBox getVBox(GridPane gridPane)
    {
        ScrollPane sPane = null;
        VBox vb = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        int row = 0, col = 1;
        for(Node node : childrens)
        {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == col)
                sPane = (ScrollPane)node;  // get the node that is the vbox
        }
        vb = (VBox)sPane.getContent();
        return vb;
    }
    // create a button with the accept or decline friend request options
    private GridPane acceptFriendRequest(Account friend)
    {
        GridPane gPane = new GridPane();
        Button accept = new Button("Accept");
        Button decline = new Button("Decline");
        Label name = new Label(friend.getName());
        gPane.setGridLinesVisible(DEBUG);
        accept.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                // adds the friend to friendList
                ArrayList fList = signedInAccount.getFriendList();
                fList.add(friend);
                signedInAccount.friendList = fList;
            }
        });
        decline.setOnAction(new EventHandler<ActionEvent>()
        {
           @Override
           public void handle(ActionEvent event)
           {
               // removes the request from the list
               // signedInAccount.removeFriendRequest(friend);
           }
        });
        gPane.add(name, 0,0);
        gPane.add(accept,0,1);
        gPane.add(decline,0,2);
        return gPane;
    }
    // creates the pane with the account name and profile picture
    private void initAccountInfoPane()
    {
        String accName = signedInAccount.getName();
        Text nameText = new Text(accName);
        Button friendRequest = new Button();
        // Keep this commented out until the Profile Pictures work
        // Image img = new Image(signedInAccount.getProfilePicture());
        Image img;
        try
        {
            img = new Image(signedInAccount.getProfilePic(), profilePicRes, profilePicRes, false, false);
        }
        catch(Exception e)
        {
            img = new Image("/images/default.jpg", profilePicRes, profilePicRes, false, false);
        }
        ImageView profilePic = new ImageView(img);
        accountInfoPane = new GridPane();
        
        friendRequest.setOnAction(new EventHandler<ActionEvent>()
        {
           @Override
           public void handle(ActionEvent event)
           {
               previousNode = currentNode;
               previousNode.setVisible(false);
               
               currentNode = friendRequestPane();
               mainPane.add(currentNode,1,0);
           }
        });
        accountInfoPane.setGridLinesVisible(DEBUG);
        friendRequest.setGraphic(profilePic);
        accountInfoPane.add(nameText, 1, 0);
        accountInfoPane.add(friendRequest, 0, 0);
        
        accountInfoPane.setHgap(10);
        accountInfoPane.setVgap(10);
    }
    // creates the ScrollPane with every contact button
    private void initContactPane()
    {
        Name accName = new Name("");
        contactPane = new ScrollPane();
        ArrayList<Account> cList = signedInAccount.getFriendList();
        VBox vb = new VBox();
        Image img;
        ImageView imgView;
        Button tmpBtn;
        for(Account tmp : cList)
        {
            tmpBtn = createButton(tmp);           
            vb.getChildren().add(tmpBtn);
        }
        contactPane.setContent(vb);
        //currentNode = createConversation();
    }
    private Button createButton(Account acc)
    {
        String name = acc.getName();
        Button btn = new Button(acc.getName());
        Image img = new Image("/images/default.jpg",contactPicRes,contactPicRes,false,false);
        ImageView imgV = new ImageView(img);
        btn.setMaxWidth(contactMaxWidth);
        btn.setMinWidth(contactMinWidth);
        btn.setGraphic(imgV);
        btn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                currentNode = createConversation(name);
            }
        });
        return btn;
    }
    // creates a pane with a logout and add friend button
    private Node createConversation(String name)
    {
        List<Account> list = signedInAccount.getFriendList();
        Node pane = new GridPane();
        GridPane gPane = new GridPane();
        ScrollPane sPane = new ScrollPane();
        TextArea tBox = new TextArea();
        Button sendBtn = new Button("Send");
        Label contName = new Label(name);
        tBox.setPrefRowCount(numRows);
        tBox.setPrefColumnCount(numCols);
        sPane.setContent(new VBox());
        gPane.setGridLinesVisible(DEBUG);
        for(Account tmp : list)
        {
            if(tmp.getName().equals(name))
            {
                pane = gPane;
                sPane = loadConversation();
                tBox = new TextArea();
                tBox.setPrefRowCount(numRows);
                tBox.setPrefColumnCount(numCols);
                
                sendBtn = new Button("Send");
                contName = new Label(name);
                
                setBtnFunc(sendBtn, tBox);
                
                gPane.add(contName,0,0);
                gPane.add(sPane,0,1);
                gPane.add(tBox,0,2);
                gPane.add(sendBtn,1,2);
            }
        }
        
        
        previousNode = currentNode;
        previousNode.setVisible(false);
        currentNode = gPane;
        mainPane.add(currentNode,1,0);
        return gPane;
    }
    
    private void setBtnFunc(Button sendBtn, TextArea tBox)
    {
        sendBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                sendMessage(tBox.getText().toString());
                tBox.clear();
            }
        });
    }
    private void initButtonPane()
    {
        buttonPane = new GridPane();
        Button logoutBtn;
        Button addFriendBtn;
        Button removeFriendBtn;
        // create the Logout Button
        
        buttonPane.setGridLinesVisible(DEBUG);
        
        logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                mainStage.hide();
                client.logOut();
                Platform.exit();
                System.exit(0);
            }
        });
        
        // create the Add Friend Button
        addFriendBtn = new Button("Add Friend");
        addFriendBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                friendWindow("add");
            }
        });
        
        // create a remove friend button
        removeFriendBtn = new Button("Remove Friend");
        removeFriendBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                friendWindow("remove");
            }
        });
        buttonPane.add(logoutBtn, 0, 0);
        buttonPane.add(addFriendBtn, 1, 0);
        buttonPane.add(removeFriendBtn, 2, 0);
    }
    // creates a StackPane and fills it with a TextArea and ScrollPane containing the conversation between the two accounts
    private void initConversationStack()
    {
        // to be implemented
        conversationStack = new StackPane();
        GridPane gPane;
        ScrollPane sPane;
        TextArea tBox;
        List<Account> cList = signedInAccount.getFriendList();
        Button sendBtn;
        Label contName;
        for(Account tmp : cList)
        {
            // create a gridpane
            gPane = new GridPane();
            
            // create a scrollpane
            sPane = loadConversation();
            
            // create a textarea
            tBox = new TextArea();
            tBox.setPrefRowCount(numRows);
            tBox.setPrefColumnCount(numCols);
            
            // create button
            sendBtn = new Button("Send");

            // create tab label
            contName = new Label(tmp.getName());
            
            
            // add scrollpane and textarea to gridpane
            gPane.add(contName, 0, 0);
            gPane.add(sPane, 0, 1);
            gPane.add(tBox, 0, 2);
            gPane.add(sendBtn, 1, 2);
            
            // add gridpane to stack
            //conversationStack.getChildren().add(gPane);
        }
    }
    // gets the conversation from the server and loads it into a ScrollPane
    private ScrollPane loadConversation()
    {
        ScrollPane history = new ScrollPane();
        history.setPrefViewportHeight(vpHeight);
        // if there is no conversation history on the server
        //if(/* check if there is conversation history*/)
            return history;   // return empty StackPane
        // get conversation history and add formatted text to scrollPane
        //return history;
    }
    // adds a friend to the accounts friend list, adds another contact button with the new friend
    private void addFriend()
    {
        // to be implemented
        
        // create a new window to get friends name/id
        // check if it is in database
            // add to friend list
            // add a contact button
        // else say friend is not
    }
    // removes friend from accounts friend, removes button associated with friend
    private void removeFriend(String friend)
    {
        int x = 0;
        ArrayList<Account> list = signedInAccount.getFriendList();
        for(Account tmp : list)
        {
            if(tmp.getName().equals(friend))
                list.remove(x);
            else
                x++;
        }
        signedInAccount.friendList = list;
    }
    
    private void friendWindow(String type)
    {
        ArrayList<Account> list = signedInAccount.getFriendList();
        Stage stage = new Stage();
        Scene scene;
        TextField textField = new TextField();
        Label label = new Label("Enter ID");
        GridPane gPane = new GridPane();
        Button cancel = new Button("Cancel");
        Button check;
        if(type.equals("add"))
        {
            check = new Button("Add Friend");
            check.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    // check if name/id is in database
                    addFriend();
                    updateContactPane();
                }
            });
        }
        else
        {
            check = new Button("Remove Friend");
            check.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    for(Account tmp : list)
                    {
                        if(tmp.getName().equals(textField.getCharacters().toString()))
                        {
                            if(DEBUG)
                                System.out.print(textField.getCharacters().toString());
                            removeFriend(textField.getCharacters().toString());
                        }
                    }
                    updateContactPane();
                }
            });
        }
        cancel.setOnAction(new EventHandler<ActionEvent>()
        {
           @Override
           public void handle(ActionEvent event)
           {
               stage.hide();
           }
        });
        gPane.setPadding(new Insets(friendWindowPadding));
        gPane.setHgap(10);
        gPane.setVgap(10);
        gPane.add(label, 0,0);
        gPane.add(textField,0,1);
        gPane.add(check,0,2);
        gPane.add(cancel,1,2);
        
        scene = new Scene(gPane, friendWinRes, friendWinRes);
        stage.setScene(scene);
        stage.show();
    }
    
    private Message sendMessage(String str)
    {
        Message msg = new Message(str, signedInAccount);
        if(DEBUG)
            System.out.print(msg.getMsg() + " " + msg.getFrom().getName());
        return msg;
    }
    public void appendMessage(Message msg)
    {
        String str = msg.getFrom().getName() + ": " + msg.getMsg();
        appendMessage(str, vBox);
    }
    public void appendMessage(String str)
    {
        String msg = str;
        appendMessage(str, vBox);
    }
    private void appendMessage(String msg, VBox vBox)
    {
        Text txt = new Text(msg);
        vBox.getChildren().add(txt);
    }
    private void updateContactPane()
    {
        initContactPane();
    }
}