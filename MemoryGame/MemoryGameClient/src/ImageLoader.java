import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;


public class ImageLoader {

	private static ImageLoader instance;
	private ImageLoader(){
		
	}
	public static ImageLoader getInstance() {
        if(instance == null)
        {
            instance = new ImageLoader();
        }
        return instance;
    }
	
	/**
	 * 
	 * @param cardsToAdd
	 * @param picType
	 * @return
	 */
	public Icon[] getImageIcon(int[] cardsToAdd,String picType){
		
		int size=MemoryGameClient.getDimension();
		Icon[]pics=new Icon[size*size];
		int num=0;
		String folder=null;
		
		if(picType.equals("fruit")){
			if(size==4) folder="/fruits16";
			else if(size==8) folder="/fruits64";
			else folder="/fruits100";
		}
		else if(picType.equals("animal")) {
			if(size==4)folder="/animals16";
			else if(size==8) folder="/animals64";
			else folder="/animals100";
		}
			
		for(int x=0; x<cardsToAdd.length; x++)
        {
			try {
				num=cardsToAdd[x];
	            String fileName = folder+ "/pic"+(num+1)+".jpg";
	            URL loadURL = ImageLoader.class.getResource(fileName);			
	     		pics[x]=new ImageIcon(loadURL);  
			} catch (Exception e) {
				System.out.println("Error load image pictures..!"  + e.getMessage() );
				e.printStackTrace();
			}
            
         }
		return pics;
		
	}
	
	
}
