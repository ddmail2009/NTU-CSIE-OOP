package LWC.pkgfinal.Effect;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Object.*;
import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
public class LWC_BatAttack extends LWC_AbstractAttack{
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
    
    public LWC_BatAttack(LWC_RPG rpg) {
        super(rpg, "BatAttackEffect");
        img = rpg.imgPool().getFolderImages("img/skill/BatAnimate/Animate");
        setDuration(5*img.length);
    }
    
    public void setPosition(Point p){
        Dimension dim = LWC_Util.getImgDim(img[0]);
        if(((String)user.getRegister("Position")).equals("left"))
                position.x = p.x - dim.width;
        else position.x = p.x + dim.width;
        
        position.x = position.x - dim.width/2;      
        position.y = p.y - dim.height/2;
        
    }

    @Override
    protected boolean attack_update() {
        if(counter == 1) {
            LWC_MP3Player mp3 = new LWC_MP3Player("music/Battl2.mp3");
            mp3.play();
            rpg.addObj(obj, position);
            BufferedImage []tmp = null;
            if(user.getRegister("Position").equals("left")) tmp = rpg.imgPool().getFolderImages("img/character/Player/WeaponSwing");
            else if(user.getRegister("Position").equals("right")) tmp = rpg.imgPool().getFolderImages("img/character/Player/WeaponSwingRight");
            user.setAnimate(tmp, 10, 40);
        }
        
        
        if(counter > getDuration()/4 && counter <= 3*getDuration()/4 ){
            if(counter%(getDuration()/6) == 0){
                Rectangle area = LWC_Util.getObjArea(rpg, obj);
                area.x += 45;
                area.y += 30;
                area.width = 80;
                area.height = 60;
                ArrayList<LWC_AbstractCharacter> targets = LWC_Util.getCharacterWithInArea(rpg, area);
                for(int i = 0; i < targets.size(); i++)
                    targets.get(i).damage(10, user);
            }
        }
        
        if(counter >= getDuration()){
            alive = false;
            return false;
        }
        return true;
    }
}
