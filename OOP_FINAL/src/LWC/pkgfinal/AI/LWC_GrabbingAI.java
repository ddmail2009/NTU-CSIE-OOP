package LWC.pkgfinal.AI;

import LWC.pkgfinal.Effect.*;
import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.LWC_Util;
import LWC.pkgfinal.Object.LWC_AbstractCharacter;
import LWC.pkgfinal.Object.LWC_Player;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class LWC_GrabbingAI extends LWC_AbstractAI {

    public LWC_GrabbingAI(LWC_RPG rpg, LWC_AbstractCharacter obj) {
        super(rpg, obj);
    }

    @Override
    public Point move() {
        return new Point();
    }

    @Override
    public boolean AIaction() {
        Point p = new Point();
        if(rpg.getKeys().getCurrentKey().contains(KeyEvent.VK_UP))
            p.y -= 5;
        else if(rpg.getKeys().getCurrentKey().contains(KeyEvent.VK_DOWN))
            p.y += 5;
        rpg.movePosition(obj, p, false);
        if(rpg.getKeys().getCurrentKey().contains(KeyEvent.VK_SPACE) && (obj instanceof LWC_Player && obj.getRegister("Grab") != null)){
            if(rpg.isSolid(new Rectangle(rpg.getPosition(obj), LWC_Util.getImgDim(obj.show()))) == false){
                LWC_Player player = (LWC_Player)obj;
                player.setAI((LWC_AbstractAI) player.getRegister("Grab"));
                player.clearRegister("Grab");
                ((LWC_Gravity)player.getRegister("Gravity")).stop(false);
            }
        }
        if(obj.getHP() > 0) return true;
        else return false;
    }
}
