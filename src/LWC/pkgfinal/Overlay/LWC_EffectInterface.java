package LWC.pkgfinal.Overlay;

import LWC.pkgfinal.LWC_RPG;
import LWC.pkgfinal.Effect.LWC_Gravity;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class LWC_EffectInterface extends LWC_AbstractEffectInterface {    
    public LWC_EffectInterface(LWC_RPG rpg) {
        super(rpg);
    }
    
    @Override
    public Point getPosition() {
        return new Point();
    }

    @Override
    public BufferedImage show() {
        return null;
    }

    @Override
    public boolean effect_interface_update() {
        if(effectList.size()==1 && effectList.get(0) instanceof LWC_Gravity){
            return false;
        }
        else if(effectList.isEmpty()) return false;
        else return true;
    }
}
