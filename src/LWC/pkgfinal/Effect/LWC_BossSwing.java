package LWC.pkgfinal.Effect;

import LWC.pkgfinal.LWC_MP3Player;
import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import LWC.pkgfinal.Object.*;
import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

public class LWC_BossSwing extends LWC_AbstractAttack{
    
    public LWC_BossSwing(LWC_RPG rpg, LWC_AbstractCharacter user) {
        super(rpg, "BossSwing");
        this.user = user;
        BufferedImage []img = null;
        if(user.getRegister("Position").equals("left")) 
            img = rpg.imgPool().getFolderImages("img/devilSwing");
        else if(user.getRegister("Position").equals("right")) 
            img = LWC_Util.flipImages(rpg.imgPool().getFolderImages("img/devilSwing"));
        setDuration(5 * img.length);
    }
    
    @Override
    protected boolean attack_update() {
        if(counter == 1) {
            LWC_MP3Player mp3 = rpg.mp3Pool().replacePlayer("bossSwing", "bossSwing.mp3");
            if(mp3 != null)
                mp3.play();
            
            BufferedImage []img = null;
            if(user.getRegister("Position").equals("left")) img = rpg.imgPool().getFolderImages("img/devilSwing/");
            else if(user.getRegister("Position").equals("right")) img = LWC_Util.flipImages(rpg.imgPool().getFolderImages("img/devilSwing/"));
            user.setAnimate(img, 5, 5 * img.length);
        }

        if(counter%(getDuration()) == getDuration()/2) {
            Rectangle area = LWC_Util.getObjArea(rpg, user);
            area.x += 100;
            area.y += 100;
            area.width *= 0.7;
            area.height *= 0.7;
            ArrayList<LWC_AbstractCharacter> targets = LWC_Util.getCharacterWithInArea(rpg, area);
            for(int i = 0; i < targets.size(); i++)
                if(targets.get(i) instanceof LWC_Player)
                    targets.get(i).damage(20, user);
        }

        if(counter >= getDuration()){
            LWC_MP3Player mp3 = rpg.mp3Pool().getPlayer("bossSwing");
            mp3.stop();
            return false;
        }
        return true;
    }
}
