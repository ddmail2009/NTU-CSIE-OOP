package LWC.pkgfinal;

import java.io.*;
import javazoom.jl.player.Player;

public class LWC_MP3Player{
	private String filename;
	private Player player;
    private boolean loop = false;

	public LWC_MP3Player(String file_name){
		this.filename = file_name;
	}
    
    public String getName(){
        return filename;
    }

    public void setLoop(boolean option){
        loop = option;
    }
    
	public void play(){
		new Thread(new Runnable(){
            @Override
			public void run(){
                while(true){
                    try{
                        BufferedInputStream bis = new BufferedInputStream(getClass().getResourceAsStream('/'+filename));
                        player = new Player(bis);
                        player.play();
                    }catch(Exception e){
                        System.err.println("Problem when playing "+ '/'+filename);
                        System.err.println(e);
                    }
                    if(loop == false) break;
                }
			}
		}).start();
	}

	public void stop(){
        loop = false;
		player.close();
	}

	public boolean isComplete(){
        if(player != null)
		  return player.isComplete();
        else
            return false;
	}
}