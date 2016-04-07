import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class MemoryGameGUI {

	private static MemoryGameGUI instance=null;
	//declaration of the remote object in the server
    MemoryGameInterface memoryGame;
	
	private static JFrame frm;
	private JMenuBar menuBar;
	JPanel game,message,status,id;
	JScrollPane sp;
    JButton buttons[],hold,held,sendMessage;
    JLabel lbIdentification,lbStatus,lbScore;
    JTextArea txtMessage;
    Border border;
    ImageIcon image;
    
    Clicked bPressed;
    SendMessaged bSendMessaged;
    
    Icon[] pics=new Icon[100];
    ArrayList <Icon> iconList=new ArrayList<>();
    int clickedIndex[]=new int[2];
    int cardsToAdd[];
    int count=0;
	
	private MemoryGameGUI(){
		
	}
	
	public static MemoryGameGUI getInstance(){
		if(instance==null){
			instance=new MemoryGameGUI();
		}
		return instance;
	}
	
	private void createGUI()
    {
        //Create and set up the window.
        frm.setTitle(MemoryGameConstants.GAME_TITLE);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //add panels
        //this.getContentPane().add(bgPanel);
        frm.setJMenuBar(menuBar);

        //no resize is possible
        frm.setResizable(false);

    }
	
	public void showGUI()
    {
        createMenuBar();
        createGUI();
        frm.setVisible(true);
    }
	
	public void init(){
		frm=new JFrame("Memory Game RMI");
    	id=new JPanel();
    	game=new JPanel();
        message=new JPanel();
        image = new ImageIcon(getClass().getResource("bg.jpg"));
        
        buttons=new JButton[100];
        txtMessage=new JTextArea(3,1);
        sendMessage=new JButton("Send Message");
        sp= new JScrollPane(txtMessage);
        
        border=BorderFactory.createLineBorder(Color.BLUE,5);
        
        id.setBorder(border);
        game.setLayout(new GridLayout(10,10,5,5));
        game.setOpaque(true);
        game.setBackground(Color.BLUE);
        message.setLayout(new GridLayout(2,2,5,5));
        message.setBorder(border);
        
        lbIdentification=new JLabel();
        lbStatus=new JLabel();
        lbScore=new JLabel("Your's score : [ 0 ]");
        
        bSendMessaged=new SendMessaged();
        sendMessage.addActionListener(bSendMessaged);
        
        bPressed=new Clicked();
        
        //add array of JButton to game JPanel
        //addButtonList();
        
        id.add(lbIdentification);
        message.add(sp);
        message.add(sendMessage);
        message.add(lbStatus);
        message.add(lbScore);
	}
	private void createMenuBar()
    {
		//Create the menubar.
        menuBar = new JMenuBar();

        //Build the first menu.
        JMenu menu = new JMenu("File");
        menuBar.add(menu);

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
        menuBar.add(menu2);
        ButtonGroup soundGroup = new ButtonGroup();

        //"Fruit" menuitem
        JRadioButtonMenuItem soundsOn = new JRadioButtonMenuItem ("Fruit");
        soundsOn.addActionListener(new MenuBarListener(MemoryGameConstants.FRUIT));
        soundGroup.add(soundsOn);
        soundGroup.setSelected(soundsOn.getModel(), true);
        menu2.add(soundsOn);
        
        //"Animal" menuitem
        JRadioButtonMenuItem  soundsOff = new JRadioButtonMenuItem ("Animal");
        soundsOff.addActionListener(new MenuBarListener(MemoryGameConstants.ANIMAL));
        soundGroup.add(soundsOff);
        menu2.add(soundsOff);

        //Third Menu
        JMenu menu3 = new JMenu("Help");
        menuBar.add(menu3);
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new MenuBarListener(MemoryGameConstants.ABOUT_ACTION));
        menu3.add(about);
    }
	
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
	                //MemoryGame.getInstance().startAgain();
	            }
	
	            if(action.equals(MemoryGameConstants.FRUIT))
	            {
	                //pics=ImageLoader.getInstance().getImageIcon(cardsToAdd,"fruit");
	                //memoryGame.setPicIcon(pics,getPlayer());
	            }
	            if(action.equals(MemoryGameConstants.ANIMAL))
	            {
	            	//pics=ImageLoader.getInstance().getImageIcon(cardsToAdd,"animal");
	            	//memoryGame.setPicIcon(pics,getPlayer());
	            }
	            if(action.equals(MemoryGameConstants.ABOUT_ACTION))
	            {
	                //showAboutDialog();
	            }
	            
        	} catch (Exception ex) {
				System.out.println("Picture change error " + ex.getMessage());
				ex.printStackTrace();
			}
        }

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
        	if(true)
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
    		                //memoryGame.myMove(index, player,iconList);
    		                
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
     * This class working as chat application that allow each client
     * can communicate each other by text message
     * @author Bunna Kal & Channak Chhon
     *
     */
    private class SendMessaged implements ActionListener
    {

		public void actionPerformed(ActionEvent e) {
			if(txtMessage.getText()!=""){
				
				//String chatText=txtMessage.getText();
				try {
					
					//memoryGame.myChat(player,chatText);
					
				} catch (Exception ex) {
					System.out.println("Error Chat : " + ex.getMessage());
					ex.printStackTrace();
					System.exit(0);
				}
			}
			
		}
    	
    }
	
}
