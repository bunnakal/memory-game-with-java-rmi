import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class NotifyWin {

	private JFrame frm;
	private JLabel lbConfirmWin;
	private JButton btnNewGame,btnClose;
	private boolean isAlreadyClosed=false;
	private NewGame bNewGamePressed;
	
	public NotifyWin(){
		
		frm=new JFrame("You are win");
		frm.setLayout(null);
		
		lbConfirmWin=new JLabel();
		lbConfirmWin.setText("Congratulation You are Win.");
		lbConfirmWin.setFont(new Font("Trajan Pro",Font.BOLD,14));
		lbConfirmWin.setForeground(Color.RED);
		
		btnNewGame=new JButton("New Game");
		btnNewGame.setFont(new Font("Trajan Pro",Font.BOLD,12));
		btnNewGame.setForeground(Color.GREEN);
		btnClose=new JButton("Close");
		btnClose.setFont(new Font("Trajan Pro",Font.BOLD,12));
		btnClose.setForeground(Color.GREEN);
		lbConfirmWin.setBounds(15,20,250,30);
		
		btnNewGame.setBounds(10, 60, 125,40);
		btnClose.setBounds(145,60, 125,40);
		
		frm.add(lbConfirmWin);
		frm.add(btnNewGame);
		frm.add(btnClose);
		
		bNewGamePressed=new NewGame();
		btnNewGame.addActionListener(bNewGamePressed);
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frm.dispose();
			}
		});
		
		frm.setLocationRelativeTo(null);
		frm.setResizable(false);
		//frm.setUndecorated(true);
		frm.setSize(285,150);
		frm.setVisible(true);
	}
	
	public void setAlreadyClosed(){
		this.isAlreadyClosed=true;
	}
	
	public boolean getAlreadyClosed(){
		return this.isAlreadyClosed;
	}
	private class NewGame implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			try {
				frm.dispose();
				InitializeRMI.getInstance().getRemoteObject().createNewGame();
				
			} catch (Exception ex) {
				System.out.println("Error New Game " + ex.getMessage());
			}
			
		}
		
	}
}
