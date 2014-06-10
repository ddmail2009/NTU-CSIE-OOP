package LWC.pkgfinal.Object;

import LWC.pkgfinal.*;
import LWC.pkgfinal.AI.*;
import LWC.pkgfinal.Item.*;
import LWC.pkgfinal.Loader.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class LWC_Monster extends LWC_AbstractCharacter{
    ItemLoader loader = new ItemLoader("config/Item.txt");
    protected void randomGive(){
        LWC_Player player = rpg.getPlayer();
        Random ran = new Random();
        int value = ran.nextInt(2);
        int possibility = ran.nextInt(100);
        if(value == 0){//weapon
            if(possibility < 5)
                player.addItem(loader.getItem(rpg,"Mace"));
            else if(possibility < 15)
                player.addItem(loader.getItem(rpg,"Axe"));
            else if(possibility < 30)
                player.addItem(loader.getItem(rpg,"Sword"));
            else if(possibility < 50)
                player.addItem(loader.getItem(rpg,"BasicWeapon"));
        }
        else if(value == 1){//consumable
            if(possibility < 5)
                player.addItem(loader.getItem(rpg,"Chicken"));
            else if(possibility < 15)
                player.addItem(loader.getItem(rpg,"RedWater"));
            else if(possibility < 30)
                player.addItem(loader.getItem(rpg,"BlueWater"));
            else if(possibility < 50)
                player.addItem(loader.getItem(rpg,"Pie"));
        }
    }
    
    public LWC_Monster(String name, String group, LWC_RPG rpg) {
        super(name, group, rpg);
        img = new BufferedImage[2][2][];
        
        ai = new LWC_MonsterAI(rpg, this);
        setRegister("Power", 10);
        img[0][0] = rpg.imgPool().getFolderImages("img/character/Pig/runLeft/");
        img[0][1] = rpg.imgPool().getFolderImages("img/character/Pig/runRight/");
        img[1][0] = rpg.imgPool().getFolderImages("img/character/Pig/standLeft");
        img[1][1] = rpg.imgPool().getFolderImages("img/character/Pig/standRight");
        
        setRegister("MaxMP", 25);
        setRegister("MaxHP", 25);
        setRegister("HP", 25);
        setRegister("MP", 25);
        setRegister("Power", 5);
        setRegister("Defence", 10);
    }
    @Override
    public boolean action(){
        if(getCounter("Character")%100 == 0) setMP(getMP()+1);
        
        if(ai == null){
            System.err.println("Character must have an AI, Object destroyed");
            return false;
        }
        else 
        {
            boolean isLive = ai.action();
            if(!isLive && getRegister("Dead") == null)
            {
                rpg.getPlayer().setRegister("Exp", (int)(rpg.getPlayer().getRegister("Exp")) + (int)(Math.random() * 30));
                setAI(new LWC_EmptyAI(rpg, this));
                setRegister("Dead", "True");
                BufferedImage []img = null;
                if(getRegister("Position").equals("left")) img = rpg.imgPool().getFolderImages("img/character/Pig/Die/");
                else if(getRegister("Position").equals("right")) img = rpg.imgPool().getFolderImages("img/character/Pig/DieRight");
                setAnimate(img, 10, 60);
                randomGive();

            }
            if(getRegister("Dead") != null && getRegister("Dead").equals("True"))
                return true;
            return isLive;
        }
    }
    @Override
    public BufferedImage show() {
        if(getRegister("Dead") != null && getRegister("Dead").equals("False"))
            return null;
        if(getRegister("Animate") == null){
            switch ((String)getRegister("Position")) {
                case "left":
                    if(getRegister("run") != null)
                        return img[0][0][(getCounter("Character")/10)%img[0][0].length];
                    else 
                        return img[1][0][(getCounter("Character")/10)%img[1][0].length];
                case "right":
                    if(getRegister("run") != null)
                        return img[0][1][(getCounter("Character")/10)%img[0][1].length];
                    else 
                        return img[1][1][(getCounter("Character")/10)%img[1][1].length];
                default:
                    return img[1][0][(getCounter("Character")/40)%img[1][0].length];
            }
        }
        else{
            BufferedImage []img = (BufferedImage[]) getRegister("Animate");
            int i = (getCounter("Animate")/(int)getRegister("AnimateSpeed"))%img.length;
            if(getCounter("Animate")/(int)getRegister("AnimateSpeed") >= img.length && getRegister("Dead") != null)
            {
                i = img.length - 1;
            }
            if((int)getCounter("Animate") == (int)getRegister("AnimateDuration")) {
                clearRegister("AnimateDuration");
                clearRegister("AnimateSpeed");
                clearCounter("Animate");
                clearRegister("Animate");
                if(getRegister("Dead") != null && getRegister("Dead").equals("True"))
                    setRegister("Dead", "False");
            }
            return img[i];
        }
    }
}