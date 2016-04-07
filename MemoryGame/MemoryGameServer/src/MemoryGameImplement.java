

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Icon;

public class MemoryGameImplement extends UnicastRemoteObject 
	implements MemoryGameInterface {
	
	//static String hostName = "192.168.0.254";
	
	// Declare two players, used to call player back
	private CallBack player1=null;
	private CallBack player2=null;
	
	// list of click index from client 
	ArrayList<Integer> clickedIndex=new ArrayList<>();
	
	//array of randomized integer value to random icon image
	private int[]arr;
	
	//indicate weather to random or not  
	private boolean initrandom = false;
	
	//the score for each player
	private int player1Score=0;
	private int player2Score=0;
	
	//the game level get from every client
	private int gameLevel=4;

	
	private static final long serialVersionUID = 1L;
	public MemoryGameImplement() throws RemoteException {

	}

	public MemoryGameImplement(int port) throws RemoteException {
		super(port);
		
	}

	public MemoryGameImplement(int port, RMIClientSocketFactory csf,
			RMIServerSocketFactory ssf) throws RemoteException {
		super(port, csf, ssf);
		
	}
	
	/**
	 * this method is call by client for get the identification 
	 * which player would it get from player the first connect is player 1 then player 2
	 * if already two players in the it will return empty string 
	 * Connect to the MemoryGame server and return the token
	 * If the return token is "", the client is not connected to the server
	 * @param client object that connected
	 * @return player1,player2 or ""
	 * @throws RemoteException
	 */
	@Override
	public String connect(CallBack client) throws RemoteException {
		String player=null;
		if(player1==null){
			
			//player1(first player) registered
			player1=client;
			initrandom = true;
			player1.notify("Wait for a second player to join...!");
			player= "Player 1";
		}
		else if(player2==null){
			
			//player2 (second player) registered
			player2=client;
			initrandom = false;
			player2.notify("Wait for the first player to play...!");
			player2.takeTurn(false);
			player1.notify("It is my turn (Player 1 token...!)");
			player1.takeTurn(true);
			player= "Player 2";
		}
		else{
			
			//Already two player
			client.notify("Two player are already in the game");
			player="";
		}
		return player;
	}
	
	/**
	 * the array of Integer to be randomized the picture icon on each button when we click
	 * @param a array of Integer that we have to random it.
	 * @return the array of Integer after got random.
	 * @throws RemoteException
	 */
	 @Override
	public int[] randomizeIntArray(int[]a)
	{
		Random randomizer = new Random();
		
		if(initrandom){
			arr=new int[a.length];
			// iterate over the array
			for(int i = 0; i < a.length; i++ )
			{
				// choose a random int to swap with
				int d = randomizer.nextInt(a.length);
				// swap the entries
				int t = a[d];
				a[d] = a[i];
				a[i] = t;
			} 
			arr=copyArrayElement(a);
		}
		return arr;
	}
	 
	 public int[] copyArrayElement(int arr[])
	 {
		int arrCopy[]=new int[arr.length];
		for(int i=0;i<arr.length;i++){
			arrCopy[i]=arr[i];
		}
		return arrCopy;
	}
	 
	/**
	 * A client invoke this method to notify the server of its move
	 * @param index position of open card every client
	 * @param player the player1 or 2 get from client
	 * @param pics image icon to be compare at server side
	 * @throws RemoteException
	 */
	@Override
	public void myMove(int index, String player,ArrayList<Icon> pics) throws RemoteException {
		//Notify the other player of the move
		if(player.equals("Player 1")){
			//set visible icon image on player 2 game panel
			player2.setVisibleCard(index);
			
			//add the first click index to ArrayList clickedIndex to 
			//be sent back to client for clear icon image after not match happened
			clickedIndex.add(index);
			if(clickedIndex.size()==2){
				if(!checkSameIconImage(pics, player)) {
					player1.notify("This was not a match.");
					player2.notify("Now your turn.");
					player2.takeTurn(true);
					player1.takeTurn(false);
					setClearIfNoMatch();
				}
				else{
					player1Score++;
					player1.notify("There is a match.Countinue");
					player1.setMatchScore(player1Score);
				}
				clickedIndex.clear();	
			}	
		}
		else{
			//set visible icon image on player 1 game panel
			player1.setVisibleCard(index);
			
			//add the first click index to ArrayList clickedIndex to 
			//be sent back to client for clear icon image after not match happened
			clickedIndex.add(index);
			if(clickedIndex.size()==2){
				if(!checkSameIconImage(pics, player)) {
					player2.notify("This was not a match.");
					player1.notify("Now your turn.");
					player1.takeTurn(true);
					player2.takeTurn(false);
					setClearIfNoMatch();
				}
				else {
					player2Score++;
					player2.notify("There is a match.Continue");
					player2.setMatchScore(player2Score);
				}
				
				clickedIndex.clear();	
			}
		}
		//check the winner player
		if(gameLevel==4){
			
			if(player1Score==4 && player2Score==4){
				notifyTheSameScore();
			}
			else if(player1Score>=4){
				notifyPlayer1Win();
			}
			else if(player2Score>=4){
				notifyPlayer2Win();
			}
			
		}
		else if(gameLevel==8){
			
			if(player1Score==16 && player2Score==16){
				notifyTheSameScore();
			}
			else if(player1Score>=16){
				notifyPlayer1Win();
			}
			else if(player2Score>=16){
				notifyPlayer2Win();
			}
		}
		else{
			
			if(player1Score==25 && player2Score==25){
				notifyTheSameScore();
			}
			else if(player1Score>=25){
				notifyPlayer1Win();
			}
			else if(player2Score>=25){
				notifyPlayer2Win();
			}
		}
		player1.setClickSound();
		player2.setClickSound();
	}
	
	/**
	  * Check the two icon image sent from each client to make sure it the same or not.
	  * @param pics only two element ArrayList to be checked match
	  * @param player the player1 or 2 
	  * @return true if there is a match and false if not match icon image found
	 */
	public boolean checkSameIconImage(ArrayList<Icon> pics,String player){
		if(pics.get(0).toString().equals(pics.get(1).toString())){
			return true;
		}
		pics.clear();
		return false;
	}
	
	/**
	 * Flip down the not match image each client
	 */
	public void setClearIfNoMatch(){
		try {
			player1.setNotMatchClear(clickedIndex);
			player2.setNotMatchClear(clickedIndex);	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Notify the win action for player1
	 */
	public void notifyPlayer1Win(){
		try {
			player1.notifyWin();
			player1.notify("You win...!");
			player2.notifyLost();
			player2.notify("You lost...!");
			player1.notify("");
			player1Score=0;
			player1.setMatchScore(0);
			player2.setMatchScore(0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Notify the win action for player2
	 */
	public void notifyPlayer2Win(){
		try {
			player2.notifyWin();
			player2.notify("You win...!");
			player1.notifyLost();
			player1.notify("You lost...!");
			player2Score=0;
			player1.setMatchScore(0);
			player2.setMatchScore(0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Notify the same score action for player1 and 2
	 */
	public void notifyTheSameScore(){
		try {
			player1.notify("You got the same score. Try the New Game");
			player2.notify("You got the same score. Try the New Game");
			player1Score=0;
			player2Score=0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Client invoke this method to notify the server of its chat message
	 * @param message message sent to communicate each other
	 * @throws RemoteException
	 */
	@Override
	public void myChat(String player,String message) throws RemoteException {
		if(player.equals("Player 1")){
			player2.setChatMessage("[Player 1] : " + message);
		}
		else {
			player1.setChatMessage("[Player 2] : " + message);
		}	
	}
	
	/**
	 * create the new game panel when one user got the win
	 * @throws RemoteException
	 */
	@Override
	public void createNewGame() throws RemoteException {
			player1.createNewGame();
			player2.createNewGame();
	}
	
	/**
	 * New random for the new game, picture type changed , and game level changed when both client existing
	 * @param arr the card of element to be re-random
	 * @return the array of integer
	 * @throws RemoteException
	 */
	@Override
	public int[] randomizedIntArrayNewGame(int[] a) throws RemoteException {
		Random randomizer = new Random();
		if(player1!=null && player2!=null){
			// iterate over the array
			for(int i = 0; i < a.length; i++ )
			{
				// choose a random int to swap with
				int d = randomizer.nextInt(a.length);
				// swap the entries
				int t = a[d];
				a[d] = a[i];
				a[i] = t;
			} 
			arr=copyArrayElement(a);		
		}
		return arr;
	}
	
	/**
	 * re-arrange the icon image when the one is win
	 * @throws RemoteException
	 */
	@Override
	public void setNewGameIcon(Icon[] pics) throws RemoteException {
		player1.setChangePicIcon(pics);
		player2.setChangePicIcon(pics);
		player1Score=0;
		player2Score=0;
		player1.setMatchScore(0);
		player2.setMatchScore(0);
	}
	
	/**
	 * this method for the updated picture while playing
	 * it will change both size of client to the new picture type
	 */
	@Override
	public void setPicIcon(Icon[] pics,String player,String picType) throws RemoteException {
		String LINEBREAK = "\n";
		if(player.equals("Player 1")){
			
			player2.setChangePicIcon(pics);
			player1.takeTurn(true);
			player1.notify("Your start first.");
			player2.notify("The picture type was changed by " + player + "." + LINEBREAK + "Wait for the " + player + " to move first.");
			player2.takeTurn(false);
		}
		else{
			
			player1.setChangePicIcon(pics);
			player2.takeTurn(true);
			player2.notify("Your start first.");
			player1.notify("The picture type was changed by " + player + "." + LINEBREAK + "Wait for the " + player + " to move first.");
			player1.takeTurn(false);
		}
		
		player1.notifyPicType(picType);
		player2.notifyPicType(picType);
		
	}
	
	/**
	 * Set changing the grid of the game panel when other player change the the game Level
	 * @param pics array of picture icon to be map to each button when other player change game level.
	 * it must be the same each player
	 * @param player the player who play the game it can be paleyer1 or 2.
	 * @param levelSize the size of grid it can be 4(4x4), 8(8x8), and 10(10x10).
	 * @throws RemoteException
	 */
	@Override
	public void setLevelChange(Icon[] pics, String player, int levelSize) throws RemoteException{
		String LINEBREAK = "\n";
		if(player.equals("Player 1")){
			player2.notifyGameLevel(levelSize);
			player2.setChangeLevel(pics);
			player1.takeTurn(true);
			player1.notify("Your start first.");
			player2.notify("The game Level was changed by " + player + "." + LINEBREAK + "Wait for the " + player + " to move first.");
			player2.takeTurn(false);
		}
		else{
			player1.notifyGameLevel(levelSize);
			player1.setChangeLevel(pics);
			player2.takeTurn(true);
			player2.notify("Your start first.");
			player1.notify("The picture type was changed by " + player + "." + LINEBREAK + "Wait for the " + player + " to move first.");
			player1.takeTurn(false);
		}
		gameLevel=levelSize;
	}
	
	/**
	 * Confirm the player when the other player is was disconnected
	 * @param player the palyer1 or 2 that was disconnected
	 * @throws RemoteException
	 */
	@Override
	public void playerDisconnected(String player) throws RemoteException {
		if(player.equals("Player 1")){
			player2.ConfirmDisconnected(player);
			player2.notify("Player 1 was disconnected or closed..!");
		}
		else{
			player1.ConfirmDisconnected(player);
			player1.notify("Player 2 was disconnected or closed..!");
		}
		
	}
	
	/** 
	 * Main 
	 * @param arg
	 */
	
	public static void main(String []arg){
		try {
			//System.setProperty("java.rmi.server.hostname", "192.168.0.254");
			MemoryGameImplement obj=new MemoryGameImplement();
			Registry reg=LocateRegistry.getRegistry(MemoryGameConstants.RMI_PORT);
			//reg.rebind("//"+hostName+"/" + MemoryGameConstants.RMI_ID, obj);
			reg.rebind(MemoryGameConstants.RMI_ID, obj);
			System.out.print("Server [ "+ obj + " ] is running....!");
		} catch (Exception e) {
			System.out.print("Server Error " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}
}
