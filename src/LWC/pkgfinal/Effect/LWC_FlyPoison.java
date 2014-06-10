package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import LWC.pkgfinal.Object.*;
import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import LWC.pkgfinal.*;
public class LWC_FlyPoison extends LWC_AbstractAttack{
    private boolean alive = true;
    private Point position = new Point();
    
    protected LWC_AbstractObject obj = new LWC_AbstractObject(rpg) {
        @Override
        public boolean action() {
            return alive;
        }
        
        @Override
        public BufferedImage show() {
            return img[counter/(getDuration()/img.length)%img.length];
        }
    };
    
    public LWC_FlyPoison(LWC_RPG rpg) {
        super(rpg, "PoisonEffect");
        img = rpg.imgPool().getFolderImages("img/skill/Poison/bluePoisonLeft");
        setDuration(5*img.length);
    }
    
    public void setPosition(Point p){
        if(((String)user.getRegister("Position")).equals("left"))
            img = rpg.imgPool().getFolderImages("img/skill/Poison/bluePoisonLeft");
        else
            img = rpg.imgPool().getFolderImages("img/skill/Poison/bluePoisonRight");
        
        Dimension dim = LWC_Util.getImgDim(img[0]);
        if(((String)user.getRegister("Position")).equals("left"))
                position.x = p.x - dim.width/2;
        else position.x = p.x + dim.width/2;
        
        position.x = position.x - dim.width/2;      
        position.y = p.y - dim.height/2;
    }

    @Override
    protected boolean attack_update() {
        if(counter == 1) {
            LWC_MP3Player mp3 = rpg.mp3Pool().setPlayer("Poison", "music/Fire5.mp3");
            if(mp3 != null)
                mp3.play();
            rpg.addObj(obj, position);
        }

        if(counter%(getDuration()/1) == 0) {
            Rectangle area = LWC_Util.getObjArea(rpg, obj);
            area.x += 6;
            area.y += 5;
            area.width = 80;
            area.height = 30;
            ArrayList<LWC_AbstractCharacter> targets = LWC_Util.getCharacterWithInArea(rpg, area);
            for(int i = 0; i < targets.size(); i++)
                if(targets.get(i) instanceof LWC_Player)
                    targets.get(i).damage(8, user);
        }

        if(counter >= getDuration()){
            alive = false;
            return false;
        }
        return true;
    }
}
