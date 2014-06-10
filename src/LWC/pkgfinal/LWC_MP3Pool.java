package LWC.pkgfinal;

import java.util.*;
public class LWC_MP3Pool {    
    private HashMap<String, LWC_MP3Player> playerList = new HashMap<>();
    
    public LWC_MP3Player getPlayer(String tag){
        purge();
        if(playerList.containsKey(tag)) 
            return playerList.get(tag);
        else return null;
    }
    
    
    public LWC_MP3Player setPlayer(String tag, String fileName){
        purge();
        if(playerList.containsKey(tag)) 
            return null;
        
        LWC_MP3Player player = new LWC_MP3Player(fileName);
        playerList.put(tag, player);
        return player;
    }
    
    public LWC_MP3Player replacePlayer(String tag, String fileName){
        purge();
        
        if(playerList.containsKey(tag)) 
            playerList.get(tag).stop();
        
        LWC_MP3Player player = new LWC_MP3Player(fileName);
        playerList.put(tag, player);
        return player;
    }
    
    private void purge(){
        try
        {
            for (String string : playerList.keySet()) {
                if(playerList.get(string).isComplete()) 
                    playerList.remove(string);
            }
        }
        catch(ConcurrentModificationException e)
        {
            return;
        }
    }
}
