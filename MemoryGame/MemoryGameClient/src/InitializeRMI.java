import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class InitializeRMI {
	
	//private final String hostname="192.168.0.254";
	private static InitializeRMI instance = null;
	
	private InitializeRMI() {
		
	}
    
    public static InitializeRMI getInstance() {
        if(instance == null)
        {
            instance = new InitializeRMI();
        }
        return instance;
    }
    
    public MemoryGameInterface getRemoteObject(){
    	MemoryGameInterface memoryGame=null;
    	try {
			//Registry reg= LocateRegistry.getRegistry(hostname,MemoryGameConstants.RMI_PORT);
			//memoryGame=(MemoryGameInterface)reg.lookup("//"+hostname+"/" + MemoryGameConstants.RMI_ID);
			Registry reg= LocateRegistry.getRegistry(MemoryGameConstants.RMI_PORT);
			memoryGame=(MemoryGameInterface)reg.lookup(MemoryGameConstants.RMI_ID);
			System.out.println("Server object [ " + memoryGame + " ] was found...!");
		} catch (Exception e) {
			System.out.println("Cannot connect to server :" + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
		return memoryGame;
    }
  
  
}
