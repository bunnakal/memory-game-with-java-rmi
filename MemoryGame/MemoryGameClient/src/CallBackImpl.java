

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Icon;


public class CallBackImpl extends UnicastRemoteObject 
	implements CallBack {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MemoryGameClient thisClient;

	protected CallBackImpl() throws RemoteException {
		super();
		
	}
	public CallBackImpl(Object client) throws RemoteException{
		 thisClient=(MemoryGameClient) client;
	}
	
	/**
	 * the server notifies the client for taking a turn
	 * @param turn the boolean value sent to client
	 * @throws RemoteException
	 */
	@Override
	public void takeTurn(boolean turn) throws RemoteException 
	{
		thisClient.setTurn(turn);
	}
	
	/**
	 * The server sends a message to be displayed by the client
	 * @param message the message sent by server when other player perform some activity
	 * @throws RemoteException
	 */
	@Override
	public void notify(String message) throws RemoteException 
	{
		thisClient.setMessage(message);
	} 
	
	/**
	 * the sever sent the index of the clicked card on each client
	 * @param index the position of card to be visible with icon image
	 * @throws RemoteException
	 */
	@Override
	public void setVisibleCard(int index) throws RemoteException
	{
		thisClient.setVisibleCard(index);
	}
	
	/**
	 * the server sent the list of index of picture that will be closed or clear the icon image
	 * @param clickedIndex list of index that sent to the client to be set icon image to null value
	 * @throws RemoteException
	 */
	@Override
	public void setNotMatchClear(ArrayList<Integer> clickedIndex) throws RemoteException 
	{
		thisClient.setNotMatchClear(clickedIndex);
	}
	
	/**
	 * Sent the matched score to the client when the got the matching
	 * @param score the value to be display as current score
	 * @throws RemoteException
	 */
	@Override
	public void setMatchScore(int score) throws RemoteException
	{
		thisClient.setPlayScore(score);
	}
	
	/**
	 * server sent the chat message to the client with the parameter message
	 * @param message text that sent to client
	 * @throws RemoteException
	 */
	@Override
	public void setChatMessage(String message) throws RemoteException
	{
		thisClient.chatMessage(message);	
	}
	
	/**
	 * Create the New Game, it just clear the game panel and re-random icon image again.
	 * @throws RemoteException
	 */
	@Override
	public void createNewGame() throws RemoteException
	{
		thisClient.createNewGame();
	}
	
	/**
	 * This method is update the list of pic icon 
	 * @param pics list of pics that was updated
	 * @throws RemoteException
	 */
	@Override
	public void setChangePicIcon(Icon[] pics) throws RemoteException
	{
		thisClient.setUpdatePicsIcon(pics);
	}
	
	/**
	 * Sent the picture type to each client when picture was changed by every client
	 * @param picType String of picture it can be "Fruit" or "Animal".
	 * @throws RemoteException
	 */
	@Override
	public void notifyPicType(String picType) throws RemoteException 
	{
		thisClient.setPicType(picType);
	}
	
	/**
	 * Re-arrange the game panel and get the icon image list from other client who change the game level
	 * @param pics the icon array which was sent by other client who had changed the game level
	 * @throws RemoteException
	 */
	@Override
	public void setChangeLevel(Icon[] pics) throws RemoteException 
	{
		thisClient.setUpdateGameLevel(pics);
	}
	
	/**
	 * Notify the game size or grid size to player when the game level was changed by other player
	 * @param levelSize the game dimension or grid size sent to other player
	 * @throws RemoteException
	 */
	@Override
	public void notifyGameLevel(int levelSize) throws RemoteException
	{
		thisClient.setGameLevel(levelSize);
	}
	
	/**
	 * Notify the win player when other player get win
	 * @throws RemoteException
	 */
	@Override
	public void notifyWin() throws RemoteException
	{
		thisClient.setWin();
	}
	
	/**
	 * Notify the lost player when other player get win
	 * @throws RemoteException
	 */
	@Override
	public void notifyLost() throws RemoteException
	{
		thisClient.setLost();
	}
	
	/**
	 * confirm the other palyer1 or 2 weather other was disconnected
	 * @param player player1 or 2 that was disconnected
	 * @throws RemoteException
	 */
	@Override
	public void ConfirmDisconnected(String player) throws RemoteException {
		thisClient.confirmDisconnected(player);
		
	}
	@Override
	public void setClickSound() throws RemoteException {
		thisClient.setClick();
		
	}
	
	
}
