package LWC.pkgfinal.Effect;

import LWC.pkgfinal.*;
import LWC.pkgfinal.Overlay.*;

/**
 * Change Map from transparent to Black
 */
public class LWC_FadeOutEffect extends LWC_AbstractTime {
    protected LWC_InterfaceEmpty empty;
    
    public LWC_FadeOutEffect(LWC_RPG rpg) {
        super(rpg, "Empty");
        empty = new LWC_InterfaceEmpty(rpg);
        empty.setOpaque(0);
    }
    
    public LWC_InterfaceEmpty getInterface(){
        return empty;
    }
    
    public void setInterface(LWC_InterfaceEmpty empty){
        this.empty = empty;
    }

    @Override
    protected boolean effect_update() {
        if(counter == 1 && !rpg.hasInterface(empty)) rpg.addInterface(empty);

        if(counter <= getDuration()){
            empty.setOpaque((float) (counter)/getDuration());
            return true;
        }
        else
            return false;
    }
}