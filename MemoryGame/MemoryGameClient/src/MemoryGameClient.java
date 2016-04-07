import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MemoryGameClient{
	
	// myTurn indicates whether the player can move now
	private boolean myTurn = false;
	
	//the player 1 or 2
	private static String player=null;
	
	//the picture to be matched either Fruit or Animal
	//the default picture when the game start up is Fruit
	private String picType="fruit";
	
	//JFrame form to place JPanel and other components
	private static JFrame frm;
	
	//the size of the game it can be 4(4x4), 8(8x8), and 10(10x10);
	private static int size;
	
	//Declaration of component that will be added to JFrame or JPanel
	JMenuBar menubar = null;
	JPanel game, message,status,id;
	JScrollPane sp;
    JButton buttons[],hold,held,sendMessage;
    JLabel lbIdentification,lbStatus,lbScore;
    JTextArea txtMessage;
    Clicked bPressed;
    SendMessaged bSendMessaged; 
    Icon[] pics;
    ArrayList <Icon> iconList=new ArrayList<>();
    int clickedIndex[]=new int[2];
    int cardsToAdd[];
    int count=0;
    
    //declaration of the remote object of the server
    private MemoryGameInterface memoryGame;
    
    //MemoryGameClient constructor

	public MemoryGameClient() throws RemoteException
    {
    	setDimension(4);
    	
    	init();
        
        try
        {	
        	//create object to communicate to server
            //that we already bind in the server side code at the registry
			memoryGame=InitializeRMI.getInstance().getRemoteObject();
			player=getPlayerName();
			
			if(player!=""){
				System.out.println("Connected as " + player + " player...!");
				lbIdentification.setText("You are " + player);
			}
			else{
				System.out.println("Already two player connected...!");
			}
			
	        pics=new Icon[getSize()];
	        cardsToAdd=new int[getSize()];
			
			//place the icon image to array of buttons
			//it random with randomizeIntArray from server
			//to be the same randomized of icon image on 
			//both client when it start up
	        
	     	addCardPair();
	     	
	     	//randomize the order of the array
	     	cardsToAdd=memoryGame.randomizeIntArray(cardsToAdd);
	     	
	     	//get the default image icon
	        pics=ImageLoader.getInstance().getImageIcon(cardsToAdd,"fruit");
	        
	        //add menubar to JFrame
	        frm.setJMenuBar(menubar);
	        frm.getContentPane().add(id,BorderLayout.NORTH);
	        
	        //show the default game panel(4x4) to JFrame windows
	        showGUI(size);
	        frm.getContentPane().add(message,BorderLayout.SOUTH);
	        
	        frm.addWindowListener(getWindowAdapter());
	        //frm.setLocationRelativeTo(null);
	        //memoryGame.setUndecorated(true);
	        frm.setSize(600,620);
	        frm.setResizable(false);
	        frm.setVisible(true);
        }
		catch(Exception ex){
			System.out.println("Error Create GUI :" + ex.getMessage());
			ex.printStackTrace();
		}
    }
	// End MemoryGameClient constructor
    
	/**
     * initialize the components to be 
     * display on JFrame.
     */
	 public void init(){
	    	
	    frm=new JFrame(MemoryGameConstants.TITLE);
	    game=new JPanel();
	    id=new JPanel();
	    message=new JPanel();
	        
	    buttons=new JButton[100];
	    txtMessage=new JTextArea(3,1);
	    sendMessage=new JButton("Send Message");
	    sp= new JScrollPane(txtMessage);
	        
	    id.setBorder(BorderFactory.createEmptyBorder(5,5,10,5));
	    id.setBackground(Color.LIGHT_GRAY);
	        
	    message.setLayout(new GridLayout(2,2,5,5));
	    message.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	    message.setBackground(Color.LIGHT_GRAY);
	    Font playerFont = new Font("Trajan Pro",Font.BOLD,14);
	    lbIdentification=new JLabel();
	    lbIdentification.setFont(playerFont);
	    lbIdentification.setForeground(Color.BLUE);
	    Font font=new Font("Time New Roman", Font.ITALIC, 14);
	    lbStatus=new JLabel();
	    lbStatus.setFont(font);
	    lbScore=new JLabel("Your's score : [ 0 ]");
	    lbScore.setFont(font);
	        
	    bSendMessaged=new SendMessaged();
	    sendMessage.addActionListener(bSendMessaged);
	        
	    id.add(lbIdentification);
	    message.add(sp);
	    message.add(sendMessage);
	    message.add(lbStatus);
	    message.add(lbScore);
	         
	    createMenuBar();   
	 }
	 
	 /**
	  * setter to set the game dimension
	  * @param dim dimension to be display in the game  panel
	  */
	public void setDimension(int dim)
	{
			size=dim;
	}
	 
	 /**
	  * getter to get the current dimension of the game panel
	  * @return dimension of the game panel
	  */
	 public static int getDimension()
	 {
		return size;
	 }
	
		
	/**
	 * update the client side picType
	 * @param picType
	 */
	public String getPicType()
	{
		return this.picType;
	}
		
	/**
	 * Set variable myTurn to true or false
	 * @param myTurn enable player to play after 
	 * other one player ready
	 */
	public void setTurn(boolean myTurn)
	{
		this.myTurn=myTurn;
	}
	
	/**
	 * getter to get the turn from the server
	 * @return true or false specify turn by server
	 */
	public  boolean getTurn()
	{
		return this.myTurn;
	}
	/**
	 * Set the score of player that he got 
	 * to display on client 
	 * @param score the value of score get from server
	 */
	public void setPlayScore(int score)
	{
		lbScore.setText("Your's score: [ " + score + " ]");
		setSound("match");
		

	}
		
	/**
	 * Set message on the status label
	 * @param message the message to display on client it sent 
	 * from server to notify update each client
	 */
	public void setMessage(String message)
	{
		lbStatus.setText("Status: " + message);
	}
	

	public void setPicType(String picType)
	{
		this.picType=picType;
	}
	/**
	 * get the current player 
	 * @return string player 1 or 2
	 */
	public static String getPlayer()
	{
		return player;
	}
		
	/**
	 * get the actual size of grid
	 * @return dimension x dimension
	 */
	public int getSize()
	{
    	return (getDimension()*getDimension());
    }
	
	/**
     * get the player name
     * @return
     */
    public String getPlayerName()
    {
    	String player=null;
    	try {
    		
    		CallBackImpl callBackControl=new CallBackImpl(this);
			player=memoryGame.connect((CallBack)callBackControl);
		} catch (Exception e) {
			System.out.println("Cannot connect as player " + e.getMessage());
		}
    	return player;
    }
    
	/**
	 * show the game panel every time started and refresh game level
	 * @param gameSize dimension of the game it can be 4(4x4), 8(8x8), and 10(10x10)
	 */
    public void showGUI(int gameSize)
    {
    	game.removeAll();
		game.setLayout(new GridLayout(gameSize,gameSize,5,5));
        game.setOpaque(true);
        game.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        game.setBackground(Color.WHITE);
        buttons=new JButton[gameSize*gameSize];
        bPressed=new Clicked();
        for(int i=0;i<gameSize*gameSize;i++)
        {	
        	buttons[i]=new JButton();
        	buttons[i].setBorder(null);
        	buttons[i].addActionListener(bPressed);
            game.add(buttons[i]);
        }
    	game.validate();
    	game.repaint();
    	frm.repaint();
    	frm.getContentPane().add(game,BorderLayout.CENTER);
    }
    
    /**
     * random the pair image to be added
     */
	private void addCardPair()
	{
		int size=getDimension();
		for(int i = 0; i<(size*size)/2; i++)
		{
			cardsToAdd[2*i] = i;
			cardsToAdd[2*i + 1] = i;
		}
	}
    
    private WindowAdapter getWindowAdapter()
    {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                //super.windowClosing(we);
                new ConfirmQuit();

            }
        };
    }
    
    /**
     * Create a MenurBar for main game panel
     */
    public void createMenuBar()
    {
    	//Create the menubar.
        menubar = new JMenuBar();

        //Build the first menu.
        JMenu menu = new JMenu("File");
        menubar.add(menu);

        //"New game" menuitem
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.setMnemonic(KeyEvent.VK_N);
        newGame.addActionListener(new MenuBarListener(MemoryGameConstants.NEWGAME_ACTION));
        menu.add(newGame);
        
        //"Quit" menuitem
        JMenuItem quit = new JMenuItem("Quit");
        quit.setMnemonic(KeyEvent.VK_Q);
        quit.addActionListener(new MenuBarListener(MemoryGameConstants.QUIT_ACTION));
        menu.add(quit);
        
      //Second menu
        JMenu menu2 = new JMenu("Picture");
        menubar.add(menu2);
        ButtonGroup pictureGroup = new ButtonGroup();

        //"Fruit" menuitem
        JRadioButtonMenuItem fruit = new JRadioButtonMenuItem ("Fruit");
        fruit.addActionListener(new MenuBarListener(MemoryGameConstants.FRUIT));
        pictureGroup.add(fruit);
        pictureGroup.setSelected(fruit.getModel(), true);
        menu2.add(fruit);
        
        //"Animal" menuitem
        JRadioButtonMenuItem  animal = new JRadioButtonMenuItem ("Animal");
        animal.addActionListener(new MenuBarListener(MemoryGameConstants.ANIMAL));
        pictureGroup.add(animal);
        menu2.add(animal);
        
      //Second menu
        JMenu menu3 = new JMenu("Level");
        menubar.add(menu3);
        ButtonGroup levelGroup = new ButtonGroup();

        //"Easy" menuitem
        JRadioButtonMenuItem easy = new JRadioButtonMenuItem ("Easy");
        easy.addActionListener(new MenuBarListener(MemoryGameConstants.EASY_LEVEL));
        levelGroup.add(easy);
        levelGroup.setSelected(easy.getModel(), true);
        menu3.add(easy);
        
        //"Normal" menuitem
        JRadioButtonMenuItem  normal = new JRadioButtonMenuItem ("Normal");
        normal.addActionListener(new MenuBarListener(MemoryGameConstants.NORMAL_LEVEL));
        levelGroup.add(normal);
        menu3.add(normal);
        
      //"Hard" menuitem
        JRadioButtonMenuItem  hard = new JRadioButtonMenuItem ("Hard");
        hard.addActionListener(new MenuBarListener(MemoryGameConstants.HARD_LEVEL));
        levelGroup.add(normal);
        menu3.add(hard);

        //Third Menu
        JMenu menu4 = new JMenu("Help");
        menubar.add(menu4);
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new MenuBarListener(MemoryGameConstants.ABOUT_ACTION));
        menu4.add(about);
    }
    
    /**
     * Display the icon image to the the button
     * with the specific position specify by index parameter
     * @param index the position of button to be set icon image
     */
    public void setVisibleCard(int index)
    {
    	buttons[index].setIcon(pics[index]);
    }
    
    public void setClick(){
    	setSound("click");
    }
    /**
     * this method for playing sound during click, match or win
     * @param type giving argument specific for type. It can be click, 
     * match, and win
     */
    public void setSound(String type){
	    try {
	    	 String path="sound";
	    	 if (type.equals("click")) path+="/click.wav";
	    	 else if(type.equals("match")) path+="/match.wav";
	    	 else path+="/win.wav";
	         // Open an audio input stream.
	         URL url = this.getClass().getClassLoader().getResource(path);
	         AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	         // Get a sound clip resource.
	         Clip clip = AudioSystem.getClip();
	         // Open audio clip and load samples from the audio input stream.
	         clip.open(audioIn);
	         clip.start();
	     } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	     } catch (IOException e) {
	         e.printStackTrace();
	     } catch (LineUnavailableException e) {
	         e.printStackTrace();
	     }
    }
    
    /**
     * this method will be clear the icon image 
     * on the button as it not match.
     * @param clickedIndex list of position to set icon image to null
     */
    public void setNotMatchClear(final ArrayList<Integer> clickedIndex)
    {
    	new Thread(){
    		public void run(){
    			try 
    			{
    				Thread.sleep(1000);
    				buttons[clickedIndex.get(0)].setIcon(null);
    		    	buttons[clickedIndex.get(1)].setIcon(null);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
    			
    		}
    	}.start();
    	
    }
    
    /**
     * confirm other play
     * @param player
     */
    public void confirmDisconnected(String player)
    {
    	JOptionPane.showMessageDialog(null,player + " was disconnected");
    }
	
	/**
	 * get the message from each other 
	 * this method invoke at server side
	 * and this display on the text area at the same time
	 * @param message text to be append to text area
	 */
	public void chatMessage(String message)
	{
		txtMessage.setText(message);
	}
	
	/**
	 * set the update icon pics from the server
	 * @param pics the new updated list
	 */
	public void setUpdatePicsIcon(Icon[]pics)
	{
		this.pics=pics;
	}
	
	/**
	 * set the game level to 4, 8, 10 it notify from the server
	 * when the other player change the game level
	 * @param level is number of level(4, 8, 10) sent by other player
	 */
	public void setGameLevel(int level)
	{
	    size=level;
	}
	
	/**
	 * update the game panel belong to the level game changing by each player
	 * @param pics list of pic sent by other player to make it have the same icon image
	 */
	public void setUpdateGameLevel(Icon[]pics)
	{
		this.pics=pics;
		setDimension(size);
    	showGUI(getDimension());	
	}
	
	/**
	 * pop up the win form
	 */
	public void setWin()
	{
		setSound("win");
		new NotifyWin();
	}
	
	/**
	 * pop up the lost form
	 */
	public void setLost()
	{
		new NotifyLost();
	}

	/**
	 * create the new game by random new image icon
	 * and clear game panel
	 */
	public void createNewGame()
	{
		for(int i=0;i<getSize();i++){
			buttons[i].setIcon(null);
		}
		try {
			cardsToAdd=memoryGame.randomizedIntArrayNewGame(cardsToAdd);
        	pics=ImageLoader.getInstance().getImageIcon(cardsToAdd,getPicType());
        	memoryGame.setPicIcon(pics,getPlayer(),getPicType());
		} catch (Exception e) {
			System.out.println("Cannot create New Game " + e.getMessage());
		}
		
	}
	
	/**
     * close the main window form of memory game
     */
    public static void closingFrm()
    {
    	frm.dispose();
    }
    
	
    /**
     * this class invoke the button click event handler 
     * 
     * @author Bunna Kal & Channak Chhon
     *
     */
    private class Clicked implements ActionListener
    {
    	int numofTurn=0;
        public void actionPerformed(ActionEvent e)
        {
        	if(getTurn())
        	{
        		
        		try {
    	        	int index;
    	            
    		            hold=(JButton)e.getSource();
    		            if(hold.getIcon()==null)
    		            {
    		                for(index=0;buttons[index]!=hold;index++);
    		                if(count==0)
    		                {
    		                	 hold.setIcon(pics[index]);
    		                     iconList.add(pics[index]);
    		                     count++;
    		                     numofTurn++;
    		                     held=hold;
    		                     
    		                }
    		                else
    		                {
    		                	 hold.setIcon(pics[index]);
    		                     iconList.add(pics[index]);
    		                     numofTurn++;
    		                     count=0; 
    		                     
    		            	}
    		                
    		                //invoke the method myMove on the server to notify the move 
    		                //of each client with the specific position index.
    		                memoryGame.myMove(index, MemoryGameClient.getPlayer(),iconList);
    		                
    		                //clear the icon list to make sure it contain only two elements
    		                //sent to server to check match icon image
    		                if(numofTurn==2){
    		                	iconList.clear();
    		                	numofTurn=0;
    		                }  
    		            }
            		} 
            		catch (Exception ex) 
            		{
    				ex.printStackTrace();
            		}
        		} 
        	}
    	}
   
    /**
     * this class you for event handler with MemuBar 
     * @author Bunna Kal & Channak Chhon
     *
     */
    private class MenuBarListener implements ActionListener
    {
        private String action = null;

        public MenuBarListener(String action)
        {
            this.action = action;
        }

        public void actionPerformed(ActionEvent e)
        {
        	try {
				
	            if(action.equals(MemoryGameConstants.QUIT_ACTION))
	            {
	            	new ConfirmQuit();
	            }
	            if(action.equals(MemoryGameConstants.NEWGAME_ACTION))
	            {
	            	clearAllIconImage();
	                createNewGame();
	            }
	
	            if(action.equals(MemoryGameConstants.FRUIT))
	            {
	            	cardsToAdd=memoryGame.randomizedIntArrayNewGame(cardsToAdd);
	                pics=ImageLoader.getInstance().getImageIcon(cardsToAdd,"fruit");
	                memoryGame.setPicIcon(pics,getPlayer(),getPicType());
	                setPicType("fruit");
	            }
	            if(action.equals(MemoryGameConstants.ANIMAL))
	            {
	            	cardsToAdd=memoryGame.randomizedIntArrayNewGame(cardsToAdd);
	            	pics=ImageLoader.getInstance().getImageIcon(cardsToAdd,"animal");
	            	memoryGame.setPicIcon(pics,getPlayer(),getPicType());
	            	setPicType("animal");
	            }
	            if(action.equals(MemoryGameConstants.ABOUT_ACTION))
	            {
	                showAboutDialog();
	            }
	            if(action.equals(MemoryGameConstants.EASY_LEVEL)){
	            	
	            	setDimension(4);
	            	
	            	pics=new Icon[getSize()];
	     	        cardsToAdd=new int[getSize()];
	     	        
	     	     	addCardPair();
	     	     	
	     	     	//randomize the order of the array
	     	     	cardsToAdd=memoryGame.randomizedIntArrayNewGame(cardsToAdd);
	     	        pics=ImageLoader.getInstance().getImageIcon(cardsToAdd,"fruit");
	     	        memoryGame.setLevelChange(pics,getPlayer(),getDimension());
	     	        
	            	showGUI(4);

	            }
	            if(action.equals(MemoryGameConstants.NORMAL_LEVEL)){
	            	
	            	setDimension(8);
	            	
	            	pics=new Icon[getSize()];
	     	        cardsToAdd=new int[getSize()];
	     	        
	     	     	addCardPair();
	     	     	
	     	     	//randomize the order of the array
	     	     	cardsToAdd=memoryGame.randomizedIntArrayNewGame(cardsToAdd);
	     	     	//System.out.println("card size :" + cardsToAdd.length);
	     	        pics=ImageLoader.getInstance().getImageIcon(cardsToAdd,"fruit");
	     	        memoryGame.setLevelChange(pics,getPlayer(),8);
	     	        System.out.println("Size pics" + pics.length);
	     	        
	            	showGUI(8);

	            }
	            if(action.equals(MemoryGameConstants.HARD_LEVEL)){
	            	
	            	setDimension(10);
	            	
	            	pics=new Icon[getSize()];
	     	        cardsToAdd=new int[getSize()];
	     	        
	     	     	addCardPair();
	     	     	
	     	     	//randomize the order of the array
	     	     	cardsToAdd=memoryGame.randomizedIntArrayNewGame(cardsToAdd);
	     	     	//System.out.println("card size :" + cardsToAdd.length);
	     	        pics=ImageLoader.getInstance().getImageIcon(cardsToAdd,"fruit");
	     	        memoryGame.setLevelChange(pics,getPlayer(),getDimension());
	     	        
	            	showGUI(10);
	     	        
	            }
        	} catch (Exception ex) {
				System.out.println("Picture change error " + ex.getMessage());
				ex.printStackTrace();
			}
        }
        
        /**
         * clear all icon image with the new game call
         */
        public void clearAllIconImage()
        {
        	for(int i=0;i<getSize();i++){
        		buttons[i].setIcon(null);
        	}
        }
        
        /**
         * Display the about dialog 
         */
        private void showAboutDialog()
        {
            String COPYRIGHT = "\u00a9";
            String LINEBREAK = "\n";
            JOptionPane.showMessageDialog(frm,
            "Memory Game RMI " + LINEBREAK + COPYRIGHT + "  Groupt@2012",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
        }

    }
    
    /**
     * This class working as chat application that allow each client
     * can communicate each other by text message
     * @author Bunna Kal & Channak Chhon
     *
     */
    private class SendMessaged implements ActionListener
    {

		public void actionPerformed(ActionEvent e) {
			if(txtMessage.getText()!=""){
				
				String chatText=txtMessage.getText();
				try {
					
					memoryGame.myChat(player,chatText);
					
				} catch (Exception ex) {
					System.out.println("Error Chat : " + ex.getMessage());
					ex.printStackTrace();
					System.exit(0);
				}
			}
			
		}
    	
    }
   
    public static void main(String [] args) throws Exception
    {
    	new MemoryGameClient();
        
    }
}
