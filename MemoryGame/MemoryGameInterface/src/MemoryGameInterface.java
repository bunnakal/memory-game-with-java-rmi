 

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.Icon;

/**
 * this interface consist of method that can be remote call from client
 * it implemented by the the server side
 * @author Bunna Kal & Channak Chhon
 *
 */
public interface MemoryGameInterface extends Remote {

	/**
	 * Connect to the MemoryGame server and return the token
	 * If the return token is "", the client is not connected to the server
	 * @param client object that connected
	 * @return player1,player2 or ""
	 * @throws RemoteException
	 */
	public String connect(CallBack client) throws RemoteException;
	
	/**
	 * the array of Integer to be randomized the picture icon on each button when we click
	 * @param a array of Integer that we have to random it.
	 * @return the array of Integer after got random.
	 * @throws RemoteException
	 */
	public int[] randomizeIntArray(int[] a) throws RemoteException;
	
	/**
	 * A client invoke this method to notify the server of its move
	 * @param index
	 * @param player
	 * @param pics
	 * @throws RemoteException
	 */
	public void myMove(int index,String player,ArrayList<Icon> pics) throws RemoteException;
	
	/**
	 * client invoke this method to notify the server of its chat message
	 * @param message message sent to communicate each other
	 * @throws RemoteException
	 */
	public void myChat(String player,String message) throws RemoteException;
	
	/**
	 * create the new game panel when one user got the win
	 * @throws RemoteException
	 */
	public void createNewGame() throws RemoteException;
	
	/**
	 * new random for the new game, picture type changed , and game level changed
	 * @param arr the card of element to be re-random
	 * @return the array of integer
	 * @throws RemoteException
	 */
	public int[] randomizedIntArrayNewGame(int[] arr) throws RemoteException;
	
	/**
	 * re-arrange the icon image when the one is win
	 * @throws RemoteException
	 */
	public void setNewGameIcon(Icon[] pics) throws RemoteException;
	
	/**
	 * set change to icon image with both player with the new selected icon image type
	 * player can change picture of image icon type among fruit or animal 
	 * @param pics the new icon list as type of image is changed
	 * @return
	 * @throws RemoteException
	 */
	public void setPicIcon(Icon[]pics,String player,String picType)throws RemoteException;
	
	/**
	 * Set changing the grid of the game panel when other player change the the game Level
	 * @param pics array of picture icon to be map to each button when other player change game level.
	 * it must be the same each player
	 * @param player the player who play the game it can be paleyer1 or 2.
	 * @param levelSize the size of grid it can be 4(4x4), 8(8x8), and 10(10x10).
	 * @throws RemoteException
	 */
	public void setLevelChange(Icon[] pics,String player, int levelSize) throws RemoteException;
	
	/**
	 * confirm the other player when the other player is was disconnected
	 * @param player the palyer1 or 2 that was disconnected
	 * @throws RemoteException
	 */
	public void playerDisconnected(String player) throws RemoteException;
	
}
