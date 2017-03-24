package LWC.pkgfinal.Object;

import LWC.pkgfinal.*;
import LWC.pkgfinal.AI.*;
import LWC.pkgfinal.Item.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class LWC_FlyMonster extends LWC_Monster{
    
    public LWC_FlyMonster(String name, String group, LWC_RPG rpg) {
        super(name, group, rpg);
        img = new BufferedImage[2][2][];
        
        ai = new LWC_FlyMonsterAI(rpg, this);
        setRegister("Power", 10);
        img[0][0] = rpg.imgPool().getFolderImages("img/character/FlyMonster/runLeft/");
        img[0][1] = rpg.imgPool().getFolderImages("img/character/FlyMonster/runRight/");
        img[1][0] = rpg.imgPool().getFolderImages("img/character/FlyMonster/standLeft");
        img[1][1] = rpg.imgPool().getFolderImages("img/character/FlyMonster/standRight");   
        
        setRegister("MaxMP", 25);
        setRegister("MaxHP", 50);
        setRegister("HP", 50);
        setRegister("MP", 25);
        setRegister("Power", 15);
        setRegister("Defence", 6);
    }
    
    @Override
    public Rectangle getPrefferedSize(){
        if(getRegister("Position").equals("left")) return new Rectangle(12, 65, 155, 91);
        else return new Rectangle(19, 65, 164, 91);
    }

    @Override
    public boolean action(){
        if(getCounter("Character")%100 == 0) setMP(getMP()+1);
        
        if(ai == null){
            System.err.println("Character must have an AI, Object destroyed");
            return false;
        }
        else{
            boolean isLive = ai.action();
            if(!isLive && getRegister("Dead") == null)
            {
                rpg.getPlayer().setRegister("Exp", (int)(rpg.getPlayer().getRegister("Exp")) + 10);
                setAI(new LWC_EmptyAI(rpg, this));
                setRegister("Dead", "True");
                BufferedImage []img = null;
                if(getRegister("Position").equals("left")) img = rpg.imgPool().getFolderImages("img/character/FlyMonster/Die/");
                else if(getRegister("Position").equals("right")) img = rpg.imgPool().getFolderImages("img/character/FlyMonster/DieRight");
                setAnimate(img, 10, 60);
                randomGive();
            }
            if(getRegister("Dead") != null && getRegister("Dead").equals("True"))
                return true;
            return isLive;
        }
    }
    
    
}