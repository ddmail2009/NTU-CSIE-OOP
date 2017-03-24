package LWC.pkgfinal.Effect;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Object.*;
import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
public class LWC_IceBlink extends LWC_AbstractAttack{
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
    
    public LWC_IceBlink(LWC_RPG rpg) {
        super(rpg, "IceBlinkEffect");
        img = rpg.imgPool().getFolderImages("img/skill/IceBlink/Right");
        setDuration(5*img.length);
    }
    
    public void setPosition(Point p){
        if(((String)user.getRegister("Position")).equals("left"))
            img = rpg.imgPool().getFolderImages("img/skill/IceBlink/Left");
        else img = rpg.imgPool().getFolderImages("img/skill/IceBlink/Right");
        
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
            LWC_MP3Player mp3 = new LWC_MP3Player("music/Sword2.mp3");
            mp3.play();
            rpg.addObj(obj, position);
            BufferedImage []tmp = null;
            if(user.getRegister("Position").equals("left")) tmp = rpg.imgPool().getFolderImages("img/character/Player/IceSwingLeft");
            else if(user.getRegister("Position").equals("right")) tmp = rpg.imgPool().getFolderImages("img/character/Player/IceSwingRight");
            user.setAnimate(tmp, 10, 20);
        }
        
        if(counter%(getDuration()) == getDuration()/2) {
            Rectangle area = LWC_Util.getObjArea(rpg, obj);
            
            area.x += 45;
            area.y += 42;
            area.width = 60;
            area.height = 30;
            ArrayList<LWC_AbstractCharacter> targets = LWC_Util.getCharacterWithInArea(rpg, area);
            for(int i = 0; i < targets.size(); i++)
                if(!(targets.get(i) instanceof LWC_Player || targets.get(i) instanceof LWC_NPC))
                    targets.get(i).damage(2, user);
        }
        
        if(counter > getDuration()){
            alive = false;
            return false;
        }
        return true;
    }
}
