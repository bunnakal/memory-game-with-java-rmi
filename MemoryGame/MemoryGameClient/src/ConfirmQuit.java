import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class ConfirmQuit{
	
	private JFrame frm;
	private JLabel lbConfirmMessaged;
	private JButton btnQuit,btnCancel;
	private boolean isAlreadyClosed=false;
	private QuitClick bQuitPressed;
	
	public ConfirmQuit(){
		
		frm=new JFrame("Quit Game");
		frm.setLayout(null);
		
		lbConfirmMessaged=new JLabel();
		lbConfirmMessaged.setText("Are you want to close?");
		
		btnQuit=new JButton("Quit");
		btnCancel=new JButton("Cancel");
		
		lbConfirmMessaged.setBounds(15,20,250,30);
		
		btnQuit.setBounds(10, 60, 125,40);
		btnCancel.setBounds(145,60, 125,40);
		
		frm.add(lbConfirmMessaged);
		frm.add(btnQuit);
		frm.add(btnCancel);
		
		bQuitPressed=new QuitClick();
		btnQuit.addActionListener(bQuitPressed);
		btnCancel.addActionListener(new ActionListener() {
			
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
	private class QuitClick implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String player=null;
			try {
				frm.dispose();
				player=MemoryGameClient.getPlayer();
				MemoryGameClient.closingFrm();
				InitializeRMI.getInstance().getRemoteObject().playerDisconnected(player);
				
			} catch (Exception ex) {
				System.out.println("Error Quit " + ex.getMessage());
			}
			
		}
		
	}

}
