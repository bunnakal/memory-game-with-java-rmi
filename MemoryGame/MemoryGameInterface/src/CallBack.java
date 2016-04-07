

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.Icon;

/**
 * this interface allow communicate from server to the click when call back 
 * @author Bunna Kal & Channak Chhon
 *
 */
public interface CallBack extends Remote {

	/**
	 * the server notifies the client for taking a turn
	 * @param turn the boolean value sent to client
	 * @throws RemoteException
	 */
	public void takeTurn(boolean turn) throws RemoteException;
	
	/**
	 * The server sends a message to be displayed by the client
	 * @param message the message sent by server when other player perform some activity
	 * @throws RemoteException
	 */
	public void notify(String message) throws RemoteException;
	
	/**
	 * the sever sent the index of the clicked card on each client
	 * @param index the position of card to be visible with icon image
	 * @throws RemoteException
	 */
	public void setVisibleCard(int index) throws RemoteException;

	public void setClickSound() throws RemoteException;
	/**
	 * the server sent the list of index of picture that will be closed or clear the icon image
	 * @param clickedIndex list of index that sent to the client to be set icon image to null value
	 * @throws RemoteException
	 */
	public void setNotMatchClear(ArrayList<Integer>clickedIndex) throws RemoteException;
	
	/**
	 * sent the matched score to the client when the got the matching
	 * @param score the value to be display as current score
	 * @throws RemoteException
	 */
	public void setMatchScore(int score) throws RemoteException;
	
	/**
	 * server sent the chat message to the client with the parameter message
	 * @param message text that sent to client
	 * @throws RemoteException
	 */
	public void setChatMessage(String message) throws RemoteException;
	
	/**
	 * Create the New Game, it just clear the game panel and re-random icon image again.
	 * @throws RemoteException
	 */
	public void createNewGame() throws RemoteException;
	
	/**
	 * This method is update the list of pic icon 
	 * @param pics list of pics that was updated
	 * @throws RemoteException
	 */
	public void setChangePicIcon(Icon[]pics)throws RemoteException;
	
	/**
	 * Sent the picture type to each client when picture was changed by every client
	 * @param picType String of picture it can be "Fruit" or "Animal".
	 * @throws RemoteException
	 */
	public void notifyPicType(String picType) throws RemoteException;
	
	/**
	 * Re-arrange the game panel and get the icon image list from other client who change the game level
	 * @param pics the icon array which was sent by other client who had changed the game level
	 * @throws RemoteException
	 */
	public void setChangeLevel(Icon[]pics) throws RemoteException;
	
	/**
	 * Notify the game size or grid size to player when the game level was changed by other player
	 * @param levelSize the game dimension or grid size sent to other player
	 * @throws RemoteException
	 */
	public void notifyGameLevel(int levelSize) throws RemoteException;
	
	/**
	 * Notify the win player when other player get win
	 * @throws RemoteException
	 */
	public void notifyWin() throws RemoteException;
	
	/**
	 * Notify the lost player when other player get win
	 * @throws RemoteException
	 */
	public void notifyLost() throws RemoteException;
	
	/**
	 * confirm the other palyer1 or 2 weather other was disconnected
	 * @param player player1 or 2 that was disconnected
	 * @throws RemoteException
	 */
	public void ConfirmDisconnected(String player) throws RemoteException;
}
