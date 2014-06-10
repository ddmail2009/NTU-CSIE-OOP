package LWC.pkgfinal.AI;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Object.*;
import java.awt.Point;

public class LWC_NPCAI extends LWC_AbstractAI {
    public LWC_NPCAI(LWC_RPG rpg, LWC_AbstractCharacter obj) {
        super(rpg, obj);
    }

    @Override
    public Point move() {
        return new Point();
    }

    @Override
    public boolean AIaction() {
        if(obj.getHP() <= 0) return false;
        return true;
    }
    
}
