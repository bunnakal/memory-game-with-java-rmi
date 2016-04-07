import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;



public class GamePanle extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JButton buttons[];
	Border border;
	private static int rowXcol;

	private static GamePanle instance=null;
	private GamePanle(){
		
	}
	
	public static GamePanle getInstance(){
		if(instance==null){
			instance=new GamePanle();
		}
		return instance;
	}
	
	public int getRowCol(){
		return rowXcol;
	}
	public static void setRowCol(int rowcol){
		rowXcol=rowcol;
	}
	public int getSizePanel(){
		int panelSize=0;
		if(getRowCol()==4) panelSize=4;
		else if(getRowCol()==8) panelSize=8;
		else if(getRowCol()==10) panelSize=10;
		return panelSize;
	}
	
	public JButton[] getButtonList(){
		return buttons;
	}
	public JPanel createPanel(){
		JPanel game=new JPanel();
		int panelsize=getSizePanel();
		game.setLayout(new GridLayout(panelsize,panelsize,5,5));
        game.setOpaque(true);
        border=BorderFactory.createLineBorder(Color.BLUE,5);
        game.setBorder(border);
        game.setBackground(Color.BLUE);
        buttons=new JButton[panelsize*panelsize];
        for(int i=0;i<panelsize*panelsize;i++)
        {	
        	buttons[i]=new JButton();
        	buttons[i].setBorder(null);
            game.add(buttons[i]);
        }
		return game;
	}
}
