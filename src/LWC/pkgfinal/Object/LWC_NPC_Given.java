package LWC.pkgfinal.Object;

import LWC.pkgfinal.AI.LWC_NPCAI;
import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Overlay.*;
import LWC.pkgfinal.Item.*;
import LWC.pkgfinal.Loader.*;

import java.awt.image.BufferedImage;



public class LWC_NPC_Given extends LWC_NPC {
    
    public LWC_NPC_Given(String name, String group, LWC_RPG rpg) {
        super(name, group, rpg);
        setCondition("Self");
        if(!msg.load_conversation(name, rpg.getPlotState()))
            msg.load_conversation(name,getCondition());
        else
            setCondition(rpg.getPlotState());
    }
    @Override
    public LWC_AbstractInterface getMsg(){
        if(!msg.load_conversation(name, rpg.getPlotState())){
            msg.load_conversation(name, getCondition());
            if(getCondition().equals("Self"))
                rpg.getPlayer().addItem(new ItemLoader("config/Item.txt").getItem(rpg, "FinalWeapon"));
        }
        else
            setCondition(rpg.getPlotState());
        setCondition("Self1");

        return msg;
    }
}