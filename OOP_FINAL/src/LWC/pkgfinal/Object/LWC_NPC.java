package LWC.pkgfinal.Object;

import LWC.pkgfinal.AI.LWC_NPCAI;
import LWC.pkgfinal.LWC_MP3Player;
import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Overlay.*;
import java.awt.image.BufferedImage;


public class LWC_NPC extends LWC_AbstractCharacter {
    protected LWC_InterfaceNPCMsg msg;
    
    public LWC_NPC(String name, String group, LWC_RPG rpg) {
        super(name, group, rpg);
        img = new BufferedImage[1][1][];
        setCounter("Character", 0);
        
        loadImages(name);
        msg = new LWC_InterfaceNPCMsg(rpg);
        ai = new LWC_NPCAI(rpg, this);

        System.out.println("name:"+name+"condition:"+rpg.getPlotState());
        setCondition("Normal", true);

        if(!msg.load_conversation(name, rpg.getPlotState()))
            msg.load_conversation(name,getCondition());
        else
            setCondition(rpg.getPlotState());
        
        if(name.equals("LittleGirl")){
            LWC_MP3Player mp3 = rpg.mp3Pool().setPlayer("littleGirl", "music/littleGrilCry.mp3");
            if(mp3!=null) {
                mp3.setLoop(true);
                mp3.play();
            }
        }
    }
    public void loadImages(String name) {
        img[0][0] = rpg.imgPool().getFolderImages("img/character/NPC/"+name);
    }

    public LWC_AbstractInterface getMsg(){
        if(name.contentEquals("Hinter1")){
            System.out.println("In the hinter");
            if(!rpg.getPlotState().equals("Normal"))
                setCondition("Self");
        }
        
        if(!msg.load_conversation(name, rpg.getPlotState()))
            msg.load_conversation(name, getCondition());
        else
            setCondition(rpg.getPlotState());

        return msg;
    }

    @Override
    public BufferedImage show() {
        return img[0][0][(getCounter("Character")/10)%img[0][0].length];
    }   
}