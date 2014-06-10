package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Object.*;
import java.awt.*;

public class LWC_TriggerSolidMap extends LWC_AbstractTrigger{
    @Override
    public void act(LWC_RPG rpg, LWC_AbstractObject obj, Point enter_vector) {
        enter_vector.setLocation(0,0);
    }
    
    @Override
    public boolean isTriggered(Rectangle rec, Point recordPoint){
        for (int i=rec.x; i<rec.x+rec.width; i++)
            for (int j=rec.y; j<rec.y+rec.height; j++)
                if(isTriggered(new Point(i, j))){
                    if(recordPoint != null){
                        recordPoint.x = i;
                        recordPoint.y = j;
                    }
                    return true;
                }
        return false;
    }

    public LWC_TriggerSolidMap(LWC_RPG rpg) {
        super(rpg);
    }
    
    @Override
    public void ignore(LWC_RPG rpg, LWC_AbstractObject obj, Point enter_vector){
        Point origin = rpg.getPosition(obj);
        Point new_position = new Point(origin.x+enter_vector.x, origin.y+enter_vector.y);
        
        rpg.setPosition(obj, new_position);
    }
}
