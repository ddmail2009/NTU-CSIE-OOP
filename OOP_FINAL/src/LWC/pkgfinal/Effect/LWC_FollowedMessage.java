package LWC.pkgfinal.Effect;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Object.*;
import java.awt.Point;

public class LWC_FollowedMessage extends LWC_StaticMessage{
    protected Point relative = new Point();
    protected LWC_AbstractObject owner;
    
    public LWC_FollowedMessage(LWC_RPG rpg, LWC_AbstractObject obj) {
        super(rpg);
        owner = obj;
    }
    
    public void setPosition(Point p){
        relative = new Point(p);
        position = new Point(rpg.getPosition(owner).x + relative.x, rpg.getPosition(owner).y + relative.y);
    }
    
    @Override
    protected boolean effect_update() {
        boolean result = super.effect_update();
        setPosition(relative);
        rpg.setPosition(obj, position);
        return result;
    }
}