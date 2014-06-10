package LWC.pkgfinal.Object;

import LWC.pkgfinal.*;
import LWC.pkgfinal.AI.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class LWC_Boss extends LWC_Monster{
    protected Rectangle []prefferedSize = null;
    protected String dead;
    
    public LWC_Boss(String name, String group, LWC_RPG rpg) {
        super(name, group, rpg);
        
        setRegister("Power", 20);
        setRegister("Defence", 50);
        setRegister("HP", 100);
        setRegister("MaxHP", 100);
        
        img = new BufferedImage[3][2][];
        img[0][0] = rpg.imgPool().getFolderImages("img/devilrun");
        img[0][1] = LWC_Util.flipImages(img[0][0]);
        img[1][0] = rpg.imgPool().getFolderImages("img/devilstand");
        img[1][1] = LWC_Util.flipImages(img[1][0]);
        img[2][0] = rpg.imgPool().getFolderImages("img/devilDie");
        img[2][1] = LWC_Util.flipImages(img[2][0]);
        
        ai = new LWC_BossAI(rpg, this);
        prefferedSize = LWC_Util.ApproxCenterAndDim(this);
        prefferedSize[0].x = prefferedSize[1].x;
    }
    @Override
    public Rectangle getPrefferedSize(){
        if(getRegister("Position").equals("left")) return new Rectangle(prefferedSize[0]);
        else return new Rectangle(prefferedSize[1]);
    }
    @Override
    public boolean action(){
        if(getCounter("Character")%100 == 0) setMP(getMP()+1);
        
        if(ai == null){
            System.err.println("Character must have an AI, Object destroyed");
            return false;
        }
        else {
            boolean isLive = ai.action();
            if(!isLive && getRegister("Dead") == null){
                dead = getName();
                rpg.getPlayer().setRegister("Exp", (int)(rpg.getPlayer().getRegister("Exp")) + 10);
                setAI(new LWC_EmptyAI(rpg, this));
                setRegister("Dead", "True");
                BufferedImage []tmp = null;
                if(getRegister("Position").equals("left")) tmp = rpg.imgPool().getFolderImages("img/devilDie");
                else if(getRegister("Position").equals("right")) tmp = LWC_Util.flipImages(rpg.imgPool().getFolderImages("img/devilDie"));
                setAnimate(tmp, 5, 5*tmp.length);
            }
            if(getRegister("Dead") != null && getRegister("Dead").equals("True"))
                return true;
            return isLive;
        }
    }
}